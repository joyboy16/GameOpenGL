package shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram
{
	
	private int pID;
	private int vID;
	private int fID;	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	// Constructor
	public ShaderProgram(String vertexFile,String fragmentFile)
	{
		vID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		pID = GL20.glCreateProgram();
		GL20.glAttachShader(pID, vID);
		GL20.glAttachShader(pID, fID);
		bindAttributes();
		GL20.glLinkProgram(pID);
		GL20.glValidateProgram(pID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(pID,uniformName);
	}
	
	public void start(){
		GL20.glUseProgram(pID);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void cleanUp()
	{
		// stop the shader
		stop();
		// delete vertex
		GL20.glDetachShader(pID, vID);
		GL20.glDeleteShader(vID);
		
		// delete fragmet
		GL20.glDetachShader(pID, fID);
		GL20.glDeleteShader(fID);
		
		// delete shader program
		GL20.glDeleteProgram(pID);
	}
	
	protected abstract void bindAttributes();
	
	/********************************************************
	 * 														*
	 * 						OPENGL-METHODS					*
	 * 														*
	 ********************************************************/
	
	protected void bindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(pID, attribute, variableName);
	}
	
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value)
	{
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	protected void loadVector(int location, Vector4f vector)
	{
		GL20.glUniform4f(location,vector.x,vector.y,vector.z, vector.w);
	}
	
	protected void load2DVector(int location, Vector2f vector)
	{
		GL20.glUniform2f(location,vector.x,vector.y);
	}
	
	protected void loadBoolean(int location, boolean value)
	{
		if(value) GL20.glUniform1f(location, 1.0f);
		else GL20.glUniform1f(location, 0.0f);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	private static int loadShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		try
		{
			// try to read the shader and store it in buffer
			InputStream in = Class.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine())!=null)	shaderSource.append(line).append("//\n");
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		// openGL code
		GL20.glShaderSource(shaderID, shaderSource);
		// compile shader
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE)
		{
			System.err.println("Konnte Shader nicht kompilieren");
			System.exit(-1);
		}
		return shaderID;
	}

}
