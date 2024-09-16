package fr.heriamc.games.jumpscade.task;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.utils.task.CountdownTask;
import fr.heriamc.games.engine.utils.task.GameCountdownTask;
import fr.heriamc.games.jumpscade.JumpScadeGame;

public class JumpScadeEndTask extends GameCountdownTask<JumpScadeGame> {

    public JumpScadeEndTask(JumpScadeGame game) {
        super(10, game);
    }

    @Override
    public void onStart() {
        game.setState(GameState.END);

        game.getFirstTeamAlive().ifPresent(team -> {
            team.getMembers().forEach(gamePlayer -> gamePlayer.sendMessage("Vous êtes dans l'équipe gagnante ! uwu"));

            game.broadcast(
                    "---------------------------",
                    "",
                    "Vainqueur: " + team.getFormattedMembersNames(),
                    "Récompense: " + 0 + " ⛃",
                    "",
                    "---------------------------");
        });
    }

    @Override
    public void onNext(CountdownTask task) {
        switch (task.getSecondsLeft().get()) {
            case 5, 10, 15, 20 -> game.broadcast("This game gonna be deleted in " + task.getSecondsLeft().get() + " seconds !");
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