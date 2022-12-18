package com.tristankechlo.toolleveling.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.tristankechlo.toolleveling.config.CommandConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collection;
import java.util.Map;

public final class SuperEnchantCommand {

    private static final DynamicCommandExceptionType NONLIVING_ENTITY_EXCEPTION;
    private static final DynamicCommandExceptionType INCOMPATIBLE_ENCHANTS_EXCEPTION;
    private static final DynamicCommandExceptionType WRONG_ENCHANTS_EXCEPTION;
    private static final DynamicCommandExceptionType ITEMLESS_EXCEPTION;
    private static final SimpleCommandExceptionType FAILED_EXCEPTION;

    static {
        NONLIVING_ENTITY_EXCEPTION = new DynamicCommandExceptionType((entityName) -> {
            return new TranslatableComponent("commands.enchant.failed.entity", entityName);
        });
        INCOMPATIBLE_ENCHANTS_EXCEPTION = new DynamicCommandExceptionType((itemName) -> {
            return new TranslatableComponent("commands.superenchant.failed.incompatible", itemName);
        });
        WRONG_ENCHANTS_EXCEPTION = new DynamicCommandExceptionType((itemName) -> {
            return new TranslatableComponent("commands.superenchant.failed.wrong", itemName);
        });
        ITEMLESS_EXCEPTION = new DynamicCommandExceptionType((entityName) -> {
            return new TranslatableComponent("commands.enchant.failed.itemless", entityName);
        });
        FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslatableComponent("commands.enchant.failed"));
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("superenchant").requires((player) -> {
            return player.hasPermission(3);
        }).then(Commands.argument("targets", EntityArgument.entities())
                .then(Commands.argument("enchantment", ItemEnchantmentArgument.enchantment()).executes((context) -> {
                    return enchant(context.getSource(), EntityArgument.getEntities(context, "targets"),
                            ItemEnchantmentArgument.getEnchantment(context, "enchantment"), 1);
                }).then(Commands.argument("level", IntegerArgumentType.integer(0, Short.MAX_VALUE))
                        .executes((context) -> {
                            return enchant(context.getSource(), EntityArgument.getEntities(context, "targets"),
                                    ItemEnchantmentArgument.getEnchantment(context, "enchantment"),
                                    IntegerArgumentType.getInteger(context, "level"));
                        })))));
    }

    private static int enchant(CommandSourceStack source, Collection<? extends Entity> targets, Enchantment enchantment, int level) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity;
                ItemStack stack = livingentity.getMainHandItem();
                if (!stack.isEmpty()) {
                    if (enchantment.canEnchant(stack) || CommandConfig.allowWrongEnchantments.getValue()) {
                        if (EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantments(stack).keySet(), enchantment)
                                || CommandConfig.allowIncompatibleEnchantments.getValue()) {

                            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                            if (enchantments.containsKey(enchantment) && level == 0) {
                                enchantments.remove(enchantment);
                            } else {
                                enchantments.put(enchantment, level);
                            }
                            EnchantmentHelper.setEnchantments(enchantments, stack);
                            i++;

                        } else if (targets.size() == 1) {
                            throw INCOMPATIBLE_ENCHANTS_EXCEPTION.create(stack.getItem().getName(stack).getString());
                        }
                    } else if (targets.size() == 1) {
                        throw WRONG_ENCHANTS_EXCEPTION.create(stack.getItem().getName(stack).getString());
                    }
                } else if (targets.size() == 1) {
                    throw ITEMLESS_EXCEPTION.create(livingentity.getName().getString());
                }
            } else if (targets.size() == 1) {
                throw NONLIVING_ENTITY_EXCEPTION.create(entity.getName().getString());
            }
        }

        if (i == 0) {
            throw FAILED_EXCEPTION.create();
        } else {
            if (targets.size() == 1) {
                source.sendSuccess(new TranslatableComponent("commands.enchant.success.single", enchantment.getFullname(level), targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslatableComponent("commands.enchant.success.multiple", enchantment.getFullname(level), targets.size()), true);
            }

            return i;
        }
    }
}