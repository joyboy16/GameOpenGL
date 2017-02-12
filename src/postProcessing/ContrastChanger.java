package postProcessing;

import java.awt.font.ImageGraphicAttribute;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger {

	private ImageRenderer imgRenderer;
	private ContrastShader contShader;
	
	public ContrastChanger() {
		
		imgRenderer = new ImageRenderer();
		contShader = new ContrastShader();
	}
	
	public void render(int tex){
		contShader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		imgRenderer.renderQuad();
		contShader.stop();
		
	}
	
	public void clean() {
		imgRenderer.clean();
		contShader.cleanUp();

	}
}
