package objConverter;

public class ModelData
{
	// declare variables
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private float[] tangents;
	private int[] indices;
	private float farestPoint;

	public ModelData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices,	float farestPoint)
	{
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.tangents = tangents;
		this.indices = indices;
		this.farestPoint = farestPoint;
	}
	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/

	public float[] getVertices()
	{
		return vertices;
	}

	public float[] getTextureCoords()
	{
		return textureCoords;
	}
	
	public float[] getTangents()
	{
		return tangents;
	}

	public float[] getNormals()
	{
		return normals;
	}

	public int[] getIndices()
	{
		return indices;
	}

	public float getFurthestPoint()
	{
		return farestPoint;
	}

}
