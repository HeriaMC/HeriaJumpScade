package fr.heriamc.games.jumpscade.waiting;

import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.waitingroom.WaitingRoomItems;
import fr.heriamc.games.jumpscade.JumpScadeAddon;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JumpScadeItems implements WaitingRoomItems {

    TEAM_SELECTOR (4,
            new ItemBuilder(Material.CHEST).setName("§eÉquipes§8・§7Clic droit").build(),
            JumpScadeAddon::openTeamSelector),

    LEAVE (8,
            new ItemBuilder(Material.BED).setName("§cQuitter§8・§7Clic droit").build(),
            JumpScadeAddon::redirectToHub);

    private final int slot;
    private final ItemStack itemStack;
    private final BiConsumer<JumpScadeAddon, Player> consumer;

    public static final EnumSet<JumpScadeItems> items = EnumSet.allOf(JumpScadeItems.class);

    public static Stream<JumpScadeItems> getItemsAsStream() {
        return items.stream();
    }

    public void giveItem(JumpScadePlayer gamePlayer) {
        gamePlayer.getInventory().setItem(slot, itemStack);
    }

    public static void giveItems(JumpScadePlayer gamePlayer) {
        items.forEach(item -> item.giveItem(gamePlayer));
    }

    public static void giveItems(Collection<JumpScadePlayer> collection) {
        collection.forEach(JumpScadeItems::giveItems);
    }

    public static Optional<JumpScadeItems> getByItem(ItemStack itemStack){
        return getItemsAsStream().filter(item -> item.getItemStack().equals(itemStack)).findFirst();
    }

}