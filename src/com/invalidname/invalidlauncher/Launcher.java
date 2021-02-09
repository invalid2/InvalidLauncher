package com.invalidname.invalidlauncher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.management.modelmbean.ModelMBeanInfoSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.io.Files;
import com.invalidname.invalidlauncher.panels.BottomPanel;

import javassist.bytecode.stackmap.TypeData.UninitThis;

public class Launcher {
	
	//DZ Version Constants
	public static final String RELEASES_DIR = "releases/";
	public static final String DOMAIN_ADRESS = "http://invalidlauncher.invalid2.tk/";
	
	//Enviroment arguments constants
	public static final String[] DZ_SP_ARGS = {"java","-jar", "GameRunner.jar", "singleplayer", "-singleplayer", "--singleplayer"};
	public static final String[] DZ_CLIENT_ARGS = {"java","-jar", "GameRunner.jar", "client", "-client", "--client"};
	public static final String[] DZ_SERVER_ARGS = {"java","-jar", "GameRunner.jar", "server", "-server", "--server"};
	
	/**
	 * Launchs DangerZone, assumes everything required for launch has already been done
	 * @param profile name of the profile selected for launch
	 */
	public static void Launch( String profile, String[] args) {
		
		ProcessBuilder ps = new ProcessBuilder(args);
		File log = new File("./launch.log");
		ps.redirectOutput(log);
		ps.redirectError(log);
		ps.directory(new File(String.format("%s/%s/%s", System.getProperty("user.dir"), RELEASES_DIR, profile)));
		try {
			ps.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Wait a bit so we dont launch the game twice
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			LauncherGui.setLaunching(false);
			
			Thread.currentThread().join();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launchs DangerZone, but first copies content from another profile with the 
	 * same version, this is done to avoid having to download files from the internet
	 * @param version the DangerZone version that will be launched
	 * @param profileName The name of the profile selected for launch
	 */
	public static void LaunchNoDownload(String version, String profileName) {
		
		//Get all the profiles
		JSONObject profilesObject = LauncherGui.getJsonData().getJSONObject("profiles");
		
		//Create array from available profiles
		String[] profiles = profilesObject.getNames(LauncherGui.getJsonData().getJSONObject("profiles"));
		
		//Profile that we will be copying the content from
		String selectedProfile = null;
		
		for(int i = 0; 0 < profiles.length; i++) {
			if(profilesObject.getString(profiles[i]) == version) {
				selectedProfile = profiles[i];
				break;
			}
		}
		
		try {
			//Copy necessary files if they already exist locally
			Files.copy(new File("./"+RELEASES_DIR+selectedProfile+"/DangerZone_lib"), new File("./"+RELEASES_DIR+profileName+"/DangerZone_lib"));
			System.out.println(String.format("[LAUNCHER] Copied DangerZone_lib from %s to %s succesfully", selectedProfile, profileName));
			
			Files.copy(new File("./"+RELEASES_DIR+selectedProfile+"/GameRunner_lib"), new File("./"+RELEASES_DIR+profileName+"/GameRunner_lib"));
			
			
			Files.copy(new File("./"+RELEASES_DIR+selectedProfile+"/Launcher_lib"), new File("./"+RELEASES_DIR+profileName+"/Launcher_lib"));
			
			
			Files.copy(new File("./"+RELEASES_DIR+selectedProfile+"/DangerZone.jar"), new File("./"+RELEASES_DIR+profileName+"/DangerZone.jar"));
			
			
			Files.copy(new File("./Player.png"), new File("./"+RELEASES_DIR+profileName+"/Player.png"));
			
			
			Files.copy(new File("./music"), new File("./"+RELEASES_DIR+selectedProfile+"/music"));
			
			//Generate default properties file
			Properties prop = new Properties();
			
			prop.setProperty("CraftingAnimation", "true");
			prop.setProperty("FieldOfView", "40");
			prop.setProperty("WorldName", "New World");
			prop.setProperty("RenderDistance", "24");
			prop.setProperty("ServerPort", "true");
			prop.setProperty("CraftingAnimation", "18668");
			prop.setProperty("CaveGeneration", "true");
			prop.setProperty("SingleMaxG", "2");
			prop.setProperty("ClientMaxG", "2");
			prop.setProperty("FastLighting", "true");
			prop.setProperty("Registered", LauncherGui.getJsonData().get("password") != null? "true" : "false");
			prop.setProperty("ScreenWidth", "1920");
			prop.setProperty("MaxGraphics", "true");
			prop.setProperty("ServerAddress", "127.0.0.1");
			prop.setProperty("NameServerAddress", "69.140.167.10");
			prop.setProperty("MusicVolume", "3");
			prop.setProperty("CryptedPassword", LauncherGui.getJsonData().get("password") != null? LauncherGui.getJsonData().getString("password") : null);
			prop.setProperty("Playername", LauncherGui.getJsonData().getString("nickname"));
			prop.setProperty("ScreenHeight", "1080");
			prop.setProperty("MovePart", "true");
			prop.setProperty("MouseSensitivity", "0");
			prop.setProperty("ShowRain", "true");
			prop.setProperty("ShowHitBox", "true");
			prop.setProperty("Volume", "8");
			prop.setProperty("NameServerPort", "18669");
			prop.setProperty("ShowClouds", "true");
			prop.setProperty("CharacterFeel", "0");
			prop.setProperty("AltTextureDir", null);
			prop.setProperty("MinDrawLevel", "0");
			prop.setProperty("RunFinalization", "false");
			prop.setProperty("FogEnable", "false");
			prop.setProperty("FullScreen", "true");
			prop.setProperty("ServerMaxG", "2");
			prop.setProperty("PlayerDied", "false");
			
			prop.store(new FileOutputStream( new File("./"+RELEASES_DIR+selectedProfile+"/DangerZone.properties")), null);
			
			if(BottomPanel.getLaunchType().isSelected(BottomPanel.getSinglePlayerButton()))
				Launch(profileName, DZ_SP_ARGS);
			
			if(BottomPanel.getLaunchType().isSelected(BottomPanel.getClientButton()))
				Launch(profileName, DZ_CLIENT_ARGS);
			
			if(BottomPanel.getLaunchType().isSelected(BottomPanel.getServerButton()))
				Launch(profileName, DZ_SERVER_ARGS);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Launchs DangerZone, but first grabs all the necessary files
	 * to do so from a remote server, specifically the contents of
	 * {@link https://github.com/invalid2/invalidlauncher.invalid2.tk} 
	 * @param version the DangerZone version that will be launched
	 * @param profileName The name of the profile selected for launch
	 */
	public static void LaunchDownload(String version, String profileName) {
		try {
			//Get the resource from remote
			BufferedInputStream bIS = new BufferedInputStream( new URL(DOMAIN_ADRESS+version+".zip").openStream());
			
			//Create the directory the file will be put in
			File dir = new File("./"+RELEASES_DIR+profileName);
			dir.mkdirs();
			
			FileOutputStream fOS = new FileOutputStream("./"+RELEASES_DIR+profileName+"/"+version+".zip");
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(DOMAIN_ADRESS+version+".zip").openConnection();
			
			//System.out.println(httpConnection.getContentLengthLong());
			long contentSize = httpConnection.getContentLengthLong();
			byte[] data = new byte[1024];
			int dataLength;
			
			long taskPercent = 0L;
			//Write the data to pc
			while( (dataLength = bIS.read(data, 0, 1024)) != -1) {
				fOS.write(data, 0, dataLength);
				//System.out.println(taskPercent * 1.0 / contentSize);
				System.out.println(String.format("[LAUNCHER] Downloading file %s.zip from remote: %d%%", version , taskPercent*100/contentSize));
				taskPercent += dataLength;
			}
			
			//Close the buffered input stream and the file output stream
			bIS.close();
			fOS.close();
			
			//Now unzip it!
			ZipInputStream zIS = new ZipInputStream( new FileInputStream( new File(String.format("./%s%s/%s.zip", RELEASES_DIR, profileName, version))));
			ZipEntry zE = zIS.getNextEntry();
			File destination = new File(String.format("./%s%s", RELEASES_DIR, profileName));
			data = new byte[1024];
			while(zE != null) {
				File current = new File(destination, zE.getName().substring(zE.getName().indexOf("/")));
				
				System.out.println("[LAUNCHER] Extracting file: "+zE.getName());
				
				if(!current.getCanonicalPath().startsWith(destination.getCanonicalPath()+File.separator))
					throw new IOException("ZipEntry outside of target directory:"+zE.getName());
				
				if(zE.isDirectory()) {
					if(!current.isDirectory() && !current.mkdirs())
						throw new IOException("Unable to create directory "+current);
				} else {
					//Check if parent dir exists, because windows
					File parent = current.getParentFile();
					if(!parent.isDirectory() && !parent.mkdirs())
						throw new  IOException("Unable to create directory "+parent);
					
					//Write content to File Output Stream
					FileOutputStream zipfOS = new FileOutputStream(current);
					while((dataLength = zIS.read(data, 0, 1024)) != -1) {
						zipfOS.write(data, 0, dataLength);
					}
					
					zipfOS.close();
				}
				//Get next entry
				zE = zIS.getNextEntry();
			}
			
			//Close the zip input stream and zip entry
			zIS.closeEntry();
			zIS.close();
			
			//Delete the zip
			File zip = new File(String.format("./%s%s/%s.zip", RELEASES_DIR, profileName, version));
			zip.delete();
			
			//Add version to list of locally available versions
			JSONObject newJsonData = LauncherGui.getJsonData();
			newJsonData.getJSONArray("versions").put(version);
			FileWriter fW = new FileWriter(new File("./data.json"));
			System.out.println("[LAUNCHER] Updated data.json with new version refs");
			fW.write(newJsonData.toString());
			fW.close();
			//Now that the files are ready launch the game
			if(BottomPanel.getLaunchType().isSelected(BottomPanel.getSinglePlayerButton()))
				Launch(profileName, DZ_SP_ARGS);
			
			if(BottomPanel.getLaunchType().isSelected(BottomPanel.getClientButton()))
				Launch(profileName, DZ_CLIENT_ARGS);
			
			if(BottomPanel.getLaunchType().isSelected(BottomPanel.getServerButton()))
				Launch(profileName, DZ_SERVER_ARGS);
			
		} catch (IOException e) {
			System.err.println("[LAUNCHER] Failed to download files");
			e.printStackTrace();
		}
	}
}
