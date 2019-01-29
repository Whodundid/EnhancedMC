package com.Whodundid.autoCorrect;

import java.io.File;
import java.io.PrintWriter;

import com.Whodundid.main.MainMod;

//Last edited: Oct 15, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class CommandSaver {
	
	static AutoCorrectManager man;
	
	public CommandSaver(AutoCorrectManager manIn) {
		man = manIn;
	}
	
	public void saveCommands() {
		try {
			File commandsFile = new File(MainMod.NAME + "/AutoCorrect");
			if (!commandsFile.exists()) { commandsFile.mkdirs(); }
			PrintWriter saver = new PrintWriter(MainMod.NAME + "/AutoCorrect/Commands.cfg", "UTF-8");
			
			saver.println("** AutoCorrect Commands");
			saver.println("** CommandName followed by aliases");
			saver.println();
			for (AutoCorrectCommand c : man.getCommandList()) {
				saver.println("COMMAND " + c.getBaseCommand());
				for (String alias : c.getAliases()) { saver.println("ALIAS " + alias); }
				saver.println("CMDEND\n");
			}
			saver.print("END");
			saver.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
