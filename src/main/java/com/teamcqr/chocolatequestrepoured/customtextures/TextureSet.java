package com.teamcqr.chocolatequestrepoured.customtextures;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public class TextureSet {
	
	private static final Random random = new Random();
	private String name;
	private Map<ResourceLocation, Set<ResourceLocation>> entityTextureMap = new HashMap<>();

	public TextureSet(Properties config, String name) {
		this.name = name;
		try {
			for(String entry : config.stringPropertyNames()) {
				if (entry.startsWith("#")) {
					continue;
				}
				String rlkey = entry.replace('.', ':');
				ResourceLocation resLoc = new ResourceLocation(rlkey);
				String texturesString = config.getProperty(entry, "");
				if(texturesString.isEmpty()) {
					continue;
				}
				texturesString.replaceAll(" ", "");
				//This strings represent the FILE PATHS, not the actual resource locations
				for(String texture : texturesString.split(",")) {
					File tf = new File(CQRMain.CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES.getAbsolutePath() + texture + ".png");
					if(tf.exists()) {
						ResourceLocation rs = new ResourceLocation(Reference.MODID + "_ctts_" + name, texture);
						if(TextureSetManager.loadTexture(tf, rs)) {
							entityTextureMap.getOrDefault(resLoc, new HashSet<ResourceLocation>()).add(rs);
						}
					}
				}
			}
			if(!entityTextureMap.isEmpty()) {
				TextureSetManager.registerTextureSet(this);
			}
		} catch(Exception ex) {
			entityTextureMap.clear();
		}
	}
	
	@Nullable
	public ResourceLocation getRandomTextureFor(Entity ent) {
		ResourceLocation ers = EntityList.getKey(ent);
		if(entityTextureMap.containsKey(ers)) {
			Object[] textures = entityTextureMap.get(ers).toArray();
			int indx = random.nextInt(textures.length);
			return (ResourceLocation) textures[indx];
		}
		return null;
	}
	
	public String getName() {
		return name;
	}

}