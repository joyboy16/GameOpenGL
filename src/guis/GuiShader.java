package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram{
	
	private static final String VS = "/guis/guiVertexShader.txt";
	private static final String FS = "/guis/guiFragmentShader.txt";
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(VS, FS);
	}
	
	public void loadTransformation(Matrix4f mat){
		super.loadMatrix(location_transformationMatrix, mat);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "pos");
	}
	
	
	

}
