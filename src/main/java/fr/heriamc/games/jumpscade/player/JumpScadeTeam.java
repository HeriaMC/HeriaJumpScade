package fr.heriamc.games.jumpscade.player;

import fr.heriamc.games.engine.point.SinglePoint;
import fr.heriamc.games.engine.team.GameTeam;
import fr.heriamc.games.engine.team.GameTeamColor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JumpScadeTeam extends GameTeam<JumpScadePlayer> {

    private SinglePoint spawnPoint;

    public JumpScadeTeam(int maxSize, GameTeamColor color) {
        super(maxSize, color);
    }

    public void teleportMembers() {
        spawnPoint.syncTeleport(members);
    }

}