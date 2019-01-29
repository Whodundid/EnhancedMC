package com.Whodundid.main.util.enhancedGui.guiObjectUtil;

import com.Whodundid.main.util.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.main.util.storageUtil.EArrayList;
import java.util.Iterator;

//Last edited: Jan 10, 2019
//First Added: Jan 10, 2019
//Author: Hunter Bragg

public class EObjectGroup {
	
	protected EArrayList<IEnhancedGuiObject> objects = new EArrayList();
	protected IEnhancedGuiObject groupParent;
	
	public EObjectGroup() {}
	public EObjectGroup(IEnhancedGuiObject parentIn) {
		objects.add(parentIn);
		groupParent = parentIn;
	}
	
	public EObjectGroup addObject(IEnhancedGuiObject... objectIn) {
		objects.addAll(objectIn);
		return this;
	}
	
	public EObjectGroup removeObject(IEnhancedGuiObject... objectIn) {
		Iterator<IEnhancedGuiObject> it = objects.iterator();
		while (it.hasNext()) {
			for (IEnhancedGuiObject o : objectIn) {
				if (o.equals(it.next())) { it.remove(); }
			}
		}
		return this;
	}
	
	public EArrayList<IEnhancedGuiObject> getObjects() { return objects; }
	public EObjectGroup setGroupParent(IEnhancedGuiObject parentIn) { groupParent = parentIn; return this; }
	public IEnhancedGuiObject getGroupParent() { return groupParent; }
}
