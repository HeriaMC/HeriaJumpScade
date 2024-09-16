package fr.heriamc.games.jumpscade.player;

import fr.heriamc.games.engine.player.GamePlayer;

import java.util.UUID;

public class JumpScadePlayer extends GamePlayer<JumpScadeTeam> {

    public JumpScadePlayer(UUID uuid, int kills, int deaths, boolean spectator) {
        super(uuid, kills, deaths, spectator);
    }

}