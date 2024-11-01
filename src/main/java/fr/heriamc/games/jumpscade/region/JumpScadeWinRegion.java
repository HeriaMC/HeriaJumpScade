package fr.heriamc.games.jumpscade.region;

import fr.heriamc.api.game.GameState;
import fr.heriamc.games.engine.region.GameRegion;
import fr.heriamc.games.engine.region.RegionObserver;
import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collection;
import java.util.function.Predicate;

public class JumpScadeWinRegion extends GameRegion<JumpScadeGame> implements RegionObserver<JumpScadePlayer> {

    private JumpScadePlayer winner;

    public JumpScadeWinRegion(JumpScadeGame game, Location minLocation, Location maxLocation) {
        super(game, "WinRegion", minLocation, maxLocation);
        this.winner = null;
    }

    @Override
    public void onEnter(JumpScadePlayer gamePlayer) {
        if (game.getState() != GameState.IN_GAME) return;

        if (winner == null) this.winner = gamePlayer;
        else return;

        var location = gamePlayer.getLocation();
        var team = gamePlayer.getTeam();

        BukkitThreading.runTask(() -> {
            game.getAlivePlayers().stream()
                    .filter(Predicate.not(gamePlayer::equals))
                    .forEach(jumpScadePlayer -> {
                        if (team.isNotMember(jumpScadePlayer))
                            jumpScadePlayer.setSpectator(true);

                        jumpScadePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                        jumpScadePlayer.teleport(location);
                    });

            if (!game.getEndTask().isStarted())
                game.getEndTask().run();
        });

        spawnFirework(location, game.getPlayers().values());
    }

    /*
        NEED TO BE REWORKED DO A BETTER END ANIMATION...
     */
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
    public void onExit(JumpScadePlayer gamePlayer) {}

}