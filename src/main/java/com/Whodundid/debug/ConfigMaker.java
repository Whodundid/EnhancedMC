package com.Whodundid.debug;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.Vector3D;
import java.io.File;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;

//Last edited: Jan 23, 2019
//First Added: Jan 23, 2019
//Author: Hunter Bragg

public class ConfigMaker {
	
	public static ConfigMaker currentConfigMaker = null;
	
	static public Minecraft mc = MainMod.getMC();
	static EArrayList<StorageBox<String, WorldLocation>> spawnPoints = new EArrayList();
	static EArrayList<StorageBox<String, WorldLocation>> initialSpawnPoints = new EArrayList();
	static HypixelGameType configType = HypixelGameType.none;
	static int playerCount = 0;
	static String mapName = "";
	static String teamName = "";
	static boolean initial = false;
	
	public static void createNewConfigMaker(HypixelGameType gameIn) {
		if (currentConfigMaker != null) { currentConfigMaker = new ConfigMaker(gameIn); }
	}
	
	public static void destroyConfigMaker() {
		currentConfigMaker = null;
		spawnPoints.clear();
		initialSpawnPoints.clear();
		configType = HypixelGameType.none;
		playerCount = 0;
		mapName = "";
	}
	
	private ConfigMaker(HypixelGameType gameIn) {
		configType = gameIn;
	}
	
	public static void createConfig() {
		switch (configType) {
		case quake: createQuakeConfig(); break;
		default: break;
		}
	}
	
	public static void addSpawnPoint() {
		if (initial) {
			if (mc.thePlayer != null) {
				initialSpawnPoints.add(new StorageBox(teamName, new WorldLocation(new Vector3D(mc.thePlayer.getPosition()), mc.thePlayer.rotationYaw, 0)));
				System.out.println("added " + teamName + " initial spawnPoint: " + mc.thePlayer.getPosition() + ", " + mc.thePlayer.rotationYaw + ", " + 0);
			}
		} else {
			if (mc.thePlayer != null) {
				spawnPoints.add(new StorageBox(teamName, new WorldLocation(new Vector3D(mc.thePlayer.getPosition()), mc.thePlayer.rotationYaw, 0)));
				System.out.println("added " + teamName + " spawnPoint: " + mc.thePlayer.getPosition() + ", " + mc.thePlayer.rotationYaw + ", " + 0);
			}
		}
	}
	
	public static void setMapName(String mapNameIn) { mapName = mapNameIn; }
	public static void setPlayerCount(int playerCountIn) { playerCount = playerCountIn; }
	public static void setTeamName(String teamNameIn) { teamName = teamNameIn; }
	public static void setInitial(boolean val) { initial = val; }
	
	public static void createQuakeConfig() {
		System.out.println("ye");
		File hypixelConfigFolder = new File("EnhancedMC/HypixelConfigs/");
		if (!hypixelConfigFolder.exists()) { hypixelConfigFolder.mkdirs(); }
		
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter("EnhancedMC/HypixelConfigs/config.yml", "UTF-8");
			
			writer.println("worldName: " + mapName);
			writer.println("maxPlayers: " + playerCount);
			writer.println("spawnPoint: ");
			writer.println("mode: ");
			writer.println("lockedTime: ");
			writer.println("doMapFix: true");
			
			writer.println("spawnPoints:");
			
			for (StorageBox<String, WorldLocation> p : spawnPoints) {
				WorldLocation l = p.getValue();
				Vector3D pos = l.getPosition();
				writer.println("- '" + pos.x + "," + pos.y + "," + pos.z + "," + l.pitch + "," + l.yaw + "'");
			}
			
			/*if (initialSpawnPoints.size() > 0) { writer.println("initialSpawns:"); }
			String currentTeam = "";
			for (StorageBox<String, WorldLocation> p : initialSpawnPoints) {
				if (!currentTeam.equals(p.getObject())) { currentTeam = p.getObject(); writer.println("  " + p.getObject() + ":"); }
				WorldLocation l = p.getValue();
				Vector3D pos = l.getPosition();
				writer.println("    - '" + pos.x + "," + pos.y + "," + pos.z + "," + l.pitch + "," + l.yaw + "'");
			}
			
			if (spawnPoints.size() > 0) { writer.println("randomSpawns:"); }
			currentTeam = "";
			for (StorageBox<String, WorldLocation> p : spawnPoints) {
				if (!currentTeam.equals(p.getObject())) { currentTeam = p.getObject(); writer.println("  " + p.getObject() + ":"); }
				WorldLocation l = p.getValue();
				Vector3D pos = l.getPosition();
				writer.println("    - '" + pos.x + "," + pos.y + "," + pos.z + "," + l.pitch + "," + l.yaw + "'");
			}*/
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { if (writer != null) { writer.close(); } }
	}
}
