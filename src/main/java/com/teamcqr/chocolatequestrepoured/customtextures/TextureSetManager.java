package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.CustomTexturesPacket;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureSetManager {

	private Map<String, TextureSet> textureSets = new HashMap<>();
	private static TextureSetManager INSTANCE;

	private TextureSetManager() {
	}

	public static TextureSetManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextureSetManager();
		}
		return INSTANCE;
	}

	public static void registerTextureSet(TextureSet set) {
		getInstance().registerTextureSetImpl(set);
	}

	public static void unloadTextures() {
		try {
			getInstance().unloadTexturesImpl();
		} catch (NoSuchMethodError ex) {
			// Ignore
		}
	}

	public static void sendTexturesToClient(EntityPlayerMP joiningPlayer) {
		try {
			getInstance().sendTexturesToClientImpl(joiningPlayer);
		} catch (NoSuchMethodError ex) {
			// Ignore
		}
	}

	@SideOnly(Side.SERVER)
	private void sendTexturesToClientImpl(EntityPlayerMP joiningPlayer) {
		CustomTexturesPacket packet = new CustomTexturesPacket();
		for (File texture : TextureSet.getLoadedTextures()) {
			String base64 = CompressionUtil.encodeFileToBase64(texture);
			String path = texture.getAbsolutePath().replaceFirst(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES.getAbsolutePath(), "");

			packet.addPair(base64, path);
		}
		for (TextureSet ts : this.textureSets.values()) {
			packet.addTextureSet(ts);
		}
		CQRMain.NETWORK.sendTo(packet, joiningPlayer);
	}

	private void registerTextureSetImpl(TextureSet set) {
		this.textureSets.put(set.getName(), set);
	}

	@SideOnly(Side.CLIENT)
	private void unloadTexturesImpl() {
		for (TextureSet set : this.textureSets.values()) {
			for (ResourceLocation rs : set.getTextures()) {
				try {
					TextureUtil.unloadTexture(rs);
				} catch (Exception ex) {
					// Ignore
				}
			}
			set.clearTextureCache();
		}
		this.textureSets.clear();
	}

}
