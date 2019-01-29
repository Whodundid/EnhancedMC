package com.Whodundid.parkourHelper;

import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class HelperBlock {
	
	public volatile Block helperBlock;
	public volatile BlockPos helperBlockPos;
	public volatile IBlockState helperBlockState;
	public volatile int helperBlockID = 0;
	public volatile Direction jumpDir = Direction.OUT;
	public volatile boolean isXFacing = false;
	public volatile boolean isPositiveXZFacing = false;
	
	public HelperBlock(BlockPos pos) {
		if (pos != null) {
			helperBlockPos = pos;
			helperBlockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
			helperBlock = helperBlockState.getBlock();
			helperBlockID = Block.getIdFromBlock(helperBlock);
			jumpDir = PlayerFacing.getCompassFacingDir();
			isXFacing = PlayerFacing.isXFacing();
			isPositiveXZFacing = PlayerFacing.isPositiveXZFacing();
		}
	}
	
	public synchronized double getJumpPos() {
		double pos = 0;
		try {
			pos = isXFacing ? helperBlockPos.getX() : helperBlockPos.getZ();
			if (isPositiveXZFacing) { pos += 1; }
		} catch (NullPointerException e) { e.printStackTrace(); }
		return pos;
	}
	
	public synchronized Block getHelperBlock() { return helperBlock; }
	public synchronized BlockPos getHelperBlockLocation() { return helperBlockPos; }
	public synchronized Direction getJumpDirection() { return jumpDir; }
	public synchronized boolean getIsXFacing() { return isXFacing; }
	public synchronized int getHelperBlockID() { return helperBlockID; }
	public synchronized IBlockState getHelperBlockState() { return helperBlockState; }
}
