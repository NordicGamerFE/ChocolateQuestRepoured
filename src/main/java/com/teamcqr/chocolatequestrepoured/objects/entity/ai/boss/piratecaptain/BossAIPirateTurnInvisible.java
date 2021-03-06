package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class BossAIPirateTurnInvisible extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private int cooldown = 0;
	private int invisibleTime = 0;

	public BossAIPirateTurnInvisible(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity != null && this.entity.getHealth() / this.entity.getMaxHealth() <= 0.5 && this.entity.getAttackTarget() != null && !this.entity.isDead) {
			this.cooldown--;
			return this.cooldown <= 0;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.invisibleTime = 200;
		this.entity.setInvisibleTicks(1);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.invisibleTime > 0;
	}

	@Override
	public void updateTask() {
		boolean disInt = false;
		boolean reInt = false;
		boolean invi = true;
		if (this.invisibleTime <= EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME) {
			reInt = true;
			invi = false;
			this.entity.setInvisibleTicks(this.entity.getInvisibleTicks() - 1);
			this.entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER, 1));
		} else if (this.invisibleTime >= 200 - EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME) {
			disInt = true;
			invi = false;
			this.entity.setInvisibleTicks(this.entity.getInvisibleTicks() + 1);
			this.entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.DAGGER_NINJA, 1));
		}

		this.invisibleTime--;
		if (this.invisibleTime == 0) {
			disInt = false;
			reInt = false;
			invi = false;
			this.cooldown = 100;
		}
		this.entity.setInvisible(invi);
		this.entity.setIsReintegrating(reInt);
		this.entity.setIsDisintegrating(disInt);
	}

}
