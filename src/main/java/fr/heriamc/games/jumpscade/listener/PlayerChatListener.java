package fr.heriamc.games.jumpscade.listener;

import fr.heriamc.bukkit.chat.event.HeriaChatEvent;
import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.jumpscade.JumpScadeGame;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record PlayerChatListener(GameManager<JumpScadeGame> gameManager) implements Listener {

    // SHIT THING TO BE REMOVED FAST :)
    @EventHandler
    public void onChat(HeriaChatEvent event) {
        event.setCancelled(true);
        var player = event.getPlayer();
        var game = gameManager.getNullableGame(player);

        if (game == null) return;

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null) return;

        var team = gamePlayer.getTeam();
        var space = new TextComponent(" ");
        var finalText = new TextComponent((team == null ? "§7" : team.getColor().getChatColor().toString())
                + ChatColor.stripColor(event.getDisplayedRank()) + event.getName()
                + " §8» §f" + event.getFormattedMessage());

        game.getPlayers().values().forEach(g -> g.getPlayer().spigot().sendMessage(event.getReportComponent(), space, finalText));
    }

}
