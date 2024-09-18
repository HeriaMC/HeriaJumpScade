package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.games.jumpscade.JumpScadeAddon;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public record ProjectileListener(JumpScadeAddon addon) implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        switch (event.getEntityType()) {
            case ARROW, SNOWBALL, EGG -> ((Player) event.getEntity().getShooter()).sendMessage("Vous avez lancer " + event.getEntityType());
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow arrow)
            arrow.remove();
    }

}