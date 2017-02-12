package skybox;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import entities.Camera;

public class SkyboxRenderer {
	
	private static final float S = 500f;
	
	private static final float[] VERTICES = {        
	    -S,  S, -S,
	    -S, -S, -S,
	    S, -S, -S,
	     S, -S, -S,
	     S,  S, -S,
	    -S,  S, -S,

	    -S, -S,  S,
	    -S, -S, -S,
	    -S,  S, -S,
	    -S,  S, -S,
	    -S,  S,  S,
	    -S, -S,  S,

	     S, -S, -S,
	     S, -S,  S,
	     S,  S,  S,
	     S,  S,  S,
	     S,  S, -S,
	     S, -S, -S,

	    -S, -S,  S,
	    -S,  S,  S,
	     S,  S,  S,
	     S,  S,  S,
	     S, -S,  S,
	    -S, -S,  S,

	    -S,  S, -S,
	     S,  S, -S,
	     S,  S,  S,
	     S,  S,  S,
	    -S,  S,  S,
	    -S,  S, -S,

	    -S, -S, -S,
	    -S, -S,  S,
	     S, -S, -S,
	     S, -S, -S,
	    -S, -S,  S,
	     S, -S,  S
	};
	
	private static String[] TEX_FILES = {"right", "left", "top", "bottom", "back", "front"};
	
	private RawModel cube;
	private int tex;
	private SkyboxShader skyShader;

	public SkyboxRenderer(Loader loader, Matrix4f projection_Matrix){
		cube = loader.loadToVAO(VERTICES, 3);
		tex = loader.loadCubeMap(TEX_FILES);
		skyShader = new SkyboxShader();
		skyShader.start();
		skyShader.loadProjectionMatrix(projection_Matrix);
		skyShader.stop();
	}
	
	public void renderSky(Camera cam){
		skyShader.start();
		skyShader.loadViewMatrix(cam);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, tex);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		skyShader.stop();
	}
	
	
	
	

}
