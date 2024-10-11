package fr.heriamc.games.jumpscade.player;

import fr.heriamc.games.engine.player.GamePlayer;
import fr.heriamc.games.engine.team.GameTeam;
import fr.heriamc.games.engine.utils.Pair;
import fr.heriamc.games.jumpscade.utils.NameTag;
import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.UUID;

@Getter
public class JumpScadePlayer extends GamePlayer<JumpScadeTeam> {

    private final Pair<JumpScadePlayer, EntityType> lastHitter;

    public JumpScadePlayer(UUID uuid, int kills, int deaths, boolean spectator) {
        super(uuid, kills, deaths, spectator);
        this.lastHitter = new Pair<>();
    }

    @Override
    public <G extends GamePlayer<?>> void setTeam(GameTeam<G> team) {
        super.setTeam(team);
        NameTag.setNameTag(player,
                team == null ? "§7" : team.getColoredName() + " ", " ",
                team == null ? 999 : team.getColor().ordinal());
    }

    public void cleanUp() {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(player.getMaxHealth());
        player.getActivePotionEffects().clear();
        player.setLevel(0);
        player.setExp(0);
        player.setFoodLevel(20);
    }

}