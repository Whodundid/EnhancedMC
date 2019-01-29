package com.Whodundid.enhancedChat.externalChatWindows;

import com.Whodundid.enhancedChat.ChatOrganizer;
import com.Whodundid.enhancedChat.chatUtil.ChatType;
import com.Whodundid.enhancedChat.chatUtil.ChatUtil;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.global.SettingsGui;
import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;

//Last edited: Oct 26, 2018
//First Added: Oct 26, 2018
//Author: Hunter Bragg

public class ChatWindowFrame extends JFrame {
	
	protected static Minecraft mc;
	private JFrame instance;
	protected ChatType type;
	private JTextPane chatHistory;
	private JTextField inputField;
	private JScrollPane scrollBar;
	private Style style;
	public ArrayList<String> entryHistory;
	protected int historyLine = 0;
	protected int lastUsed = 2;
	
	public ChatWindowFrame(ChatType typeIn) {
		mc = MainMod.getMC();
		chatHistory = new JTextPane();
		inputField = new JTextField();
		scrollBar = new JScrollPane();
		entryHistory = new ArrayList();
		type = typeIn;
		init();
		mc.setIngameFocus();
	}
	
	private void init() {
		try {
			instance = this;
			instance.getContentPane().setBackground(SystemColor.inactiveCaption);
			instance.setLayout(null);
			instance.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			instance.setTitle(type.getChatType() + " Chat");
			instance.setBounds(100, 100, 800, 600);
			instance.setVisible(true);
			
			instance.addWindowListener(new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) { ChatWindows.closeChatWindow(type); }
			});
			
			inputField.setBackground(SystemColor.menu);
			inputField.setBounds(0, instance.getHeight() - 27 - 39, instance.getWidth() - 15, 27);
			instance.getContentPane().add(inputField);
			
			chatHistory.setEditable(false);
			chatHistory.setBackground(SystemColor.windowText);
			style = chatHistory.addStyle("Console Text", null);
			
			scrollBar.setViewportView(chatHistory);
			scrollBar.setBounds(0, 0, instance.getWidth() - 15, instance.getHeight() - inputField.getHeight() - 39);
			instance.getContentPane().add(scrollBar);
			
			inputField.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == 192) { }
					if (e.getKeyCode() == 10) {
						String text = inputField.getText();
						entryHistory.add(text);
						historyLine = 0;
						lastUsed = 2;
						println(text, Color.WHITE);
						if (!text.isEmpty()) { sendMessageMC(); }
						inputField.setText("");
					} else if (e.getKeyCode() == 38) {
						if (historyLine < entryHistory.size()) {
							if (lastUsed == 0) {
								historyLine += 1;
								inputField.setText(getHistoryLine(historyLine));
								historyLine += 1;
							} else {
								inputField.setText(getHistoryLine(historyLine));
								historyLine += 1;
							}
						}
						lastUsed = 1;
					} else if (e.getKeyCode() == 40) {
						if (historyLine <= entryHistory.size() && historyLine > 0) {
							if (historyLine == entryHistory.size() || lastUsed == 1) { historyLine -= 2; }
							else { historyLine -= 1; }
							inputField.setText(getHistoryLine(historyLine));
							lastUsed = 0;
						} else if (historyLine == 0) { 
							inputField.setText("");
							lastUsed = 2;
						}
					} else if (inputField.getText().length() > 256) {
						String text = inputField.getText();
						inputField.setText(text.substring(0, 256));
					}
				}
				@Override public void keyReleased(KeyEvent e) {}
				@Override public void keyTyped(KeyEvent e) {}
			});
			
			StyledDocument area = chatHistory.getStyledDocument();
			/*
			try {
				ArrayList<String> currentList = ChatOrganizer.getChat(type);
				for (String s : currentList) {
					StyleConstants.setForeground(style, Color.WHITE);
					area.insertString(area.getLength(), s + "\n", style);
				}
			} catch (BadLocationException e) { e.printStackTrace(); }
			*/
			scrollBar.getVerticalScrollBar().setValue(scrollBar.getVerticalScrollBar().getVisibleAmount());
		} catch (Exception e) { e.printStackTrace(); ChatWindows.closeChatWindow(type); }
	}
	
	private String getHistoryLine(int lineNum) {
		if (!entryHistory.isEmpty()) {
			if (lineNum == 0) { return entryHistory.get(entryHistory.size() - 1); }
			else if (lineNum > 0) { return entryHistory.get(entryHistory.size() - (lineNum + 1)); }
		}
		return null;
	}
	
	public ChatWindowFrame addToChatList(String entry) {
		println(entry);
		return this;
	}
	
	public void println() { println("", Color.BLACK); }	
	public void println(String text) { println(text, Color.WHITE); }
	
	public void println(String text, Color color) {
		StyleConstants.setForeground(style, color);
		StyledDocument area = chatHistory.getStyledDocument();
		try {
			area.insertString(area.getLength(), text + "\n", style);
			if (scrollBar != null && scrollBar.getVerticalScrollBar() != null) { scrollBar.getVerticalScrollBar().setValue(scrollBar.getVerticalScrollBar().getMaximum() + 1); }
		} catch (BadLocationException e) { e.printStackTrace(); }
	}
	
	public void print(String text, Color color) {
		StyleConstants.setForeground(style, color);
		StyledDocument area = chatHistory.getStyledDocument();
		try {
			area.insertString(area.getLength(), text, style);
			if (scrollBar != null && scrollBar.getVerticalScrollBar() != null) { scrollBar.getVerticalScrollBar().setValue(scrollBar.getVerticalScrollBar().getMaximum() + 1); }
		} catch (BadLocationException e) { e.printStackTrace(); }
	}
	
	public JFrame getInstance() { return instance; }
	public ChatType getChatType() { return type; }
	protected void sendMessageMC() { ChatUtil.sendLongerChatMessage(inputField.getText()); }
	public void killInstance() { instance.dispose(); }
}
