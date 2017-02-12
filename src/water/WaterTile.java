package water;

public class WaterTile
{
	// tileSize, height and position
	public static final float TILE_SIZE = 150;	
	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height)
	{
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}
	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
}
