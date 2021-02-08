package com.invalidname.invalidlauncher.tabs;

import java.io.PrintStream;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextArea;

public class ConsoleTab extends Panel {
	
	public PrintStream ps;
	TextArea consoleContent;
	public ConsoleTab(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setFocusable(false);
		consoleContent = new TextArea( x, y, width, height);
		//consoleContent.getTextState().setTextWidth(width);
		consoleContent.setEditable(false);
		consoleContent.setFocusable(false);
		consoleContent.getHorizontalScrollBar().setSize(width, 32);
		consoleContent.getTextState().setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor \n incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		//System.setOut(ps);
		//System.setErr(ps);
		
		//consoleContent.getTextState().setText(ps.toString());
		this.add(consoleContent);
	}
}
