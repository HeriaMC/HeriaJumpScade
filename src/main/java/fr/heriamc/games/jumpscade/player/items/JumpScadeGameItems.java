package fr.heriamc.games.jumpscade.player.items;

import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.CollectionUtils;
import fr.heriamc.games.jumpscade.player.JumpScadePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JumpScadeGameItems {

    SNOW_BALL (new ItemBuilder(Material.SNOW_BALL)
            .setName("§fBoule de Neiges")
            .build()),

    BOW (new ItemBuilder(Material.BOW)
            .setName("§6Arc")
            .setInfinityDurability()
            .build()),

    EGG (new ItemBuilder(Material.EGG)
            .setName("§2Oeufs")
            .build());

    private final ItemStack itemStack;

    public static final EnumSet<JumpScadeGameItems> items = EnumSet.allOf(JumpScadeGameItems.class);

    public static Stream<JumpScadeGameItems> getItemsAsStream() {
        return items.stream();
    }

    public static void giveRandomItem(JumpScadePlayer gamePlayer) {
        CollectionUtils.random(items)
                .map(JumpScadeGameItems::getItemStack)
                .ifPresent(gamePlayer.getInventory()::addItem);
    }

}
