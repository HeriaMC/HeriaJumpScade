package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.api.game.GameState;
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

        if (game == null) return;

        var winRegion = game.getWinRegion();
        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null || gamePlayer.isSpectator()) return;

        if (game.getState() == GameState.IN_GAME && winRegion.contains(gamePlayer)) {
            winRegion.onEnter(gamePlayer);
            return; // USELESS ?
        }

        if (event.getTo().getY() >= 0) return;

        switch (game.getState()) {
            case WAIT, STARTING -> game.getWaitingRoom().teleport(gamePlayer);
            case IN_GAME -> player.setHealth(0.0D);
        }
    }

}