package com.everest;

import com.everest.entity.GraveEntity;
import com.everest.init.CarnationEntities;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Carnation implements ModInitializer {
	public static final String MODID = "carnation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final GameRules.Key<GameRules.IntRule> XP_SAVE_PERCENTAGE = GameRuleRegistry.register("xpSavingPercentage", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(50));

	@Override
	public void onInitialize() {
		CarnationEntities.register();

		ServerLivingEntityEvents.AFTER_DEATH.register(((entity, damageSource) -> {
			if (!(entity instanceof ServerPlayerEntity player)) return;
			player.sendMessage(Text.literal("☠ You died at: " + (player.getBlockX() + ", " + player.getBlockY() + ", "  + player.getBlockZ())).formatted(Formatting.RED));

			PlayerInventory inventory = player.getInventory();
			List<Pair<Integer, ItemStack>> storedItems = new ArrayList<>();

			for (int slot = 0; slot < inventory.size(); slot++) {
				ItemStack stack = inventory.getStack(slot);
				if (!stack.isEmpty()) {
					storedItems.add(Pair.of(slot, stack.copy()));
					inventory.setStack(slot, ItemStack.EMPTY);
				}
			}

			int xpLevel = player.experienceLevel;
			int xpProgress = (int) (player.experienceProgress * player.getNextLevelExperience());

			player.setExperienceLevel(0);
			player.setExperiencePoints(0);

			if (!storedItems.isEmpty()) {
				GraveEntity grave = new GraveEntity(CarnationEntities.GRAVE_ENTITY_TYPE, player.getWorld());
				grave.setPosition(player.getX(), player.getY(), player.getZ());
				grave.setOwner(player);
				grave.storeItems(storedItems);
				grave.storeXP(xpLevel, xpProgress, player.getWorld().getGameRules().getInt(XP_SAVE_PERCENTAGE));
				player.getWorld().spawnEntity(grave);
			}
		}));

		LOGGER.info("six ft ladder");
	}
}