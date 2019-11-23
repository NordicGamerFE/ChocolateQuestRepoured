package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySummoningCircle extends EntityLivingBase {

	protected ResourceLocation entityToSpawn;
	protected float timeMultiplierForSummon = 1F;
	protected ECircleTexture texture = ECircleTexture.METEOR;
	protected final int BORDER_WHEN_TO_SPAWN_IN_TICKS = 60;

	protected static final DataParameter<Boolean> IS_SPAWNING_PARTICLES = EntityDataManager
			.<Boolean>createKey(EntitySummoningCircle.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Float> ANIMATION_PROGRESS = EntityDataManager
			.<Float>createKey(EntitySummoningCircle.class, DataSerializers.FLOAT);
	protected static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager
			.<Integer>createKey(EntitySummoningCircle.class, DataSerializers.VARINT);

	public enum ECircleTexture {
		ZOMBIE(0), SKELETON(1), FLYING_SKULL(2), FLYING_SWORD(3), METEOR(4),;

		private int textureID = 0;

		public int getTextureID() {
			return this.textureID;
		}

		private ECircleTexture(int textureID) {
			this.textureID = textureID;
		}

		static {
			for (ECircleTexture ect : values()) {
				ect.getTextureID();
			}
		}
	}

	public EntitySummoningCircle(World worldIn) {
		this(worldIn, new ResourceLocation("minecraft", "zombie"), 1F, ECircleTexture.SKELETON);
	}

	public EntitySummoningCircle(World worldIn, ResourceLocation entityToSpawn, float timeMultiplier,
			ECircleTexture textre) {
		super(worldIn);
		setSize(2.0F, 0.005F);
		System.out.println("Mob: " + entityToSpawn);
		this.entityToSpawn = entityToSpawn;
		this.timeMultiplierForSummon = timeMultiplier;
		this.texture = textre;
		this.noClip = true;
		this.dataManager.set(TEXTURE_INDEX, this.texture.getTextureID());
		setHealth(1F);
		this.setEntityInvulnerable(true);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		// Summon entity
		if (this.ticksExisted >= BORDER_WHEN_TO_SPAWN_IN_TICKS * timeMultiplierForSummon && !getEntityWorld().isRemote
				&& world != null && entityToSpawn != null) {
			boolean flag = true;
			try {
				//System.out.println("Summoning: " + entityToSpawn.toString());
				Entity summon = EntityList.createEntityByIDFromName(entityToSpawn, world);
				summon.setUniqueId(MathHelper.getRandomUUID());
				summon.setPosition(posX, posY + 0.5D, posZ);

				world.spawnEntity(summon);

			} catch(NullPointerException ex) {
				flag = false;
			}
			if(flag) {
				this.setDead();
			}
		} else if (this.ticksExisted >= (BORDER_WHEN_TO_SPAWN_IN_TICKS * timeMultiplierForSummon) * 0.8
				&& !isSpawningParticles()) {
			this.dataManager.set(IS_SPAWNING_PARTICLES, true);
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean getIsInvulnerable() {
		return true;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(IS_SPAWNING_PARTICLES, false);
		this.dataManager.register(ANIMATION_PROGRESS, 0F);
		this.dataManager.register(TEXTURE_INDEX, this.texture != null ? this.texture.getTextureID() : 1);
	}

	@SideOnly(Side.CLIENT)
	public int getTextureID() {
		return this.dataManager.get(TEXTURE_INDEX);
	}

	public boolean isSpawningParticles() {
		return this.dataManager.get(IS_SPAWNING_PARTICLES);
	}

	public float getAnimationProgress() {
		return this.dataManager.get(ANIMATION_PROGRESS);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.dataManager.set(IS_SPAWNING_PARTICLES, compound.getBoolean("cqrdata.isSpawningParticles"));
		this.dataManager.set(ANIMATION_PROGRESS, compound.getFloat("cqrdata.animationProgress"));
		this.dataManager.set(TEXTURE_INDEX, compound.getInteger("cqrdata.textureID"));

		this.timeMultiplierForSummon = compound.getFloat("cqrdata.timeMultiplier");
		String resD = compound.getString("cqrdata.entityToSpawn.Domain");
		String resP = compound.getString("cqrdata.entityToSpawn.Path");
		this.entityToSpawn = new ResourceLocation(resD, resP);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setBoolean("cqrdata.isSpawningParticles", this.dataManager.get(IS_SPAWNING_PARTICLES));
		compound.setFloat("cqrdata.animationProgress", this.dataManager.get(ANIMATION_PROGRESS));
		compound.setFloat("cqrdata.timeMultiplier", this.timeMultiplierForSummon);
		compound.setInteger("cqrdata.textureID", this.dataManager.get(TEXTURE_INDEX));
		compound.setString("cqrdata.entityToSpawn.Domain",entityToSpawn.getResourceDomain());
		compound.setString("cqrdata.entityToSpawn.Path", entityToSpawn.getResourcePath());
		System.out.println("RP: " + entityToSpawn.getResourcePath());
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return new ArrayList<ItemStack>();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return new ItemStack(Items.AIR);
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}

}
