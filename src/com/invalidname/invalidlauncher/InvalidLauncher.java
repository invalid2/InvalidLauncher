package com.invalidname.invalidlauncher;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import org.joml.Vector2i;
import org.json.JSONException;
import org.json.JSONObject;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.Animator;
import org.liquidengine.legui.animation.AnimatorProvider;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.liquidengine.legui.system.renderer.Renderer;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;


public class InvalidLauncher {
		
	public volatile static boolean isRunning;
	private static Context context;
	
	private static File launcherLog = new File("./launcher.log");
	
	//If you look a few lines further, you will see that it is indeed used
	@SuppressWarnings("unused")
	private static PrintStream logPS;
	
	public static void main(String[] args) {
		System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
		System.setProperty("java.awt.headless", Boolean.FALSE.toString());
		
		try {
			logPS = new PrintStream(launcherLog);
			//System.setOut(logPS);
			//System.setErr(logPS);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Window window = new Window();
		window.create();
		
		//Create LEGUI instance
		DefaultInitializer initializer = new DefaultInitializer(window.getWindow(), window.getFrame());
		
		GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> {
			JSONObject newData = new JSONObject();
			Iterator<String> keys = LauncherGui.getJsonData().keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if(key.equals("password")) {
					newData.put(key, LauncherGui.getJsonData().getBoolean("remember")? LauncherGui.getJsonData().get(key) : "");
					continue;
				}
				
				if(key.equals("nickname")) {
					newData.put(key, LauncherGui.getJsonData().getBoolean("remember")? LauncherGui.getJsonData().get(key) : "Player");
					continue;
				}
				
				newData.put(key, LauncherGui.getJsonData().get(key));
			}
			System.out.println(newData.toString());
			try {
				FileWriter fW = new FileWriter("./data.json");
				
				fW.write(newData.toString(1));
				fW.close();
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isRunning = false;
		};
		
		initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);
		
		// Set to true to start render loop
		isRunning = true;
		
		// before render loop we need to initialize renderer
		Renderer renderer = initializer.getRenderer();
		Animator animator = AnimatorProvider.getAnimator();
		renderer.initialize();
		
		context = initializer.getContext();
		while(isRunning) {
			context.updateGlfwWindow();
			Vector2i windowSize = context.getFramebufferSize();
			
			glClearColor(1, 1, 1, 1);
			// Sets the current viewport size
			glViewport(0, 0, windowSize.x, windowSize.y);
			// Clears screen
			glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			
			// Renders the current frame
			renderer.render(window.getFrame(), context);
			
			// poll events to callbacks
			glfwPollEvents();
			glfwSwapBuffers(window.getWindow());
			
			animator.runAnimations();
			
			// Now we need to handle events. Firstly we need to handle system events.
			// And we need to know to which frame they should be passed.
			initializer.getSystemEventProcessor().processEvents(window.getFrame(), context);
			
			
			// This event processor calls listeners added to ui components
			initializer.getGuiEventProcessor().processEvents();
		}
		
		renderer.destroy();
		
		glfwDestroyWindow(window.getWindow());
		glfwTerminate();
	}
}
