package com.invalidname.invalidlauncher.panels;

import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.BreakIterator;
import java.util.Iterator;

import org.joml.Vector2f;
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

import com.invalidname.invalidlauncher.Constants;
import com.invalidname.invalidlauncher.Launcher;
import com.invalidname.invalidlauncher.LauncherGui;
import com.invalidname.invalidlauncher.LoginHandler;

public class BottomPanel extends Panel {
	
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
			if (event.getAction() == CLICK)
				if(passwordInput.getTextState().getText().length() == 0) {
					LauncherGui.getJsonData().put(Constants.JSON_KEY_NICKNAME, nicknameInput.getTextState().getText());
				} else {
					LoginHandler.sendLogin(nicknameInput.getTextState().getText(), passwordInput.getTextState().getText());
				}
		});
		
		nicknameInput = new TextInput(LauncherGui.getJsonData().getString(Constants.JSON_KEY_NICKNAME), width*5/6, height/8, width/10, height/5);
		passwordInput = new PasswordInput("123", width*5/6, height/4+height/8, width/10, height/5);
		if(LauncherGui.getJsonData().get(Constants.JSON_KEY_PASSWORD) != null)
			passwordInput.getTextState().setText(LauncherGui.getJsonData().getString(Constants.JSON_KEY_PASSWORD));
		
		nicknameLabel = new Label("Nickname:", width*5/6-nicknameInput.getSize().x()/2, height/9, width/10, height/4);
		passwordLabel = new Label("Password:", width*5/6-nicknameInput.getSize().x()/2, height/4+height/9, width/10, height/4);
		
		userRemember = new CheckBox("Remember User", width*5/6-nicknameInput.getSize().x()/3, height*2/3, width/6, height/3);
		userRemember.setChecked(LauncherGui.getJsonData().getBoolean(Constants.JSON_KEY_REMEMBER));
		userRemember.addCheckBoxChangeValueListener((CheckBoxChangeValueEventListener) event -> {
			LauncherGui.getJsonData().put(Constants.JSON_KEY_REMEMBER, event.getNewValue());
		});
		
		availableProfiles = new SelectBox<>(height/4+height/2+height/4, height/3, width/6, height/3);
		availableProfiles.setButtonWidth(width);
		availableProfiles.setVisibleCount(5);
		availableProfiles.setFocusable(false);
		availableProfiles.setElementHeight(height/3);
		
		for(String name : LauncherGui.getJsonData().getNames(LauncherGui.getJsonData().getJSONObject(Constants.JSON_KEY_PROFILES))) {
			availableProfiles.addElement(name);
		}
		availableProfiles.setSelected("latest", true);
		
		launchType = new RadioButtonGroup();
		
		singlePlayerButton = new RadioButton("Singleplayer", width/4, height/5, width/6, height/4);
		clientButton = new RadioButton("Client", width/4, height*2/5, width/6, height/4);
		serverButton = new RadioButton("Server", width/4, height*3/5, width/6, height/4);
		
		singlePlayerButton.setRadioButtonGroup(launchType);
		clientButton.setRadioButtonGroup(launchType);
		serverButton.setRadioButtonGroup(launchType);
		
		singlePlayerButton.setChecked(true);
		clientButton.setChecked(false);
		serverButton.setChecked(false);
		
		playButton = new Button("Play!", width/2-width/10, height/8, width/5, height*3/4);
		playButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			//System.out.println(LauncherGui.isLaunching);
			
			if(LauncherGui.isLaunching)
				return;
			
			LauncherGui.setLaunching(true);
			
			new Thread() {
				
				@Override
				public void run() {
					String profile = "";
					Iterator<String> keys = LauncherGui.getJsonData().getJSONObject(Constants.JSON_KEY_PROFILES).keys();
					
					while(keys.hasNext()) {
						if(keys.next().equals(availableProfiles.getSelection())) {
							profile = availableProfiles.getSelection().toString();
							break;
						}
					}
					
					System.out.println(String.format("[LAUNCHER] Launching profile \"%s\"", profile));
					
					if( new File("./"+Constants.PROFILES_DIR+profile).exists()) {
						if(launchType.isSelected(singlePlayerButton))
							Launcher.Launch(profile, Constants.DZ_SP_ARGS);
						
						if(launchType.isSelected(clientButton))
							Launcher.Launch(profile, Constants.DZ_CLIENT_ARGS);
						
						if(launchType.isSelected(serverButton))
							Launcher.Launch(profile, Constants.DZ_SERVER_ARGS);
					} else {
						for(Object version : LauncherGui.getJsonData().getJSONArray(Constants.JSON_KEY_VERSIONS)) {
							if(version.equals(profile)) {
								Launcher.LaunchNoDownload(version.toString(), profile);
								continue;
							}
						}
						//System.out.println(profile);
						Launcher.LaunchDownload( LauncherGui.getJsonData().getJSONObject(Constants.JSON_KEY_PROFILES).get(profile).toString(), profile);
					}
				}
			}.start();
			//System.out.println(profile);
			
		});
		
		this.add(nicknameLabel);
		this.add(passwordLabel);
		
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
