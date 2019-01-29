package com.Whodundid.parkourHelper;

import java.util.ArrayList;

import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.playerUtil.PlayerFacing;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class ParkourUtil {
	
	static Minecraft mc = MainMod.getMC();
	static ParkourAI parkour = (ParkourAI) RegisteredSubMods.getMod(SubModType.PARKOUR);
	public static boolean alreadyRecalc = false;
	
	public static void recalculateHelperBlock() {
		try {
			if (!isAirBeneath() && mc.thePlayer.onGround) {
				ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
				int incrementor = 0;
				double pos1 = 0;
				boolean goNegative = false;
				boolean goX = false;
				switch (PlayerFacing.getCompassFacingDir()) {
				case N:
					goNegative = true;
				case S:
					pos1 = mc.getRenderViewEntity().posZ;
					break;
				case W:
					goNegative = true;
					goX = true;
				case E:
					pos1 = mc.getRenderViewEntity().posX;
					goX = true;
					break;
				default:
					break;
				}
				
				for (int i = 0; i < 7; i++) {
					BlockPos testPos;
					if (goX) {
						testPos = new BlockPos(pos1 + incrementor, mc.getRenderViewEntity().getEntityBoundingBox().minY - 1, mc.getRenderViewEntity().posZ);
					} else {
						testPos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY - 1, pos1 + incrementor);
					}
					if (goNegative) {
						incrementor -= 1;
					} else {
						incrementor += 1;
					}
					
					Block testBlock = mc.theWorld.getBlockState(testPos).getBlock();
					
					try {
						if (!testBlock.getMaterial().blocksMovement()) {
							if (!positions.isEmpty()) {
								BlockPos actual = positions.get(positions.size() - 1);
								BlockPos firstTest = new BlockPos(actual.getX(), actual.getY() + 1, actual.getZ());
								BlockPos secondTest = new BlockPos(actual.getX(), actual.getY() + 2, actual.getZ());
								Block firstTestBlock = mc.theWorld.getBlockState(firstTest).getBlock(); //Bug with half slabs going out of array bounds
								if (WorldHelper.testBlockForPassable(firstTest)) {
									if (!firstTestBlock.getMaterial().blocksMovement()) { /////// FIX WALKABLE BLOCKS ONTOP OF HELPER BLOCK!!! (carpet, grass, redstone, ect.)
										//ChatDrawer.addChatToBeDrawnToScreen(mc.theWorld.getBlockState(actual).getBlock().getLocalizedName(), 50, 50, ChatType.GAME.getChatDisplayColor());
										Block potentialProblemBlock = mc.theWorld.getBlockState(actual).getBlock();
										if (Block.getIdFromBlock(potentialProblemBlock) == 44 || Block.getIdFromBlock(potentialProblemBlock) == 126 || Block.getIdFromBlock(potentialProblemBlock) == 182) {
											actual = positions.get(positions.size() - 1);
										}
										if (WorldHelper.compareID(actual, 323)) {
											
											actual = positions.get(positions.size() - 1);
											parkour.setHelperBlock(new HelperBlock(actual));
										}
										Block secondBlock = mc.theWorld.getBlockState(secondTest).getBlock();
										if (Block.getIdFromBlock(secondBlock) == 96 || Block.getIdFromBlock(secondBlock) == 167) {
											if (!(Boolean) mc.theWorld.getBlockState(secondTest).getProperties().values().toArray()[2]) {
												parkour.setHelperBlock(new HelperBlock(actual));
											}
										} else {
											parkour.setHelperBlock(new HelperBlock(actual));
										}
									}															
								}						
							}
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
									
					positions.add(testPos);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			parkour.getBackgroundChecker().kill();
		}			
	}
	
	public static boolean isAirBeneath() {
		try {
			BlockPos oneBelow = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY - 1, mc.getRenderViewEntity().posZ);
			Block blocky = Minecraft.getMinecraft().theWorld.getBlockState(oneBelow).getBlock();
			if (Block.getIdFromBlock(blocky) == 0 || Block.getIdFromBlock(blocky) == 44 || Block.getIdFromBlock(blocky) == 126 || Block.getIdFromBlock(blocky) == 182) {
				return true;
			}			
		} catch (Exception e) {
			e.printStackTrace();
			parkour.getBackgroundChecker().kill();
		}
		return false;
	}	
}
