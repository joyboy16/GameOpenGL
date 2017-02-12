package water;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL42;

public class WaterFrameBuffers {

	protected static final int REFL_WIDTH = 1920;
	private static final int REFL_HEIGHT = 1080;	
	protected static final int REFR_WIDTH = 1280;
	private static final int REFR_HEIGHT = 720;
	private int reflFrameBuffer;
	private int relfTexture;
	private int relfDepthBuffer;	
	private int refrFrameBuffer;
	private int refrTexture;
	private int refrDepthTexture;

	// Constructor
	public WaterFrameBuffers()
	{
		// init buffers
		initialiseRefractionFrameBuffer();
		initialiseReflectionFrameBuffer();
	}

	public void cleanUp()
	{
		GL30.glDeleteFramebuffers(refrFrameBuffer);
		GL30.glDeleteFramebuffers(reflFrameBuffer);
		GL11.glDeleteTextures(refrTexture);
		GL11.glDeleteTextures(relfTexture);
		GL11.glDeleteTextures(refrDepthTexture);
		GL30.glDeleteRenderbuffers(relfDepthBuffer);
	}

	public void bindReflectionFrameBuffer()
	{
		bindFrameBuffer(reflFrameBuffer,REFL_WIDTH,REFL_HEIGHT);
	}
	
	public void bindRefractionFrameBuffer()
	{
		bindFrameBuffer(refrFrameBuffer,REFR_WIDTH,REFR_HEIGHT);
	}
	
	/********************************************************
	 * 														*
	 * 				OPENGL CODE FOR WATER SETUP				*
	 * 			not much space for interpretation			*
	 ********************************************************/
	
	public void unbindCurrentFrameBuffer()
	{
		GL11.glViewport(0, 0, 1280, 720);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	private void initialiseReflectionFrameBuffer() {
		reflFrameBuffer = createFrameBuffer();
		relfTexture = createTextureAttachment(REFL_WIDTH,REFL_HEIGHT);
		relfDepthBuffer = createDepthBufferAttachment(REFL_WIDTH,REFL_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	private void initialiseRefractionFrameBuffer() {
		refrFrameBuffer = createFrameBuffer();
		refrTexture = createTextureAttachment(REFR_WIDTH,REFR_HEIGHT);
		refrDepthTexture = createDepthTextureAttachment(REFR_WIDTH,REFR_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	private void bindFrameBuffer(int frameBuffer, int width, int height){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return frameBuffer;
	}

	private int createTextureAttachment( int width, int height) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,texture, 0);
		return texture;
	}
	
	private int createDepthTextureAttachment(int width, int height){
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,texture, 0);
		return texture;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/
	
	public int getReflectionTexture() {
		return relfTexture;
	}
	
	public int getRefractionTexture() {
		return refrTexture;
	}
	
	public int getRefractionDepthTexture(){
		return refrDepthTexture;
	}
}
