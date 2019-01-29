package com.Whodundid.hotkeys.control.hotKeyUtil;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public enum KeyActionType {
	COMMAND(true),
	COMMAND_PLAYER_HANDHELD_CONDITION(true),
	SCRIPT(true),
	MOD_ACTIVATOR(true),
	MOD_DEACTIVATOR(true),
	GUI_OPENER(true),
	MC_KEYBIND_MODIFIER(false),
	DEBUG(true),
	BUILTINCODE(false),
	UNDEFINED(false);
	
	private boolean canUserCreate;
	
	private KeyActionType(boolean canUserCreateIn) {
		canUserCreate = canUserCreateIn;
	}
	
	public boolean canUserCreate() { return canUserCreate; }
	
	public static String getStringFromType(KeyActionType typeIn) {
		switch (typeIn) {
		case BUILTINCODE: return "Built In Hotkey";
		case COMMAND: return "Command";
		case COMMAND_PLAYER_HANDHELD_CONDITION: return "Command with Item Test";
		case DEBUG: return "Debug Command Runner";
		case GUI_OPENER: return "Gui Opener";
		case MC_KEYBIND_MODIFIER: return "KeyBind Modifier";
		case MOD_ACTIVATOR: return "EnhancedMC Mod Activator";
		case MOD_DEACTIVATOR: return "EnhancedMC Mod Deactivator";
		case SCRIPT: return "Script Runner";
		default: return "Undefined";
		}
	}
	
	public static KeyActionType getActionTypeFromString(String typeIn) {
		try {
			return valueOf(typeIn);
		} catch (IllegalArgumentException e) {}
		
		switch (typeIn.toLowerCase()) {
		case "code": return BUILTINCODE;
		case "command": return COMMAND;
		case "command item test": return COMMAND_PLAYER_HANDHELD_CONDITION;
		case "debug": return DEBUG;
		case "gui opener": return GUI_OPENER;
		case "keybind modifier": return MC_KEYBIND_MODIFIER;
		case "mod activator": return MOD_ACTIVATOR;
		case "mod deactivator": return MOD_DEACTIVATOR;
		case "script": return SCRIPT;
		default: return KeyActionType.UNDEFINED;
		}
	}
}
