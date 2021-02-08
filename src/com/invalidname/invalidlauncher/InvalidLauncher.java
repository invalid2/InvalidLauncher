package com.invalidname.invalidlauncher;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector2i;
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
	
	public static void main(String[] args) {
		Window window = new Window();
		window.create();
		
		//Create LEGUI instance
		DefaultInitializer initializer = new DefaultInitializer(window.getWindow(), window.getFrame());
		
		GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> isRunning = false;
		
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
			// Set viewport size
			glViewport(0, 0, windowSize.x, windowSize.y);
			// Clear screen
			glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			
			// render frame
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
