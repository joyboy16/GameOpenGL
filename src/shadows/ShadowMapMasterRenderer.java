package shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;

public class ShadowMapMasterRenderer {

	private static final int SHADOWMAP_SIZE = 16384;

	private ShadowFrameBuffer shadowFbo;
	private ShadowShader shadowShader;
	private ShadowBox shadowBox;
	private Matrix4f projection = new Matrix4f();
	private Matrix4f lightView = new Matrix4f();
	private Matrix4f projectionView = new Matrix4f();
	private Matrix4f off = createOffset();

	private ShadowMapEntityRenderer entRenderer;

	public ShadowMapMasterRenderer(Camera cam) {
		shadowShader = new ShadowShader();
		shadowBox = new ShadowBox(lightView, cam);
		shadowFbo = new ShadowFrameBuffer(SHADOWMAP_SIZE, SHADOWMAP_SIZE);
		entRenderer = new ShadowMapEntityRenderer(shadowShader, projectionView);
	}

	public void renderShadow(Map<TexturedModel, List<Entity>> entity_list, Light light) {
		shadowBox.update();
		Vector3f lightPosition = light.getPosition();
		Vector3f lightDirection = new Vector3f(-lightPosition.x, -lightPosition.y, -lightPosition.z);
		prepareForRender(lightDirection, shadowBox);
		entRenderer.renderShadow(entity_list);
		finishRender();
	}

	public Matrix4f getShadowMapSpaceMatrix() {
		return Matrix4f.mul(off, projectionView, null);
	}

	public void clean() {
		shadowShader.cleanUp();
		shadowFbo.clean();
	}

	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}

	protected Matrix4f getLightSpaceTransform() {
		return lightView;
	}

	private void prepareForRender(Vector3f lightDir, ShadowBox box) {
		updateOrthogonalProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateLightViewMatrix(lightDir, box.getCenter());
		Matrix4f.mul(projection, lightView, projectionView);
		shadowFbo.bindFB();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		shadowShader.start();
	}

	private void finishRender() {
		shadowShader.stop();
		shadowFbo.unbindFB();
	}

	private void updateLightViewMatrix(Vector3f dir, Vector3f center) {
		dir.normalise();
		center.negate();
		lightView.setIdentity();
		float pitch = (float) Math.acos(new Vector2f(dir.x, dir.z).length());
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightView, lightView);
		float yaw = (float) Math.toDegrees(((float) Math.atan(dir.x / dir.z)));
		yaw = dir.z > 0 ? yaw - 180 : yaw;
		Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightView,lightView);
		Matrix4f.translate(center, lightView, lightView);
	}


	private void updateOrthogonalProjectionMatrix(float w, float h, float len) {
		projection.setIdentity();
		projection.m00 = 2f / w;
		projection.m11 = 2f / h;
		projection.m22 = -2f / len;
		projection.m33 = 1;
	}

	private static Matrix4f createOffset() {
		Matrix4f off = new Matrix4f();
		off.translate(new Vector3f(0.5f, 0.5f, 0.5f));
		off.scale(new Vector3f(0.5f, 0.5f, 0.5f));
		return off;
	}
}
