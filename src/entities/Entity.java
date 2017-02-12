package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity
{
	// Initiate Variables
	private TexturedModel texturedModel;
	private Vector3f p;
	private float rX, rY, rZ;
	private float s;	
	private int texIndex = 0;
	
	// Constructor #1
	public Entity(TexturedModel texturedModel, Vector3f p, float rX, float rY, float rZ, float s)
	{
		this.texturedModel = texturedModel;
		this.p = p;
		this.rX = rX;
		this.rY = rY;
		this.rZ = rZ;
		this.s = s;
	}
	
	// Constructor #2
	public Entity(TexturedModel texturedModel, int i, Vector3f p, float rX, float rY, float rZ, float s)
	{
		this.texIndex = i;
		this.texturedModel = texturedModel;
		this.p = p;
		this.rX = rX;
		this.rY = rY;
		this.rZ = rZ;
		this.s = s;
	}
	
	public float getTextureXOffset()
	{
		return (float)texIndex%texturedModel.getTexture().getnumRows()/(float)texturedModel.getTexture().getnumRows();
	}
	
	public float getTextureYOffset(){
		return (float)texIndex/texturedModel.getTexture().getnumRows()/(float)texturedModel.getTexture().getnumRows();
	}

	public void increasePosition(float dx, float dy, float dz)
	{
		this.p.x += dx;
		this.p.y += dy;
		this.p.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz)
	{
		this.rX += dx;
		this.rX %= 360;
		this.rY += dy;
		this.rY %= 360;
		this.rZ += dz;
		this.rZ %= 360;
	}
	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/

	public TexturedModel getModel()
	{
		return texturedModel;
	}

	public void setModel(TexturedModel model)
	{
		this.texturedModel = model;
	}

	public Vector3f getPosition()
	{
		return p;
	}

	public void setPosition(Vector3f position)
	{
		this.p = position;
	}

	public float getRotX()
	{
		return rX;
	}

	public void setRotX(float rotX)
	{
		while(rotX < 0) rotX += 360;
		this.rX = rotX%360;
	}

	public float getRotY()
	{
		return rY;
	}

	public void setRotY(float rotY)
	{
		while(rotY < 0) rotY += 360;
		this.rY = rotY%360;
	}

	public float getRotZ()
	{
		return rZ;
	}

	public void setRotZ(float rotZ)
	{
		while(rotZ < 0) rotZ += 360;
		this.rZ = rotZ%360;
	}

	public float getScale()
	{
		return s;
	}

	public void setScale(float scale)
	{
		this.s = scale;
	}

}
