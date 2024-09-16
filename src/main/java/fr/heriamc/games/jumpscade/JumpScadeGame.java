package fr.heriamc.games.jumpscade;

import fr.heriamc.games.engine.Game;
import fr.heriamc.games.engine.region.Region;
import fr.heriamc.games.engine.team.GameTeamColor;
import fr.heriamc.games.engine.utils.GameSizeTemplate;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.player.JumpScadeTeam;
import fr.heriamc.games.jumpscade.setting.JumpScadeMapManager;
import fr.heriamc.games.jumpscade.setting.JumpScadeSettings;
import fr.heriamc.games.jumpscade.task.JumpScadeEndTask;
import fr.heriamc.games.jumpscade.waiting.JumpScadeWaitingRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JumpScadeGame extends Game<JumpScadePlayer, JumpScadeTeam, JumpScadeSettings> {

    private final JumpScadeWaitingRoom waitingRoom;
    private final JumpScadeEndTask endTask;

    private Region winRegion;

    public JumpScadeGame() {
        super("jumpscade", new JumpScadeSettings(GameSizeTemplate.SIZE_1V1.toGameSize()));
        this.settings.setGameMapManager(new JumpScadeMapManager(this));
        this.waitingRoom = new JumpScadeWaitingRoom(this);
        this.endTask = new JumpScadeEndTask(this);
    }

    public JumpScadeGame(UUID uuid) {
        this();
    }

    @Override
    public void load() {
        settings.getGameMapManager().setup();
    }

    @Override
    public JumpScadeTeam defaultGameTeam(int size, GameTeamColor gameTeamColor) {
        return new JumpScadeTeam(size, gameTeamColor);
    }

    @Override
    public JumpScadePlayer defaultGamePlayer(UUID uuid, boolean spectator) {
        return new JumpScadePlayer(uuid, 0, 0, spectator);
    }

}