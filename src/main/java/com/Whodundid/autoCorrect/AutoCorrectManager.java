package com.Whodundid.autoCorrect;

import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.enhancedGui.EnhancedGui;
import com.Whodundid.main.util.storageUtil.StorageBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;

//Last edited: Oct 22, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class AutoCorrectManager extends SubMod {
	
	protected ArrayList<AutoCorrectCommand> commands;
	public BuiltInAutoCorrectCommands builtIn;
	public CommandSaver saver;
	public CommandLoader loader;
	
	public AutoCorrectManager() {
		super(SubModType.AUTOCORRECT);
		commands = new ArrayList();
		saver = new CommandSaver(this);
		loader = new CommandLoader(this);
		if (!loader.loadCommands()) {
			System.out.println("Could not load command list. Loading default autocorrect commands.");
			builtIn = new BuiltInAutoCorrectCommands(this);
			builtIn.registerCommands();
			saver.saveCommands();
			registerCommands();
		}
	}
	
	@Override
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) {
		if (oldGui != null) { return setPosition ? new AutoCorrectGui(pos.getObject(), pos.getValue(), oldGui) : new AutoCorrectGui(oldGui); }
		return setPosition ? new AutoCorrectGui(pos.getObject(), pos.getValue()) : new AutoCorrectGui();
	}
	
	public synchronized boolean addCommand(AutoCorrectCommand commandIn) {
		//System.out.println(commandIn.getBaseCommand());
		return !doesCommandExist(commandIn) ? commands.add(commandIn) : false;
	}
	
	public boolean doesCommandExist(AutoCorrectCommand commandIn) {
		for (AutoCorrectCommand c : commands) {
			if (c.getBaseCommand().equals(commandIn.getBaseCommand())) { return true; }
		}
		return false;
	}
	
	public synchronized AutoCorrectCommand removeCommand(String commandName) { return removeCommand(getCommand(commandName)); }
	
	public synchronized AutoCorrectCommand removeCommand(AutoCorrectCommand commandIn) {
		if (commandIn != null && doesCommandExist(commandIn)) {
			Iterator<AutoCorrectCommand> it = commands.iterator();
			while (it.hasNext()) {
				AutoCorrectCommand c = it.next();
				if (commandIn.getBaseCommand().equals(c.getBaseCommand())) {
					it.remove();
					return c;
				}
			}
		}
		return null;
	}
	
	public synchronized AutoCorrectCommand getCommand(String commandName) {
		for (AutoCorrectCommand c : commands) {
			if (c.getBaseCommand().equals(commandName)) { return c; }
		}
		return null;
	}
	
	public AutoCorrectManager registerCommands() {
		ClientCommandHandler h = ClientCommandHandler.instance;
		Map<String, ICommand> map = h.getCommands();
		synchronized (map) {
			for (AutoCorrectCommand c : commands) {
				for (String alias : c.getAliases()) {
					if (!map.containsKey(alias)) {
						map.put(alias, c.getCommandMC());
					} else { System.out.println("Alias: '" + alias + "' already exists!"); }
				}
			}
		}
		return this;
	}
	
	public AutoCorrectManager unloadCommandFromRegistry(AutoCorrectCommand commandIn) {
		if (doesCommandExist(commandIn)) {
			ClientCommandHandler h = ClientCommandHandler.instance;
			Map<String, ICommand> map = h.getCommands();
			synchronized (map) {
				for (String alias : commandIn.getAliases()) {
					Iterator<Entry<String, ICommand>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, ICommand> cmd = it.next();
						if (cmd.getKey().equals(alias) && cmd.getValue().equals(commandIn.getCommandMC())) { it.remove(); }
					}
				}
			}
		}
		return this;
	}
	
	public AutoCorrectManager unloadAllCommandsFromRegistry() {
		ClientCommandHandler h = ClientCommandHandler.instance;
		Map<String, ICommand> map = h.getCommands();
		synchronized (map) {
			for (AutoCorrectCommand c : commands) {
				for (String alias : c.getAliases()) {
					Iterator<Entry<String, ICommand>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, ICommand> cmd = it.next();
						if (cmd.getKey().equals(alias) && cmd.getValue().equals(c.getCommandMC())) {
							it.remove();
						}
					}
				}
			}
		}
		return this;
	}
	
	public List<AutoCorrectCommand> getCommandList() { return Collections.unmodifiableList(commands); }
	
	public class CommandAlreadyExistsException extends Exception {}
}
