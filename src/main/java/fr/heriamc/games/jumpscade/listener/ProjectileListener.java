package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.api.game.GameState;
import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public record ProjectileListener(GameManager<JumpScadeGame> gameManager) implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        switch (event.getEntityType()) {
            case ARROW, SNOWBALL, EGG -> event.setCancelled(false);
            default -> event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile projectile
                && projectile.getShooter() instanceof Player shooter
                && event.getEntity() instanceof Player victim) {

            var game = gameManager.getNullableGame(shooter);

            if (game == null
                    || game.getState() != GameState.IN_GAME
                    || (!game.containsPlayer(shooter)
                    && !game.containsPlayer(victim))) return;

            var shooterGamePlayer = game.getNullablePlayer(shooter);
            var victimGamePlayer = game.getNullablePlayer(victim);

            if (shooterGamePlayer == null || victimGamePlayer == null) return;

            victimGamePlayer.getLastHitter().setKeyAndValue(shooterGamePlayer, projectile.getType());
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow arrow)
            arrow.remove();
    }

}