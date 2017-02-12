package entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera
{
	// initiate the variables
	private float pitch = 15;
	private float yaw = 0;
	private float roll;
	private float dfS = 5;
	private Vector3f p = new Vector3f(0, 0, 0);	
	private Entity ent;	
	
	// constructor
	public Camera(Entity e)
	{
		this.ent = e;
	}
	
	// invert the pitch
	public void invertPitch()
	{
		this.pitch = -pitch;
	}
	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/

	public Vector3f getPosition()
	{
		return p;
	}

	public float getPitch()
	{
		return pitch;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getRoll()
	{
		return roll;
	}
	
	/********************************************************
	 * 														*
	 * 						MATHS							*
	 * 														*
	 ********************************************************/
	
	// move the camera
	public void updatePosition()
	{
		cLimCamPos(cHorDistance(), cVertDistance());
		this.yaw = -ent.getRotY();
		yaw %= 360;
	}
	
	// calculate the limited camera position
	private void cLimCamPos(float horDistance, float vertDistance)
	{
		// phi (angle)
		float phi = ent.getRotY();
		
		// new position
		p.x = ent.getPosition().x + (float)(horDistance * Math.sin(Math.toRadians(phi)));
		p.z = ent.getPosition().z + (float)(horDistance * Math.cos(Math.toRadians(phi)));
		p.y = ent.getPosition().y + vertDistance + 0.5f;
	}
	
	// calculate the horizontal distance
	private float cHorDistance()
	{
		return (float) ((dfS) * Math.cos(Math.toRadians(pitch+45)));
	}
	
	// calculate the vertical distance
	private float cVertDistance()
	{
		return (float) ((dfS + 5) * Math.sin(Math.toRadians(pitch+4)));
	}
}
