package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light
{
	// Initiate Variables
	private Vector3f p;
	private Vector3f c;
	private Vector3f atten = new Vector3f(1, 0, 0);
	
	// Constructor #1
	public Light(Vector3f p, Vector3f c)
	{
		this.p = p;
		this.c = c;
	}
	
	// Constructor #2
	public Light(Vector3f p, Vector3f c, Vector3f atten)
	{
		this.p = p;
		this.c = c;
		this.atten = atten;
	}
	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/
	
	public Vector3f getAttenuation()
	{
		return atten;
	}

	public Vector3f getPosition()
	{
		return p;
	}

	public void setPosition(Vector3f p)
	{
		this.p = p;
	}

	public Vector3f getColour()
	{
		return c;
	}

	public void setColour(Vector3f c)
	{
		this.c = c;
	}
	

}
