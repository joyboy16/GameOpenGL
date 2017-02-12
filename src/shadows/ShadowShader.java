package shadows;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class ShadowShader extends ShaderProgram {
	
	private static final String VS = "/shadows/shadowVertexShader.txt";
	private static final String FS = "/shadows/shadowFragmentShader.txt";
	
	private int locationMvpMatrix;

	protected ShadowShader() {
		super(VS, FS);
	}

	@Override
	protected void getAllUniformLocations() {
		locationMvpMatrix = super.getUniformLocation("mvp_Matrix");
		
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(locationMvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_pos");
		super.bindAttribute(1, "in_textureCoordinates");
	}

}
