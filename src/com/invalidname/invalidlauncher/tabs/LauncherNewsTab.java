package com.invalidname.invalidlauncher.tabs;

import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.TextArea;

public class LauncherNewsTab extends Panel {
	
	TextArea testContent;
	
	public LauncherNewsTab(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setFocusable(false);
		
		testContent = new TextArea( x, y, width, height);
		//consoleContent.getTextState().setTextWidth(width);
		testContent.setEditable(false);
		testContent.setFocusable(false);
		//testContent.getHorizontalScrollBar().setSize(width, 32);
		testContent.getTextState().setText("{\r\n"
				+ "	\"nickname\":\"Player\",\r\n"
				+ "	\"password\":\"\",\r\n"
				+ "	\"remember\":false,\r\n"
				+ "	\"profiles\":{\r\n"
				+ "		\"latest\":\"2.1\"\r\n"
				+ "	},\r\n"
				+ "	\"versions\":null,\r\n"
				+ "	\"musicLocal\":false\r\n"
				+ "}");
		//System.setOut(ps);
		//System.setErr(ps);
		
		//consoleContent.getTextState().setText(ps.toString());
		this.add(testContent);
	}

}
