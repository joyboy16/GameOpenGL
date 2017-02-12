package shadows;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class ShadowFrameBuffer {

	private final int width;
	private final int height;
	private int fbo;
	private int shadowMap;

	protected ShadowFrameBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		initialiseFB();
	}

	protected void clean() {
		GL30.glDeleteFramebuffers(fbo);
		GL11.glDeleteTextures(shadowMap);
	}

	protected void bindFB() {
		bindFrameBuffer(fbo, width, height);
	}

	protected void unbindFB() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	protected int getShadowMap() {
		return shadowMap;
	}

	private void initialiseFB() {
		fbo = createFB();
		shadowMap = createDepthBuffer(width, height);
		unbindFB();
	}

	private static void bindFrameBuffer(int fb, int w, int h) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb);
		GL11.glViewport(0, 0, w, h);
	}

	private static int createFB() {
		int fb = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fb);
		GL11.glDrawBuffer(GL11.GL_NONE);
		GL11.glReadBuffer(GL11.GL_NONE);
		return fb;
	}

	private static int createDepthBuffer(int w, int h) {
		int tex = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, w, h, 0,
				GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, tex, 0);
		return tex;
	}
}
