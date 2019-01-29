package com.Whodundid.nameHistory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class PlayerSkin {
	
	static boolean isAlexSkin = false;
	public static ResourceLocation skinPic;
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static void getSkinPic(final String name) {
		Thread getter = new Thread() {
			@Override
			public void run() {
				String uuid = "";
				try {
	    			URL uuidFetcher = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
	                InputStreamReader uuidReader = new InputStreamReader(uuidFetcher.openStream());
	                BufferedReader getUuid = new BufferedReader(uuidReader);
	                String testUUID = getUuid.readLine();
	                if (testUUID != null) {
	                	uuid = testUUID.substring(7, 39);
	                }
	                getUuid.close();
	    		} catch (IOException e) { 
	    			e.printStackTrace(); 
	    		}
				if (uuid != null) {
					try {
						URL skinFetcher = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
						InputStreamReader skinReader = new InputStreamReader(skinFetcher.openStream());
						BufferedReader reader = new BufferedReader(skinReader);
						String skin = reader.readLine();

						skin = skin.replace("[", "").replace("{", "").replace("}", "").replace("\"", "");
						String[] parts = skin.split(":");
						
						byte[] skinBase64 = Base64.decodeBase64(parts[5]);
						String nextPart = new String(skinBase64);
						nextPart = nextPart.replace("[", "").replace("{", "").replace("}", "").replace("\"", "");
						String[] nextParts = nextPart.split(":");
						
						String link = "http:";
						if (nextParts.length > 8) {
							isAlexSkin = true;
							link += nextParts[9];
						} else {
							isAlexSkin = false;
							link += nextParts[7];
						}
						
						System.out.println(link);
						
						URL skinImgFetcher = new URL(link);
						InputStream in = new BufferedInputStream(skinImgFetcher.openStream());
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						byte[] buf = new byte[1024];
						int n = 0;
						while (-1 != (n = in.read(buf))) {
						   out.write(buf, 0, n);
						}
						out.close();
						in.close();
						byte[] response = out.toByteArray();
						
						FileOutputStream fos = new FileOutputStream("img.png");
						fos.write(response);
						fos.close();						
					} catch (Exception e) { 
						e.printStackTrace(); 
					}
				}
			}
		};
		getter.start();
		
		try {
			getter.join();
			File returnFile = new File("img.png");
			if (returnFile.exists()) {			
				DynamicTexture preview = new DynamicTexture(ImageIO.read(returnFile));
				TextureManager man = mc.getTextureManager();
				ResourceLocation loc = man.getDynamicTextureLocation("preview", preview);
				skinPic = loc;
				if (skinPic != null) {
					mc.displayGuiScreen(new SkinDisplayer(loc));
					returnFile.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ResourceLocation getResource() {
		return skinPic;
	}
}