package water;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import tools.Maths;
import entities.Camera;
import entities.Light;

public class WaterRenderer
{	
	// Variables
	private static final String UV_MAP = "waterDUDV";
	private static final String NORM_MAP = "normal";
	private static final float WAVE_SPEED = 0.03f;
	private RawModel wTile;
	private WaterShader shader;
	private WaterFrameBuffers fbos;	
	private float moveFactor = 0;	
	private int dudvTexture;
	private int normalMap;

	// Constuctor
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		// declare important stuff
		dudvTexture = loader.loadTexture(UV_MAP);
		normalMap = loader.loadTexture(NORM_MAP);
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.conTexUnits();
		shader.stop();
		this.fbos = fbos;
		// create the actual waterTile
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera, Light sun)
	{
		prepareRender(camera, sun);
		for(int i=0;i<water.size();i++)
		{
			WaterTile tile = water.get(i);
			Matrix4f modelMatrix = Maths.generateTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, wTile.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, Light sun){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor = moveFactor + WAVE_SPEED * DisplayManager.delta();
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		
		// openGL code
		GL30.glBindVertexArray(wTile.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		// activate and bind texture layers
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind()
	{
		//openGL code
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// only y is not important (set later)
		// simple quad
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		wTile = loader.loadToVAO(vertices, 2);
	}

}
