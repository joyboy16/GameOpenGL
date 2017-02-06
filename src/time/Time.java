package time;

import java.util.Currency;

import org.lwjgl.Sys;

public class Time
{
	private static long lastFrameTime;
	private static float delta;
	
	public Time()
	{
		lastFrameTime = getCurrentTime();
	}
	
	private static long getCurrentTime()
	{
		return Sys.getTime();
	}
	
	private static long getTime()
	{
		return getCurrentTime();
	}
	
	public static void updateTime()
	{
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float deltaTime()
	{
		return lastFrameTime - getCurrentTime();
	}
}
