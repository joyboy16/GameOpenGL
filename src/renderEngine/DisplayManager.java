package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager
{
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static int MAX_FPS = 60;	
	private static long lastFrame;
	private static float d;
	
	public static void createDisplay()
	{		
		// Context attributes
		ContextAttribs attribs = new ContextAttribs(3,3).withForwardCompatible(true).withProfileCore(true);
		
		// Undecorated Window
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		
		// realtime
		lastFrame = getCurrentTime();
		
		try
		{
			// create Display
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			//Display.setFullscreen(true);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Battleship!");
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}		
		GL11.glViewport(0,0, WIDTH, HEIGHT);		
	}	
	
	public static void updateDisplay()
	{
		// sync wit MAX
		Display.sync(MAX_FPS);
		// update
		Display.update();
		// calc d
		long currentFrameTime = getCurrentTime();
		d = (currentFrameTime - lastFrame)/1000f;
		lastFrame = currentFrameTime;
	}
	
	public static void closeDisplay()
	{
		// close
		Display.destroy();
	}
	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/
	
	private static long getCurrentTime()
	{
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
	public static float delta()
	{
		return d;
	}
	
	public static void setMaxFPS(int fps)
	{
		MAX_FPS = fps;
	}
}
