package com.invalidname.invalidlauncher.tabs;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.liquidengine.legui.component.Panel;

public class DZNewsTab extends Panel {
	
	public DZNewsTab(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setFocusable(false);
		//System.out.println("Test");
		
		try {
			Element elem = Jsoup.connect("http://www.dangerzonegame.net/").get().getElementById("wsite-content");
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
