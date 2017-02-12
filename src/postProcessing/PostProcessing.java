package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger changer;
	
	
	public static void init(Loader loader){
		quad = loader.loadToVAO(POS, 2);
		changer = new ContrastChanger();
	}
	
	public static void runPostProcessing(int colourTex){
		startProcess();
		changer.render(colourTex);
		endProcess();
	}
	
	public static void clean(){
		changer.clean();
	}
	
	private static void startProcess(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void endProcess(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
