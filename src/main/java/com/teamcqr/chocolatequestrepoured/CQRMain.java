package com.teamcqr.chocolatequestrepoured;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.teamcqr.chocolatequestrepoured.command.CommandExport;
import com.teamcqr.chocolatequestrepoured.customtextures.TextureSetManager;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.init.CQRCapabilities;
import com.teamcqr.chocolatequestrepoured.init.CQRDispenseBehaviors;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.init.CQRMaterials;
import com.teamcqr.chocolatequestrepoured.init.CQRMessages;
import com.teamcqr.chocolatequestrepoured.init.CQRSerializers;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBannerPatternsCQ;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.proxy.IProxy;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonRegistry;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.WorldWallGenerator;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.CopyHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.handlers.GuiHandler;

import net.minecraft.block.BlockFire;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:llibrary@[1.7.19]; required:forge@[14.23.5.2817,)")
public class CQRMain {

	@Instance
	public static CQRMain INSTANCE;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static IProxy proxy;

	public static Logger logger = null;

	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_EXPORT_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	public static File CQ_FACTION_FOLDER = null;
	public static File CQ_INHABITANT_FOLDER = null;
	public static File CQ_ITEM_FOLDER = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_SETS = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_ROOT = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = null;

	public static boolean isPhosphorInstalled;
	public static boolean isEntityCullingInstalled;
	
	public static final CreativeTabs CQR_ITEMS_TAB = new CreativeTabs("ChocolateQuestRepouredItemsTab") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(CQRItems.BOOTS_CLOUD);
		}
	};
	public static final CreativeTabs CQR_BLOCKS_TAB = new CreativeTabs("ChocolateQuestRepouredBlocksTab") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(CQRBlocks.TABLE_OAK);
		}
	};
	public static final CreativeTabs CQR_BANNERS_TAB = new CreativeTabs("ChocolateQuestRepouredBannerTab") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.BANNER);
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
			super.displayAllRelevantItems(itemList);
			List<ItemStack> banners = BannerHelper.addBannersToTabs();
			for (ItemStack stack : banners) {
				itemList.add(stack);
			}
		}
	};
	public static final CreativeTabs CQR_DUNGEON_PLACER_TAB = new CreativeTabs("ChocolateQuestRepouredDungeonPlacers") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.STONEBRICK);
		}
	};
	public static final CreativeTabs CQR_EXPORTER_CHEST_TAB = new CreativeTabs("ChocolateQuestRepouredExporterChests") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.CHEST);
		}
	};
	public static final CreativeTabs CQR_SPAWN_EGG_TAB = new CreativeTabs("CQR Spawn Eggs") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.SPAWN_EGG);
		}
	};

	public static final WorldDungeonGenerator DUNGEON_GENERATOR = new WorldDungeonGenerator();
	public static final WorldWallGenerator WALL_GENERATOR = new WorldWallGenerator();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		// Important: This has to be the F I R S T statement
		this.initConfigFolder(event);

		proxy.preInit();

		// !!Custom texture system has to load BEFORE the faction system!!
		// Custom Textures System
		TextureSetManager.loadTextureSetsFromFolder(CQ_CUSTOM_TEXTURES_FOLDER_SETS);

		// Faction system
		FactionRegistry.instance().loadFactions();

		// Enables Dungeon generation in worlds, do not change the number (!) and do NOT
		// remove this line, moving it somewhere else is fine, but it must be called in
		// pre initialization (!)
		GameRegistry.registerWorldGenerator(DUNGEON_GENERATOR, 100);
		GameRegistry.registerWorldGenerator(WALL_GENERATOR, 101);

		// Instantiating enums
		EBannerPatternsCQ.values();
		EBanners.values();

		// Register event handling for dungeon protection system
		// MinecraftForge.EVENT_BUS.register(ProtectedRegionManager.getInstance());
		MinecraftForge.EVENT_BUS.register(CQRSerializers.class);

		CQRMessages.registerMessages();
		CQRCapabilities.registerCapabilities();
		CQRLoottables.registerLootTables();
	}

	private void initConfigFolder(FMLPreInitializationEvent event) {
		CQ_CONFIG_FOLDER = new File(event.getModConfigurationDirectory(), "CQR");
		File[] subfolders = new File[] {
				CQ_DUNGEON_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeons"),
				CQ_CHEST_FOLDER = new File(CQ_CONFIG_FOLDER, "lootconfigs"),
				CQ_STRUCTURE_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "structures"),
				CQ_EXPORT_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "exporter_output"),
				CQ_FACTION_FOLDER = new File(CQ_CONFIG_FOLDER, "factions"),
				CQ_INHABITANT_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeon_inhabitants"),
				CQ_ITEM_FOLDER = new File(CQ_CONFIG_FOLDER, "items"),
				CQ_CUSTOM_TEXTURES_FOLDER_ROOT = new File(CQ_CONFIG_FOLDER, "textures"),
				CQ_CUSTOM_TEXTURES_FOLDER_SETS = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "texture_sets"),
				CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "textures"),
				CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "sync")
		};

		if (!CQ_CONFIG_FOLDER.exists() || CQRConfig.general.reinstallDefaultConfigs) {
			CopyHelper.copyFromJarOrWorkspace("/assets/cqrepoured/defaultConfigs", CQ_CONFIG_FOLDER, true);
		}

		int i = CQ_CONFIG_FOLDER.getAbsolutePath().length();
		for (File folder : subfolders) {
			CopyHelper.copyFromJarOrWorkspace("/assets/cqrepoured/defaultConfigs/" + folder.getAbsolutePath().substring(i), folder, false);
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
		CQRMaterials.setRepairItemsForMaterials();
		// SmeltingHandler.init();
		BlockFire.init();
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();

		isPhosphorInstalled = CQRMain.class.getResource("").getProtocol().equals("jar") && Loader.isModLoaded("phosphor-lighting");
		isEntityCullingInstalled = Loader.isModLoaded("entity_culling");

		DungeonRegistry.getInstance().loadDungeonFiles();
		CQStructure.cacheFiles();
		CQStructure.updateSpecialBlocks();
		CQStructure.updateSpecialEntities();
		ProtectedRegionHelper.updateBreakableBlockWhitelist();
		ProtectedRegionHelper.updatePlaceableBlockWhitelist();
		CQRDispenseBehaviors.registerDispenseBehaviors();
		EntityCQRNetherDragon.reloadBreakableBlocks();
		DungeonInhabitantManager.init();
	}

	@EventHandler
	public static void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandExport());
	}

}
