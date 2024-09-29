package fr.heriamc.games.jumpscade.setting.message;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum JumpScadeMessages {

    PREFIX ("§6§lJUMPSCADE §8┃ "),

    STARTING_CANCELLED ("§cLancement de la partie annuler..."),
    STARTING_MESSAGE ("§fLancement dans §e%d §fsecondes !"),

    DEATH_MESSAGE ("§fVous avez été tué par §c%s §f%s"),
    KILL_MESSAGE ("§fTu as tué §c%s §f!"),
    VOID_DEATH_MESSAGE ("§c%s §fest tombé..."),

    END_VICTORY_MESSAGE (
            "§7§m---------------------------",
            "",
            "Vainqueur: %s",
            "Récompenses: %d ⛃",
            "",
            "§7§m---------------------------"),

    END_BACK_TO_HUB ("§fRetour au hub dans §e%d §fsecondes !");

    private String message;
    private String[] messages;

    JumpScadeMessages(String message) {
        this.message = message;
    }

    JumpScadeMessages(String... messages) {
        this.messages = messages;
    }

    public String getMessage() {
        return PREFIX.message + message;
    }

    public String getMessage(Object... objects) {
        return PREFIX.message + message.formatted(objects);
    }

    public String[] getMessages(Object... objects) {
        return Arrays.stream(messages).map(string -> string.formatted(objects)).toArray(String[]::new);
    }

    public String getMessageWithoutPrefix() {
        return message;
    }

}
