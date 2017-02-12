package objConverter;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Vertex
{
	// static
	private static final int NO_INDEX = -1;
	
	// variables
	private Vector3f position;
	private int textureIndex = -1;
	private int normalIndex = -1;
	private Vertex dupVertex;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f aTangent = new Vector3f(0, 0, 0);
	private int i;
	private float l;
	
	// Constructor
	public Vertex(int index,Vector3f position)
	{
		this.i = index;
		this.position = position;
		this.l = position.length();
		dupVertex = null;
	}
	
	public void averageTangents()
	{
		// no average if isEmpty
		if(tangents.isEmpty()) return;
		// add all
		for(Vector3f tangent : tangents) Vector3f.add(aTangent, tangent, aTangent);
		// normalise
		aTangent.normalise();
	}
	
	/********************************************************
	 * 														*
	 * 						ONE-LINER						*
	 * 														*
	 ********************************************************/
	
	public void addTangent(Vector3f tangent)
	{
		tangents.add(tangent);
	}
	
	public boolean hasSameTextureAndNormal(int tI,int nI)
	{
		return nI==normalIndex&&tI==textureIndex;
	}
	
	public boolean isSet()
	{
		return textureIndex!=-1 && normalIndex!=-1;
	}	
	/********************************************************
	 * 														*
	 * 						GETTER-SETTER					*
	 * 														*
	 ********************************************************/
	
	public Vector3f getAverageTangent(){
		return aTangent;
	}
	
	public int getIndex(){
		return i;
	}
	
	public float getLength(){
		return l;
	}
	
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return dupVertex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.dupVertex = duplicateVertex;
	}

}
