package fr.heriamc.games.jumpscade.task;

import fr.heriamc.games.engine.utils.CollectionUtils;
import fr.heriamc.games.engine.utils.task.cycle.CycleTask;
import fr.heriamc.games.engine.utils.task.cycle.GameCycleTask;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.player.items.JumpScadeGameItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class JumpScadeGameCycleTask extends GameCycleTask<JumpScadeGame> {

    private static final ItemStack arrow = new ItemStack(Material.ARROW);

    public JumpScadeGameCycleTask(JumpScadeGame game) {
        super(10, game);
    }

    @Override
    public void onStart() {
        game.getAlivePlayers().forEach(JumpScadePlayer::cleanUp);
    }

    @Override
    public void onNext(CycleTask cycleTask) {
        game.getAlivePlayers()
                .forEach(this::giveRandomItem);
    }

    private void giveRandomItem(JumpScadePlayer gamePlayer) {
        var inventory = gamePlayer.getInventory();

        if (inventory.firstEmpty() == -1) return;

        var randomGameItem = CollectionUtils.oldRandom(JumpScadeGameItems.items).orElseThrow();

        if (randomGameItem == JumpScadeGameItems.BOW) {

            if (inventory.contains(Material.BOW)) {
                inventory.addItem(arrow);
                return;
            }

            inventory.addItem(randomGameItem.getItemStack());

            if (!inventory.contains(Material.ARROW))
                inventory.addItem(arrow);

            return;
        }

        var random = ThreadLocalRandom.current().nextInt(1, 4);
        var randomItem = randomGameItem.getItemStack().clone();

        randomItem.setAmount(random);
        inventory.addItem(randomItem);
    }

    @Override
    public void onComplete() {
        game.getAlivePlayers()
                .forEach(JumpScadePlayer::cleanUp);
    }

    @Override
    public void onCancel() {}

}