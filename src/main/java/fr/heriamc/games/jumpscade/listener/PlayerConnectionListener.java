package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.engine.event.player.GamePlayerJoinEvent;
import fr.heriamc.games.engine.event.player.GamePlayerLeaveEvent;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.pool.JumpScadePool;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record PlayerConnectionListener(JumpScadePool pool) implements Listener {

    @EventHandler
    public void onJoin(GamePlayerJoinEvent<JumpScadeGame, JumpScadePlayer> event) {
         var game = event.getGame();
         var player = event.getPlayer();
         var gamePlayer = event.getGamePlayer();

        if (game.getState().is(GameState.WAIT, GameState.STARTING))
            game.getWaitingRoom().processJoin(event.getGamePlayer());

        game.checkGameState(GameState.IN_GAME, () -> {
            gamePlayer.setSpectator(true);
            player.setGameMode(GameMode.SPECTATOR);

            /*
                GIVE SPECTATOR ITEM OR do interface SpectatorState ...

                class JumpScadePlayer implements SpectatorState {

                    @Override
                    public void giveSpectatorItem() {
                        ......
                    }

                }
             */

        });
    }

    @EventHandler
    public void onLeave(GamePlayerLeaveEvent<JumpScadeGame, JumpScadePlayer> event) {
        var game = event.getGame();
        var gamePlayer = event.getGamePlayer();

        if (game.getState().is(GameState.WAIT, GameState.STARTING))
            game.getWaitingRoom().processLeave(gamePlayer);

        if (game.getState().is(GameState.IN_GAME) && game.oneTeamAlive())
            game.getEndTask().run();
    }

}