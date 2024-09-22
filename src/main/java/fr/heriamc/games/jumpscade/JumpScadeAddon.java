package fr.heriamc.games.jumpscade;

import fr.heriamc.games.api.addon.GameAddon;
import fr.heriamc.games.engine.waitingroom.gui.GameTeamSelectorGui;
import fr.heriamc.games.jumpscade.command.SetupCommand;
import fr.heriamc.games.jumpscade.listener.*;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.pool.JumpScadePool;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class JumpScadeAddon extends GameAddon<JumpScadePool> {

    public JumpScadeAddon() {
        super(new JumpScadePool());
    }

    @Override
    public void enable() {
        pool.loadDefaultGames();

        registerListener(
                new PlayerConnectionListener(pool),
                new CancelListener(pool.getGamesManager()),
                new PlayerInteractListener(this),
                new PlayerMoveListener(pool.getGamesManager()),
                new ProjectileListener(pool.getGamesManager()),
                new PlayerDamageListener(pool.getGamesManager())
        );

        registerCommand(new SetupCommand(this));
    }

    public void openTeamSelector(Player player) {
        pool.getGamesManager().getGame(player, JumpScadePlayer.class,
                (game, gamePlayer) -> openGui(new GameTeamSelectorGui<>(game, gamePlayer, "Équipes")));
    }

    @Override
    public void disable() {

    }

}