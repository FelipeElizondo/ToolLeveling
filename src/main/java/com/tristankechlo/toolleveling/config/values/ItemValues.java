package com.tristankechlo.toolleveling.config.values;

import com.google.gson.reflect.TypeToken;
import com.tristankechlo.toolleveling.ToolLeveling;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class ItemValues extends RegistryMapConfig<Item, Long> {

    private static final Type TYPE = new TypeToken<Map<String, Long>>() {}.getType();

    public ItemValues(String identifier) {
        super(identifier, ForgeRegistries.ITEMS, getDefaultItemValues());
    }

    @Override
    protected boolean isKeyValid(Item item, ResourceLocation identifier) {
        if (item == null || item == Items.AIR) {
            ToolLeveling.LOGGER.warn("Item {} not found in registry", identifier);
            return false;
        }
        ItemStack stack = new ItemStack(item);
        if (item.isDamageable(stack)) {
            ToolLeveling.LOGGER.warn("Item {} is damageable, it not a valid item to use in the toolleveling table", identifier);
            return false;
        }
        if (item.isEnchantable(stack)) {
            ToolLeveling.LOGGER.warn("Item {} is enchantable, it not a valid item to use in the toolleveling table", identifier);
            return false;
        }
        return true;
    }

    @Override
    protected boolean isValueValid(Long value) {
        return value >= 0L;
    }

    @Override
    protected Type getType() {
        return TYPE;
    }

    public static Map<Item, Long> getDefaultItemValues() {
        Map<Item, Long> values = new HashMap<>();

        // Ores
        values.put(Items.COAL, 80L);
        values.put(Items.COAL_ORE, 300L);
        values.put(Items.DEEPSLATE_COAL_ORE, 300L);
        values.put(Items.COAL_BLOCK, 730L);

        values.put(Items.COPPER_ORE, 100L);
        values.put(Items.DEEPSLATE_COPPER_ORE, 100L);
        values.put(Items.RAW_COPPER, 110L);
        values.put(Items.COPPER_INGOT, 140L);
        values.put(Items.COPPER_BLOCK, 1260L);
        values.put(Items.RAW_COPPER_BLOCK, 990L);

        values.put(Items.RAW_IRON, 130L);
        values.put(Items.IRON_INGOT, 150L);
        values.put(Items.IRON_ORE, 120L);
        values.put(Items.DEEPSLATE_IRON_ORE, 120L);
        values.put(Items.RAW_IRON_BLOCK, 1170L);
        values.put(Items.IRON_BLOCK, 1350L);

        values.put(Items.GOLD_INGOT, 400L);
        values.put(Items.RAW_GOLD, 350L);
        values.put(Items.GOLD_ORE, 300L);
        values.put(Items.DEEPSLATE_GOLD_ORE, 300L);
        values.put(Items.RAW_GOLD_BLOCK, 3150L);
        values.put(Items.GOLD_BLOCK, 3600L);

        values.put(Items.DIAMOND, 1600L);
        values.put(Items.DIAMOND_ORE, 1600L);
        values.put(Items.DEEPSLATE_DIAMOND_ORE, 1600L);
        values.put(Items.DIAMOND_BLOCK, 14500L);
        values.put(Items.NETHERITE_INGOT, 2000L);
        values.put(Items.NETHERITE_SCRAP, 500L);
        values.put(Items.ANCIENT_DEBRIS, 500L);
        values.put(Items.NETHERITE_BLOCK, 18000L);
        values.put(Items.LAPIS_LAZULI, 80L);
        values.put(Items.LAPIS_ORE, 1200L);
        values.put(Items.DEEPSLATE_LAPIS_ORE, 1200L);
        values.put(Items.LAPIS_BLOCK, 700L);
        values.put(Items.EMERALD, 1000L);
        values.put(Items.EMERALD_ORE, 8000L);
        values.put(Items.DEEPSLATE_EMERALD_ORE, 8000L);
        values.put(Items.EMERALD_BLOCK, 9000L);
        values.put(Items.QUARTZ, 100L);
        values.put(Items.NETHER_QUARTZ_ORE, 400L);
        values.put(Items.QUARTZ_BLOCK, 400L);
        values.put(Items.REDSTONE, 40L);
        values.put(Items.REDSTONE_ORE, 600L);
        values.put(Items.DEEPSLATE_REDSTONE_ORE, 600L);
        values.put(Items.REDSTONE_BLOCK, 360L);
        values.put(Items.GLOWSTONE_DUST, 40L);
        values.put(Items.GLOWSTONE, 150L);

        // other
        values.put(Items.AMETHYST_BLOCK, 110L);
        values.put(Items.AMETHYST_SHARD, 170L);

        // Food
        values.put(Items.GOLDEN_APPLE, 4000L);
        values.put(Items.GOLDEN_CARROT, 1000L);
        values.put(Items.GLISTERING_MELON_SLICE, 1000L);
        values.put(Items.ENCHANTED_GOLDEN_APPLE, 25000L);

        // Drops
        values.put(Items.SLIME_BALL, 250L);
        values.put(Items.SLIME_BLOCK, 2250L);
        values.put(Items.ENDER_PEARL, 200L);
        values.put(Items.BLAZE_ROD, 300L);
        values.put(Items.ENDER_EYE, 500L);
        values.put(Items.BLAZE_POWDER, 150L);
        values.put(Items.MAGMA_CREAM, 500L);
        values.put(Items.GHAST_TEAR, 2000L);
        values.put(Items.NETHER_STAR, 25000L);
        values.put(Items.SHULKER_SHELL, 2000L);
        values.put(Items.END_CRYSTAL, 3000L);
        values.put(Items.EXPERIENCE_BOTTLE, 1000L);
        values.put(Items.DRAGON_EGG, 20000L);
        values.put(Items.DRAGON_HEAD, 20000L);

        // Decorative
        values.put(Items.ENDER_CHEST, 1400L);
        values.put(Items.BEACON, 2500L);
        return values;
    }

}
