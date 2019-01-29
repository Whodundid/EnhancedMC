package com.Whodundid.scripts.scriptUtil;

public enum ScriptKeyWord {
	Script,
	Int,
	DInt,
	Bool,
	String,
	For,
	Do,
	While,
	System,
	Player,
	World,
	Server,
	New,
	Delete,
	Try,
	Catch,
	Undefined;
	
	public boolean isStringKeyWord(String in) {
		try {
			valueOf(in);
			return true;
		} catch (IllegalArgumentException e) {}
		
		switch (in) {
		case "script":
		case "int":
		case "dint":
		case "bool":
		case "string":
		case "for":
		case "do":
		case "while":
		case "system":
		case "player":
		case "world":
		case "server":
		case "new":
		case "delete":
		case "try":
		case "catch": return true;
		default: return false;
		}
	}
	
	public ScriptKeyWord getKeyWordFromString(String in) {
		try {
			return valueOf(in);
		} catch (IllegalArgumentException e) {}
		
		switch (in) {
		case "script": return Script;
		case "int": return Int;
		case "dint": return DInt;
		case "bool": return Bool;
		case "string": return String;
		case "for": return For;
		case "do": return Do;
		case "while": return While;
		case "system": return System;
		case "player": return Player;
		case "world": return World;
		case "server": return Server;
		case "new": return New;
		case "delete": return Delete;
		case "try": return Try;
		case "catch": return Catch;
		default: return Undefined;
		}
	}
}
