package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import fr.heriamc.games.jumpscade.setting.message.JumpScadeMessages;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.function.Function;

public record PlayerDamageListener(GameManager<JumpScadeGame> gameManager) implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        var game = gameManager.getNullableGame(player);

        event.getDrops().clear();
        event.setDroppedExp(0);

        if (game == null || game.getState() != GameState.IN_GAME) return;

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null || gamePlayer.isSpectator()) return;

        handleDeath(game, gamePlayer);
    }

    /*
        NEED TO CHECK TIME WHEN THE VICTIM GET HIT AND RESET THE LAST HITTER ECT
     */
    private void handleDeath(JumpScadeGame game, JumpScadePlayer gamePlayer) {
        gamePlayer.getPlayer().spigot().respawn();

        var lastHitter = gamePlayer.getLastHitter().getKey();

        sendDeathMessage(game, gamePlayer);

        if (lastHitter != null)
            lastHitter.addKill();

        gamePlayer.addDeath();
        gamePlayer.getLastHitter().setBothNull();
        gamePlayer.getTeam().getSpawnPoint().teleport(gamePlayer);
    }

    private void sendDeathMessage(JumpScadeGame game, JumpScadePlayer gamePlayer) {
        var lastHitter = gamePlayer.getLastHitter();

        if (lastHitter.bothIsNull()) {
            game.broadcast(JumpScadeMessages.VOID_DEATH_MESSAGE.getMessage(gamePlayer.getName()));
            return;
        }

        var killer = lastHitter.getKey();
        var projectile = lastHitter.getValue();

        killer.sendMessage(JumpScadeMessages.KILL_MESSAGE.getMessage(gamePlayer.getName()));
        gamePlayer.sendMessage(JumpScadeMessages.DEATH_MESSAGE.getMessage(killer.getName(), func().apply(projectile)));
    }

    private Function<EntityType, String> func() {
        return entityType -> switch (entityType) {
            case SNOWBALL -> "avec des Boule de Neiges !";
            case EGG -> "avec un §2Oeufs §f!";
            case ARROW -> "avec un §6Arc §f!";
            default -> "avec son esprit gonesque";
        };
    }

}