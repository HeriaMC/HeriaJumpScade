package fr.heriamc.games.jumpscade.task;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.items.JumpScadeGameItems;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class JumpScadeGameCycleTask extends BukkitRunnable {

    private final JumpScadeGame game;
    private final int duration;

    private int timer;

    public JumpScadeGameCycleTask(JumpScadeGame game, int duration) {
        this.game = game;
        this.duration = duration;
        this.timer = duration;
    }

    @Override
    public void run() {
        if (game.getState() != GameState.IN_GAME) {
            cancel();
            return;
        }

        if (timer == 0) {
            game.getAlivePlayers()
                    .forEach(JumpScadeGameItems::giveRandomItem);

            resetTimer();
            return;
        }

        this.timer -= 1;
    }

    public void startTask() {
        BukkitThreading.runTaskTimer(this, 0, 20);
    }

    private void resetTimer() {
        this.timer = duration;
    }

}