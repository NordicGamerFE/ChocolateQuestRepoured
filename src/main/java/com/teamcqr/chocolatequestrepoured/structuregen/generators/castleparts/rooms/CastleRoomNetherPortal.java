package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.GenerationTemplate;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class CastleRoomNetherPortal extends CastleRoomDecoratedBase {
	private enum Alignment {
		HORIZONTAL, VERTICAL
	}

	private Alignment portalAlignment;

	public CastleRoomNetherPortal(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.PORTAL;
		this.maxSlotsUsed = 1;
		this.defaultCeiling = true;
		this.defaultFloor = true;
		this.portalAlignment = this.random.nextBoolean() ? Alignment.HORIZONTAL : Alignment.VERTICAL;
	}

	@Override
	protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		int endX = this.getDecorationLengthX() - 1;
		int endZ = this.getDecorationLengthZ() - 1;
		int halfX = endX / 2;
		int halfZ = endZ / 2;

		int xStart = halfX - 2;
		int xEnd = halfX + 3;
		int zStart = halfZ - 2;
		int zEnd = halfZ + 2;

		Predicate<Vec3i> firstLayer = (v -> (v.getY() == 0));
		Predicate<Vec3i> northEdge = firstLayer.and(v -> (v.getX() >= xStart) && (v.getX() <= xEnd) && (v.getZ() == zStart));
		Predicate<Vec3i> southEdge = firstLayer.and(v -> (v.getX() >= xStart) && (v.getX() <= xEnd) && (v.getZ() == zEnd));
		Predicate<Vec3i> westEdge = firstLayer.and(v -> (v.getZ() >= zStart) && (v.getZ() <= zEnd) && (v.getX() == xStart));
		Predicate<Vec3i> eastEdge = firstLayer.and(v -> (v.getZ() >= zStart) && (v.getZ() <= zEnd) && (v.getX() == xEnd));
		Predicate<Vec3i> portalBot = (v -> (v.getY() == 0) && (v.getZ() == halfZ) && (v.getX() >= xStart + 1) && (v.getX() <= xEnd - 1));
		Predicate<Vec3i> portalTop = (v -> (v.getY() == 4) && (v.getZ() == halfZ) && (v.getX() >= xStart + 1) && (v.getX() <= xEnd - 1));
		Predicate<Vec3i> portalSides = (v -> (v.getY() > 0) && (v.getY() < 4) && (v.getZ() == halfZ) && ((v.getX() == xStart + 1) || (v.getX() == xEnd - 1)));
		Predicate<Vec3i> portalMid = (v -> (v.getY() > 0) && (v.getY() < 4) && (v.getZ() == halfZ) && ((v.getX() > xStart + 1) && (v.getX() < xEnd - 1)));
		Predicate<Vec3i> portal = portalBot.or(portalTop).or(portalSides);
		Predicate<Vec3i> platform = portal.negate().and(firstLayer.and(v -> (v.getX() >= xStart + 1) && (v.getX() <= xEnd - 1) && (v.getZ() >= zStart + 1) && (v.getZ() <= zEnd - 1)));

		GenerationTemplate portalRoomTemplate = new GenerationTemplate(this.getDecorationLengthX(), this.getDecorationLengthY(), this.getDecorationLengthZ());
		portalRoomTemplate.addRule(northEdge, dungeon.getWoodStairBlockState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		portalRoomTemplate.addRule(southEdge, dungeon.getWoodStairBlockState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		portalRoomTemplate.addRule(westEdge, dungeon.getWoodStairBlockState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
		portalRoomTemplate.addRule(eastEdge, dungeon.getWoodStairBlockState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
		portalRoomTemplate.addRule(platform, dungeon.getMainBlockState());
		portalRoomTemplate.addRule(portal, Blocks.OBSIDIAN.getDefaultState());
		portalRoomTemplate.addRule(portalMid, Blocks.PORTAL.getDefaultState());

		HashMap<BlockPos, IBlockState> genMap = portalRoomTemplate.GetGenerationMap(this.getDecorationStartPos(), true);
		genArray.addBlockStateMap(genMap, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
		for (Map.Entry<BlockPos, IBlockState> entry : genMap.entrySet()) {
			if (entry.getValue().getBlock() != Blocks.AIR) {
				this.usedDecoPositions.add(entry.getKey());
			}
		}

	}

	@Override
	protected IBlockState getFloorBlock(DungeonRandomizedCastle dungeon) {
		return dungeon.getMainBlockState();
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}
}
