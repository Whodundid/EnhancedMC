package com.Whodundid.nameHistory;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import com.Whodundid.main.global.subMod.SubMod;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.main.util.playerUtil.PlayerFacing;

public class NameHistory extends SubMod {

	private ArrayList<String> preSorted = new ArrayList<String>();
    private ArrayList<String> pastNames = new ArrayList<String>();
    private ArrayList<String> changeDates = new ArrayList<String>();
    private int startPos = 0;
    private Minecraft mc = Minecraft.getMinecraft();
    public String uuid = null;
    private boolean isAlexSkin = false;
    
    public NameHistory() {
    	super(SubModType.NAMEHISTORY);
    }
    
    public void startFetchingHistory(String playerName, boolean isFacing, boolean getSkin) {
    	if (isEnabled()) {
    		System.out.println("grabbed: " + playerName);
        	Thread fetcher = new Thread() {
            	@Override
            	public void run() {
            		try {
            			if (isFacing) { fetchHistory(PlayerFacing.getFacingPlayerName(), getSkin); }
            			else { fetchHistory(playerName, getSkin); }
                		displayPlayerNameHistory(playerName);
            		} catch (Exception e) { e.printStackTrace(); }
            	}
        	};
        	fetcher.start();
    	}
    }
    
    private void displayPlayerNameHistory(String originalName) {
    	if (!pastNames.isEmpty() && !changeDates.isEmpty()) {
    		if (pastNames.size() > 1) {
                mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "" + originalName + "'s " + EnumChatFormatting.GREEN + "previous name history:").build());
                mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.AQUA + "---------------------------------------------").build());
                for (int i = 0; i < pastNames.size(); i++) {
                	mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "" + pastNames.get(i) + " : " + EnumChatFormatting.GREEN + changeDates.get(i)).build());
                }
                mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.AQUA + "---------------------------------------------").build());
        	} else {
        		mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "" + pastNames.get(pastNames.size() - 1) + EnumChatFormatting.GREEN + " has not changed their name.").build());
        	}
    	} else {
    		mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GREEN + "Username: " + EnumChatFormatting.GOLD + originalName + EnumChatFormatting.GREEN + " does not exist!").build());
    	}    	
    }

    private void fetchHistory(String playerName, boolean getSkin) {
    	if (playerName.equals(null)) { return; }
        changeDates.clear();
        pastNames.clear();
        preSorted.clear();
        startPos = 0;
        
        try {
        	URL uuidFetcher = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStreamReader uuidReader = new InputStreamReader(uuidFetcher.openStream());
            BufferedReader getUuid = new BufferedReader(uuidReader);
            String testUUID = getUuid.readLine();
            if (testUUID != null) { uuid = testUUID.substring(7, 39); }
            getUuid.close();
            if (uuid != null) {
                URL nameFetcher = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");
                InputStreamReader nameReader = new InputStreamReader(nameFetcher.openStream());
                BufferedReader getName = new BufferedReader(nameReader);
                String names = getName.readLine();
                getName.close();
                names = names.replace("[", "").replace("{", "").replace("}", "").replace("\"", "");
                for (int i = 0; i < names.length(); i++) {
                    if (names.charAt(i) == ':') startPos = i + 1;
                    if (names.charAt(i) == ']' || names.charAt(i) == ',') {
                    	pastNames.add(names.substring(startPos, i));
                    	changeDates.add("Original");
                        startPos = i;
                        break;
                    }
                }
                if (names.charAt(startPos) == ']') {
                	return;
                }
                while (names.charAt(startPos) != ']') {
                    names = names.substring(startPos + 1);
                    preSorted.add(splitValues(names));
                }
                while (!preSorted.isEmpty()) {
                    Date changeDate = new Date(Long.valueOf(preSorted.get(1)));
                    pastNames.add(preSorted.get(0));
                    changeDates.add(changeDate.toString());
                    preSorted.remove(0); preSorted.remove(0);
                }
                
                if (getSkin) {
                	URL skinFetcher = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
    				InputStreamReader skinReader = new InputStreamReader(skinFetcher.openStream());
    				BufferedReader reader = new BufferedReader(skinReader);
    				String skin = reader.readLine();

    				skin = skin.replace("[", "").replace("{", "").replace("}", "").replace("\"", "");
    				String[] parts = skin.split(":");
    				
    				byte[] skinBase64 = Base64.decodeBase64(parts[5]);
    				String nextPart = new String(skinBase64);
    				nextPart = nextPart.replace("[", "").replace("{", "").replace("}", "").replace("\"", "");
    				String[] nextParts = nextPart.split(":");
    				
    				String link = "http:";
    				if (nextParts.length > 8) {
    					isAlexSkin = true;
    					link += nextParts[9];
    				} else {
    					isAlexSkin = false;
    					link += nextParts[7];
    				}
    				
    				URL skinImgFetcher = new URL(link);
    				InputStream in = new BufferedInputStream(skinImgFetcher.openStream());
    				ByteArrayOutputStream out = new ByteArrayOutputStream();
    				byte[] buf = new byte[1024];
    				int n = 0;
    				while (-1!=(n=in.read(buf)))
    				{
    				   out.write(buf, 0, n);
    				}
    				out.close();
    				in.close();
    				byte[] response = out.toByteArray();
    				
    				FileOutputStream fos = new FileOutputStream("img.png");
    				fos.write(response);
    				fos.close();
                }
            }
        } catch (IOException e) {
        	 mc.thePlayer.addChatMessage(ChatBuilder.of("").build());
        	 mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + playerName + " " + EnumChatFormatting.GREEN + "is availible.").build());
        	 mc.thePlayer.addChatMessage(ChatBuilder.of("").build());
        }
		return;
    }

    private String splitValues(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ':') startPos = i + 1;
            if (input.charAt(i) == ']' || input.charAt(i) == ',') {
                String value = input.substring(startPos, i);
                startPos = i;                
                return value;
            }
        } return null;
    }
}