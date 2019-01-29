package com.Whodundid.worldEditor.EditorUtil;

import com.Whodundid.worldEditor.Editor;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.miscUtil.WorldHelper;
import com.Whodundid.main.util.storageUtil.Vector3DInt;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class EditorSet {
	
	static Minecraft mc = MainMod.getMC();
	
	public static void set(Editor editor) {
		try {
			Vector3DInt pos1 = Editor.pos1;
			Vector3DInt pos2 = Editor.pos2;
			
			int startX, startY, startZ;
			int endX, endY, endZ;
			
			startX = (pos1.x < pos2.x) ? pos1.x : pos2.x;
			startY = (pos1.y < pos2.y) ? pos1.y : pos2.y;
			startZ = (pos1.z < pos2.z) ? pos1.z : pos2.z;
			endX = (pos1.x < pos2.x) ? pos2.x : pos1.x;
			endY = (pos1.y < pos2.y) ? pos2.y : pos1.y;
			endZ = (pos1.z < pos2.z) ? pos2.z : pos1.z;
			
			System.out.println(startX + " " + startY + " " + startZ);
			
			for (int x = startX; x < endX + 1; x++) {
				for (int z = startZ; z < endZ + 1; z++) {
					for (int y = startY; y < endY + 1; y++) {
						int id = 0;
						String blockInput = editor.getGuiInstance().editorTextInput.idInput.getText();
						if (!blockInput.isEmpty()) { id = Integer.parseInt(blockInput); }
						mc.theWorld.setBlockState(new BlockPos(x, y, z), Block.getStateById(id));
					}
				}
			}
		} catch (Exception e) { System.out.println("single player failure"); e.printStackTrace(); }
		/*try {
			if (mc.isSingleplayer()) {
				
			} else {
				mc.thePlayer.sendChatMessage("//set " + editor.getGuiInstance().editorTextInput.idInput.getText());
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		//editor.requestRedraw();
		if (Editor.render3D) {
			//editor.renderer.generateWorld(StaticEditor.getCamPos());
		}
		*/
	}
}
