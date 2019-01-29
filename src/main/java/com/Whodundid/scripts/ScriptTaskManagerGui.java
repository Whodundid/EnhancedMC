package com.Whodundid.scripts;

import java.io.IOException;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.Minecraft;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class ScriptTaskManagerGui extends EnhancedGui {
	
	static Minecraft mc = MainMod.getMC();
	EGuiButton killScript;
	StorageBoxHolder<Integer, Script> selected = new StorageBoxHolder();
	
	public ScriptTaskManagerGui() { super(); }
	public ScriptTaskManagerGui(EnhancedGui oldGui) { super(oldGui); }
	public ScriptTaskManagerGui(int posX, int posY) { super(posX, posY); }
	public ScriptTaskManagerGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		killScript = new EGuiButton(this, wPos + 35, hPos + 103, 60, 20, "Kill Script");
		addObject(killScript);
	}
	
	@Override
	public void drawObject(int x, int y, float ticks) {
		mc.renderEngine.bindTexture(Resources.guiBase);
		drawModalRectWithCustomSizedTexture(wPos - width / 2, hPos - height / 2, 0, 0, width, height, width, height);
		drawActiveScripts();
		drawSelectedScripts();
	}
	
	private void drawActiveScripts() {
		drawInnerGui();
		/*;
		ArrayList<Script> runningScripts = ScriptManager.getCurrentlyRunningScripts();
		for (int i = 0; i < runningScripts.size(); i++) {
			drawCenteredString(fontRendererObj, runningScripts.get(i).getScriptName(), wPos - 37, hPos - 106 + ((i * 17) + i), 0xFFFFFF);
			drawCenteredString(fontRendererObj, runningScripts.get(i).scriptID + "", wPos + 38, hPos - 106 + ((i * 17) + i), 0xFFFFFF);
			drawCenteredString(fontRendererObj, runningScripts.get(i).timeAlive + "", wPos + 74, hPos - 106 + ((i * 17) + i), 0xFFFFFF);
		}
		*/
	}
	
	private void drawInnerGui() {
		mc.renderEngine.bindTexture(Resources.innerTaskManager);
		drawModalRectWithCustomSizedTexture(wPos - 95, hPos - 112, 0, 0, 190, 199, 190, 199);
		mc.renderEngine.bindTexture(Resources.guiBase);
		drawTexturedModalRect(wPos + 22, hPos - 112, 0, 0, 1, 199);
		drawTexturedModalRect(wPos + 52, hPos - 112, 0, 0, 1, 199);
		
		drawCenteredString(fontRendererObj, "Script name", wPos - 37, hPos - 123, 0x00FF00);
		drawCenteredString(fontRendererObj, "ID", wPos + 38, hPos - 123, 0x00FF00);
		drawCenteredString(fontRendererObj, "RTime", wPos + 74, hPos - 123, 0x00FF00);
		
		drawHardwareStatistics();
		
		for (int i = 0; i < 11; i++) {
			mc.renderEngine.bindTexture(Resources.guiBase);
			drawTexturedModalRect(wPos - 95, hPos - 94 + ((i * 17) + i), 0, 0, 190, 1);
		}
	}
	
	@Override
	public void mouseClicked(int mX, int mY, int button) throws IOException {
		super.mouseClicked(mX, mY, button);
		/*
		if (mX >= wPos - 90 && mX <= wPos + 90) {
			if (mY >= hPos - 110 && mY <= hPos + 87) {
				int startY = mY - (hPos - 110);
				int tempPos = (startY / 18);
				if (!selected.contains(tempPos)) {
					if (tempPos <= ScriptManager.getCurrentlyRunningScripts().size() - 1) {
						if (this.isCtrlKeyDown()) {
							selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
							return;
						} else if (this.isShiftKeyDown()) {
							if (!selected.isEmpty()) {
								int highest = this.getHighestSelectedPosition();
								int lowest = this.getLowestSelectedPosition();
								if (tempPos < highest) {
									while (tempPos < highest) {
										selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
										tempPos++;
									}
								} else {
									while (tempPos > lowest) {
										selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
										tempPos--;
									}
								}
							}
							return;
						} else {
							selected.clear();
							selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
							return;
						}
					}
				} else {
					if (tempPos <= ScriptManager.getCurrentlyRunningScripts().size() - 1) {
						if (this.isCtrlKeyDown()) {
							try {
								selected.removeBoxesContainingObj(tempPos);
								return;
							} catch (Exception e) { e.printStackTrace(); }
						}
					}
				}
			}
		}
		selected.clear();
		*/
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(killScript)) {
			killSelectedScripts();
		}
	}
	
	private void drawSelectedScripts() {
		for (StorageBox<Integer, Script> i : selected) {
			mc.renderEngine.bindTexture(Resources.scriptSelectionBox);
			drawModalRectWithCustomSizedTexture(wPos - 94, hPos - 111 + ((i.getObject() * 17) + i.getObject()), 0, 0, 188, 17, 188, 17);
		}
	}
	
	private void killSelectedScripts() {
		/*
		if (!selected.isEmpty()) {
			for (StorageBox<Integer, Script> s : selected) {
				ScriptManager.removeRunningScript(s.getValue().scriptID);
			}
		}
		*/
	}
	
	private int getHighestSelectedPosition() {
		int returnVal = 0;
		for (StorageBox<Integer, Script> i : selected) {
			if (i.getObject() > returnVal) { returnVal = i.getObject(); }
		}
		return returnVal;
	}
	
	private int getLowestSelectedPosition() {
		int returnVal = 11;
		for (StorageBox<Integer, Script> i : selected) {
			if (i.getObject() < returnVal) { returnVal = i.getObject(); }
		}
		return returnVal;
	}
	
	private void drawHardwareStatistics() {
		try {
			double cpuProcessUsage = 0;
		    AttributeList list = ManagementFactory.getPlatformMBeanServer().getAttributes(ObjectName.getInstance("java.lang:type=OperatingSystem"), new String[] { "ProcessCpuLoad" });
		    Double value = (Double) ((Attribute) list.get(0)).getValue();
		    cpuProcessUsage = (value * 1000) / 10;
		    String usage = new DecimalFormat("0.0").format(cpuProcessUsage);
		    drawString(fontRendererObj, "CPU usage: " + usage + "%", wPos - 90, hPos + 96, 0x00FF00);
		} catch (Exception e) { e.printStackTrace(); }
		
		long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000;
		drawString(fontRendererObj, "RAM usage: " + String.valueOf(memory) + " MB", wPos - 90, hPos + 110, 0x00FF00);
	}
}
