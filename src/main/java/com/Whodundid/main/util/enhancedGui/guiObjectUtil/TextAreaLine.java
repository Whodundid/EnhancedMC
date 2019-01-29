package com.Whodundid.main.util.enhancedGui.guiObjectUtil;

import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

//Last edited: Jan 9, 2019
//First Added: Oct 2, 2018
//Author: Hunter Bragg

public class TextAreaLine<obj> extends EGuiTextField {
	
	EGuiTextArea parentTextArea;
	public int lineNumberColor = 0xb2b2b2;
	public String visibleText = "";
	protected int lineNumber = 0;
	protected int drawnLineNumber = 0;
	protected int lineNumberWidth = 0;
	protected int maxVisibleLength = 3;
	protected boolean textRecentlyEntered = false;
	protected boolean deleting = false;
	protected boolean creating = false;
	protected boolean highlighted = false;
	protected boolean lineEquals = false, drawCursor = false;
	protected long startTime = 0l;
	obj storedObj;
	
	public TextAreaLine(EGuiTextArea textAreaIn) { this(textAreaIn, -1); }
	public TextAreaLine(EGuiTextArea textAreaIn, int lineNumberIn) {
		init(textAreaIn, 0, 0, 0, 0);
		lineNumber = lineNumberIn;
		parent = textAreaIn;
		parentTextArea = textAreaIn;
		setMaxStringLength(1500);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		updateValues();
		
		if (highlighted || lineEquals) {
			drawRect(startX - 3, startY, endX + 1, endY, 0x20ffffff);
		}
		
		if (parentTextArea.getDrawLineNumbers()) { drawLineNumber(); }
		drawText();
		
		if (checkDraw()) {
			for (int i = 0; i <= getTopParent().getHighestZLevel(); i++) {
	        	for (IEnhancedGuiObject o : guiObjects) {
	    			if (o.checkDraw() && o.getZLevel() == i) {
	    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    	        	o.drawObject(mX, mY, ticks);
	    			}
	            }
	        }
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (hasFocus()) {
			if (drawnLineNumber < 0) { parentTextArea.makeLineNumberDrawn(lineNumber); }
			if (GuiScreen.isKeyComboCtrlA(keyCode)) { setCursorPositionEnd(); }
			else if (GuiScreen.isKeyComboCtrlC(keyCode)) { GuiScreen.setClipboardString(getSelectedText()); }
			else if (GuiScreen.isKeyComboCtrlV(keyCode) && isEnabled()) { writeText(GuiScreen.getClipboardString()); checkLine(); }
			else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
				GuiScreen.setClipboardString(getSelectedText());
				if (isEnabled() && parentTextArea.isEditable()) {
					writeText("");
					checkLine();
				}
			} else {
				switch (keyCode) {
				case 28: //enter
					if (parentTextArea.isEditable()) {
						if (!creating) {
							creating = true;
							TextAreaLine newLine = parentTextArea.insertTextLine(lineNumber + 1, getSelectionEnd() == text.length(), cursorPosition);
							if (newLine != null) { newLine.requestFocus(); }
						}
					}
					break;
				case 200: //up
					parentTextArea.moveCurrentLineUp();
					break;
				case 208: //down
					parentTextArea.moveCurrentLineDown();
					break;
				case 14: //backspace
					if (parentTextArea.isEditable()) {
						if (getText().isEmpty()) {
							if (!deleting) {
								deleting = true;
								if (parentTextArea.getTextLineWithLineNumber(lineNumber - 1) != null) { parentTextArea.removeTextLine(this); }
								else { deleting = false; }
							}
						} else if (cursorPosition == 0) {
							if (!deleting) {
								deleting = true;
								parentTextArea.deleteLineAndMoveTextUp(this);
							}
						}
						if (GuiScreen.isCtrlKeyDown()) {
							if (isEnabled()) { deleteWords(-1); }
						} else if (isEnabled()) { deleteFromCursor(-1); }
						startTextTimer();
						checkLine();
					}
					break;
				case 199: //home
					if (GuiScreen.isShiftKeyDown()) { setSelectionPos(0); }
					else { setCursorPositionZero(); }
					break;
				case 203: //left
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) { setSelectionPos(getNthWordFromPos(-1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() - 1); }
					} else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(-1)); }
					else { moveCursorBy(-1); }
					startTextTimer();
					break;
				case 205: //right
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) { setSelectionPos(getNthWordFromPos(1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() + 1); }
					} else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(1)); }
					else { moveCursorBy(1); }
					startTextTimer();
					break;
				case 207: //end
					if (GuiScreen.isShiftKeyDown()) { setSelectionPos(text.length()); }
					else { setCursorPositionEnd(); }
					break;
				case 211: //delete
					if (parentTextArea.isEditable()) {
						if (GuiScreen.isCtrlKeyDown()) {
							if (isEnabled()) { deleteWords(1); startTextTimer(); }
						} else if (isEnabled()) { deleteFromCursor(1); startTextTimer(); }
						checkLine();
					}
					break;
				default:
					if (parentTextArea.isEditable()) {
						if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
							if (isEnabled()) {
								writeText(Character.toString(typedChar));
								startTextTimer();
								checkLine();
							}
						}
					}
				}
			}
		}
	}
	
	@Override 
	public void mousePressed(int mX, int mY, int button) {
		try {
			if (isMouseHover) { requestFocus(); }
			if (button == 0 && isResizeable() && !getEdgeAreaMouseIsOn().equals(Direction.OUT)) {
				getTopParent().setModifyingObject(this, ObjectModifyTypes.resize);
				getTopParent().setResizingDir(getEdgeAreaMouseIsOn());
				getTopParent().setModifyMousePos(mX, mY);
			}
			if (hasFocus() && button == 0) {
				int i = mX - startX + 2;
				String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
				setCursorPosition(fontRenderer.trimStringToWidth(s, i).length() + lineScrollOffset);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void mouseScrolled(int change) {
		//parentTextArea.mouseScrolled(change);
	}
	
	@Override
	public void updateScreen() {
		if (textRecentlyEntered) {
			if (System.currentTimeMillis() - startTime >= 150) {
				textRecentlyEntered = false;
				cursorCounter = 0;
			}
		}
		super.updateScreen();
	}
	
	@Override
	public void onFocusLost(FocusEvent eventIn) {
		super.onFocusLost(eventIn);
		creating = false;
	}
	
	@Override
	public void onFocusGained(FocusEvent eventIn) {
		parentTextArea.setSelectedLine(this);
		super.onFocusGained(eventIn);
	}
	
	protected void checkLine() {
		TextAreaLine l = parentTextArea.updateAndGetLongestLine();
		if (l.equals(this)) {
			if (fontRenderer.getStringWidth(text.substring(parentTextArea.getCurrentHorizontalPos())) > width - maxVisibleLength) {
				parentTextArea.makeLineNumberEndDrawn(lineNumber);
			}
		}
	}
	
	protected void updateValues() {
		if (parentTextArea != null && parentTextArea.getCurrentLine() != null) {
			lineEquals = parentTextArea.getCurrentLine().equals(this);
			drawCursor = parentTextArea.isEditable() && lineEquals && cursorCounter / 11 % 2 == 0;
		}
	}
	
	private void startTextTimer() {
		startTime = System.currentTimeMillis();
		textRecentlyEntered = true;
	}
	
	protected void drawLineNumber() {
		drawStringWithShadow("" + lineNumber, parentTextArea.startX + parentTextArea.getLineNumberSeparatorPos() - (lineNumberWidth + 2), startY + 2, lineNumberColor);
	}
	
	protected void drawText() {
		int j = cursorPosition - lineScrollOffset;
		int k = selectionEnd - lineScrollOffset;
		//System.out.println(lineScrollOffset);
		String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
		//drawStringWithShadow(s, startX, startY + 50, enabledColor);
		visibleText = fontRenderer.trimStringToWidth(text.substring(parentTextArea.getCurrentHorizontalPos()), width - maxVisibleLength);
		
		int textLength = fontRenderer.getStringWidth(text);
		if (textLength > width) {
			
			
		} else {
			if (lineEquals && (textRecentlyEntered || drawCursor)) {
				int textCursorPosLength = fontRenderer.getStringWidth(text.substring(0, cursorPosition)); //this is not finished -- does not check for horizontal position
				drawRect(startX + textCursorPosLength, startY + 1, startX + textCursorPosLength + 1, endY - 2, 0xffffffff);
			}
			//drawStringWithShadow(text, startX, startY + 2, enabledColor);
		}
		drawStringWithShadow(visibleText, startX, startY + 2, mainDrawColor);
	}
	
	public int getDrawnLineNumber() { return drawnLineNumber; }
	public int getLineNumber() { return lineNumber; }
	public obj getStoredObj() { return storedObj; }
	public String getVisibleText() { return visibleText; }
	
	public TextAreaLine incrementLineNumber() { setLineNumber(lineNumber + 1); return this; }
	public TextAreaLine decrementLineNumber() { setLineNumber(lineNumber - 1); return this; }
	public TextAreaLine setHighlighted(boolean val) { highlighted = val; return this; }
	public TextAreaLine setStoredObj(obj objectIn) { storedObj = objectIn; return this; }
	public TextAreaLine setLineNumber(int numberIn) { lineNumber = numberIn; lineNumberWidth = fontRenderer.getStringWidth(String.valueOf(lineNumber)); return this; }
	public TextAreaLine setLineNumberColor(int colorIn) { lineNumberColor = colorIn; return this; }
	public TextAreaLine setDrawnLineNumber(int numberIn) { drawnLineNumber = numberIn; return this; }
	public TextAreaLine indent() { setText("    " + getText()); return this; }
	
	@Override public String toString() { return "[" + lineNumber + ": " + getText() + "]"; }
}
