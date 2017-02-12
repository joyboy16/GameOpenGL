package shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.MasterRenderer;
import tools.Maths;

public class ShadowMapEntityRenderer {

	private Matrix4f projectionView;
	private ShadowShader shadowShader;

	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionView) {
		this.shadowShader = shader;
		this.projectionView = projectionView;
	}

	protected void renderShadow(Map<TexturedModel, List<Entity>> entity_list) {
		for (TexturedModel texModel : entity_list.keySet()) {
			RawModel raw = texModel.getRawModel();
			bindModel(raw);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texModel.getTexture().getID());
			if (texModel.getTexture().isTransparent()) {
				MasterRenderer.disableCulling();
			}
			for (Entity entity : entity_list.get(texModel)) {
				prepare(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, raw.getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			if (texModel.getTexture().isTransparent()) {
				MasterRenderer.enableCulling();
			}
			
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void bindModel(RawModel raw) {
		GL30.glBindVertexArray(raw.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}


	private void prepare(Entity entity) {
		Matrix4f modelMat = Maths.generateTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		Matrix4f mvpMat = Matrix4f.mul(projectionView, modelMat, null);
		shadowShader.loadMvpMatrix(mvpMat);
	}

}
