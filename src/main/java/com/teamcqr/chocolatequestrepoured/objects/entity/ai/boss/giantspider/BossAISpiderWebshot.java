package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileWeb;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAISpiderWebshot extends AbstractCQREntityAI<EntityCQRGiantSpider> {

	private static final int MIN_WEBS = 3;
	private static final int MAX_WEBS = 7;
	private static final int MIN_COOLDOWN = 80;
	private static final int MAX_COOLDOWN = 120;

	private static final int MAX_DISTANCE_TO_TARGET = 20 * 20;
	protected static final double SPEED_MULTIPLIER = 1.3;

	private int cooldown = 100;

	public BossAISpiderWebshot(EntityCQRGiantSpider entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity == null || this.entity.isDead) {
			return false;
		}
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity.hasAttackTarget() && this.entity.getDistanceSq(this.entity.getAttackTarget()) < MAX_DISTANCE_TO_TARGET;
	}

	@Override
	public void startExecuting() {
		if (this.entity == null) {
			return;
		}
		int projCount = DungeonGenUtils.randomBetween(MIN_WEBS, MAX_WEBS, this.entity.getRNG());
		double angle = 180 / projCount;
		Vec3d v = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector()).normalize();
		for (int i = -(projCount / 2); i <= (projCount / 2); i++) {
			Vec3d velo = VectorUtil.rotateVectorAroundY(v, i * angle);
			velo = velo.add(0, 0.1, 0);

			ProjectileBase web = this.entity.getRNG().nextDouble() > 0.8 ? new ProjectilePoisonSpell(this.entity.world, this.entity) : new ProjectileWeb(this.entity.world, this.entity);
			web.motionX = velo.x * SPEED_MULTIPLIER;
			web.motionY = velo.y * SPEED_MULTIPLIER;
			web.motionZ = velo.z * SPEED_MULTIPLIER;
			web.velocityChanged = true;
			this.entity.world.spawnEntity(web);

		}
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

}
