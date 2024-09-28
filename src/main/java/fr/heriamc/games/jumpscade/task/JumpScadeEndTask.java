package fr.heriamc.games.jumpscade.task;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.utils.task.countdown.CountdownTask;
import fr.heriamc.games.engine.utils.task.countdown.GameCountdownTask;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.setting.message.JumpScadeMessages;

public class JumpScadeEndTask extends GameCountdownTask<JumpScadeGame> {

    public JumpScadeEndTask(JumpScadeGame game) {
        super(10, game);
    }

    @Override
    public void onStart() {
        game.getGameCycleTask().end();
        game.setState(GameState.END);

        game.getFirstTeamAlive().ifPresent(team -> game.broadcast(JumpScadeMessages.END_VICTORY_MESSAGE.getMessages(team.getFormattedMembersNames(), 0)));
    }

    @Override
    public void onNext(CountdownTask task) {
        switch (task.getSecondsLeft().get()) {
            case 5, 10, 15, 20 -> game.broadcast(JumpScadeMessages.END_BACK_TO_HUB.getMessage(task.getSecondsLeft().get()));
        }
    }

    @Override
    public void onComplete() {
        game.getPlayers().values().forEach(GameApi.getInstance()::redirectToHub);
        game.endGame();
    }

    @Override
    public void onCancel() {
        log.info("[JumpScadeEndTask] SOMETHING WRONG HAPPEN SHOULD NOT BE CALLED");
    }

}