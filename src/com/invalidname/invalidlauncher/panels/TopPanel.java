package com.invalidname.invalidlauncher.panels;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;

import com.invalidname.invalidlauncher.LauncherGui;
import com.invalidname.invalidlauncher.tabs.DZNewsTab;

public class TopPanel extends Panel {
	
	public static final byte TAB_PANEL_INDEX = 1;
	//Tab buttons(for this)
	Button dzNews, launcherNews, console, profiles, getMods;
	
	public TopPanel(float x, float y, float width, float height) {
		super(x, y, width, height);
		//this.setSize(width, height);
		this.setFocusable(false);
		dzNews = new Button("DangerZone News", 0, 0, width/5, this.getSize().y());
		dzNews.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if(event.getAction().equals(MouseClickAction.CLICK)) {
				LauncherGui.tabPanel.setCurrentTab(LauncherGui.dzNewsTab);
			}
		});
		
		launcherNews = new Button("Launcher News", width*1/5, 0, width/5, this.getSize().y());
		launcherNews.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if(event.getAction().equals(MouseClickAction.CLICK)) {
				LauncherGui.tabPanel.setCurrentTab(LauncherGui.launcherNewsTab);
			}
		});
		
		console = new Button("Console", width*2/5, 0, width/5, this.getSize().y());
		console.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if(event.getAction().equals(MouseClickAction.CLICK)) {
				LauncherGui.tabPanel.setCurrentTab(LauncherGui.consoleTab);
			}
		});
		
		profiles = new Button("Profiles", width*3/5, 0, width/5, this.getSize().y());
		profiles.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if(event.getAction().equals(MouseClickAction.CLICK)) {
				LauncherGui.tabPanel.setCurrentTab(LauncherGui.profilesTab);
			}
		});
		
		getMods = new Button("Get mods! (external link)", width*4/5, 0, width/5, this.getSize().y());
		getMods.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if(event.getAction().equals(MouseClickAction.CLICK))
				if(Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
					try {
						Desktop.getDesktop().browse( new URI("http://dzarchive.jtrent238.tk/mods.html"));
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		});
		
		this.add(dzNews);
		this.add(launcherNews);
		this.add(console);
		this.add(profiles);
		this.add(getMods);
	}

}
