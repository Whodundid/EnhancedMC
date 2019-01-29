package com.Whodundid.main.global.subMod;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.storageUtil.EArrayList;
import java.io.File;

//Last edited: Dec 28, 2018
//First Added: Oct 16, 2018
//Author: Hunter Bragg

public class RegisteredSubMods {
	
	private static EArrayList<SubMod> registeredMods = new EArrayList();
	
	public static boolean registerSubMod(SubMod modIn) {
		if (!isModAlreadyRegistered(modIn)) { return registeredMods.add(modIn); }
		return false;
	}
	
	public static SubMod getMod(SubModType typeIn) {
		for (SubMod m : registeredMods) { if (m.getModType().equals(typeIn)) { return m; } }
		return null;
	}
	
	public static File getModConfigBaseFileLocation(SubModType typeIn) {
		return new File(MainMod.MODID + "/" + typeIn.modName);
	}
	
	public static boolean isModAlreadyRegistered(SubMod modIn) {
		for (SubMod m : registeredMods) {
			if (m.getModType().equals(modIn.getModType())) { return true; }
		}
		return false;
	}
	
	public static EArrayList<SubMod> getRegisteredModsList() { return registeredMods; }
	
	public static EArrayList<SubMod> getEnabledModsList() {
		EArrayList<SubMod> enabledMods = new EArrayList();
		registeredMods.forEach((m) -> { if (m.isEnabled()) { enabledMods.add(m); } });
		return enabledMods;
	}
	
	public static EArrayList<SubMod> getDisabledModsList() {
		EArrayList<SubMod> disabledMods = new EArrayList();
		registeredMods.forEach((m) -> { if (!m.isEnabled()) { disabledMods.add(m); } });
		return disabledMods;
	}
	
	public static EArrayList<SubModType> getAllModDependencies(SubMod modIn) {
		EArrayList<SubModType> allDependencies = new EArrayList(modIn.getDependencies());
		EArrayList<SubModType> withDep = new EArrayList();
		EArrayList<SubModType> workList = new EArrayList();
		
		allDependencies.forEach(t -> { SubMod m = getMod(t); if (!m.getDependencies().isEmpty()) { withDep.add(m.getModType()); } });
		withDep.forEach(t -> { getMod(t).getDependencies().forEach(d -> { workList.add(d); }); });
		
		while (true) {
			if (!workList.isEmpty()) {
				allDependencies.addAll(workList);
				withDep.clear();
				workList.forEach(t -> { SubMod m = getMod(t); if (!m.getDependencies().isEmpty()) { withDep.add(m.getModType()); } });
				workList.clear();
				withDep.forEach(t -> { getMod(t).getDependencies().forEach(d -> { workList.add(d); }); });
			} else { break; }
		}
		return allDependencies;
	}
	
	public static EArrayList<SubModType> getAllDependantsOfMod(SubMod modIn) {
		EArrayList<SubModType> dependants = new EArrayList();
		EArrayList<SubModType> workList = new EArrayList();
		registeredMods.forEach(m -> { m.getDependencies().forEach(t -> { if (t.equals(modIn.getModType())) { dependants.add(m.getModType()); } }); });
		for (SubMod m : registeredMods) {
			for (SubModType t : m.getDependencies()) {
				for (SubModType d : dependants) {
					if (t.equals(d)) { if (!dependants.contains(m.getModType())) { workList.add(m.getModType()); } }
				}
			}
		}
		dependants.addAll(workList);
		return dependants;
	}
	
	public static EArrayList<SubModType> getAllModsWithDepenency(SubModType typeIn) {
		EArrayList<SubModType> mods = new EArrayList();
		getRegisteredModsList().forEach(m -> { if (m.getDependencies().contains(typeIn)) { mods.add(m.getModType()); } });
		return mods;
	}
	
	public static EArrayList<SubModType> getAllEnabledModsWithDepenency(SubModType typeIn) {
		EArrayList<SubModType> mods = new EArrayList();
		getEnabledModsList().forEach(m -> { if (m.getDependencies().contains(typeIn)) { mods.add(m.getModType()); } });
		return mods;
	}
	
	public static EArrayList<SubModType> getAllDisabledModsWithDepenency(SubModType typeIn) {
		EArrayList<SubModType> mods = new EArrayList();
		getDisabledModsList().forEach(m -> { if (m.getDependencies().contains(typeIn)) { mods.add(m.getModType()); } });
		return mods;
	}
}
