package renderEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import textures.TextureModel;
import tools.Maths;
import entities.Entity;

public class EntityRenderer
{
	// shader
	private StaticShader shader;

	// Constructor
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		// start shader
		shader.start();
		// load projection matrix
		shader.loadProjectionMatrix(projectionMatrix);
		// stop shader
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities)
	{
		// render all entities
		for (TexturedModel model : entities.keySet())
		{
			// prepare the model
			prepareTexturedModel(model);
			List<Entity> eList = entities.get(model);
			// draw
			for (Entity entity : eList)
			{
				prepareInstance(entity);
				// draw-call
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			// unbind model
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model)
	{
		// new model
		RawModel rawModel = model.getRawModel();
		// new texture
		TextureModel texture = model.getTexture();
		// texture-atlas
		shader.loadNumberOfRows(texture.getnumRows());
		
		
		// transparency
		if(texture.isTransparent())	MasterRenderer.disableCulling();
		// lightning
		shader.loadFakeLightingVariable(texture.isuseLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		// Open-GL code
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	private void unbindTexturedModel()
	{
		// backface culling
		MasterRenderer.enableCulling();
		// Open-GL code
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity)
	{
		// calculate worldPosition -> screenPosition
		Matrix4f transformationMatrix = Maths.generateTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		// load matrix and offset
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

}
