package com.Whodundid.worldEditor.Editor3D.renderingUtil;

import com.Whodundid.worldEditor.Editor;
import com.Whodundid.worldEditor.Editor3D.Editor3D;
import com.Whodundid.worldEditor.Editor3D.block.EditorDirection;
import com.Whodundid.main.util.storageUtil.Vector3DInt;
import java.awt.Color;

public class FlatPane {
	
	protected Editor editor;
	protected Editor3D renderer;
	public Vector3DInt blockLocation;
	double x, y, z, width, length, height, rot = Math.PI * 0.75;
	double[] RotAdd = new double[4];
	Color c;
	double x1, x2, x3, x4, y1, y2, y3, y4;
	double[] angle;
	
	public FlatPane(double x, double y, double z, EditorDirection dir, Editor editorIn) {
		
	}
}
