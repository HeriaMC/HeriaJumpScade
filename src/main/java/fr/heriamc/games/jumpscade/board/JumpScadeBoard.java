package fr.heriamc.games.jumpscade.board;

import fr.heriamc.games.engine.board.GameBoard;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;

public class JumpScadeBoard extends GameBoard<JumpScadeGame, JumpScadePlayer> {

    private int playerCount, kill, death, countdown;
    private String ratio;

    public JumpScadeBoard(JumpScadeGame game, JumpScadePlayer gamePlayer) {
        super(game, gamePlayer);
    }

    @Override
    public void reloadData() {
        this.playerCount = game.getSize();
        this.countdown = game.getWaitingRoom().getCountdownTask().getSecondsLeft().get();
        this.kill = gamePlayer.getKills();
        this.death = gamePlayer.getDeaths();
        this.ratio = gamePlayer.getRatio();
    }

    @Override
    public void setLines(String ip) {
        objectiveSign.clearScores();
        objectiveSign.setDisplayName("§e§l» §6§lJumpScade §e§l«");

        switch (game.getState()) {
            case WAIT -> {
                objectiveSign.setLine(0, "§1");
                objectiveSign.setLine(1, "§8■ §7En attente...");
                objectiveSign.setLine(2, "§2");
                objectiveSign.setLine(3,  "§8■ §7Connectés : §e" + playerCount + "§7/§e" + game.getGameSize().getMaxPlayer());
                objectiveSign.setLine(4, "§3");
                objectiveSign.setLine(6, ip);
            }

            case STARTING -> {
                objectiveSign.setLine(0, "§1");
                objectiveSign.setLine(1, "§8■ §7Début dans: §6" + countdown + "s");
                objectiveSign.setLine(2, "§2");
                objectiveSign.setLine(3,  "§8■ §7Connectés : §e" + playerCount + "§7/§e" + game.getGameSize().getMaxPlayer());
                objectiveSign.setLine(4, "§3");
                objectiveSign.setLine(5, ip);
            }

            case END, IN_GAME -> {
                objectiveSign.setLine(0, "§1");
                objectiveSign.setLine(1, "§8■ §7Tué(s) : §c" + kill);
                objectiveSign.setLine(2, "§8■ §7Mort(s) : §c" + death);
                objectiveSign.setLine(3, "§8■ §7Ratio : §c" + ratio);
                objectiveSign.setLine(4, "§2");
                objectiveSign.setLine(5,  "§8■ §7Connectés : §e" + playerCount + "§7/§e" + game.getGameSize().getMaxPlayer());
                objectiveSign.setLine(6, "§3");
                objectiveSign.setLine(7, ip);
            }
        }

        objectiveSign.updateLines();
    }

}