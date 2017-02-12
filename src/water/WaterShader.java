package water;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;
import tools.Maths;
import entities.Camera;
import entities.Light;

public class WaterShader extends ShaderProgram
{
	// Variables
	private final static String V_FILE = "/water/waterVertex.txt";
	private final static String F_FILE = "/water/waterFragment.txt";
	private int l_modelMatrix;
	private int l_viewMatrix;
	private int l_projectionMatrix;
	private int l_reflectionTexture;
	private int l_refractionTexture;
	private int l_dudvMap;
	private int l_moveFactor;
	private int l_cameraPosition;
	private int l_normalMap;
	private int l_lightPosition;
	private int l_lightColour;
	private int l_depthMap;

	// Constructor
	public WaterShader() {
		super(V_FILE, F_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations()
	{
		// define the uniform variables
		l_projectionMatrix = getUniformLocation("projectionMatrix");
		l_viewMatrix = getUniformLocation("viewMatrix");
		l_modelMatrix = getUniformLocation("modelMatrix");
		l_reflectionTexture = getUniformLocation("reflectionTexture");
		l_refractionTexture = getUniformLocation("refractionTexture");
		l_dudvMap = getUniformLocation("dudvMap");
		l_moveFactor = getUniformLocation("moveFactor");
		l_cameraPosition = getUniformLocation("cameraPosition");
		l_normalMap = getUniformLocation("normalMap");
		l_lightPosition = getUniformLocation("lightPosition");
		l_lightColour = getUniformLocation("lightColour");
		l_depthMap = getUniformLocation("depthMap");
	}
	
	/********************************************************
	 * 														*
	 * 					UNIFORM METHODS						*
	 * 														*
	 ********************************************************/
	
	public void loadLight(Light light){
		super.loadVector(l_lightColour, light.getColour());
		super.loadVector(l_lightPosition, light.getPosition());
	}
	
	public void loadMoveFactor(float factor){
		super.loadFloat(l_moveFactor, factor);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(l_projectionMatrix, projection);
	}
	
	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(l_modelMatrix, modelMatrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.generateViewMatrix(camera);
		loadMatrix(l_viewMatrix, viewMatrix);
		super.loadVector(l_cameraPosition, camera.getPosition());
	}
	
	public void conTexUnits(){
		super.loadInt(l_reflectionTexture, 0);
		super.loadInt(l_refractionTexture, 1);
		super.loadInt(l_dudvMap, 2);
		super.loadInt(l_normalMap, 3);
		super.loadInt(l_depthMap, 4);
	}	
}
