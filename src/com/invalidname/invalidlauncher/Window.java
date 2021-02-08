package com.invalidname.invalidlauncher;

import static org.liquidengine.legui.style.color.ColorUtil.fromInt;
import static org.lwjgl.glfw.GLFW.*;

import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.theme.Themes;
import org.liquidengine.legui.theme.colored.FlatColoredTheme;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window {
	private int width, height;
	private String title = "Invalid Launcher: A DangerZone Launcher";
	private long window;
	private Frame frame;
	private static long[] monitors = null;
	private LauncherGui launcherGui;

	//Constructor for the launcher window
	public Window(int width, int height, String title) {
		super();
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public Window() {
		
	}

	public void create() {
		//Start lwjgl
		if (!glfwInit()) {
			throw new RuntimeException("Error: Unable to initialize GLFW");
		}
		
		//Get the vid mode
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//Set the window size
		if(width == 0 && height == 0) {
			width = videoMode.width() * 3/5;
			height = videoMode.height() * 3/5;
		}
		
		glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		window = glfwCreateWindow(width, height, title, 0, 0);
		
		glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		glfwShowWindow(window);
		
		// make window current on thread
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glfwSwapInterval(0);
		
		// read monitors
		PointerBuffer pointerBuffer = glfwGetMonitors();
		int remaining = pointerBuffer.remaining();
		monitors = new long[remaining];
		for (int i = 0; i < remaining; i++) {
			monitors[i] = pointerBuffer.get(i);
		}
		
		// create LEGUI theme and set it as default --default theme for now
		Themes.setDefaultTheme(new FlatColoredTheme(
				fromInt(245, 245, 245, 1), // backgroundColor
				fromInt(176, 190, 197, 1), // borderColor
				fromInt(176, 190, 197, 1), // sliderColor
				fromInt(100, 181, 246, 1), // strokeColor
				fromInt(165, 214, 167, 1), // allowColor
				fromInt(239, 154, 154, 1), // denyColor
				ColorConstants.transparent(), // shadowColor
				ColorConstants.darkGray(), // text color
				FontRegistry.getDefaultFont(), // font
				16f //font size
		));
		
		frame = new Frame(width, height);
		createGUI(frame, width, height);
		
		
	} 
	
	public Frame getFrame() {
		return frame;
	}

	public long getWindow() {
		return window;
	}
	
	public LauncherGui getLauncherGui() {
		return launcherGui;
	}

	private void createGUI(Frame frame, int width, int height) {
		
		launcherGui = new LauncherGui(width, height);
		launcherGui.setFocusable(false);
		frame.getContainer().add(launcherGui);
		
	}

	public boolean wasClosed() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public void update() {
		GLFW.glfwPollEvents();
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}
}