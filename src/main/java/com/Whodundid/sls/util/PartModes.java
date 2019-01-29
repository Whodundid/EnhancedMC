package com.Whodundid.sls.util;

public enum PartModes {
	
	SW("switching"),
	BL("blinking"),
	IN("individual"),
	N("none");
	
	private String currentMode;
	
	PartModes(String mode) {
		currentMode = mode;
	}
	
	public String getMode() { return this.currentMode; }
}