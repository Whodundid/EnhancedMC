package com.Whodundid.parkourHelper;

public abstract class JumpIdentifyer {
	
	public enum jumpType {
		ONEBLOCK,
		TWOBLOCK,
		THREEBLOCK,
		FOURBLOCK,
		NEO_ONE,
		NEO_TWO,
		NEO_THREE,
		FIVE35,
		SIX29,
		NONE;
	}
	
	public static jumpType determineTypeOfJump() {
		return jumpType.NONE;
	}
}
