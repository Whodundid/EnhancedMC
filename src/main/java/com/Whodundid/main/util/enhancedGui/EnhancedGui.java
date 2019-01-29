package com.Whodundid.main.util.enhancedGui;

import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.SettingsGui;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiFocusLockBorder;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.main.util.enhancedGui.guiObjects.InnerEnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusEvent;
import com.Whodundid.main.util.enhancedGui.guiUtil.FocusType;
import com.Whodundid.main.util.enhancedGui.guiUtil.HeaderAlreadyExistsException;
import com.Whodundid.main.util.enhancedGui.guiUtil.ObjectModifyTypes;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.main.util.miscUtil.CursorHelper;
import com.Whodundid.main.util.miscUtil.EFontRenderer;
import com.Whodundid.main.util.miscUtil.Resources;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.main.util.storageUtil.EDimension;
import com.Whodundid.main.util.storageUtil.EArrayList;
import com.Whodundid.main.util.storageUtil.StorageBox;
import com.Whodundid.main.util.storageUtil.StorageBoxHolder;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

//Jan 4, 2019
//Jan 21, 2019
//Jan 24, 2019 : added objsToBeAdded
//Jan 25, 2019 : fixed logic behind adding objects with drawing delays, implemented fix for adding objects when moving
//Last edited: Jan 27, 2019
//Edit note: removed gWidth/height -> width/height, shadowed GuiScreen width/height with sWidth/height
//First Added: Sep 7, 2018
//Author: Hunter Bragg

public abstract class EnhancedGui extends GuiScreen implements IEnhancedTopParent {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
	public static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	public EnhancedGui guiInstance;
	protected IEnhancedGuiObject parent, modifyingObject;
	public URI clickedLinkURI;
	protected EGuiFocusLockBorder border;
	public int startXPos, startYPos, startWidth, startHeight;
	public int sWidth = 0, sHeight = 0;
	public int wPos = 0, hPos = 0;
	public int startX, startY, endX, endY;
	public int width = 200, height = 256;
	protected long lastMouseEvent;
	protected long timeSinceLastClick;
	protected int mX, mY;
	protected int lastMouseButton = -1;
	protected int lastScrollChange = 0;
	public boolean mouseClicked = false;
	public boolean leftClick = false;
	public boolean rightClick = false;
	public boolean middleClick = false;
	public boolean useCustomPosition = false;
	protected boolean backwardsTraverseable = true;
	protected boolean hasBeenInitialized = false;
	protected boolean enabled = true;
	protected boolean visible = true;
	public boolean isMouseHover = false;
	public boolean mouseEntered = false;
	protected boolean resizeable = false;
	protected boolean positionLocked = false;
	protected boolean persistent = false;
	protected boolean closeAndRecenter = false;
	protected int minWidth = 0;
	protected int minHeight = 0;
	protected int maxWidth = 0;
	protected int maxHeight = 0;
	public int objZLevel = 0;
	public int objectId = -1;
	protected int highestZLevel = 0;
	protected int lowestZLevel = 0;
	protected Stack<EnhancedGui> guiHistory = new Stack();
	protected Deque<FocusEvent> focusQueue = new ArrayDeque();
	public EFontRenderer fontRenderer;
	public RenderItem itemRenderer;
	protected IEnhancedGuiObject focusedObject, defaultFocusObject, focusLockObject;
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> newDrawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> drawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected Direction oldArea = Direction.OUT, resizingDir;
	protected ObjectModifyTypes modifyType = ObjectModifyTypes.none;
	protected StorageBoxHolder<Integer, Integer> originalButtonPositions = new StorageBoxHolder();
	protected EObjectGroup objectGroup;
	protected EGuiHeader header;
	protected ScaledResolution res;
	
	protected EnhancedGui() { guiInstance = this; }
	protected EnhancedGui(int posX, int posY) { this(posX, posY, null); }
	protected EnhancedGui(EnhancedGui oldGuiIn) {
		guiInstance = this;
		if (oldGuiIn != null) {
			guiHistory = oldGuiIn.getGuiHistory();
			guiHistory.push(oldGuiIn);
		}
	}
	protected EnhancedGui(int posX, int posY, EnhancedGui oldGuiIn) {
		startX = posX;
		startY = posY;
		useCustomPosition = true;
		guiInstance = this;
		if (oldGuiIn != null) {
			guiHistory = oldGuiIn.getGuiHistory();
			guiHistory.push(oldGuiIn);
		}
	}
	
	//-------------------
	//GuiScreen Overrides
	//-------------------
	
	@Override
	public void initGui() {
		res = new ScaledResolution(mc);
		sWidth = res.getScaledWidth();
		sHeight = res.getScaledHeight();
		fontRenderer = MainMod.getFontRenderer();
		itemRenderer = itemRender;
		startXPos = (sWidth / 2) - (width / 2);
		startYPos = (sHeight / 2) - (height / 2);
		startWidth = width;
		startHeight = height;
		if (!useCustomPosition) {
			startX = startXPos;
			startY = startYPos;
		}
		updatePosition();
		originalButtonPositions.allowDuplicates = true;
		
		header = new EGuiHeader(this);
		header.setPersistent(true);
		addObject(header);
		header.updateFileUpButtonVisibility();
		
		initObjects();
		hasBeenInitialized = true;
	}
	
	/**Place at end of initGui(). Only required if vanilla GuiButton is used.*/
	public void finishInit() {
		for (GuiButton b : buttonList) { originalButtonPositions.add(b.xPosition, b.yPosition); }
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		if (checkDraw()) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawObject(mX, mY, ticks);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			synchronized (drawOrder) {
				for (IEnhancedGuiObject o : drawOrder) {
					if (o.checkDraw()) {
	    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    	        	o.drawObject(mXIn, mYIn, ticks);
	    			}
				}
				for (GuiButton b : buttonList) { b.drawButton(this.mc, mX, mY); }
				for (GuiLabel l : labelList) { l.drawLabel(this.mc, mX, mY); }
			}
			GlStateManager.popMatrix();
		}
	}
	
	//updateScreen
	@Override
	public void updateScreen() {
		for (IEnhancedGuiObject o : guiObjects) { o.updateScreen(); }
		super.updateScreen();
	}
	
	//basic inputs
	@Override
	protected void mouseClicked(int mX, int mY, int button) throws IOException {
		IEnhancedGuiObject underMouse = getHighestZObjectUnderMouse();
		if (focusedObject == this) {
			if (underMouse != this) { focusQueue.add(new FocusEvent(underMouse, FocusType.mousePress, button, mX, mY)); }
			else { mousePressed(mX, mY, button); }
		}
		else if (focusedObject != null && focusedObject != this) {
			if (focusLockObject != null) {
				if (underMouse != null) {
					if (underMouse.equals(focusLockObject) || underMouse.isChildOfObject(focusLockObject)) {
						focusQueue.add(new FocusEvent(underMouse, FocusType.mousePress, button, mX, mY));
					} else { focusLockObject.drawFocusLockBorder(); }
				} else { focusLockObject.drawFocusLockBorder(); }
			}
			else if (underMouse != null) {
				if (underMouse.equals(focusedObject)) {
					focusedObject.mousePressed(mX, mY, button);
				} else {
					focusQueue.add(new FocusEvent(underMouse, FocusType.mousePress, button, mX, mY));
				}
			}
		}
		else { clearFocusedObject(); }
		super.mouseClicked(mX, mY, button);
	}
	protected void mouseUnclicked(int mX, int mY, int button) {
		if (modifyingObject != null && modifyType == ObjectModifyTypes.moveAlreadyClicked) { modifyType = ObjectModifyTypes.none; modifyingObject = null; }
		if (isResizing()) { modifyType = ObjectModifyTypes.none; modifyingObject = null; }
		mouseReleased(mX, mY, button);
		if (focusedObject != null && focusedObject != this) { focusedObject.mouseReleased(mX, mY, button); }
		super.mouseReleased(mX, mY, button);
	}
	@Override
	public void mouseClickMove(int mX, int mY, int button, long timeSinceLastClick) {
		mouseDragged(mX, mY, button, timeSinceLastClick);
		if (focusedObject != null) { focusedObject.mouseDragged(mX, mY, button, timeSinceLastClick); }
		super.mouseClickMove(mX, mY, button, timeSinceLastClick);
	}
	@Override protected void keyTyped(char typedChar, int keyCode) throws IOException { if (keyCode == 1) { closeGui(); } }
	
	//basic input handlers
	@Override
	public void handleMouseInput() throws IOException {
		mX = (Mouse.getEventX() * sWidth / mc.displayWidth);
        mY = (sHeight - Mouse.getEventY() * sHeight / mc.displayHeight - 1);
        parseMousePosition(mX, mY);
        
		int button = Mouse.getEventButton();
        
        if (Mouse.hasWheel()) {
        	lastScrollChange = Integer.signum(Mouse.getEventDWheel());
        	if (lastScrollChange != 0) { mouseScrolled(lastScrollChange); }
        }
        
        if (Mouse.getEventButtonState()) {
        	lastMouseEvent = Minecraft.getSystemTime();
        	lastMouseButton = button;
        	mouseClicked = true;
        	leftClick = (button == 0);
        	rightClick = (button == 1);
        	middleClick = (button == 2);
            mouseClicked(mX, mY, lastMouseButton);
        } else if (button != -1) {
        	lastMouseButton = -1;
        	mouseClicked = false;
        	leftClick = false;
        	rightClick = false;
        	middleClick = false;
            mouseUnclicked(mX, mY, button);
        } else if (lastMouseButton != -1 && this.lastMouseEvent > 0L) {
        	timeSinceLastClick = Minecraft.getSystemTime() - lastMouseEvent;
            mouseClickMove(mX, mY, lastMouseButton, timeSinceLastClick);
        }
	}
	@Override
	public void handleKeyboardInput() throws IOException {
		if (Keyboard.getEventKeyState()) {
			if (Keyboard.getEventKey() == 41) {
				if (focusedObject instanceof EGuiButton) {
					System.out.println("FocuedObject: " + (((EGuiButton) focusedObject).displayString.isEmpty() ? focusedObject : "EGuiButton: " + ((EGuiButton) focusedObject).displayString));
				}
				else { System.out.println("FocuedObject: " + focusedObject); }
				System.out.println("GuiHistory: " + guiHistory);
				System.out.println("ModifyingObject & type: " + modifyingObject + " " + modifyType);
			}
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			if (focusedObject != null) { focusedObject.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
		} else {
			if (focusedObject != null) { focusedObject.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
		}
		mc.dispatchKeypresses();
	}
	
	//send chat
	@Override public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	@Override public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) { mc.ingameGUI.getChatGUI().addToSentMessages(msg); }
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
        ChatUtil.sendLongerChatMessage(msg);
    }
	
	//defaultBackground
	@Override
	public void drawDefaultBackground() {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff383838);
		drawRect(startX + 2, startY + 2, endX - 2, endY - 2, 0xff3f3f3f);
		drawRect(startX + 3, startY + 3, endX - 3, endY - 3, 0xff424242);
	}
	
	//displayGuiScreen call
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		itemRender = mc.getRenderItem();
		fontRenderer = MainMod.getFontRenderer();
		fontRendererObj = mc.fontRendererObj;
		sWidth = width;
		sHeight = height;
		if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			buttonList.clear();
			guiObjects.clear();
			clearFocusedObject();
			setObjectRequestingFocus(null);
			initGui();
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}
	
	//resize
	@Override
	public void onResize(Minecraft mcIn, int width, int height) {
		useCustomPosition = true;
		sWidth = width;
		sHeight = height;
		setPosition(width / 2 - width / 2, height / 2 - height / 2);
	}
	
	@Override public boolean doesGuiPauseGame() { return false; }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean hasBeenInitialized() { return hasBeenInitialized; }
	@Override public void initObjects() {}
	@Override
	public void reInitObjects() {
		IEnhancedTopParent p = getTopParent();
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!p.isResizing()) {
			if (children.contains(p.getFocusedObject())) { p.clearFocusedObject(); }
			if (children.contains(p.getFocusLockObject())) { p.clearFocusLockObject(); }
			if (children.contains(p.getModifyingObject())) { p.clearModifyingObject(); }
		}
		guiObjects.clear();
		
		initObjects();
	}
	@Override public void onObjectAddedToParent() {}
	
	//main draw
	@Override public void drawObject(int mXIn, int mYIn, float ticks) {}
	@Override
	public void updateCursorImage() {
		if (isResizeable()) {
			int rStartY = hasHeader() ? getHeader().startY : startY;
			boolean inside = (mX >= startX && mX <= endX && mY >= rStartY && mY <= endY);
			Direction newArea = getEdgeAreaMouseIsOn();
			if (newArea != oldArea) {
				if (!Mouse.isButtonDown(0)) {
					oldArea = newArea;
					switch (newArea) {
					case N:
					case S: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeNS)); } break;
					case E:
					case W: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeEW)); } break;
					case NE:
					case SW: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeDL)); } break;
					case NW:
					case SE: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeDR)); } break;
					default: CursorHelper.setCursor(null); break;
					}
				} else { CursorHelper.setCursor(null); }				
			}
		}
	}
	
	//obj ids
	@Override public int getObjectID() { return objectId; }
	@Override public EnhancedGui setObjectID(int idIn) { objectId = idIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public EnhancedGui setEnabled(boolean val) { enabled = val; return this; }
	@Override public EnhancedGui setVisible(boolean val) { visible = val; return this; }
	@Override public EnhancedGui setPersistent(boolean val) { persistent = val; return this; }
	
	//size
	@Override public boolean hasHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return true; } } return false; }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public EGuiHeader getHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return (EGuiHeader) o; } } return null; }
	@Override public int getMinimumWidth() { return minWidth; }
	@Override public int getMinimumHeight() { return minHeight; }
	@Override public int getMaximumWidth() { return maxWidth; }
	@Override public int getMaximumHeight() { return maxHeight; }
	@Override public EnhancedGui setMinimumWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public EnhancedGui setMinimumHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public EnhancedGui setMaximumWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public EnhancedGui setMaximumHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public EnhancedGui setResizeable(boolean val) { resizeable = val; return this; }
	@Override
	public EnhancedGui resize(int xIn, int yIn, Direction areaIn) {
		if (xIn != 0 || yIn != 0) {
			int x = 0, y = 0, w = 0, h = 0;
			boolean e = false, s = false;
			switch (areaIn) {
			case N: x = startX; y = startY + yIn; w = width; h = height - yIn; break;
			case S: x = startX; y = startY; w = width; h = height + yIn; break;
			case E: x = startX; y = startY; w = width + xIn; h = height; break;
			case W: x = startX + xIn; y = startY; w = width - xIn; h = height; break;
			case NE: x = startX; y = startY + yIn; w = width + xIn; h = height - yIn; break;
			case SE: x = startX; y = startY; w = width + xIn; h = height + yIn; break;
			case NW: x = startX + xIn; y = startY + yIn; w = width - xIn; h = height - yIn; break;
			case SW: x = startX + xIn; y = startY; w = width - xIn; h = height + yIn; break;
			default: break;
			}
			if (w < getMinimumWidth()) {
				w = getMinimumWidth();
				switch (areaIn) {
				case E: case NE: case SE: x = startX; break;
				case W: case NW: case SW: x = endX - w; break;
				default: break;
				}
			}
			if (h < getMinimumHeight()) {
				h = getMinimumHeight();
				switch (areaIn) {
				case N: case NE: case NW: y = endY - h; break;
				case S: case SE: case SW: y = startY; break;
				default: break;
				}
			}
			setDimensions(x, y, w, h);
			reInitObjects();
		}
		return this;
	}
	
	//position
	@Override
	public void move(int newX, int newY) {
		if (!positionLocked) {
			EArrayList<IEnhancedGuiObject> objs = new EArrayList(guiObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IEnhancedGuiObject> it = objs.iterator();
			while (it.hasNext()) {
				IEnhancedGuiObject o = it.next();
				if (!o.isPositionLocked()) {
					if (o instanceof InnerEnhancedGui) {
						if (((InnerEnhancedGui) o).movesWithParent()) { o.move(newX, newY); }
					} else { o.move(newX, newY); }
				}
			}
			startX += newX;
			startY += newY;
			setDimensions(startX, startY, width, height);
		}
	}
	@Override public boolean isPositionLocked() { return positionLocked; }
	@Override public EnhancedGui resetPosition() { setPosition(startXPos, startYPos); return this; }
	@Override
	public EnhancedGui setPosition(int xIn, int yIn) {
		startX = xIn;
		startY = yIn;
		setWorldAndResolution(MainMod.getMC(), sWidth, sHeight);
		return this;
	}
	@Override public EnhancedGui setPositionLocked(boolean val) { positionLocked = val; return this; }
	@Override public EnhancedGui setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public EnhancedGui setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		startX = startXIn;
		startY = startYIn;
		width = widthIn;
		height = heightIn;
		endX = startX + widthIn;
		endY = startY + heightIn;
		wPos = startX + width / 2;
		hPos = startY + height / 2;
		return this;
	}
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override
	public boolean isChildOfObject(IEnhancedGuiObject objIn) {
		IEnhancedGuiObject curObj = this;
		while (curObj != null && curObj.getParent() != null) {
			if (curObj.getParent().equals(objIn)) { return true; }
			curObj = curObj.getParent();
		}
		return false;
	}
	@Override
	public EnhancedGui addObject(IEnhancedGuiObject... objsIn) {
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
	@Override public EnhancedGui removeObject(IEnhancedGuiObject... objsIn) { objsToBeRemoved.addAll(objsIn); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public EnhancedGui setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
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
	@Override public IEnhancedGuiObject getParent() { return parent; }
	@Override public EnhancedGui setParent(IEnhancedGuiObject parentIn) { parent = parentIn; return this; }
	@Override
	public IEnhancedTopParent getTopParent() {
		IEnhancedGuiObject parentObj = getParent();
		while (parentObj != null) {
			if (parentObj instanceof IEnhancedTopParent) { return (IEnhancedTopParent) parentObj; }
			if (parentObj.getParent() != null) { parentObj = parentObj.getParent(); }
		}
		return this;
	}
	
	//zLevel
	@Override public int getZLevel() { return objZLevel; }
	@Override public int getHighestZLevel() { return highestZLevel; }
	@Override public int getLowestZLevel() { return lowestZLevel; }
	@Override
	public void updateZLevel() {
		int highestZ = 0, lowestZ = 0;
		for (IEnhancedGuiObject o : guiObjects) {
			int val = o.getZLevel();
			if (val > highestZ) { highestZ = val; }
			if (val < lowestZ) { lowestZ = val; }
		}
		highestZLevel = highestZ;
		lowestZLevel = lowestZ;
		
		for (int i = lowestZLevel; i <= highestZLevel; i++) {
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.getZLevel() == i) { newDrawOrder.add(o); }
			}
		}
	}
	@Override public EnhancedGui setZLevel(int zLevelIn) { objZLevel = zLevelIn; getTopParent().updateZLevel(); return this; }
	@Override public EnhancedGui bringForward() { objZLevel++; getTopParent().updateZLevel(); return this; }
	@Override
	public EnhancedGui bringToFront() {
		int zDifference = getHighestZLevel() - getLowestZLevel();
		
		setZLevel(MathHelper.clamp_int(getTopParent().getHighestZLevel() + 1, 0, Integer.MAX_VALUE));
		for (IEnhancedGuiObject o : getTopParent().getAllChildren()) {
			if (!o.isChildOfObject(this)) {
				o.setZLevel(MathHelper.clamp_int(o.getZLevel() - zDifference, 0, Integer.MAX_VALUE));
			} else {
				o.setZLevel(MathHelper.clamp_int(getZLevel() + o.getZLevel() + 1, 0, Integer.MAX_VALUE));
			}
		}
		
		getTopParent().updateZLevel();
		return this;
	}
	@Override public EnhancedGui sendBackwards() { objZLevel = objZLevel == 0 ? 0 : objZLevel - 1; getTopParent().updateZLevel(); return this; }
	@Override
	public EnhancedGui sendToBack() {
		int zDifference = getHighestZLevel() - getLowestZLevel();
		
		for (IEnhancedGuiObject o : getTopParent().getAllChildren()) {
			if (!o.isChildOfObject(this)) {
				o.setZLevel(MathHelper.clamp_int(o.getZLevel() + zDifference, 0, Integer.MAX_VALUE));
			} else {
				o.setZLevel(MathHelper.clamp_int(o.getZLevel() - 1, 0, Integer.MAX_VALUE));
			}
		}
		setZLevel(MathHelper.clamp_int(getZLevel() - 1, 0, Integer.MAX_VALUE));
		
		getTopParent().updateZLevel();
		return this;
	}
	
	//focus
	@Override public boolean hasFocus() { return getTopParent().getFocusedObject().equals(this); }
	@Override public boolean relinquishFocus() {
		System.out.println("here");
		if (doesFocusLockExist() && getFocusLockObject().equals(this)) { clearFocusLockObject(); }
		else if (hasFocus()) {
			System.out.println("ive got focus");
			if (!getTopParent().equals(this)) { getTopParent().setObjectRequestingFocus(getTopParent()); return true; }
		}
		return false;
	}
	@Override public void onFocusGained(FocusEvent eventIn) { if (eventIn.type.equals(FocusType.mousePress)) { mousePressed(eventIn.mX, eventIn.mY, eventIn.actionCode); } }
	@Override public void onFocusLost(FocusEvent eventIn) {}
	@Override
	public void transferFocus(IEnhancedGuiObject objIn) {
		if (!doesFocusLockExist() && objIn != null) {
			relinquishFocus();
			getTopParent().setObjectRequestingFocus(objIn);
		}
	}
	@Override
	public void drawFocusLockBorder() {
		if (checkDraw() && border == null) {
			if (hasHeader() && header.isEnabled()) {
				addObject(border = new EGuiFocusLockBorder(this, header.startX, header.startY, width, height + header.height));
			} else { addObject(border = new EGuiFocusLockBorder(this)); }
		}
	}
	@Override
	public EnhancedGui requestFocus() {
		if (!hasFocus()) {
			if (doesFocusLockExist()) {
				//getTopParent().setObjectRequestingFocus(this);
			} else { getTopParent().setObjectRequestingFocus(this); }
		}
		return this;
	}
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return checkDraw() && !getEdgeAreaMouseIsOn().equals(Direction.OUT); }
	@Override
	public Direction getEdgeAreaMouseIsOn() {
		boolean left = false, right = false, top = false, bottom = false;
		int rStartY = hasHeader() ? getHeader().startY : startY;
		if (mX >= startX && mX <= startX + 1) { left = true; }
		if (mX <= endX && mX >= endX - 1) { right = true; }
		if (mY >= rStartY && mY <= rStartY + 1) { top = true; }
		if (mY <= endY && mY >= endY - 1) { bottom = true; }
		if (checkDraw() && !(left || right || top || bottom)) { return Direction.OUT; }
		if (left) {
			if (top) { return Direction.NW; }
			else if (bottom) { return Direction.SW; }
			else { return Direction.W; }
		} 
		else if (right) {
			if (top) { return Direction.NE; }
			else if (bottom) { return Direction.SE; }
			else { return Direction.E; }
		} 
		else if (top) { return Direction.N; }
		else { return Direction.S; }
	}
	@Override public void mouseEntered(int mX, int mY) {}
	@Override public void mouseExited(int mX, int mY) {}
	@Override public boolean isMouseInside(int mX, int mY) { return mX >= startX && mX <= endX && mY >= startY && mY <= endY; }
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
				//System.out.println(highestZObj);
				return highestZObj;
			}
			if (checkDraw() && isMouseInside(mX, mY)) { return this; }
		}
		
		return null;
	}
	
	//basic inputs
	@Override
	public void parseMousePosition(int mX, int mY) {
		for (IEnhancedGuiObject o : guiObjects) {
			if (o.isMouseInside(mX, mY)) { o.parseMousePosition(mX, mY); }
		}
	}
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (isMouseHover) { requestFocus(); }
		if (button == 0 && isResizeable() && !getEdgeAreaMouseIsOn().equals(Direction.OUT)) {
			getTopParent().setResizingDir(getEdgeAreaMouseIsOn());
			getTopParent().setModifyMousePos(mX, mY);
			getTopParent().setModifyingObject(this, ObjectModifyTypes.resize);
		}
	}
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (getTopParent().isResizing()) { getTopParent().clearModifyingObject(); }
		if (getTopParent().getDefaultFocusObject() != null) { getTopParent().getDefaultFocusObject().requestFocus(); }
	}
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {}
	@Override
	public void mouseScrolled(int change) {
		for (IEnhancedGuiObject o : guiObjects) {
			if (o.isMouseInside(mX, mY) && o.checkDraw()) { o.mouseScrolled(change); }
		}
	}
	@Override public void keyPressed(char typedChar, int keyCode) {}
	@Override public void keyReleased(char typedChar, int keyCode) {}
	
	//action object
	@Override public void actionPerformed(IEnhancedActionObject object) {}
	
	//close object
	@Override public void close() {
		if (parent == null || parent == this) {
			mc.displayGuiScreen(null);
			if (mc.currentScreen == null) { mc.setIngameFocus(); }
		}
		else { parent.removeObject(this); }
	}
	
	//-------------------------
	//IEnhancedTopGui Overrides
	//-------------------------
	
	//history
	@Override public Stack<EnhancedGui> getGuiHistory() { return guiHistory; }
	@Override
	public EnhancedGui sendGuiHistory(Stack<EnhancedGui> historyIn) {
		guiHistory = historyIn;
		if (header != null) { header.updateFileUpButtonVisibility(); }
		return this;
	}
	
	//focus
	@Override public IEnhancedGuiObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public EnhancedGui setDefaultFocusObject(IEnhancedGuiObject objIn) { defaultFocusObject = objIn; return this; }
	@Override public IEnhancedGuiObject getFocusedObject() { return focusedObject; }
	@Override public EnhancedGui setObjectRequestingFocus(IEnhancedGuiObject objIn) { focusQueue.add(new FocusEvent(objIn, FocusType.transfer)); return this; }
	@Override public IEnhancedGuiObject getFocusLockObject() { return focusLockObject; }
	@Override public EnhancedGui setFocusLockObject(IEnhancedGuiObject objIn) {
		focusLockObject = objIn;
		transferFocus(focusLockObject);
		return this;
	}
	@Override public EnhancedGui clearFocusLockObject() { focusLockObject = null; return this; }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override
	public void clearFocusedObject() {
		if (focusedObject != null && focusedObject != this) { focusedObject.relinquishFocus(); }
		if (parent == null || parent == this) { focusedObject = this; }
		else { transferFocus(parent); }
	}
	@Override
	public void updateFocus() {
		if (modifyingObject != null && !modifyingObject.isResizeable() && modifyType.equals(ObjectModifyTypes.resize)) { modifyType = ObjectModifyTypes.none; }
		
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
						if (obj != focusedObject) {
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
	@Override public EnhancedGui setModifyingObject(IEnhancedGuiObject objIn, ObjectModifyTypes typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override public EnhancedGui setResizingDir(Direction areaIn) { resizingDir = areaIn; return this; }
	@Override public EnhancedGui setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IEnhancedGuiObject getModifyingObject() { return modifyingObject; }
	@Override public EnhancedGui clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyTypes.none; return this; }
	
	//close
	@Override
	public void closeGui() {
		if (backwardsTraverseable && !guiHistory.isEmpty() && guiHistory.peek() != null) {
			try {
				EnhancedGui oldGuiPass = guiHistory.pop();
				EnhancedGui newGui = ((EnhancedGui) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
				if (newGui instanceof SettingsGui) {
					((SettingsGui) newGui).setPageToBeLoaded(((SettingsGui) oldGuiPass).getCurrentPageNum());
				}
				if (!closeAndRecenter) {
					newGui.useCustomPosition = true;
					newGui.setPosition(startX, startY);
				}
				newGui.sendGuiHistory(oldGuiPass.getGuiHistory());
				mc.displayGuiScreen(newGui);
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			mc.displayGuiScreen(null);
	        if (mc.currentScreen == null) { mc.setIngameFocus(); }
		}
	}
	@Override public EnhancedGui setCloseAndRecenter(boolean val) { closeAndRecenter = val; return this; }
	
	//-------------------
	//EnhancedGui methods
	//-------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		this.mX = mXIn; this.mY = mYIn;
		res = new ScaledResolution(mc);
		isMouseHover = isMouseInside(mX, mY) && getTopParent().getHighestZObjectUnderMouse() != null && getTopParent().getHighestZObjectUnderMouse().equals(this);
		if (!mouseEntered && isMouseHover) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		if (!newDrawOrder.isEmpty()) { drawOrder = new EArrayList(newDrawOrder); newDrawOrder.clear(); }
		updateFocus();
		if (modifyingObject != null) {
			switch (modifyType) {
			case move:
			case moveAlreadyClicked: modifyingObject.move(mX - mousePos.getObject(), mY - mousePos.getValue()); mousePos.setValues(mX, mY); break;
			case resize: modifyingObject.resize(mX - mousePos.getObject(), mY - mousePos.getValue(), resizingDir); mousePos.setValues(mX, mY); break;
			default: break;
			}
		}
		updateCursorImage();
	}
	
	public EnhancedGui centerGuiWithDimensions(int widthIn, int heightIn) {
		ScaledResolution res = new ScaledResolution(mc);
		int sWidth = res.getScaledWidth();
		int sHeight = res.getScaledHeight();
		if (sWidth >= widthIn) {
			startX = (sWidth - widthIn) / 2;
			width = widthIn;
		} else {
			startX = 0;
			width = sWidth;
		};
		if (sHeight >= heightIn) {
			startY = (sHeight - heightIn) / 2;
			height = heightIn;
		} else {
			startY = 0;
			height = sHeight;
		}
		useCustomPosition = true;
		setCloseAndRecenter(true);
		return this;
	}
	
	public EnhancedGui enableHeader(boolean val) {
		header.setEnabled(val);
		return this;
	}
	
	protected EnhancedGui updatePosition() {
		wPos = startX + width / 2;
		hPos = startY + height /2;
		endX = startX + width;
		endY = startY + height;
		return this;
	}
	
	protected void removeObjects() {
		synchronized (guiObjects) {
			objsToBeRemoved.forEach(o -> {
				if (o != null) {
					Iterator it = guiObjects.iterator();
					while (it.hasNext()) {
						if (o.equals(it.next())) {
							if (!o.equals(focusedObject)) {
								for (IEnhancedGuiObject child : o.getObjects()) {
									if (child.equals(focusedObject)) { child.relinquishFocus(); }
								}
							} else { o.relinquishFocus(); }
							if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).onInnerGuiClose(); }
							if (o.equals(border)) { border = null; }
							it.remove();
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
	
	public void drawMenuGradient() { this.drawGradientRect(0, 0, sWidth, sHeight, -1072689136, -804253680); }
	
	protected int drawString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x, y, color); }
    protected int drawCenteredString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x, y, color); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x, y, color, height); }
    protected int drawCenteredString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	protected int drawStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x, y, color, height); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	
	public static void drawCustomSizedTexture(int x, int y, double u, double v, double width, double height, double textureWidth, double textureHeight) {
        double f = 1.0 / textureWidth;
        double f1 = 1.0 / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
	
	public EnhancedGui setGuiWidthAndHeight(int widthIn, int heightIn) { width = widthIn; height = heightIn; updatePosition(); return this; }
}
