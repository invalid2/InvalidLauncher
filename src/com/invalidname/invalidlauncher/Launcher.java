package com.invalidname.invalidlauncher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;

import com.google.common.io.Files;

public class Launcher {
	
	//DZ Version Constants
	public static final String VERSION_08 = "0.8";
	public static final String VERSION_09 = "0.9";
	public static final String VERSION_10 = "1.0";
	public static final String VERSION_16 = "1.6";
	public static final String VERSION_17b = "1.7b";
	public static final String VERSION_17 = "1.7";
	public static final String VERSION_18a = "1.8a";
	public static final String VERSION_18b = "1.8b";
	public static final String VERSION_18 = "1.8";
	public static final String VERSION_19 = "1.9";
	public static final String VERSION_193 = "1.9.3";
	public static final String VERSION_21 = "2.1";
	
	public static final String DOMAIN_ADRESS = "http://invalidlauncher.invalid2.tk/";
	
	
	/**
	 * Launchs DangerZone, assumes everything required for launch has already been done
	 * @param profile name of the profile selected for launch
	 */
	public static void Launch( String profile) {
		ProcessBuilder ps = new ProcessBuilder(new String[] {"java","-jar", "GameRunner.jar", "singleplayer", "-singleplayer", "--singleplayer"});
		File log = new File("./dzconsole.log");
		ps.redirectOutput(log);
		ps.redirectError(log);
		ps.directory(new File(String.format("%s/%s", System.getProperty("user.dir"), profile)));
		try {
			ps.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LauncherGui.setLaunching(false);
	}
	
	/**
	 * Launchs DangerZone, but first copies content from another profile with the 
	 * same version, this is done to avoid having to download files from the internet
	 * @param version the DangerZone version that will be launched
	 * @param profileName The name of the profile selected for launch
	 */
	public static void LaunchNoDownload(String version, String profileName) {
		
		//Get all the profiles
		JSONObject profilesObject = LauncherGui.data.getJSONObject("profiles");
		
		//Create array from available profiles
		String[] profiles = profilesObject.getNames(LauncherGui.data.getJSONObject("profiles"));
		
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
			Files.copy(new File("./"+selectedProfile+"/DangerZone_lib"), new File("./"+profileName+"/DangerZone_lib"));
			Files.copy(new File("./"+selectedProfile+"/GameRunner_lib"), new File("./"+profileName+"/GameRunner_lib"));
			Files.copy(new File("./"+selectedProfile+"/Launcher_lib"), new File("./"+profileName+"/Launcher_lib"));
			Files.copy(new File("./"+selectedProfile+"/DangerZone.jar"), new File("./"+profileName+"/DangerZone.jar"));
			Files.copy(new File("./Player.png"), new File("./"+profileName+"/Player.png"));
			Files.copy(new File("./music"), new File("./"+selectedProfile+"/music"));
			
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
			prop.setProperty("Registered", LauncherGui.data.get("password") != null? "true" : "false");
			prop.setProperty("ScreenWidth", "1920");
			prop.setProperty("MaxGraphics", "true");
			prop.setProperty("ServerAddress", "127.0.0.1");
			prop.setProperty("NameServerAddress", "69.140.167.10");
			prop.setProperty("MusicVolume", "3");
			prop.setProperty("CryptedPassword", LauncherGui.data.get("password") != null? LauncherGui.data.getString("password") : null);
			prop.setProperty("Playername", LauncherGui.data.getString("nickname"));
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
			
			prop.store(new FileOutputStream( new File("./"+selectedProfile+"/DangerZone.properties")), null);
			
			Launch(profileName);
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
			File dir = new File("./"+profileName);
			dir.mkdirs();
			
			FileOutputStream fOS = new FileOutputStream("./"+profileName+"/"+version+".zip");
			HttpURLConnection httpConnection = (HttpURLConnection) new URL(DOMAIN_ADRESS+version+".zip").openConnection();
			
			//System.out.println(httpConnection.getContentLengthLong());
			byte[] data = new byte[1024];
			int dataLength;
			
			//Write the data to pc
			while( (dataLength = bIS.read(data, 0, 1024)) != -1) {
				fOS.write(data, 0, dataLength);
			}
			
			//Close the buffered input stream and the file output stream
			bIS.close();
			fOS.close();
			
			//Now unzip it!
			ZipInputStream zIS = new ZipInputStream( new FileInputStream( new File(String.format("./%s/%s.zip", profileName, version))));
			ZipEntry zE = zIS.getNextEntry();
			File destination = new File(String.format("./%s", profileName));
			data = new byte[1024];
			while(zE != null) {
				File current = new File(destination, zE.getName().substring(zE.getName().indexOf("/")));
				System.out.println(zE.getName());
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
			File zip = new File(String.format("./%s/%s.zip", profileName, version));
			zip.delete();
			
			//Now that the files are ready launch the game
			Launch(profileName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
