package postProcessing;

import org.lwjgl.opengl.GL11;

public class ImageRenderer {

	private Fbo fbo;

	protected ImageRenderer(int w, int h) {
		this.fbo = new Fbo(w, h, Fbo.NONE);
	}

	protected ImageRenderer() {}

	protected void renderQuad() {
		if (fbo != null) {
			fbo.bindfb();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindfb();
		}
	}

	protected int getOutTexture() {
		return fbo.getcolorTex();
	}

	protected void clean() {
		if (fbo != null) {
			fbo.clean();
		}
	}

}
