package postProcessing;

import shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VS = "/postProcessing/contrastVertexShader.txt";
	private static final String FS = "/postProcessing/contrastFragmentShader.txt";
	
	public ContrastShader() {
		super(VS, FS);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "pos");
	}

}
