package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorVegetatedCave;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DungeonVegetatedCave extends DungeonBase {
	
	private Block vineBlock;
	private Block airBlock;
	private Block pumpkinBlock;
	private Block[] flowerBlocks;
	private Block[] mushrooms;
	private Block[] floorBlocks;
	private int centralCaveSize = 20;
	private int caveCount = 3;
	private int caveSegmentCount = 8;
	private int posY = 30;
	private boolean placeVines;
	private boolean placeVegetation;
	private boolean placeBuilding;
	private File buildingFolder;
	
	public DungeonVegetatedCave(String name, Properties prop) {
		super(name, prop);
		this.vineBlock = PropertyFileHelper.getBlockProperty(prop, "vineBlock", Blocks.VINE);
		this.airBlock = PropertyFileHelper.getBlockProperty(prop, "airBlock", Blocks.AIR);
		this.pumpkinBlock = PropertyFileHelper.getBlockProperty(prop, "lanternBlock", Blocks.LIT_PUMPKIN);
		this.flowerBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "flowerBlocks", new Block[] {
			Blocks.RED_FLOWER,
			Blocks.YELLOW_FLOWER
		});
		this.mushrooms = PropertyFileHelper.getBlockArrayProperty(prop, "mushroomBlocks", new Block[] {
			Blocks.BROWN_MUSHROOM,
			Blocks.RED_MUSHROOM
		});
		this.floorBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "floorBlocks", new Block[] {
			Blocks.GRASS,
		});
		this.placeVines = PropertyFileHelper.getBooleanProperty(prop, "placeVines", true);
		this.placeVegetation = PropertyFileHelper.getBooleanProperty(prop, "placeVegetation", true);
		this.placeBuilding = PropertyFileHelper.getBooleanProperty(prop, "placeBuilding", true);
		this.buildingFolder = PropertyFileHelper.getFileProperty(prop, "buildingFolder", "caves/swamp");
		this.centralCaveSize = PropertyFileHelper.getIntProperty(prop, "centralCaveSize", 15);
		this.posY = PropertyFileHelper.getIntProperty(prop, "posY", 30);
		this.caveCount = PropertyFileHelper.getIntProperty(prop, "caveCount", 3);
		this.caveSegmentCount = PropertyFileHelper.getIntProperty(prop, "caveSegmentCount", 8);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		GeneratorVegetatedCave generator = new GeneratorVegetatedCave(this);
		generator.generate(world, world.getChunkFromBlockCoords(new BlockPos(x,y,z)), x, posY, z);
	}
	
	public File getRandomCentralBuilding() {
		return getStructureFileFromDirectory(buildingFolder);
	}
	
	public Block getVineBlock() {
		return vineBlock;
	}
	
	public Block getFlowerBlock(Random rdm) {
		return flowerBlocks[rdm.nextInt(flowerBlocks.length)];
	}
	
	public Block getMushroomBlock(Random rdm) {
		return mushrooms[rdm.nextInt(mushrooms.length)];
	}
	
	public Block getFloorBlock(Random rdm) {
		return floorBlocks[rdm.nextInt(floorBlocks.length)];
	}
	
	public boolean placeVegetation() {
		return placeVegetation;
	}
	
	public boolean placeBuilding() {
		return placeBuilding;
	}
	
	public boolean placeVines() {
		return placeVines;
	}

	public Block getAirBlock() {
		return airBlock;
	}

	public Block getPumpkinBlock() {
		return pumpkinBlock;
	}
	
	public int getCentralCaveSize() {
		return this.centralCaveSize;
	}
	
	public int getCaveCount() {
		return this.caveCount;
	}
	
	public int getCaveSegmentCount() {
		return this.caveSegmentCount;
	}

}