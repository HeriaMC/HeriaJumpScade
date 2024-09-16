package fr.heriamc.games.jumpscade.command;

import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.games.engine.waitingroom.gui.GameTeamSelectorGui;
import fr.heriamc.games.jumpscade.JumpScadeAddon;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;

public record SetupCommand(JumpScadeAddon addon) {

    @HeriaCommand(name = "test")
    public void test(CommandArgs commandArgs) {
        final var player = commandArgs.getPlayer();

        addon.getPool().getGamesManager().getGame(player, JumpScadePlayer.class,
                (game, gamePlayer) -> addon.openGui(new GameTeamSelectorGui<>(game, gamePlayer, "Équipes")));
    }

}