package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tools.Maths;
import entities.Camera;
import entities.Light;

public class TerrainShader extends ShaderProgram{
	
	// variables
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "/shaders/terrainFragmentShader.txt";	
	private int l_transformationMatrix;
	private int l_projectionMatrix;
	private int l_viewMatrix;
	private int l_lightPosition[];
	private int l_lightColour[];
	private int l_shineDamper;
	private int l_attenuation[];
	private int l_reflectivity;
	private int l_skyColour;
	private int l_backgroundTexture;
	private int l_rTexture;
	private int l_gTexture;
	private int l_bTexture;
	private int l_blendMap;
	private int l_plane;
	private int l_toShadowMapSpace;
	private int l_shadowMap;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations()
	{
		// define uniform variables
		l_transformationMatrix = super.getUniformLocation("transformationMatrix");
		l_projectionMatrix = super.getUniformLocation("projectionMatrix");
		l_viewMatrix = super.getUniformLocation("viewMatrix");
		l_shineDamper = super.getUniformLocation("shineDamper");
		l_reflectivity = super.getUniformLocation("reflectivity");
		l_skyColour = super.getUniformLocation("skyColour");
		l_backgroundTexture = super.getUniformLocation("backgroundTexture");
		l_rTexture = super.getUniformLocation("rTexture");
		l_gTexture = super.getUniformLocation("gTexture");
		l_bTexture = super.getUniformLocation("bTexture");
		l_blendMap = super.getUniformLocation("blendMap");
		l_plane = super.getUniformLocation("plane");
		l_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		l_shadowMap = super.getUniformLocation("shadowMap");
		
		l_lightPosition = new int[MAX_LIGHTS];
		l_lightColour = new int[MAX_LIGHTS];
		l_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			l_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			l_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			l_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	/********************************************************
	 * 														*
	 * 					UNIFORM METHODS						*
	 * 														*
	 ********************************************************/
	
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(l_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(l_lightColour[i], lights.get(i).getColour());
				super.loadVector(l_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(l_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(l_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(l_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadClipPlane(Vector4f clipPlane){
		super.loadVector(l_plane, clipPlane);
	}
	
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(l_shineDamper, damper);
		super.loadFloat(l_reflectivity, reflectivity);
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(l_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadToShadowSpaceMatrix(Matrix4f matrix){
		super.loadMatrix(l_toShadowMapSpace, matrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(l_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(l_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = tools.Maths.generateViewMatrix(camera);
		super.loadMatrix(l_viewMatrix, viewMatrix);
	}
	
	public void connectTextureUnits(){
		super.loadInt(l_backgroundTexture, 0);
		super.loadInt(l_rTexture, 1);
		super.loadInt(l_gTexture, 2);
		super.loadInt(l_bTexture, 3);
		super.loadInt(l_blendMap, 4);
		super.loadInt(l_shadowMap, 5);
	}
}
