package com.Whodundid.worldEditor.Editor3D.renderingUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import com.Whodundid.worldEditor.Editor;

public class PolygonObject {
	public Polygon poly;
	public Color color;
	public boolean draw = true, visible = true;
	public boolean seeThrough = false;
	double lighting = 1;
	
	public PolygonObject(double[] x, double[] y, Color c) {
		poly = new Polygon();
		for (int i = 0; i < x.length; i++) { poly.addPoint((int) x[i], (int) y[i]); }
		this.color = c;
	}
	
	public void updatePolygon(double[] x, double[] y) {
		poly.reset();
		for (int i = 0; i < x.length; i++) {
			poly.xpoints[i] = (int) x[i];
			poly.ypoints[i] = (int) y[i];
			poly.npoints = x.length;
		}
	}
	
	public void drawPolygon(Graphics g) {
		if (draw && visible) {
			g.setColor(new Color((int)(color.getRed() * lighting), (int)(color.getGreen() * lighting), (int)(color.getBlue() * lighting)));
			
			if (seeThrough) { g.drawPolygon(poly); }
			else { g.fillPolygon(poly); }
			
			if (Editor.drawOutlines) {
				g.setColor(new Color(0, 0, 0));
				g.drawPolygon(poly);
			}

			if (Editor.selectedBlock != null) {
				boolean passed = false;
				for (DPolygon p : Editor.selectedBlock.getVertexData()) {
					if (p.dPoly == this) {
						passed = true;
					}
				}
				if (passed) {
					for (DPolygon p : Editor.selectedBlock.getVertexData()) {
						g.setColor(new Color(255, 144, 144, 100));
						g.fillPolygon(p.dPoly.poly);
					}
				}
			}
		}
	}
	
	public boolean MouseOver(int mX, int mY) {
		if (Editor.insideEditor) {
			if (Editor.render3DHighRes) {
				int ratioX = (mX * Editor.imageHandler3DHiRes.getTextureWidth()) / Editor.imgWidth;
				int ratioY = (mY * Editor.imageHandler3DHiRes.getTextureHeight()) / Editor.imgHeight;
				return poly.contains(ratioX, ratioY - 5);
			}
			int ratioX = (mX * Editor.imageHandler3D.getTextureWidth()) / Editor.imgWidth;
			int ratioY = (mY * Editor.imageHandler3D.getTextureHeight()) / Editor.imgHeight;
			return poly.contains(ratioX, ratioY - 2);
		}
		return false;
	}
}
