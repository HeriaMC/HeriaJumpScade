package fr.heriamc.games.jumpscade.player.items;

import fr.heriamc.bukkit.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

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

}
