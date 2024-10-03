package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.games.engine.event.player.GamePlayerJoinEvent;
import fr.heriamc.games.engine.event.player.GamePlayerLeaveEvent;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.data.JumpScadeDataManager;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.pool.JumpScadePool;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public record PlayerConnectionListener(JumpScadeDataManager dataManager, JumpScadePool pool) implements Listener {

    @EventHandler
    public void onJoin(GamePlayerJoinEvent<JumpScadeGame, JumpScadePlayer> event) {
         var game = event.getGame();
         var player = event.getPlayer();
         var gamePlayer = event.getGamePlayer();
         var rank = gamePlayer.getHeriaPlayer().getRank();

         setNameTag(player, rank.getPrefix(), " ", rank.getTabPriority());

         switch (game.getState()) {
            case WAIT, STARTING -> game.getWaitingRoom().processJoin(gamePlayer);
            case IN_GAME -> {
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

    // NEED TO BE MOVED AWAY
    private void setNameTag(Player player, String prefix, String suffix, int sortPriority) {
        var manager = player.getScoreboard();
        var priority = ((sortPriority < 10) ? "0" : "") + sortPriority;
        Team team = null;

        for (Team t : manager.getTeams()) {
            if (t.getPrefix().equals(prefix) && t.getSuffix().equals(suffix) && t.getName().startsWith(priority)) {
                team = t;
                break;
            }
        }

        while (team == null) {
            var tn = priority + UUID.randomUUID().toString().substring(30);
            if (manager.getTeam(tn) == null) {
                team = manager.registerNewTeam(tn);
                team.setPrefix(prefix);
                team.setSuffix(suffix);
            }
        }
        team.addEntry(player.getName());
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