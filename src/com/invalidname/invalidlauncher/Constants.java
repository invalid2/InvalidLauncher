package com.invalidname.invalidlauncher;

public class Constants {
	
	//DZ Version Constants
	public static final String PROFILES_DIR = "profiles/";
	public static final String DOMAIN_ADRESS = "http://invalidlauncher.invalid2.tk/";
	
	//Enviroment arguments constants
	public static final String[] DZ_SP_ARGS = {"java","-jar", "GameRunner.jar", "singleplayer", "-singleplayer", "--singleplayer"};
	public static final String[] DZ_CLIENT_ARGS = {"java","-jar", "GameRunner.jar", "client", "-client", "--client"};
	public static final String[] DZ_SERVER_ARGS = {"java","-jar", "GameRunner.jar", "server", "-server", "--server"};
	
	

	//JSON keys constants
	public static final String JSON_KEY_NICKNAME = "nickname";
	public static final String JSON_KEY_PASSWORD = "password";
	public static final String JSON_KEY_REGISTERED = "registered";
	public static final String JSON_KEY_REMEMBER = "remember";
	public static final String JSON_KEY_PROFILES = "profiles";
	public static final String JSON_KEY_VERSIONS = "versions";
	public static final String JSON_KEY_USE_MUSIC = "musicUse";
	public static final String JSON_KEY_MUSIC_LOCAL = "musicLocal";
	
	public Constants() {
		
	}

}
