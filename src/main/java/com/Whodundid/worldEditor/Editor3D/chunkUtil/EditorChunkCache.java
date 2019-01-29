package com.Whodundid.worldEditor.Editor3D.chunkUtil;

import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.Editor3D.block.EditorBlock;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.DPolygon;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.storageUtil.Vector3DInt;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

//Last edited: 11-20-18
//First Added: 9-10-18
//Author: Hunter Bragg

public class EditorChunkCache {
	
	static Minecraft mc = MainMod.getMC();
	ArrayList<EditorChunk> chunks = new ArrayList();
	ArrayList<DPolygon> worldVertexData = new ArrayList();
	int viewDistance = 2;
	int startX = 0;
	int startY = 0;
	int startZ = 0;
	int endX = 0;
	int endY = 0;
	int endZ = 0;
	
	public void AssembleCache() {
		synchronized (chunks) {
			double chunkStartX = (Math.floor(Editor.getCamPos().x / 16) * 16);
			double chunkStartY = (Math.floor(Editor.getCamPos().y / 16) * 16);
			double chunkStartZ = (Math.floor(Editor.getCamPos().z / 16) * 16);
			
			startX = (int) chunkStartX;
			startY = (int) chunkStartY;
			startZ = (int) chunkStartZ;
			endX = (int) (chunkStartX + (16 * viewDistance));
			endY = (int) (chunkStartY + (16 * viewDistance));
			endZ = (int) (chunkStartZ + (16 * viewDistance));
			
			for (int x = 0; x < viewDistance; x++) {
				for (int y = 0; y < viewDistance; y++) {
					for (int z = 0; z < viewDistance; z++) {
						chunks.add(new EditorChunk(this, new Vector3DInt(chunkStartX + (16 * x), chunkStartY + (16 * y), chunkStartZ + (16 * z))));
					}
				}
			}
			
			for (EditorChunk c : chunks) {
				c.buildChunk();
			}
			
			for (EditorChunk c : chunks) {
				for (EditorBlock b : c.blockList) { 
					b.createVertexData();
					c.chunkVertexData.addAll(b.getVertexData());
				}
			}
			
			for (EditorChunk c : chunks) {
				worldVertexData.addAll(c.getChunkVertexData());
			}
		}
	}
	
	public EditorChunk getNeighborChunk(Vector3DInt posIn) {
		for (EditorChunk c : chunks) {
			if (c.isPositionWithinChunk(posIn)) { return c; }
		}
		return null;
	}
	
	public EditorBlock getBlockAtPosition(Vector3DInt posIn) {
		for (EditorChunk c : chunks) {
			if (c.isPositionWithinChunk(posIn)) { 
				return c.getBlockAtPos(posIn); 
			}
		}
		return null;
	}
	
	public ArrayList<EditorChunk> getChunks() { return chunks; }
	public ArrayList<DPolygon> getWorldVertexData() { return worldVertexData; }

	public synchronized void destroyChunks() { chunks.clear(); }
}
