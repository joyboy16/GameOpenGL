package models;

public class RawModel
{
	// declare variables
	private int vaoNumber;
	private int numberOfVertices;
	
	// Constructor
	public RawModel(int vaoNumber, int numberOfVertices)
	{
		this.vaoNumber = vaoNumber;
		this.numberOfVertices = numberOfVertices;
	}

	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/
	
	public int getVaoID()
	{
		return vaoNumber;
	}

	public int getVertexCount()
	{
		return numberOfVertices;
	}
}
