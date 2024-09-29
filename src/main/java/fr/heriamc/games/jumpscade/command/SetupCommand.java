package fr.heriamc.games.jumpscade.command;

import fr.heriamc.games.jumpscade.JumpScadeAddon;

public record SetupCommand(JumpScadeAddon addon) {

    /*@HeriaCommand(name = "test")
    public void test(CommandArgs commandArgs) {
        final var player = commandArgs.getPlayer();

        player.sendMessage(player.getWorld().getName());
    }*/

}