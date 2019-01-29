package com.Whodundid.autoCorrect;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.Whodundid.main.MainMod;

//Last edited: Oct 15, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class CommandLoader {
	
	static AutoCorrectManager man;
	
	public CommandLoader(AutoCorrectManager manIn) {
		man = manIn;
	}
	
	public boolean loadCommands() {
		try {
			File commands = new File(MainMod.NAME + "/AutoCorrect/Commands.cfg");
			if (commands.exists()) {
				boolean isEnd = false;
				
				String commandName = "";
				ArrayList<String> aliases = new ArrayList();
				
				Scanner fileReader = new Scanner(commands);
				while (!isEnd) {
					String configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					if (line.hasNext()) {
						switch (line.next()) {
						case "COMMAND": commandName = line.next(); break;
						case "ALIAS": aliases.add(line.next()); break;
						case "CMDEND":
							man.addCommand(new AutoCorrectCommand(commandName).addAliases(aliases));
							commandName = "";
							aliases.clear();
							break;
						case "END": isEnd = true; break;
						default: break;
						}
						line.close();
					}
				}
				fileReader.close();
				return true;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
}
