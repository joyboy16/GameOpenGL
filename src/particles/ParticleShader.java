package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VS = "/particles/particleVertexShader.txt";
	private static final String FS = "/particles/particleFragmentShader.txt";

	private int locationNumRows;
	private int locationProjection;
	

	public ParticleShader() {
		super(VS, FS);
	}

	@Override
	protected void getAllUniformLocations() {
		locationNumRows = super.getUniformLocation("numberOfRows");
		locationProjection = super.getUniformLocation("projectionMatrix");
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadNumberOfRows(float numRows){
		super.loadFloat(locationNumRows, numRows);
	}
	

	protected void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(locationProjection, projection);
	}

}
