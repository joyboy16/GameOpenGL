
package postProcessing;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class Fbo {

	public static final int NONE = 0;
	public static final int DEPTH_TEXTURE = 1;
	public static final int DEPTH_RENDER_BUFFER = 2;

	private final int w;
	private final int h;

	private int fb;

	private int colorTex;
	private int depthTex;

	private int db;
	private int cb;

	public Fbo(int w, int h, int dbType) {
		this.w = w;
		this.h = h;
		initialisefb(dbType);
	}

	public void clean() {
		GL30.glDeleteFramebuffers(fb);
		GL11.glDeleteTextures(colorTex);
		GL11.glDeleteTextures(depthTex);
		GL30.glDeleteRenderbuffers(db);
		GL30.glDeleteRenderbuffers(cb);
	}

	public void bindfb() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb);
		GL11.glViewport(0, 0, w, h);
	}

	public void unbindfb() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void bindRead() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	public int getcolorTex() {
		return colorTex;
	}


	public int getdepthTex() {
		return depthTex;
	}

	private void initialisefb(int type) {
		createfb();
		createTexture();
		if (type == DEPTH_RENDER_BUFFER) {
			createdbAttachment();
		} else if (type == DEPTH_TEXTURE) {
			createdepthTexAttachment();
		}
		unbindfb();
	}

	private void createfb() {
		fb = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fb);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	private void createTexture() {
		colorTex = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTex);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTex,
				0);
	}

	private void createdepthTexAttachment() {
		depthTex = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTex);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, w, h, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTex, 0);
	}

	private void createdbAttachment() {
		db = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, db);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, w, h);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				db);
	}

}
