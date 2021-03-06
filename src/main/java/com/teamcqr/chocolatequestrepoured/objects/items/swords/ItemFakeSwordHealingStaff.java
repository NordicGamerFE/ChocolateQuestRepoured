package com.teamcqr.chocolatequestrepoured.objects.items.swords;

import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.items.IFakeWeapon;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;

import net.minecraft.item.ItemSword;

public class ItemFakeSwordHealingStaff extends ItemSword implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(ToolMaterial material) {
		super(material);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING;
	}

}
