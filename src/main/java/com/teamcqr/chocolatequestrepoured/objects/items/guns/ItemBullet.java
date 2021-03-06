package com.teamcqr.chocolatequestrepoured.objects.items.guns;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.init.CQRItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBullet extends Item {

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getItem() == CQRItems.BULLET_IRON) {
			tooltip.add(TextFormatting.BLUE + "+2.5 " + I18n.format("description.bullet_damage.name"));
		}

		if (stack.getItem() == CQRItems.BULLET_GOLD) {
			tooltip.add(TextFormatting.BLUE + "+3.75 " + I18n.format("description.bullet_damage.name"));
		}

		if (stack.getItem() == CQRItems.BULLET_DIAMOND) {
			tooltip.add(TextFormatting.BLUE + "+5 " + I18n.format("description.bullet_damage.name"));
		}

		if (stack.getItem() == CQRItems.BULLET_FIRE) {
			tooltip.add(TextFormatting.RED + "+5 " + I18n.format("description.bullet_damage.name"));
			tooltip.add(TextFormatting.DARK_RED + I18n.format("description.bullet_fire.name"));
		}
	}

}
