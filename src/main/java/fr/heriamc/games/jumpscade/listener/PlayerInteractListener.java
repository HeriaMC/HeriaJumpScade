package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.api.game.GameState;
import fr.heriamc.games.jumpscade.JumpScadeAddon;
import fr.heriamc.games.jumpscade.waiting.JumpScadeItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public record PlayerInteractListener(JumpScadeAddon addon) implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var itemStack = event.getItem();

        if (itemStack == null || !itemStack.hasItemMeta()) return;

        var player = event.getPlayer();
        var game = addon.getPool().getGamesManager().getNullableGame(player);

        if (game == null
                || !game.containsPlayer(player)
                || !game.getState().is(GameState.WAIT, GameState.STARTING, GameState.END)) return;

        JumpScadeItems.getByItem(itemStack).ifPresent(item -> item.getConsumer().accept(addon, player));

        event.setCancelled(true);
    }

}