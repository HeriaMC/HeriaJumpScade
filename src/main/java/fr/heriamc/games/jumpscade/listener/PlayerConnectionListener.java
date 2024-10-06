package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.games.engine.event.player.GamePlayerJoinEvent;
import fr.heriamc.games.engine.event.player.GamePlayerLeaveEvent;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.data.JumpScadeDataManager;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.pool.JumpScadePool;
import fr.heriamc.games.jumpscade.utils.NameTag;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record PlayerConnectionListener(JumpScadeDataManager dataManager, JumpScadePool pool) implements Listener {

    @EventHandler
    public void onJoin(GamePlayerJoinEvent<JumpScadeGame, JumpScadePlayer> event) {
         var game = event.getGame();
         var player = event.getPlayer();
         var gamePlayer = event.getGamePlayer();

        NameTag.setNameTag(player, "§7", " ", 999);

         switch (game.getState()) {
            case WAIT, STARTING -> game.getWaitingRoom().processJoin(gamePlayer);
            case IN_GAME, END -> {
                var rank = gamePlayer.getHeriaPlayer().getRank();

                if (rank.getPower() >= 40 && gamePlayer.isSpectator()) {
                    NameTag.setNameTag(player, rank.getPrefix(), " ", rank.getTabPriority());
                    return;
                }

                /*
                 GIVE SPECTATOR ITEM OR do interface SpectatorState ...
                 class JumpScadePlayer implements SpectatorState {

                    @Override
                    public void giveSpectatorItem() {
                        ......
                    }
                 }
                 */
                gamePlayer.setSpectator(true);
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onLeave(GamePlayerLeaveEvent<JumpScadeGame, JumpScadePlayer> event) {
        var game = event.getGame();
        var gamePlayer = event.getGamePlayer();

        switch (game.getState()) {
            case WAIT, STARTING -> game.getWaitingRoom().processLeave(gamePlayer);
            case IN_GAME -> {
                if (game.oneTeamAlive())
                    game.getEndTask().run();

                savePlayerData(gamePlayer);
            }
            case END -> savePlayerData(gamePlayer);
        }
    }

    private void savePlayerData(JumpScadePlayer gamePlayer) {
        VirtualThreading.execute(() -> {
            var gamePlayerData = dataManager.createOrLoad(gamePlayer.getUuid());

            gamePlayerData
                    .updateKills(gamePlayer.getKills())
                    .updateDeaths(gamePlayer.getDeaths());

            dataManager.save(gamePlayerData);
            dataManager.saveInPersistant(gamePlayerData);
        });
    }

}