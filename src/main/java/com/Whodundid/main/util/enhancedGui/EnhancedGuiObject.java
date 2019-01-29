package com.Whodundid.main.util.enhancedGui;

import com.Whodundid.enhancedChat.EnhancedChat;
import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiFocusLockBorder;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.main.util.enhancedGui.guiObjects.EGuiLinkConfirmationDialogueBox;
import com.Whodundid.main.util.enhancedGui.guiObjects.InnerEnhancedGui;
import com.Whodundid.main.util.enhancedGui.guiUtil.EGui;
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
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tv.twitch.chat.ChatUserInfo;

//Jan 3, 2019
//Jan 24, 2019 : added objsToBeAdded
//Jan 25, 2019 : fixed logic behind adding objects with drawing delays, implemented fix for adding objects when moving
//Last edited: Jan 26, 2019
//Edit note: implemented a fix for zLevel drawing using drawOrder and newDrawOrder which are updated thorugh updateZLevel
//First Added: Sep 19, 2018
//Author: Hunter Bragg

public abstract class EnhancedGuiObject extends EGui implements IEnhancedGuiObject {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
	public static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	public EnhancedChat chatMod = (EnhancedChat) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
	public EnhancedGuiObject objectInstance;
	protected ScaledResolution res;
	protected EFontRenderer fontRenderer = MainMod.getFontRenderer();
	protected IEnhancedGuiObject parent, movingObject;
	protected EGuiFocusLockBorder border;
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> newDrawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> drawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected Direction oldArea = Direction.OUT;
	protected EObjectGroup objectGroup;
	public URI clickedLinkURI;
	protected boolean hasBeenInitialized = false;
	protected boolean enabled = true;
	protected boolean visible = true;
	public boolean isMouseHover = false;
	public boolean mouseEntered = false;
	protected boolean positionLocked = false;
	protected boolean hasFocus = false;
	protected boolean focusLock = false;
	protected boolean isVanillaParent = false;
	protected boolean persistent = false;
	protected boolean resizeable = false;
	protected int minWidth = 0;
	protected int minHeight = 0;
	protected int maxWidth = 0;
	protected int maxHeight = 0;
	protected int highestZLevel = 0;
	protected int lowestZLevel = 0;
	public int objZLevel = 0;
	public int objectId = -1;
	public int startXPos, startYPos, startWidth, startHeight;
	public int startX, startY, endX, endY;
	public int width, height;
	public int midX, midY;
	public int mX, mY;
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn) {
		parent = objIn;
		startX = xIn;
		startY = yIn;
		startXPos = startX;
		startYPos = startY;
		objectInstance = this;
	}
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(objIn, xIn, yIn, widthIn, heightIn, -1);
	}
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
		parent = objIn;
		objectId = objectIdIn;
		startXPos = xIn;
		startYPos = yIn;
		startWidth = widthIn;
		startHeight = heightIn;
		setDimensions(xIn, yIn, widthIn, heightIn);
		objectInstance = this;
	}
	
	//init
	@Override public boolean hasBeenInitialized() { return hasBeenInitialized; }
	@Override public void initObjects() {}
	@Override
	public void reInitObjects() {
		hasBeenInitialized = false;
		IEnhancedTopParent p = getTopParent();
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!p.isResizing()) {
			if (children.contains(p.getFocusedObject())) { p.clearFocusedObject(); }
			if (children.contains(p.getFocusLockObject())) { p.clearFocusLockObject(); }
			if (children.contains(p.getModifyingObject())) { p.clearModifyingObject(); }
		}
		guiObjects.clear();
		
		initObjects();
		hasBeenInitialized = true;
	}
	@Override public void onObjectAddedToParent() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (checkDraw()) {
				
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
		} catch (Exception e) { e.printStackTrace(); }
	}
	@Override
	public void updateCursorImage() {
		if (isResizeable() && !getTopParent().isResizing()) {
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
	@Override public EnhancedGuiObject setObjectID(int idIn) { objectId = idIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public EnhancedGuiObject setEnabled(boolean val) { enabled = val; return this; }
	@Override public EnhancedGuiObject setVisible(boolean val) { visible = val; return this; }
	@Override public EnhancedGuiObject setPersistent(boolean val) { persistent = val; return this; }
	
	//size
	@Override public boolean hasHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return true; } } return false; }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public EGuiHeader getHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return (EGuiHeader) o; } } return null; }
	@Override public int getMinimumWidth() { return minWidth; }
	@Override public int getMinimumHeight() { return minHeight; }
	@Override public int getMaximumWidth() { return maxWidth; }
	@Override public int getMaximumHeight() { return maxHeight; }
	@Override public EnhancedGuiObject setMinimumWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public EnhancedGuiObject setMinimumHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public EnhancedGuiObject setMaximumWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public EnhancedGuiObject setMaximumHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public EnhancedGuiObject setResizeable(boolean val) { resizeable = val; return this; }
	@Override
	public EnhancedGuiObject resize(int xIn, int yIn, Direction areaIn) {
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
	@Override
	public EnhancedGuiObject resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		for (IEnhancedGuiObject o : guiObjects) { o.resetPosition(); }
		return this;
	}
	@Override public EnhancedGuiObject setPosition(int newX, int newY) { setDimensions(newX, newY, width, height); return this; }
	@Override public EnhancedGuiObject setPositionLocked(boolean val) { positionLocked = val; return this; }
	@Override public EnhancedGuiObject setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public EnhancedGuiObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		startX = startXIn;
		startY = startYIn;
		width = widthIn;
		height = heightIn;
		endX = startX + widthIn;
		endY = startY + heightIn;
		midX = startX + width / 2;
		midY = startY + height / 2;
		return this;
	}
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override
	public boolean isChildOfObject(IEnhancedGuiObject objIn) {
		IEnhancedGuiObject curObj = this;
		while (curObj != null && curObj.getParent() != null) {
			if (curObj.getParent().equals(curObj)) { return false; }
			if (curObj.getParent().equals(objIn)) { return true; }
			curObj = curObj.getParent();
		}
		return false;
	}
	@Override
	public EnhancedGuiObject addObject(IEnhancedGuiObject... objsIn) {
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
		//getTopParent().addObject(objsIn);
		objsToBeAdded.addAll(objsIn);
		return this;
	}
	@Override
	public EnhancedGuiObject removeObject(IEnhancedGuiObject... objsIn) {
		//getTopParent().removeObject(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public EnhancedGuiObject setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
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
	@Override public EnhancedGuiObject setParent(IEnhancedGuiObject parentIn) { parent = parentIn; return this; }
	@Override
	public IEnhancedTopParent getTopParent() {
		IEnhancedGuiObject parentObj = getParent();
		while (parentObj != null) {
			if (parentObj instanceof IEnhancedTopParent) { return (IEnhancedTopParent) parentObj; }
			if (parentObj.getParent() != null) { parentObj = parentObj.getParent(); }
		}
		return null;
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
	@Override public EnhancedGuiObject setZLevel(int zLevelIn) { objZLevel = zLevelIn; updateZLevel(); return this; }
	@Override public EnhancedGuiObject bringForward() { objZLevel++; getTopParent().updateZLevel(); return this; }
	@Override
	public EnhancedGuiObject bringToFront() {
		int zDifference = getHighestZLevel() - getLowestZLevel();
		
		setZLevel(MathHelper.clamp_int(getTopParent().getHighestZLevel() + 1, 0, Integer.MAX_VALUE));
		System.out.println("zLevel: " + this.getZLevel());
		//System.out.println(getAllChildren().size());
		for (IEnhancedGuiObject o : getAllChildren()) {
			System.out.println(o);
			o.setZLevel(MathHelper.clamp_int(getZLevel() + o.getZLevel() + 1, 0, Integer.MAX_VALUE));
		}
		
		getTopParent().updateZLevel();
		//System.out.println(getZLevel());
		return this;
	}
	@Override public EnhancedGuiObject sendBackwards() { objZLevel = objZLevel == 0 ? 0 : objZLevel - 1; getTopParent().updateZLevel(); return this; }
	@Override
	public EnhancedGuiObject sendToBack() {
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
	@Override public boolean hasFocus() { if (getTopParent().getFocusedObject() != null) { return getTopParent().getFocusedObject().equals(this); } return false; }
	@Override
	public boolean relinquishFocus() {
		if (getTopParent().doesFocusLockExist()) {
			if (getTopParent().getFocusLockObject().equals(this)) {
				getTopParent().setObjectRequestingFocus(getTopParent());
				return true;
			}
			return false;
		}
		getTopParent().setObjectRequestingFocus(getTopParent());
		return true;
	}
	@Override public void onFocusGained(FocusEvent eventIn) { if (eventIn.type.equals(FocusType.mousePress)) { mousePressed(eventIn.mX, eventIn.mY, eventIn.actionCode); } }
	@Override public void onFocusLost(FocusEvent eventIn) {}
	@Override
	public void transferFocus(IEnhancedGuiObject objIn) {
		if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) {
			if (objIn != null) {
				getTopParent().clearFocusLockObject();
				getTopParent().setObjectRequestingFocus(objIn);
			}
		} else if (objIn != null) { getTopParent().setObjectRequestingFocus(objIn); }
	}
	@Override
	public void drawFocusLockBorder() {
		if (checkDraw() && border == null) {
			if (hasHeader() && getHeader().isEnabled()) {
				addObject(border = new EGuiFocusLockBorder(this, getHeader().startX, getHeader().startY, width, height + getHeader().height));
			} else { addObject(border = new EGuiFocusLockBorder(this)); }
		}
	}
	@Override public EnhancedGuiObject requestFocus() {
		if (!hasFocus()) { getTopParent().setObjectRequestingFocus(this); }
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
				return highestZObj;
			}
			if (checkDraw() && isMouseInside(mX, mY)) { return this; }
		}
		
		return null;
	}
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) {}
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
	@Override public void mouseScrolled(int change) {}
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (getTopParent() != null && keyCode == 15) {
			if (guiObjects != null) {
				if (guiObjects.isEmpty()) {
					int thisObjPos = 0;
					for (int i = 0; i < getTopParent().getObjects().size(); i++) {
						if (getTopParent().getObjects().get(i).equals(this)) { thisObjPos = i; }
					}
					if (thisObjPos < getTopParent().getObjects().size() - 1) {
						getTopParent().getObjects().get(thisObjPos + 1).requestFocus();
					}
				} else {
					EnhancedGuiObject selectedChild = null;
					for (IEnhancedGuiObject o : getTopParent().getObjects()) {
						if (guiObjects.contains(o) && o instanceof EnhancedGuiObject) { selectedChild = (EnhancedGuiObject) o; }
					}
					if (selectedChild != null) {
						int childPos = 0;
						for (int i = 0; i < guiObjects.size(); i++) {
							if (selectedChild.equals(guiObjects.get(i))) { childPos = i; }
						}
						if (childPos < guiObjects.size() - 1) { guiObjects.get(childPos + 1).requestFocus(); }
					}
				}
			}
		}
	}
	@Override public void keyReleased(char typedChar, int keyCode) {}
	
	//updateScreen
	@Override
	public void updateScreen() {
		for (IEnhancedGuiObject o : guiObjects) { o.updateScreen(); }
	}
	
	//action object
	@Override public void actionPerformed(IEnhancedActionObject object) {}
	
	//close object
	@Override public void close() {
		if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) { getTopParent().clearFocusLockObject(); }
		if (getTopParent().getFocusedObject().equals(this)) { relinquishFocus(); }
		parent.removeObject(this);
	}
	
	//-------------------------
	//EnhancedGuiObject methods
	//-------------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		isMouseHover = isMouseInside(mXIn, mYIn) && getTopParent().getHighestZObjectUnderMouse() != null && getTopParent().getHighestZObjectUnderMouse().equals(this);
		if (!mouseEntered && isMouseHover) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		if (!newDrawOrder.isEmpty()) { drawOrder = new EArrayList(newDrawOrder); newDrawOrder.clear(); }
		updateCursorImage();
	}
	
	public void setText(String textIn, boolean overwrite) {}
	
	public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) { mc.ingameGUI.getChatGUI().addToSentMessages(msg); }
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
        ChatUtil.sendLongerChatMessage(msg);
    }
	
	protected void removeObjects() {
		synchronized (guiObjects) {
			objsToBeRemoved.forEach(o -> {
				if (o != null) {
					Iterator it = guiObjects.iterator();
					while (it.hasNext()) {
						if (o.equals(it.next())) {
							if (!o.equals(getTopParent().getFocusedObject())) {
								for (IEnhancedGuiObject child : o.getObjects()) {
									if (child.equals(getTopParent().getFocusedObject())) { child.relinquishFocus(); }
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
			getTopParent().updateZLevel();
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
		getTopParent().updateZLevel();
	}
	
	protected void renderToolTip(ItemStack stack, int x, int y) {
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) { list.set(i, stack.getRarity().rarityColor + list.get(i)); }
            else { list.set(i, EnumChatFormatting.GRAY + list.get(i)); }
        }
        drawHoveringText(list, x, y);
    }
	
    protected void drawCreativeTabHoveringText(String tabName, int mX, int mY) {
        drawHoveringText(Arrays.<String>asList(new String[] {tabName}), mX, mY);
    }

    protected void drawHoveringText(List<String> textLines, int mX, int mY) {
    	if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = fontRenderer.getStringWidth(s);

                if (j > i) { i = j; }
            }

            int l1 = mX + 12;
            int i2 = mY - 12;
            int k = 8;

            if (textLines.size() > 1) { k += 2 + (textLines.size() - 1) * 10; }
            //if (l1 + i > width) { l1 -= 28 + i; }
            //if (i2 + k + 6 > height) { i2 = height - k - 6; }

            zLevel = 300.0F;
            renderItem.zLevel = 300.0F;
            int l = -267386864;
            drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l); //top
            drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l); //bottom
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l); //background
            drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l); //left
            drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l); //right
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1); //inner left
            drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1); //inner right
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1); //inner top
            drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1); //inner bottom

            for (int k1 = 0; k1 < textLines.size(); k1++) {
                String s1 = textLines.get(k1);
                drawStringWithShadow(s1, l1, i2, -1);
                if (k1 == 0) { i2 += 2; }
                i2 += 10;
            }

            zLevel = 0.0F;
            renderItem.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
	
	protected void handleComponentHover(IChatComponent componentIn, int mX, int mY)  {
		if (componentIn != null) {
			if (componentIn.getChatStyle().getChatHoverEvent() != null) {
	            HoverEvent hoverevent = componentIn.getChatStyle().getChatHoverEvent();
	            
	            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
	                ItemStack itemstack = null;
	                
	                try {
	                    NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
	                    if (nbtbase instanceof NBTTagCompound) { itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase); }
	                } catch (NBTException var11) { var11.printStackTrace(); }

	                if (itemstack != null) { renderToolTip(itemstack, mX, mY); }
	                else {
	                    drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", mX, mY);
	                }
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
	                if (this.mc.gameSettings.advancedItemTooltips) {
	                    try {
	                        NBTBase nbtbase1 = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());

	                        if (nbtbase1 instanceof NBTTagCompound) {
	                            List<String> list1 = Lists.<String>newArrayList();
	                            NBTTagCompound nbttagcompound = (NBTTagCompound)nbtbase1;
	                            list1.add(nbttagcompound.getString("name"));

	                            if (nbttagcompound.hasKey("type", 8)) {
	                                String s = nbttagcompound.getString("type");
	                                list1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
	                            }

	                            list1.add(nbttagcompound.getString("id"));
	                            drawHoveringText(list1, mX, mY);
	                        }
	                        else {
	                            drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY);
	                        }
	                    }
	                    catch (NBTException var10) {
	                        drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY);
	                    }
	                }
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
	            	String text = hoverevent.getValue().getFormattedText();
	            	if (chatMod.getShowMoreChatInfo()) {
	            		ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
		            	if (clickevent != null && clickevent.getAction() != null) {
		            		if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) { text += "\nRuns command: " + clickevent.getValue(); }
		            	}
	            	}
	            	drawHoveringText(NEWLINE_SPLITTER.splitToList(text), mX, mY);
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
	                StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());

	                if (statbase != null) {
	                    IChatComponent ichatcomponent = statbase.getStatName();
	                    IChatComponent ichatcomponent1 = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
	                    ichatcomponent1.getChatStyle().setItalic(Boolean.valueOf(true));
	                    String s1 = statbase instanceof Achievement ? ((Achievement)statbase).getDescription() : null;
	                    List<String> list = Lists.newArrayList(new String[] {ichatcomponent.getFormattedText(), ichatcomponent1.getFormattedText()});

	                    if (s1 != null) { list.addAll(fontRenderer.listFormattedStringToWidth(s1, 150)); }

	                    drawHoveringText(list, mX, mY);
	                }
	                else { drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", mX, mY); }
	            }

	            GlStateManager.disableLighting();
	        } else if (chatMod.getShowMoreChatInfo()) {
	        	ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
	        	if (clickevent != null && clickevent.getAction() != null) {
	        		if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
	        			String text = clickevent.getValue();
	        			text = text.length() > 60 ? text.substring(0, 57) + "..." : text;
	        			drawHoveringText(NEWLINE_SPLITTER.splitToList("§eOpens webpage: §r\n" + text), mX, mY);
	        		}
	        	}
	        }
		}
    }
	
	protected boolean handleComponentClick(IChatComponent componentIn) {
        if (componentIn == null) { return false; }
		ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
		if (clickevent != null && clickevent.getAction() != null) {
			if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
			    if (!this.mc.gameSettings.chatLinks) { return false; }
			    try {
			        URI uri = new URI(clickevent.getValue());
			        String s = uri.getScheme();

			        if (s == null) { throw new URISyntaxException(clickevent.getValue(), "Missing protocol"); }

			        if (!PROTOCOLS.contains(s.toLowerCase())) {
			            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase());
			        }

			        if (mc.gameSettings.chatLinksPrompt) { addObject(new EGuiLinkConfirmationDialogueBox(this, clickevent.getValue())); }
			        else { openWebLink(clickevent.getValue()); }
			    }
			    catch (URISyntaxException urisyntaxexception) {
			        LOGGER.error("Can\'t open url for " + clickevent, urisyntaxexception);
			    }
			}
			else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
			    openWebLink(clickevent.getValue());
			}
			else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
			    setText(clickevent.getValue(), true);
			}
			else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
			    sendChatMessage(clickevent.getValue(), false);
			}
			else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
			    ChatUserInfo chatuserinfo = this.mc.getTwitchStream().func_152926_a(clickevent.getValue());
			    if (chatuserinfo != null) {
			        mc.displayGuiScreen(new GuiTwitchUserMode(mc.getTwitchStream(), chatuserinfo));
			    }
			    else { LOGGER.error("Tried to handle twitch user but couldn\'t find them!"); }
			}
			else { LOGGER.error("Don\'t know how to handle " + clickevent); }

			return true;
		}
		return false;
    }
	
	protected void openWebLink(String linkIn) {
		if (linkIn != null && !linkIn.isEmpty()) {
			try {
				URI uri = new URI(linkIn);
	            Class<?> oclass = Class.forName("java.awt.Desktop");
	            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
	            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {uri});
	        }
	        catch (Throwable throwable) { LOGGER.error("Couldn\'t open link", throwable); }
		}
    }
	
	public static boolean isCtrlKeyDown() { return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157); }
    public static boolean isShiftKeyDown() { return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54); }
    public static boolean isAltKeyDown() { return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184); }
}
