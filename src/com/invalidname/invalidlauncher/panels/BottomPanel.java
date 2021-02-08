package com.invalidname.invalidlauncher.panels;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.BreakIterator;
import java.util.Iterator;

import org.json.JSONObject;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.CheckBox;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.Layer;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.PasswordInput;
import org.liquidengine.legui.component.SelectBox;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.component.event.checkbox.CheckBoxChangeValueEvent;
import org.liquidengine.legui.component.event.checkbox.CheckBoxChangeValueEventListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import com.invalidname.invalidlauncher.Launcher;
import com.invalidname.invalidlauncher.LauncherGui;

public class BottomPanel extends Panel {
	
	//Contents
	Button settings, sendLogin, playButton;
	CheckBox userRemember;
	SelectBox<Object> availableProfiles;
	Label nicknameLabel, passwordLabel;
	TextInput nicknameInput;
	PasswordInput passwordInput;
	
	public BottomPanel(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setFocusable(false);
		
		Layer layer = new Layer();
		layer.setSize(width, height);
		
		settings = new Button("S", height/4, height/4, height/2, height/2);
		sendLogin = new Button("->", width-height*3/4-height/8, height/8, height*3/4, height*3/4);
		sendLogin.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			
		});
		playButton = new Button("Play!", width/2-width/10, height/8, width/5, height*3/4);
		playButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if(LauncherGui.isLaunching)
				return;
			
			LauncherGui.isLaunching = true;
			
			String profile = "";
			Iterator<String> keys = LauncherGui.data.getJSONObject("profiles").keys();
			
			while(keys.hasNext()) {
				if(keys.next().equals(availableProfiles.getSelection())) {
					profile = availableProfiles.getSelection().toString();
					break;
				}
			}
			//System.out.println(profile);
			if( new File("./"+availableProfiles.getSelection().toString()).exists()) {
				Launcher.Launch(profile);
			} else {
				for(Object version : LauncherGui.data.getJSONArray("versions")) {
					if(version.equals(profile)) {
						Launcher.LaunchNoDownload(version.toString(), profile);
						continue;
					}
				}
				//System.out.println(profile);
				Launcher.LaunchDownload( LauncherGui.data.getJSONObject("profiles").get(profile).toString(), profile);
			}
		});
		
		nicknameInput = new TextInput("Player", width*5/6, height/8, width/10, height/5);
		nicknameInput.getTextState().setText(LauncherGui.data.getString("nickname"));
		passwordInput = new PasswordInput("123", width*5/6, height/4+height/8, width/10, height/5);
		if(LauncherGui.data.get("password") != null)
			passwordInput.getTextState().setText(LauncherGui.data.getString("password"));
		
		nicknameLabel = new Label("Nickname:", width*5/6-nicknameInput.getSize().x()/2, height/9, width/10, height/4);
		passwordLabel = new Label("Password:", width*5/6-nicknameInput.getSize().x()/2, height/4+height/9, width/10, height/4);
		
		userRemember = new CheckBox("Remember User", width*5/6-width/18, height-height/4, width, height);
		userRemember.addCheckBoxChangeValueListener((CheckBoxChangeValueEventListener) event -> {
			LauncherGui.data.put("remember", event.getNewValue());
		});
		
		availableProfiles = new SelectBox<>(height/4+height/2+height/4, height/3, width/6, height/3);
		//availableProfiles.setButtonWidth(width);
		//availableProfiles.setVisibleCount(5);
		
		for(String name : LauncherGui.data.getNames(LauncherGui.data.getJSONObject("profiles"))) {
			availableProfiles.addElement(name);
		}
		availableProfiles.getExpandButton().setSize(height/3, height/3);
		availableProfiles.getSelectionButton().setSize(width/6, height/3);
		availableProfiles.setFocusable(false);
		//availableProfiles.setElementHeight(height/3);
		
		layer.add(nicknameLabel);
		layer.add(passwordLabel);
		
		this.add(layer);
		this.add(settings);
		this.add(sendLogin);
		this.add(playButton);
		this.add(nicknameInput);
		this.add(passwordInput);
		this.add(userRemember);
		this.add(availableProfiles);
	}

}
