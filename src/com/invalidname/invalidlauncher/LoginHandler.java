package com.invalidname.invalidlauncher;

import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Dialog;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

public class LoginHandler {
	
	private static Socket nameServerSocket;
	private static ObjectInputStream oIS;
	private static ObjectOutputStream oOS;
	private static BufferedInputStream bIS;
	private static BufferedOutputStream bOS;
	
	public static void sendLogin(String nickname, String password) {
		try {
			nameServerSocket = new Socket(Constants.NAMESERVER_ADDRESS, Constants.NAMESERVER_PORT);
			bOS = new BufferedOutputStream(nameServerSocket.getOutputStream());
			oOS = new ObjectOutputStream(bOS);
			oOS.writeInt(-1);
			oOS.flush();
			bIS = new BufferedInputStream(nameServerSocket.getInputStream());
			oIS = new ObjectInputStream(bIS);
			System.out.println("Say somethin pls");
		} catch (UnknownHostException e) {
			openStatusDialog("[LAUNCHER] [ERROR] UnknownHostException", "UnknownHostException");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			openStatusDialog("[LAUNCHER] [ERROR] IOException", "IOException");
			e.printStackTrace();
			return;
		}
		
		if(LauncherGui.getJsonData().getBoolean("registered")) {
			System.out.println("test");
			String cryptedPassword = generateCryptedPassword(nickname, password);
			if(cryptedPassword == null) {
				openStatusDialog("[LAUNCHER] [ERROR] Password encryption failure", "Nickname/Password must be longer than 3 characters");
				return;
			}
			try {
				oOS.writeInt(0);
				oOS.writeObject(LauncherGui.getJsonData().getString("nickname"));
				oOS.writeObject(cryptedPassword);
				oOS.flush();
				
				if(oIS.readInt() == 0) {
					openStatusDialog("[LAUNCHER] [ERROR] Invalid name/password", "Invalid name/password");
				} else {
					System.out.println("Successfull login");
					openStatusDialog("[LAUNCHER] Logged in!", "Successfully logged in!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				oOS.writeInt(2);
				oOS.writeObject(LauncherGui.getJsonData().getString("nickname"));
				oOS.writeObject(LauncherGui.getJsonData().getString("password"));
				oOS.flush();
				
				if(oIS.readInt() == 0) {
					openStatusDialog("[LAUNCHER] [ERROR] Invalid name/password", "Invalid name/password");
				} else {
					LauncherGui.getJsonData().put("registered", true);
					openStatusDialog("[LAUNCHER] Logged in!", "Successfully logged in!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			oIS.close();
			bIS.close();
			oOS.close();
			bOS.close();
			nameServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String generateCryptedPassword(String nickname, String password) {
		String encryptedPassword = "";
		
		final int nicknameLength = nickname.length();
		final int passwordLength = password.length();
		final String dzString = "DangerZone is the best!";
		final int dzStringLength = dzString.length();
		final String hashbackString = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final int hashbackStringLength = hashbackString.length();
		final int[] nicknameInt = new int[32];
		final int[] passwordInt = new int[32];
		final int[] dzStringInt = new int[32];
		final int[] cryptInt = new int[32];
		
		if(nicknameLength < 4 || passwordLength < 4)
			return null;
		
		byte[] stringBytes = nickname.toLowerCase().getBytes();
		for(int i = 0; i < 32; i++) {
			nicknameInt[i] = stringBytes[i % nicknameLength];
		}
		
		stringBytes = password.toLowerCase().getBytes();
		for(int i = 0; i < 32; i++) {
			passwordInt[i] = stringBytes[i % passwordLength];
		}
		
		stringBytes = dzString.getBytes();
		for(int i = 0; i < 32; i++) {
			dzStringInt[i] = stringBytes[i % dzStringLength];
		}
		
		for(int i = 0; i < 32; i++) {
			cryptInt[i] = nicknameInt[i] * passwordInt[31 - i];
		}
		
		for(int i = 0; i < 32; i++) {
			final int n = cryptInt[i];
			cryptInt[i] = cryptInt[cryptInt[i] % 32];
			cryptInt[cryptInt[i] % 32] = n;
		}
		
		for(int i = 0; i < 32; i++) {
			encryptedPassword = String.valueOf(encryptedPassword) + hashbackString.charAt(cryptInt[i] % hashbackStringLength);
		}
		
		return encryptedPassword;
	}
	
	public static void openStatusDialog(String title, String content) {
		Dialog statusDialog = new Dialog(title, 600, 200);
		Label text = new Label(content);
		Button closeButton = new Button("ok");
		closeButton.setPosition(statusDialog.getSize().x()/2-16, statusDialog.getSize().y()-16);
		closeButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if (event.getAction() == CLICK)
				statusDialog.close();
		});
		statusDialog.getContainer().add(text);
		statusDialog.getContainer().add(closeButton);
		
		statusDialog.show(InvalidLauncher.getWindow().getFrame());
	}
}
