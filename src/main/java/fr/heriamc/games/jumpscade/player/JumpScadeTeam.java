package fr.heriamc.games.jumpscade.player;

import fr.heriamc.games.engine.point.SinglePoint;
import fr.heriamc.games.engine.team.GameTeam;
import fr.heriamc.games.engine.team.GameTeamColor;
import fr.heriamc.games.jumpscade.JumpScadeAddon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

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