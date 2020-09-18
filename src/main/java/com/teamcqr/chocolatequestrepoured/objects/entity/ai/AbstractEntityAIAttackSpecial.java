package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;

public abstract class AbstractEntityAIAttackSpecial {

	protected final Random random = new Random();
	protected boolean needsTargetToContinue;
	protected boolean needsSightToContinue;
	protected final int cooldown;
	protected int tick;

	public AbstractEntityAIAttackSpecial(boolean needsTargetToContinue, boolean needsSightToContinue, int cooldown) {
		this.needsTargetToContinue = needsTargetToContinue;
		this.needsSightToContinue = needsSightToContinue;
		this.cooldown = cooldown;
	}

	public abstract boolean shouldStartAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	public abstract boolean shouldContinueAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	public abstract boolean isInterruptible(AbstractEntityCQR entity);

	public abstract void startAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	public abstract void resetAttack(AbstractEntityCQR attacker);

	public abstract void continueAttack(AbstractEntityCQR attacker, EntityLivingBase target, int tick);

	public double getAttackRange(AbstractEntityCQR attacker, EntityLivingBase target) {
		return attacker.getAttackReach(target);
	}

	public boolean needsTargetToContinue() {
		return needsTargetToContinue;
	}

	public boolean needsSightToContinue() {
		return needsSightToContinue;
	}

	public int getCooldown(AbstractEntityCQR attacker) {
		return this.cooldown;
	}

}
