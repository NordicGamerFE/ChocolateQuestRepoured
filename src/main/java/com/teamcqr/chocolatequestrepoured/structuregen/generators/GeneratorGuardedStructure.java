package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonGuardedCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlockSpecial;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartCover;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartPlateau;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class GeneratorGuardedStructure extends AbstractDungeonGenerator<DungeonGuardedCastle> {

	// DONE? remake the part where the dungeons are chosen and the support hills are being built, it does not work how it should atm...

	private Map<BlockPos, CQStructure> toGenerate = new HashMap<>();
	private Map<BlockPos, PlacementSettings> settingsMap = new HashMap<>();

	public GeneratorGuardedStructure(World world, BlockPos pos, DungeonGuardedCastle dungeon, Random rand) {
		super(world, pos, dungeon, rand);
	}
	
	private void processStructure(CQStructure structure, BlockPos position) {
		PlacementSettings settings = new PlacementSettings();
		if (this.dungeon.rotateDungeon()) {
			settings.setRotation(Rotation.values()[this.random.nextInt(Rotation.values().length)]);
			settings.setMirror(Mirror.values()[this.random.nextInt(Mirror.values().length)]);
		}
		BlockPos structurePos = DungeonGenUtils.getCentralizedPosForStructure(position, structure, settings);
		if(this.dungeon.doBuildSupportPlatform()) {
			BlockPos startPos = structurePos.up(this.dungeon.getUnderGroundOffset()).down();
			BlockPos endPos = startPos.add(Template.transformedBlockPos(settings, new BlockPos(structure.getSize().getX() - 1, 0, structure.getSize().getZ() - 1)));
			BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
			BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
			this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ(), this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), CQRConfig.general.supportHillWallSize));
		}
		this.settingsMap.put(structurePos, settings);
		this.toGenerate.put(structurePos, structure);
	}

	@Override
	public void preProcess() {
		int buildings = DungeonGenUtils.randomBetween(this.dungeon.getMinBuildings(), this.dungeon.getMaxBuilding(), this.random);
		Double degrees = 360.0 / buildings;
		
		File structure = this.dungeon.getStructureFileFromDirectory(this.dungeon.getCenterStructureFolder(), this.random);
		BlockPos position = this.pos;
		for (int i = 0; i < buildings; i++) {
			processStructure(loadStructureFromFile(structure), position);
			structure = this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFolder(), this.random);
			Vec3i v = new Vec3i(DungeonGenUtils.randomBetween(this.dungeon.getMinDistance(), this.dungeon.getMaxDistance(), this.random), 0, 0);
			v = VectorUtil.rotateVectorAroundY(v, degrees * i);
			position = this.pos.add(v);
			position = new BlockPos(position.getX(), this.dungeon.getYForPos(getWorld(), position.getX(), position.getZ(), this.random), position.getZ());
		}
		processStructure(loadStructureFromFile(structure), position);

		
	}

	@Override
	public void buildStructure() {
		String mobType = this.dungeon.getDungeonMob();
		if (mobType.equalsIgnoreCase(DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT)) {
			mobType = DungeonInhabitantManager.getInhabitantDependingOnDistance(this.world, this.pos.getX(), this.pos.getZ()).getName();
		}
		for(Map.Entry<BlockPos, CQStructure> entry : this.toGenerate.entrySet()) {
			PlacementSettings settings = this.settingsMap.get(entry.getKey());
			CQStructure structure = entry.getValue();
			BlockPos structurePos = entry.getKey();
			
			this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, structurePos, structure.getBlockInfoList(), settings, mobType));
			this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, structurePos, structure.getEntityInfoList(), settings, mobType));
			this.dungeonGenerator.add(new DungeonPartBlockSpecial(this.world, this.dungeonGenerator, structurePos, structure.getSpecialBlockInfoList(), settings, mobType));
		}
	}

	@Override
	public void postProcess() {
		if(this.dungeon.isCoverBlockEnabled()) {
			for(Map.Entry<BlockPos, CQStructure> entry : this.toGenerate.entrySet()) {
				PlacementSettings settings = this.settingsMap.get(entry.getKey());
				CQStructure structure = entry.getValue();
				BlockPos structurePos = entry.getKey();
				
				BlockPos startPos = structurePos;
				BlockPos endPos = startPos.add(Template.transformedBlockPos(settings, new BlockPos(structure.getSize().getX() - 1, 0, structure.getSize().getZ() - 1)));
				BlockPos pos1 = DungeonGenUtils.getValidMinPos(startPos, endPos);
				BlockPos pos2 = DungeonGenUtils.getValidMaxPos(startPos, endPos);
				this.dungeonGenerator.add(new DungeonPartCover(this.world, this.dungeonGenerator, pos1.getX(), pos1.getZ(), pos2.getX(), pos2.getZ(), this.dungeon.getCoverBlock()));
			}
		}
	}

}
