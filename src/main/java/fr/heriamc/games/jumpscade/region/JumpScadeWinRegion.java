package fr.heriamc.games.jumpscade.region;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.engine.region.GameRegion;
import fr.heriamc.games.engine.region.RegionObserver;
import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class JumpScadeWinRegion extends GameRegion<JumpScadeGame> implements RegionObserver<JumpScadePlayer> {

    // DEBUG: NEED TO BE CHANGED
    private final Map<JumpScadePlayer, UUID> in;

    public JumpScadeWinRegion(JumpScadeGame game, Location minLocation, Location maxLocation) {
        super(game, "WinRegion", minLocation, maxLocation);
        // DEBUG: NEED TO BE CHANGED
        this.in = new HashMap<>();
        MultiThreading.schedule(() -> {
            game.getPlayers().values().forEach(gamePlayer -> {
                if (contains(gamePlayer) && !in.containsKey(gamePlayer))
                    onEnter(gamePlayer);
                else if (!contains(gamePlayer) && in.containsKey(gamePlayer))
                    onExit(gamePlayer);
            });
        }, 50, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onEnter(JumpScadePlayer gamePlayer) {
        if (!game.getState().is(GameState.IN_GAME)) return;

        in.putIfAbsent(gamePlayer, gamePlayer.getUuid());

        if (!game.getEndTask().isStarted())
            game.getEndTask().run();

        var location = gamePlayer.getLocation();

        BukkitThreading.runTask(() -> game.getAlivePlayers().stream()
                .filter(Predicate.not(gamePlayer::equals))
                .forEach(jumpScadePlayer -> {
                    jumpScadePlayer.setSpectator(true);
                    jumpScadePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                    jumpScadePlayer.teleport(location);
                }));

        spawnFirework(location, game.getPlayers().values());
    }

    public void spawnFirework(Location location, Collection<JumpScadePlayer> collection) {
        var firework = new ItemStack(org.bukkit.Material.FIREWORK);
        var fireworkMeta = (FireworkMeta) firework.getItemMeta();
        var effect = FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.ORANGE).with(FireworkEffect.Type.STAR).trail(true).flicker(true).build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setItemMeta(fireworkMeta);

        var world = ((CraftWorld) location.getWorld()).getHandle();
        var entityFireworks = new EntityFireworks(world, location.getX(), location.getY(), location.getZ(), CraftItemStack.asNMSCopy(firework));

        var spawnPacket = new PacketPlayOutSpawnEntity(entityFireworks, 76);
        var metaPacket = new PacketPlayOutEntityMetadata(entityFireworks.getId(), entityFireworks.getDataWatcher(), true);

        collection.forEach(gamePlayer -> {
            gamePlayer.sendPacket(spawnPacket);
            gamePlayer.sendPacket(metaPacket);
        });

        BukkitThreading.runTaskLater(() ->
                collection.forEach(gamePlayer -> {
                    gamePlayer.sendPacket(new PacketPlayOutEntityStatus(entityFireworks, (byte) 17));
                    gamePlayer.sendPacket(new PacketPlayOutEntityDestroy(entityFireworks.getId()));
                }), 2L);
    }

    @Override
    public void onExit(JumpScadePlayer gamePlayer) {
        in.remove(gamePlayer);
        gamePlayer.sendMessage("Vous êtes sortit de la zone");
    }

}