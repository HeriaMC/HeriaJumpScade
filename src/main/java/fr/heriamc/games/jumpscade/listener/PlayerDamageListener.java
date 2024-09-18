package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public record PlayerDamageListener(GameManager<JumpScadeGame> gameManager) implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        var game = gameManager.getNullableGame(player);

        if (game == null || game.getState() != GameState.IN_GAME) return;

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null || gamePlayer.isSpectator()) return;

        handleDeath(gamePlayer);
    }

    private void handleDeath(JumpScadePlayer gamePlayer) {
        //BukkitThreading.scheduleSyncDelayedTask(() -> {
            System.out.println("respawn start");
            gamePlayer.getPlayer().spigot().respawn();
            System.out.println("respawn end");
       // }, 1L);

        System.out.println("respawn next");
        // RESET LAST HITTER
        gamePlayer.addDeath();
        gamePlayer.getTeam().getSpawnPoint().teleport(gamePlayer);
    }

}