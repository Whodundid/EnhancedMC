package com.Whodundid.main;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.CHAT;

import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.TimedChatLine;
import com.Whodundid.enhancedChat.gameChat.InGameChatWindow;
import com.Whodundid.enhancedChat.gameChat.ModdedChat;
import com.Whodundid.main.util.ModdedInGameGui;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.main.util.enhancedGui.guiObjects.InnerEnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusType;
import com.Whodundid.main.util.enhancedGui.guiUtil.HeaderAlreadyExistsException;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.main.util.storageUtil.CheckForChangeEArrayList;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.EDimension;
import com.Whodundid.main.util.storageUtil.StorageBox;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

//Jan 11, 2019
//Jan 21, 2019
//Last edited: Jan 26, 2019
//First Added: Dec 17, 2018
//Author: Hunter Bragg

public class EnhancedInGameGui extends ModdedInGameGui implements IEnhancedTopParent {
	
	public EnhancedInGameGui instance;
	protected InGameChatWindow mainChatWindow;
	protected IEnhancedGuiObject modifyingObject;
	protected IEnhancedGuiObject objectRequestingFocus, focusedObject, defaultFocusObject, focusLockObject;
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> newDrawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> drawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected Deque<FocusEvent> focusQueue = new ArrayDeque();
	protected ObjectModifyTypes modifyType = ObjectModifyTypes.none;
	protected Direction resizingDir = Direction.OUT;
	protected EObjectGroup objectGroup;
	public int width = 0, height = 0;
	public int wPos = 0, hPos = 0;
	public int gWidth = 200, gHeight = 256;
	protected boolean enabled = true;
	protected boolean visible = true;
	public int mX = 0, mY = 0;
	public int objZLevel = 0;
	protected int highestZLevel = 0;
	protected int lowestZLevel = 0;
	protected boolean zLevelUpdated = false;
	
	public EnhancedInGameGui(Minecraft mc) {
		super(mc);
		initObjects();
	}
	
	//-------------------------
	//ModdedInGameGui Overrides
	//-------------------------
	
	@Override
	public void renderGameOverlay(float ticks) {
		super.renderGameOverlay(ticks);
		
		width = res.getScaledWidth();
		height = res.getScaledHeight();
		
		if (mc.currentScreen != null) {
			mX = Mouse.getX() * width / mc.displayWidth;
	        mY = height - Mouse.getY() * height / mc.displayHeight - 1;
		} else {
			mX = -1000; mY = -1000;
		}
		
        drawObject(mX, mY, 0);
	}
	
	@Override
	protected void renderChat(int width, int height) {
        mc.mcProfiler.startSection("chat");

        RenderGameOverlayEvent.Chat event = new RenderGameOverlayEvent.Chat(eventParent, 0, height - 48);
        if (MinecraftForge.EVENT_BUS.post(event)) { return; }
        
        if (!(mc.currentScreen instanceof ModdedChat) && !hasAMainChatWindow()) {
        	GlStateManager.pushMatrix();
            GlStateManager.translate(event.posX, event.posY, 0.0F);
            persistantModdedChatGUI.drawChat(updateCounter);
            GlStateManager.popMatrix();
        }

        post(CHAT);

        mc.mcProfiler.endSection();
    }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean hasBeenInitialized() { return true; }
	@Override public void initObjects() {}
	@Override
	public void reInitObjects() {
		guiObjects.clear();
		initObjects();
	}
	@Override public void onObjectAddedToParent() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		synchronized (guiObjects) {
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.checkDraw()) {
    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    	        	o.drawObject(mXIn, mYIn, ticks);
    			}
			}
		}
		GlStateManager.popMatrix();
	}
	@Override public void updateCursorImage() {}
	
	//obj ids;
	@Override public int getObjectID() { return 0; }
	@Override public EnhancedInGameGui setObjectID(int idIn) { return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return true; }
	@Override public EnhancedInGameGui setEnabled(boolean val) { enabled = val; return this; }
	@Override public EnhancedInGameGui setVisible(boolean val) { visible = val; return this; }
	@Override public EnhancedInGameGui setPersistent(boolean val) { return this; }
	
	//size
	@Override public boolean hasHeader() { return false; }
	@Override public boolean isResizeable() { return false; }
	@Override public EGuiHeader getHeader() { return null; }
	@Override public int getMinimumWidth() { return res.getScaledWidth(); }
	@Override public int getMinimumHeight() { return res.getScaledHeight(); }
	@Override public int getMaximumWidth() { return res.getScaledWidth(); }
	@Override public int getMaximumHeight() { return res.getScaledHeight(); }
	@Override public EnhancedInGameGui setMinimumWidth(int widthIn) { return this; }
	@Override public EnhancedInGameGui setMinimumHeight(int heightIn) { return this; }
	@Override public EnhancedInGameGui setMaximumWidth(int widthIn) { return this; }
	@Override public EnhancedInGameGui setMaximumHeight(int heightIn) { return this; }
	@Override public EnhancedInGameGui setResizeable(boolean val) { return this; }
	@Override public EnhancedInGameGui resize(int xIn, int yIn, Direction areaIn) { return this; }
	
	//position
	@Override public void move(int newX, int newY) {}
	@Override public boolean isPositionLocked() { return true; }
	@Override public EnhancedInGameGui resetPosition() { return this; }
	@Override public EnhancedInGameGui setPosition(int xIn, int yIn) { return this; }
	@Override public EnhancedInGameGui setPositionLocked(boolean val) { return this; }
	@Override public EnhancedInGameGui setDimensions(EDimension dimIn) { return this; }
	@Override public EnhancedInGameGui setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) { return this; }
	@Override public EDimension getDimensions() { return new EDimension(0, 0, res.getScaledWidth(), res.getScaledWidth()); }
	
	//objects
	@Override public boolean isChildOfObject(IEnhancedGuiObject objIn) { return false; }
	@Override
	public EnhancedInGameGui addObject(IEnhancedGuiObject... objsIn) {
		for (IEnhancedGuiObject o : objsIn) {
			if (o != null) {
				if (o != this) {
					if (o instanceof EGuiHeader && hasHeader()) { 
						try { throw new HeaderAlreadyExistsException(getHeader()); } catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
					}
					o.setParent(this).initObjects();
					if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).initGui(); }
				}
			}
		}
		objsToBeAdded.addAll(objsIn);
		return this;
	}
	@Override public EnhancedInGameGui removeObject(IEnhancedGuiObject... objsIn) { objsToBeRemoved.addAll(objsIn); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public EnhancedInGameGui setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public EArrayList<IEnhancedGuiObject> getObjects() { return guiObjects; }
	@Override
	public EArrayList<IEnhancedGuiObject> getAllChildren() {
		EArrayList<IEnhancedGuiObject> foundObjs = new EArrayList();
		EArrayList<IEnhancedGuiObject> objsWithChildren = new EArrayList();
		EArrayList<IEnhancedGuiObject> workList = new EArrayList();
		
		getObjects().forEach((o) -> { foundObjs.add(o); if (!o.getObjects().isEmpty()) { objsWithChildren.add(o); } });
		objsWithChildren.forEach((c) -> { c.getObjects().forEach((q) -> { workList.add(q); }); });
		
		while (true) {
			if (!workList.isEmpty()) {
				foundObjs.addAll(workList);
				objsWithChildren.clear();
				workList.forEach((o) -> { if (!o.getObjects().isEmpty()) { objsWithChildren.add(o); } });
				workList.clear();
				objsWithChildren.forEach((c) -> { c.getObjects().forEach((q) -> { workList.add(q); }); });
			} else { break; }
		}
		
		return foundObjs;
	}
	@Override
	public EArrayList<IEnhancedGuiObject> getAllObjectsUnderMouse() {
		EArrayList<IEnhancedGuiObject> underMouse = new EArrayList();
		for (IEnhancedGuiObject o : getAllChildren()) { if (o.checkDraw() && o.isMouseInside(mX, mY)) { underMouse.add(o); } }
		return underMouse;
	}
	
	//parents
	@Override public IEnhancedGuiObject getParent() { return this; }
	@Override public EnhancedInGameGui setParent(IEnhancedGuiObject parentIn) { return this; }
	@Override public IEnhancedTopParent getTopParent() { return this; }
	
	//zLevel
	@Override public int getZLevel() { return objZLevel; }
	@Override public int getHighestZLevel() { return highestZLevel; }
	@Override public int getLowestZLevel() { return lowestZLevel; }
	@Override
	public void updateZLevel() {
		int highestZ = 0, lowestZ = 0;
		for (IEnhancedGuiObject o : getAllChildren()) {
			int val = o.getZLevel();
			if (val > highestZ) { highestZ = val; }
			if (val < lowestZ) { lowestZ = val; }
		}
		highestZLevel = highestZ;
		lowestZLevel = lowestZ;
		
		EArrayList<IEnhancedGuiObject> tempList = new EArrayList(guiObjects);
		
		for (int i = lowestZLevel; i <= highestZLevel; i++) {
			for (IEnhancedGuiObject o : getAllChildren()) {
				if (o.getZLevel() == i) { newDrawOrder.add(o); }
			}
		}
		
		zLevelUpdated = true;
		
		//System.out.println(newDrawOrder);
	}
	@Override public EnhancedInGameGui setZLevel(int zLevelIn) { return this; }
	@Override public EnhancedInGameGui bringForward() { return this; }
	@Override public EnhancedInGameGui bringToFront() { return this; }
	@Override public EnhancedInGameGui sendBackwards() { return this; }
	@Override public EnhancedInGameGui sendToBack() { return this; }
	
	//focus
	@Override public boolean hasFocus() { return getFocusedObject().equals(this); }
	@Override public boolean relinquishFocus() {
		if (doesFocusLockExist() && getFocusLockObject().equals(this)) { clearFocusLockObject(); }
		else if (hasFocus()) {
			if (!equals(this)) { setObjectRequestingFocus(this); return true; }
		}
		return false;
	}
	@Override public void onFocusGained(FocusEvent eventIn) {}
	@Override public void onFocusLost(FocusEvent eventIn) {}
	@Override
	public void transferFocus(IEnhancedGuiObject objIn) {
		if (!doesFocusLockExist() && objIn != null) {
			relinquishFocus();
			setObjectRequestingFocus(objIn);
		}
	}
	@Override public void drawFocusLockBorder() {}
	@Override
	public EnhancedInGameGui requestFocus() {
		if (!hasFocus() && !doesFocusLockExist()) { setObjectRequestingFocus(this); }
		return this;
	}
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return false; }
	@Override public Direction getEdgeAreaMouseIsOn() { return Direction.OUT; }
	@Override public void mouseEntered(int mX, int mY) {}
	@Override public void mouseExited(int mX, int mY) {}
	@Override public boolean isMouseInside(int mX, int mY) { return false; }
	@Override public boolean isMouseInsideObject(int mX, int mY) { return getHighestZObjectUnderMouse() != null; }
	@Override
	public IEnhancedGuiObject getHighestZObjectUnderMouse() {
		EArrayList<IEnhancedGuiObject> foundObjs = getAllChildren();
		EArrayList<IEnhancedGuiObject> mouseIn = new EArrayList();
		if (!foundObjs.isEmpty()) {
			for (int i = foundObjs.size() - 1; i >= 0; i--) {
				IEnhancedGuiObject o = foundObjs.get(i);
				if (o.checkDraw() && o.isMouseInside(mX, mY)) { mouseIn.add(o); }
			}
			if (!mouseIn.isEmpty()) {
				IEnhancedGuiObject highestZObj = null;
				for (IEnhancedGuiObject o : mouseIn) {
					if (highestZObj != null) {
						if (o.getZLevel() > highestZObj.getZLevel()) { highestZObj = o; }
					} else { highestZObj = o; }
				}
				return highestZObj;
			}
			if (checkDraw() && isMouseInside(mX, mY)) { return this; }
		}
		return null;
	}
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { for (IEnhancedGuiObject o : guiObjects) { if (o.isMouseInside(mX, mY)) { o.parseMousePosition(mX, mY); } } }
	@Override
	public void mousePressed(int mX, int mY, int button) {
		IEnhancedGuiObject underMouse = getHighestZObjectUnderMouse();
		if (focusLockObject != null) {
			if (underMouse != null) {
				if (underMouse.equals(focusLockObject) || underMouse.isChildOfObject(focusLockObject)) {
					focusQueue.add(new FocusEvent(underMouse, FocusType.mousePress, button, mX, mY));
				} else {
					focusLockObject.drawFocusLockBorder();
				}
			} else { focusLockObject.drawFocusLockBorder(); }
		}
		else if (underMouse != null) {
			if (underMouse.equals(focusedObject)) {
				focusedObject.mousePressed(mX, mY, button);
			} else {
				focusQueue.add(new FocusEvent(underMouse, FocusType.mousePress, button, mX, mY));
			}
		}
		else { clearFocusedObject(); }
	}
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (modifyingObject != null && modifyType == ObjectModifyTypes.moveAlreadyClicked) { modifyType = ObjectModifyTypes.none; }
		if (focusedObject != null && focusedObject != this) { focusedObject.mouseReleased(mX, mY, button); }
		if (isResizing()) { clearModifyingObject(); }
		if (getDefaultFocusObject() != null) { getDefaultFocusObject().requestFocus(); }
	}
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {
		if (focusedObject != null && focusedObject != this) { focusedObject.mouseDragged(mX, mY, button, timeSinceLastClick); }
	}
	@Override
	public void mouseScrolled(int change) {
		if (isMouseInsideObject(mX, mY)) {
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.isMouseInside(mX, mY) && o.checkDraw()) { o.mouseScrolled(change); }
			}
		} else {
			if (!isShiftKeyDown()) { change *= 7; }
	        mc.ingameGUI.getChatGUI().scroll(change);
		}
	}
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 41) {
			if (focusedObject instanceof EGuiButton) {
				System.out.println("FocuedObject: " + (((EGuiButton) focusedObject).displayString.isEmpty() ? focusedObject : "EGuiButton: " + ((EGuiButton) focusedObject).displayString));
			}
			else { System.out.println("FocuedObject: " + focusedObject); }
			System.out.println("ModifyingObject & type: " + modifyingObject + " " + modifyType);
		}
		if (focusedObject != null && focusedObject != this) { focusedObject.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	@Override public void keyReleased(char typedChar, int keyCode) {
		if (focusedObject != null && focusedObject != this) { focusedObject.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	
	//updateScreen
	@Override public void updateScreen() { for (IEnhancedGuiObject o : guiObjects) { o.updateScreen(); } }
	
	//action object
	@Override public void actionPerformed(IEnhancedActionObject object) {}
	
	//close object
	@Override public void close() {}
	
	//-------------------------
	//IEnhancedTopGui Overrides
	//-------------------------
	
	//history
	@Override public Stack<EnhancedGui> getGuiHistory() { return null; }
	@Override public EnhancedInGameGui sendGuiHistory(Stack<EnhancedGui> historyIn) { return this; }
	
	//focus
	@Override public IEnhancedGuiObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public EnhancedInGameGui setDefaultFocusObject(IEnhancedGuiObject objIn) { defaultFocusObject = objIn; return this; }
	@Override public IEnhancedGuiObject getFocusedObject() { return focusedObject; }
	@Override public EnhancedInGameGui setObjectRequestingFocus(IEnhancedGuiObject objIn) { focusQueue.add(new FocusEvent(objIn, FocusType.transfer)); return this; }
	@Override public IEnhancedGuiObject getFocusLockObject() { return focusLockObject; }
	@Override public EnhancedInGameGui setFocusLockObject(IEnhancedGuiObject objIn) { focusLockObject = objIn; transferFocus(focusLockObject); return this; }
	@Override public EnhancedInGameGui clearFocusLockObject() { focusLockObject = null; return this; }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override
	public void clearFocusedObject() {
		if (focusedObject != null && focusedObject != this) { focusedObject.relinquishFocus(); }
		focusedObject = this;
	}
	@Override
	public void updateFocus() {
		if (modifyingObject != null && !modifyingObject.isResizeable() && modifyType.equals(ObjectModifyTypes.resize)) { modifyType = ObjectModifyTypes.none; }
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!children.contains(focusedObject)) { clearFocusedObject(); }
		if (!children.contains(focusLockObject)) { clearFocusLockObject(); }
		
		if (!focusQueue.isEmpty()) {
			FocusEvent event = focusQueue.pop();
			if (event.eventObject != null) {
				IEnhancedGuiObject obj = event.eventObject;
				if (doesFocusLockExist()) {
					if (obj.equals(focusLockObject) || obj.isChildOfObject(focusLockObject)) {
						focusedObject.onFocusLost(event);
						focusedObject = obj;
						focusedObject.onFocusGained(event);
					}
					else if (focusedObject != focusLockObject) {
						focusedObject.onFocusLost(new FocusEvent(focusLockObject, FocusType.defaultFocusObject));
						focusedObject = focusLockObject;
						focusedObject.onFocusGained(new FocusEvent(focusLockObject, FocusType.defaultFocusObject));
					}
				} else {
					if (focusedObject != null) {
						if (obj.equals(this) && hasAMainChatWindow()) { getMainChatWindow().getEntryField().requestFocus(); }
						else if (obj != focusedObject) {
							focusedObject.onFocusLost(event);
							focusedObject = obj;
							focusedObject.onFocusGained(event);
						}
					} else {
						if (focusedObject != null && obj != this) {
							focusedObject.onFocusLost(event);
							focusedObject = obj;
							focusedObject.onFocusGained(event);
						} else {
							if (defaultFocusObject != null) {
								focusedObject = defaultFocusObject;
								focusedObject.onFocusGained(event);
							} else {
								focusedObject = this;
								focusedObject.onFocusGained(event);
							}
						}
					}
				}
			}
		} else if (focusedObject == null) {
			focusedObject = this;
			focusedObject.onFocusGained(new FocusEvent(this, FocusType.defaultFocusObject));
		}
	}
	
	//object modification
	@Override public boolean isMoving() { return modifyType.equals(ObjectModifyTypes.move); }
	@Override public boolean isResizing() { return modifyType.equals(ObjectModifyTypes.resize); }
	@Override public ObjectModifyTypes getModifyType() { return modifyType; }
	@Override public EnhancedInGameGui setModifyingObject(IEnhancedGuiObject objIn, ObjectModifyTypes typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override public EnhancedInGameGui setResizingDir(Direction areaIn) { resizingDir = areaIn; return this; }
	@Override public EnhancedInGameGui setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IEnhancedGuiObject getModifyingObject() { return modifyingObject; }
	@Override public EnhancedInGameGui clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyTypes.none; return this; }
	
	//close
	@Override public void closeGui() {}
	@Override public EnhancedInGameGui setCloseAndRecenter(boolean val) { return this; }
	
	//-------------------------
	//EnhancedInGameGui methods
	//-------------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		if (!(mc.currentScreen instanceof ModdedChat)) { removeAllUnpinnedChatWindows(); }
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		//if (zLevelUpdated) { drawOrder.clear(); drawOrder = new EArrayList(newDrawOrder); newDrawOrder.clear(); zLevelUpdated = false; }
		updateFocus();
		if (modifyingObject != null) {
			switch (modifyType) {
			case move:
			case moveAlreadyClicked: modifyingObject.move(mX - mousePos.getObject(), mY - mousePos.getValue()); mousePos.setValues(mX, mY); break;
			case resize: modifyingObject.resize(mX - mousePos.getObject(), mY - mousePos.getValue(), resizingDir); mousePos.setValues(mX, mY); break;
			default: break;
			}
		}
	}
	
	protected void removeObjects() {
		synchronized (guiObjects) {
			objsToBeRemoved.forEach(o -> {
				if (o != null) {
					if (o != this) {
						if (guiObjects.contains(o)) {
							if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).onInnerGuiClose(); }
							guiObjects.remove(o);
						}
					}
				}
			});
			objsToBeRemoved.clear();
			updateZLevel();
		}
	}
	
	protected void addObjects() {
		objsToBeAdded.forEach(o -> {
			if (o != null) {
				if (o != this) {
					guiObjects.add(o);
					o.onObjectAddedToParent();
				}
			}
		});
		objsToBeAdded.clear();
		updateZLevel();
	}
	
	public void windowResized(int newWidth, int newHeight) {
		guiObjects.clear();
		clearFocusedObject();
		setObjectRequestingFocus(null);
		reInitObjects();
	}
	
	public void sendChatToCorrectWindow(ChatType type, TimedChatLine l) {
		InGameChatWindow w = getChatWindowOfType(type);
		if (w != null) { w.addChatLine(l); }
	}
	
	public EnhancedInGameGui addChatWindow() { return addChatWindow(""); }
	
	public EnhancedInGameGui addChatWindow(String defaultTextIn) {
		if (!hasAMainChatWindow()) {
			mainChatWindow = new InGameChatWindow(this, ChatType.ALL, ChatType.PRIVATE, ChatType.PARTY, ChatType.GUILD, ChatType.LOBBY);
			mainChatWindow.updateInitText(defaultTextIn);
			addObject(mainChatWindow);
			mainChatWindow.requestFocus();
		};
		return this;
	}
	
	public InGameChatWindow getMainChatWindow() {
		if (mainChatWindow != null) { return mainChatWindow; }
		return null;
	}
	
	public EnhancedInGameGui addChatWindow(ChatType type) {
		if (!getAllActiveChatWindowTypes().contains(type)) { addObject(new InGameChatWindow(this).setChatTypes(type)); }
		return this;
	}
	
	public EnhancedInGameGui removeChatWindow(ChatType type) {
		for (InGameChatWindow w : getAllActiveChatWindows()) { if (w.getChatTypes().contains(type)) { removeObject(w); } }
		return this;
	}
	
	public EnhancedInGameGui removeAllChatWindows() {
		for (InGameChatWindow w : getAllActiveChatWindows()) { removeObject(w); }
		return this;
	}
	
	public EnhancedInGameGui removeAllUnpinnedChatWindows() {
		for (InGameChatWindow w : getAllActiveChatWindows()) { if (!w.isPinned()) { removeObject(w); } }
		return this;
	}
	
	public boolean hasAMainChatWindow() {
		for (InGameChatWindow w : getAllActiveChatWindows()) {
			if (w.getChatTypes().contains(ChatType.ALL)) { return true; }
		}
		return false;
	}
	
	public boolean hasAChatWindow() {
		for (IEnhancedGuiObject o : guiObjects) {
			if (o instanceof InGameChatWindow) { return true; }
		}
		return false;
	}
	
	public boolean areAnyChatWindowsPinned() {
		for (InGameChatWindow w : getAllActiveChatWindows()) { if (w.isPinned()) { return true; }  }
		return false;
	}
	
	public InGameChatWindow getFocusedChatWindow() {
		return mainChatWindow;
	}
	
	public EArrayList<InGameChatWindow> getAllPinnedChatWindows() {
		EArrayList<InGameChatWindow> pinned = new EArrayList();
		for (InGameChatWindow w : getAllActiveChatWindows()) { if (w.isPinned()) { pinned.add(w); } }
		return pinned;
	}
	
	public InGameChatWindow getChatWindowOfType(ChatType type) {
		for (InGameChatWindow w : getAllActiveChatWindows()) {
			if (w.getEnabledChatTypes().contains(type)) { return w; }
		}
		return null;
	}
	
	public EArrayList<ChatType> getAllActiveChatWindowTypes() {
		EArrayList<ChatType> windowTypes = new EArrayList();
		for (InGameChatWindow w : getAllActiveChatWindows()) {  } //not sure how to address yet
		return windowTypes;
	}
	
	public EArrayList<InGameChatWindow> getAllActiveChatWindows() {
		EArrayList<InGameChatWindow> windows = new EArrayList();
		for (IEnhancedGuiObject o : guiObjects) {
			if (o instanceof InGameChatWindow) { windows.add((InGameChatWindow) o); }
		}
		return windows;
	}
}

