package fr.heriamc.games.jumpscade.waiting;

import fr.heriamc.games.engine.waitingroom.GameWaitingRoom;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.player.items.JumpScadeItems;
import fr.heriamc.games.jumpscade.task.JumpScadeStartingTask;
import lombok.Getter;

@Getter
public class JumpScadeWaitingRoom extends GameWaitingRoom<JumpScadeGame, JumpScadePlayer> {

    public JumpScadeWaitingRoom(JumpScadeGame game) {
        super(game);
        this.countdownTask = new JumpScadeStartingTask(game);
    }

    @Override
    public void onJoin(JumpScadePlayer gamePlayer) {
        var player = gamePlayer.getPlayer();
    }

    @Override
    public void onLeave(JumpScadePlayer gamePlayer) {
        var player = gamePlayer.getPlayer();
    }

    @Override
    public void giveItems(JumpScadePlayer gamePlayer) {
        JumpScadeItems.giveItems(gamePlayer);
    }

}