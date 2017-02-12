package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tools.Maths;
import entities.Camera;
import entities.Light;

public class StaticShader extends ShaderProgram{
	
	// variables
	private static final int MAX_LIGHTS = 4;	
	private static final String VERTEX_FILE = "/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "/shaders/fragmentShader.txt";	
	private int l_transformationMatrix;
	private int l_projectionMatrix;
	private int l_viewMatrix;
	private int l_lightPosition[];
	private int l_lightColour[];
	private int l_attenuation[];
	private int l_shineDamper;
	private int l_reflectivity;
	private int l_useFakeLighting;
	private int l_skyColour;
	private int l_numberOfRows;
	private int l_offset;
	private int l_plane;

	public StaticShader() {
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
		// define the uniform variables
		l_transformationMatrix = super.getUniformLocation("transformationMatrix");
		l_projectionMatrix = super.getUniformLocation("projectionMatrix");
		l_viewMatrix = super.getUniformLocation("viewMatrix");
		l_shineDamper = super.getUniformLocation("shineDamper");
		l_reflectivity = super.getUniformLocation("reflectivity");
		l_useFakeLighting = super.getUniformLocation("useFakeLighting");
		l_skyColour = super.getUniformLocation("skyColour");
		l_numberOfRows = super.getUniformLocation("numberOfRows");
		l_offset = super.getUniformLocation("offset");
		l_plane = super.getUniformLocation("plane");
		
		l_lightPosition = new int[MAX_LIGHTS];
		l_lightColour = new int[MAX_LIGHTS];
		l_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++)
		{
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
	
	
	public void loadClipPlane(Vector4f plane){
		super.loadVector(l_plane, plane);
	}
	
	public void loadOffset(float x, float y){
		super.load2DVector(l_offset, new Vector2f(x,y));
	}
	
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
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(l_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadFakeLightingVariable(boolean useFake){
		super.loadBoolean(l_useFakeLighting, useFake);
	}
	
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(l_numberOfRows, numberOfRows);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(l_projectionMatrix, projection);
	}
	
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(l_shineDamper, damper);
		super.loadFloat(l_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(l_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = tools.Maths.generateViewMatrix(camera);
		super.loadMatrix(l_viewMatrix, viewMatrix);
	}
}
