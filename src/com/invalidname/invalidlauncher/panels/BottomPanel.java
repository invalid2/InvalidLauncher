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
import org.liquidengine.legui.component.RadioButton;
import org.liquidengine.legui.component.RadioButtonGroup;
import org.liquidengine.legui.component.SelectBox;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.component.event.checkbox.CheckBoxChangeValueEvent;
import org.liquidengine.legui.component.event.checkbox.CheckBoxChangeValueEventListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import com.invalidname.invalidlauncher.Launcher;
import com.invalidname.invalidlauncher.LauncherGui;

public class BottomPanel extends Panel {
	
	//Releases directory constant
	public static final String RELEASES_DIR = "releases/";
	
	//Contents
	Button settings, sendLogin, playButton;
	CheckBox userRemember;
	SelectBox<Object> availableProfiles;
	Label nicknameLabel, passwordLabel;
	TextInput nicknameInput;
	PasswordInput passwordInput;
	static RadioButton singlePlayerButton;
	static RadioButton clientButton;
	static RadioButton serverButton;
	
	static RadioButtonGroup launchType;
	
	Thread launchThread;
	
	public BottomPanel(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.setFocusable(false);
		
		Layer layer = new Layer();
		layer.setSize(width, height);
		
		settings = new Button("S", height/4, height/4, height/2, height/2);
		sendLogin = new Button("->", width-height*3/4-height/8, height/8, height*3/4, height*3/4);
		sendLogin.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			
		});
		
		nicknameInput = new TextInput("Player", width*5/6, height/8, width/10, height/5);
		nicknameInput.getTextState().setText(LauncherGui.getJsonData().getString("nickname"));
		passwordInput = new PasswordInput("123", width*5/6, height/4+height/8, width/10, height/5);
		if(LauncherGui.getJsonData().get("password") != null)
			passwordInput.getTextState().setText(LauncherGui.getJsonData().getString("password"));
		
		nicknameLabel = new Label("Nickname:", width*5/6-nicknameInput.getSize().x()/2, height/9, width/10, height/4);
		passwordLabel = new Label("Password:", width*5/6-nicknameInput.getSize().x()/2, height/4+height/9, width/10, height/4);
		
		userRemember = new CheckBox("Remember User", width*5/6-width/18, height-height/4, width, height);
		userRemember.addCheckBoxChangeValueListener((CheckBoxChangeValueEventListener) event -> {
			LauncherGui.getJsonData().put("remember", event.getNewValue());
		});
		
		availableProfiles = new SelectBox<>(height/4+height/2+height/4, height/3, width/6, height/3);
		//availableProfiles.setButtonWidth(width);
		//availableProfiles.setVisibleCount(5);
		
		for(String name : LauncherGui.getJsonData().getNames(LauncherGui.getJsonData().getJSONObject("profiles"))) {
			availableProfiles.addElement(name);
		}
		availableProfiles.getExpandButton().setSize(height/3, height/3);
		availableProfiles.getSelectionButton().setSize(width/6, height/3);
		availableProfiles.setFocusable(false);
		//availableProfiles.setElementHeight(height/3);
		
		launchType = new RadioButtonGroup();
		
		singlePlayerButton = new RadioButton("Singleplayer", width/4, height/5, width/6, height/4);
		clientButton = new RadioButton("Client", width/4, height*2/5, width/6, height/4);
		serverButton = new RadioButton("Server", width/4, height*3/5, width/6, height/4);
		
		singlePlayerButton.setChecked(true);
		clientButton.setChecked(false);
		serverButton.setChecked(false);
		
		singlePlayerButton.setRadioButtonGroup(launchType);
		clientButton.setRadioButtonGroup(launchType);
		serverButton.setRadioButtonGroup(launchType);
		
		playButton = new Button("Play!", width/2-width/10, height/8, width/5, height*3/4);
		playButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			//System.out.println(LauncherGui.isLaunching);
			
			if(LauncherGui.isLaunching)
				return;
			
			LauncherGui.isLaunching = true;
			
			new Thread() {
				
				@Override
				public void run() {
					String profile = "";
					Iterator<String> keys = LauncherGui.getJsonData().getJSONObject("profiles").keys();
					
					while(keys.hasNext()) {
						if(keys.next().equals(availableProfiles.getSelection())) {
							profile = availableProfiles.getSelection().toString();
							break;
						}
					}
					
					System.out.println(String.format("[LAUNCHER] Launching profile \"%s\"", profile));
					
					if( new File("./"+RELEASES_DIR+availableProfiles.getSelection().toString()).exists()) {
						if(launchType.isSelected(singlePlayerButton))
							Launcher.Launch(profile, Launcher.DZ_SP_ARGS);
						
						if(launchType.isSelected(clientButton))
							Launcher.Launch(profile, Launcher.DZ_CLIENT_ARGS);
						
						if(launchType.isSelected(serverButton))
							Launcher.Launch(profile, Launcher.DZ_SERVER_ARGS);
					} else {
						for(Object version : LauncherGui.getJsonData().getJSONArray("versions")) {
							if(version.equals(profile)) {
								Launcher.LaunchNoDownload(version.toString(), profile);
								continue;
							}
						}
						//System.out.println(profile);
						Launcher.LaunchDownload( LauncherGui.getJsonData().getJSONObject("profiles").get(profile).toString(), profile);
					}
				}
			}.start();
			//System.out.println(profile);
			
		});
		
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
		
		this.add(singlePlayerButton);
		this.add(clientButton);
		this.add(serverButton);
	}

	public static RadioButton getSinglePlayerButton() {
		return singlePlayerButton;
	}

	public static RadioButton getClientButton() {
		return clientButton;
	}

	public static RadioButton getServerButton() {
		return serverButton;
	}

	public static RadioButtonGroup getLaunchType() {
		return launchType;
	}

}
