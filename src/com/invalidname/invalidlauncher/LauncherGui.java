package com.invalidname.invalidlauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Dialog;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.SelectBox;
import org.liquidengine.legui.component.event.selectbox.SelectBoxChangeSelectionEventListener;

import com.invalidname.invalidlauncher.panels.BottomPanel;
import com.invalidname.invalidlauncher.panels.TabPanel;
import com.invalidname.invalidlauncher.panels.TopPanel;
import com.invalidname.invalidlauncher.tabs.ConsoleTab;
import com.invalidname.invalidlauncher.tabs.DZNewsTab;
import com.invalidname.invalidlauncher.tabs.LauncherNewsTab;
import com.invalidname.invalidlauncher.tabs.ProfilesTab;

public class LauncherGui extends Panel {
	
	//Gui panels
	public static TopPanel topPanel;
	public static TabPanel tabPanel;
	public static BottomPanel bottomPanel;
	
	//Tabs for tabPanel
	public static DZNewsTab dzNewsTab;
	public static LauncherNewsTab launcherNewsTab;
	public static ConsoleTab consoleTab;
	public static ProfilesTab profilesTab;
	
	public static boolean isLaunching = false;
	
	public static JSONObject jsonData;
	public static final JSONObject JSON_DEFAULT = new JSONObject("{\r\n"
			+ "	\"nickname\":\"Player\",\r\n"
			+ "	\"password\":\"\",\r\n"
			+ "	\"registered\":false,\r\n"
			+ "	\"remember\":false,\r\n"
			+ "	\"profiles\":{\r\n"
			+ "		\"latest\":\"2.1\",\r\n"
			+ "		\"1.9.3\":\"1.9.3\",\r\n"
			+ "		\"1.9\":\"1.9\",\r\n"
			+ "		\"1.8b\":\"1.8b\",\r\n"
			+ "		\"1.8a\":\"1.8a\",\r\n"
			+ "		\"1.7\":\"1.7\",\r\n"
			+ "		\"1.7b\":\"1.7b\",\r\n"
			+ "		\"1.6\":\"1.6\",\r\n"
			+ "		\"1.0\":\"1.0\",\r\n"
			+ "		\"0.9\":\"0.9\",\r\n"
			+ "		\"0.8\":\"0.8\"\r\n"
			+ "	},\r\n"
			+ "	\"versions\":[],\r\n"
			+ "	\"musicUse\":false,\r\n"
			+ "	\"musicLocal\":false\r\n"
			+ "}");

	public LauncherGui(int width, int height) {
		this.setSize(width, height);
		FileInputStream fIS;
		FileWriter fW;
		try {
			fIS = new FileInputStream(new File("./data.json"));
			
			//System.out.println();
			
			jsonData = new JSONObject(new JSONTokener(fIS));
		}  catch (IOException e) {
			try {
				jsonData = JSON_DEFAULT;
				fW = new FileWriter(new File("./data.json"));
				
				fW.write(JSON_DEFAULT.toString());
				fW.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		
		
		//Set up the main panels
		topPanel = new TopPanel(0, 0, width, height/10);
		tabPanel = new TabPanel(0, height/10, width, height*4/5);
		bottomPanel = new BottomPanel(0, height*9/10, width, height/10);
		
		//Set up tabPanel tabs
		dzNewsTab = new DZNewsTab(0, 0, width, height*4/5);
		launcherNewsTab = new LauncherNewsTab(0, 0, width, height*4/5);
		consoleTab = new ConsoleTab(0, 0, width, height*4/5);
		profilesTab = new ProfilesTab(0, 0, width, height*4/5);
		
		//DefaultTab
		tabPanel.setCurrentTab(dzNewsTab);
		
		this.add(topPanel);
		this.add(tabPanel);
		this.add(bottomPanel);
	}
	
	public static void setCurrentTab(Panel tab) {
		tabPanel.setCurrentTab(tab);
	}
	
	public static void setCurrentTab(int index) {
		switch(index) {
			case 0:
				tabPanel.setCurrentTab(dzNewsTab);
				break;
			case 1:
				tabPanel.setCurrentTab(launcherNewsTab);
				break;
			case 2:
				tabPanel.setCurrentTab(consoleTab);
				break;
			case 3:
				tabPanel.setCurrentTab(profilesTab);
				break;
		}
		
	}
	
	public static boolean isLaunching() {
		return isLaunching;
	}

	public static void setLaunching(boolean isLaunching) {
		LauncherGui.isLaunching = isLaunching;
	}
	
	public static JSONObject getJsonData() {
		return jsonData;
	}

	public static void setJsonData(JSONObject jsonData) {
		LauncherGui.jsonData = jsonData;
	}

	public static JSONObject getJsonDefault() {
		return JSON_DEFAULT;
	}
}
