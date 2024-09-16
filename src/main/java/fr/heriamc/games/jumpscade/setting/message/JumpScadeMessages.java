package fr.heriamc.games.jumpscade.setting.message;

import lombok.Getter;

@Getter
public enum JumpScadeMessages {

    PREFIX ("§6§lJUMPSCADE §8§l┃ "),

    STARTING_CANCELLED ("§cLancement de la partie annuler..."),
    STARTING_MESSAGE ("§fLancement dans §e%d §fsecondes !");

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

    public String getMessageWithoutPrefix() {
        return message;
    }

}
