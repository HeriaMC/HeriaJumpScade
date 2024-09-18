package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record PlayerMoveListener(GameManager<JumpScadeGame> gameManager) implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var game = gameManager.getNullableGame(player);

        if (event.getTo().getY() >= 0 || game == null) return;

        System.out.println("c");
        var gamePlayer = game.getNullablePlayer(player);

        // Null check of gamePlayer is maybe useless ??
        if (gamePlayer == null || gamePlayer.isSpectator()) return;
        System.out.println("a");

        switch (game.getState()) {
            case WAIT, STARTING -> game.getWaitingRoom().teleport(gamePlayer);
            case IN_GAME -> player.setHealth(0.0D);
        }
    }

}