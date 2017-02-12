package skybox;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import tools.Maths;
import entities.Camera;
 
public class SkyboxShader extends ShaderProgram{
 
    private static final String VS = "/skybox/skyVertexShader.txt";
    private static final String FS = "/skybox/skyFragmentShader.txt";
    
    private static final float ROTATION_SPEED = 1f;
     
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationCubeMap;
    
    private float rotation = 0;
     
    public SkyboxShader() {
        super(VS, FS);
    }
     
    public void loadProjectionMatrix(Matrix4f mat){
        super.loadMatrix(locationProjectionMatrix, mat);
    }
 
    public void loadViewMatrix(Camera cam){
        Matrix4f mat = Maths.generateViewMatrix(cam);
        mat.m30 = 0;
        mat.m31 = 0;
        mat.m32 = 0;
        rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), mat, mat);
        super.loadMatrix(locationViewMatrix, mat);
    }
    

     
    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projection");
        locationViewMatrix = super.getUniformLocation("view");
        locationCubeMap = super.getUniformLocation("cubeMap");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "pos");
    }
 
}