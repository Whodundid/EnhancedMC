package com.Whodundid.miniMap;

import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiContainer;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiSlider;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.guiObjects.EScreenLocationSelector;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class MiniMapGui extends EnhancedGui {
	
	MiniMap map = (MiniMap) RegisteredSubMods.getMod(SubModType.MINIMAP);
	EGuiTextField downOffsetInput, trackPlayerName, zoomEntry;
	EGuiButton drawPlayerLocations, drawVertical, drawMap, drawCoords, drawDirections, drawFacingDir;
	EGuiButton drawChat, clearTrack, resetYAxis, zoomReset, gotoHotKeys;
	EGuiContainer location, zooming, tracking, yAxis, mapProperties, mapVisual, colorKey, hotkeys;
	EScreenLocationSelector locationSelector;
	EGuiSlider zoomSlider;
	
	public MiniMapGui() { super(); }
	public MiniMapGui(EnhancedGui oldGui) { super(oldGui); }
	public MiniMapGui(int posX, int posY) { super(posX, posY); }
	public MiniMapGui(int posX, int posY, EnhancedGui oldGui) { super(posX, posY, oldGui); }
	
	@Override
	public void initGui() {
		centerGuiWithDimensions(560, 302);
		super.initGui();
		setCloseAndRecenter(true);
	}
	
	@Override
	public void initObjects() {
		location = new EGuiContainer(this, startX + 2, startY + 2, 195, 162) {
			{
				setDisplayString("MiniMap Location");
				locationSelector = new EScreenLocationSelector(this, map, startX + 47, startY + 49, 100);
				locationSelector.setDisplayName("MiniMap");
				addObject(locationSelector);
			}
		};
		
		hotkeys = new EGuiContainer(this, startX + 2, startY + 213, 195, 87) {
			{
				setDisplayString("MiniMap HotKeys");
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				
			}
		};
		
		zooming = new EGuiContainer(this, startX + 198, startY + 183, 163, 117) {
			{ 
				setDisplayString("Zooming");
				zoomSlider = new EGuiSlider(zooming, startX + 10, startY + 25, width - 20, 18, 1, 10, (float) map.zoom, false);
				zoomReset = new EGuiButton(zooming, startX + 10, startY + 74, 85, 18, "Reset Zoom");
				zoomEntry = new EGuiTextField(zooming, startX + 11, startY + 50, 40, 17) {
					{ setText("" + zoomSlider.displayValue); }
					@Override
					public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) {
							if (!zoomEntry.getText().isEmpty()) {
								
							}
						}
					}
				};
				addObject(zoomSlider, zoomReset, zoomEntry);
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				drawStringWithShadow("Custom Zoom Entry", midX - 22, startY + 55, 0xb2b2b2);
				drawStringWithShadow("Current draw radius: ", startX + 11, endY - 17, 0xffd800);
				drawStringWithShadow("" + map.getMapRadius(), startX + 124, endY - 17, 0x00ff00);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(zoomSlider)) {
					map.setMiniMapZoom(zoomSlider.getSliderValue());
					zoomEntry.setText("" + zoomSlider.displayValue);
				}
			}
		};
		
		tracking = new EGuiContainer(this, startX + 362, startY + 213, 196, 87) {
			{
				setDisplayString("Player Tracking");
				clearTrack = new EGuiButton(tracking, startX + 122, startY + 23, 45, 18, "Clear");
				trackPlayerName = new EGuiTextField(tracking, startX + 7, startY + 24, 105, 16) {
					{ setMaxStringLength(16).setText(map.findPlayer.isEmpty() ? EnumChatFormatting.GRAY + "enter a name.." : map.findPlayer); }
					@Override public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) { map.findPlayer = trackPlayerName.getText(); }
					}
					@Override public void onFocusGained(FocusEvent eventIn) { 
						if (getText().equals(EnumChatFormatting.GRAY + "enter a name..")) { setText(""); } setSelectionPos(0);
					}
					@Override public void onFocusLost(FocusEvent eventIn) {
						setText(map.findPlayer.isEmpty() ? EnumChatFormatting.GRAY + "enter a name.." : map.findPlayer);
						setSelectionPos(getText().length());
					}
				};
				drawPlayerLocations = new EGuiButton(tracking, startX + 6, endY - 24, 45, 18, map.trackPlayers ? "True" : "False").setDisplayStringColor(map.trackPlayers ? 0x55ff55 : 0xff5555);
				
				addObject(trackPlayerName, drawPlayerLocations, clearTrack);
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				drawStringWithShadow("Tracking:", startX + 7, startY + 48, 0xffd800);
				drawStringWithShadow("Draw player locations", startX + 59, endY - 19, 0xb2b2b2);
				drawStringWithShadow(map.findPlayer, startX + 60, startY + 48, 0x00ffdc);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(drawPlayerLocations)) {
					map.trackPlayers = !map.trackPlayers;
					drawPlayerLocations.setDisplayString(map.trackPlayers ? "True" : "False").setDisplayStringColor(map.trackPlayers ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(clearTrack)) {
					map.findPlayer = "";
					trackPlayerName.setText(EnumChatFormatting.GRAY + "enter a name..");
				}
			}
		};
		
		yAxis = new EGuiContainer(this, startX + 362, startY + 165, 196, 47) {
			{
				setDisplayString("Y-Axis Offset");
				resetYAxis = new EGuiButton(yAxis, startX + 43, startY + 23, 45, 18, "Reset");
				downOffsetInput = new EGuiTextField(yAxis, startX + 7, startY + 24, 26, 16) {
					{ setMaxStringLength(3).setOnlyAcceptNumbers(true).setText("" + map.downOffsetY).setAllowClipboardPastes(false); }
					@Override public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) {
							if (getText().isEmpty()) { map.downOffsetY = 0; }
							else { map.downOffsetY = Integer.parseInt(getText()); }
						}
					}
					@Override public void onFocusGained(FocusEvent eventIn) { setSelectionPos(0); }
					@Override public void onFocusLost(FocusEvent eventIn) {
						setSelectionPos(getText().length()); 
						if (getText().isEmpty()) { setText("" + map.downOffsetY); }
					}
				};
				addObject(downOffsetInput, resetYAxis);
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				drawStringWithShadow("Current offset:", startX + 94, startY + 28, 0xffd800);
				drawStringWithShadow("" + map.downOffsetY, startX + 178, startY + 28, 0x00ff00);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(resetYAxis)) {
					map.downOffsetY = 0;
					downOffsetInput.setText("" + map.downOffsetY);
				}
			}
		};
		
		mapProperties = new EGuiContainer(this, startX + 362, startY + 2, 196, 162) {
			{
				setDisplayString("Map Properties");
				drawVertical = new EGuiButton(mapProperties, startX + 6, startY + 23, 45, 18, map.drawVertical ? "True" : "False").setDisplayStringColor(map.drawVertical ? 0x55ff55 : 0xff5555);
				drawChat = new EGuiButton(mapProperties, startX + 6, startY + 46, 45, 18, map.drawWithChatOpen ? "True" : "False").setDisplayStringColor(map.drawWithChatOpen ? 0x55ff55 : 0xff5555);
				drawMap = new EGuiButton(mapVisual, startX + 6, startY + 69, 45, 18, map.drawMap ? "True" : "False").setDisplayStringColor(map.drawMap ? 0x55ff55 : 0xff5555);
				drawCoords = new EGuiButton(mapVisual, startX + 6, startY + 92, 45, 18,  map.drawCoords ? "True" : "False").setDisplayStringColor(map.drawCoords ? 0x55ff55 : 0xff5555);
				drawDirections = new EGuiButton(mapVisual, startX + 6, startY + 115, 45, 18, map.drawMapDirections ? "True" : "False").setDisplayStringColor(map.drawMapDirections ? 0x55ff55 : 0xff5555);
				drawFacingDir = new EGuiButton(mapVisual, startX + 6, startY + 138, 45, 18, map.drawFacingDir ? "True" : "False").setDisplayStringColor(map.drawFacingDir ? 0x55ff55 : 0xff5555);
				addObject(drawVertical, drawChat, drawMap, drawCoords, drawDirections, drawFacingDir);
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				drawStringWithShadow("Draw Vertical", startX + 60, startY + 28, 0xb2b2b2);
				drawStringWithShadow("Draw With Chat Open", startX + 60, startY + 51, 0xb2b2b2);
				drawStringWithShadow("Draw map", startX + 60, startY + 74, 0xb2b2b2);
				drawStringWithShadow("Draw coords", startX + 60, startY + 97, 0xb2b2b2);
				drawStringWithShadow("Draw directions", startX + 60, startY + 120, 0xb2b2b2);
				drawStringWithShadow("Draw facing line", startX + 60, startY + 143, 0xb2b2b2);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				if (object.equals(drawVertical)) {
					map.drawVertical = !map.drawVertical;
					drawVertical.setDisplayString(map.drawVertical ? "True" : "False").setDisplayStringColor(map.drawVertical ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(drawChat)) {
					map.drawWithChatOpen = !map.drawWithChatOpen;
					drawChat.setDisplayString(map.drawWithChatOpen ? "True" : "False").setDisplayStringColor(map.drawWithChatOpen ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(drawMap)) {
					map.drawMap = !map.drawMap;
					drawMap.setDisplayString(map.drawMap ? "True" : "False").setDisplayStringColor(map.drawMap ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(drawCoords)) {
					map.drawCoords = !map.drawCoords;
					drawCoords.setDisplayString(map.drawCoords ? "True" : "False").setDisplayStringColor(map.drawCoords ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(drawDirections)) {
					map.drawMapDirections = !map.drawMapDirections;
					drawDirections.setDisplayString(map.drawMapDirections ? "True" : "False").setDisplayStringColor(map.drawMapDirections ? 0x55ff55 : 0xff5555);
				}
				if (object.equals(drawFacingDir)) {
					map.drawFacingDir = !map.drawFacingDir;
					drawFacingDir.setDisplayString(map.drawFacingDir ? "True" : "False").setDisplayStringColor(map.drawFacingDir ? 0x55ff55 : 0xff5555);
				}
			}
		};
		
		mapVisual = new EGuiContainer(this, startX + 198, startY + 2, 163, 180) {
			{ setDisplayString("Map Preview"); setBackgroundColor(0xffa8a8a8);}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f);
				mc.renderEngine.bindTexture(map.handler.getTextureLocation());
				int xPos = midX - map.previewSize / 2 - 1;
				int yPos = midY - map.previewSize / 2 - 9;
				if (map.drawVertical) {
					GlStateManager.translate(midX, midY, 0);
					GlStateManager.rotate(180, 0, 0, 45);
					GlStateManager.translate(-midX, -midY, 0);
				} else { xPos += 1; yPos += 17; }
				drawCustomSizedTexture(xPos, yPos, map.previewXLow, map.previewYLow, map.previewSize, map.previewSize, map.previewXHigh, map.previewYHigh);
				
				GlStateManager.popMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				
				if (!map.isMapImageCreated()) {
					drawCenteredStringWithShadow("Enable MiniMap to generate", midX, midY - 1, 0xFF4747);
					drawCenteredStringWithShadow("a map preview!", midX, midY + 9, 0xFF4747);
				}
			}
		};
		
		colorKey = new EGuiContainer(this, startX + 2, startY + 165, 195, 47) {
			{ 
				setDisplayString("Map Color Key");
			}
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				super.drawObject(mXIn, mYIn, ticks);
			}
			@Override
			public void actionPerformed(IEnhancedActionObject object) {
				
			}
		};
		
		addObject(location, zooming, tracking, yAxis, mapProperties, mapVisual, colorKey, hotkeys);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn, ticks);
	}
}
