package objLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import models.RawModel;
import renderEngine.Loader;

public class OBJFileLoader
{
	// all models are stored in res/
	private static final String resFolderLocation = "res/";

	// load an actual model from file
	public static RawModel loadOBJ(String objName, Loader loader)
	{
		// fileReader to read the obj-file
		FileReader fr = null;
		File objFile = new File(resFolderLocation + objName + ".obj");
		
		// try to init the reader
		try
		{
			fr = new FileReader(objFile);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Datei " + objName + ".obj konnte nicht gefunden werden in res/" + objName + ".");
		}
		String line = null;
		
		// bufferedReader for storing information
		BufferedReader br = new BufferedReader(fr);
		
		// lists for vertices, textureCoords, normals and indices
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		// try reading the file
		try
		{
			// we are not done yet
			boolean done = false;
			while (!done)
			{
				// read every line
				line = br.readLine();
				String[] curr = line.split(" ");
				// what is the line start?
				switch(curr[0])
				{
				case "v":	Vector3f v = new Vector3f((float) Float.valueOf(curr[1]), (float) Float.valueOf(curr[2]), (float) Float.valueOf(curr[3]));
							vertices.add(new Vertex(vertices.size(), v));
							break;
				case "vt":	Vector2f t = new Vector2f((float) Float.valueOf(curr[1]),	(float) Float.valueOf(curr[2]));
							textures.add(t);
							break;
				case "vn":	Vector3f n = new Vector3f((float) Float.valueOf(curr[1]), (float) Float.valueOf(curr[2]), (float) Float.valueOf(curr[3]));
							normals.add(n);
							break;
				case "f":	done = true;
				}
			}
			
			// read and process the faces
			while (line != null && line.startsWith("f "))
			{
				String[] curr = line.split(" ");
				
				// each face has 3 vertices
				Vertex v0 = processVertex(curr[1].split("/"), vertices, indices);
				Vertex v1 = processVertex(curr[2].split("/"), vertices, indices);
				Vertex v2 = processVertex(curr[3].split("/"), vertices, indices);
				calculateTangents(v0, v1, v2, textures);
				line = br.readLine();
			}
			// close the bufferedReader
			br.close();
		}
		catch (IOException e)
		{
			System.err.println("Datei konnte nicht gelesen werden!");
		}
		// delete not used vertices
		deleteNotUsedVertices(vertices);
		
		// prepare vertices, textureCoords, normals and indices
		float[] vertArray = new float[vertices.size() * 3];
		float[] texArray = new float[vertices.size() * 2];
		float[] normArray = new float[vertices.size() * 3];
		float[] tangentsArray = new float[vertices.size() * 3];
		float farest = convertDataToArrays(vertices, textures, normals, vertArray, texArray, normArray, tangentsArray);
		int[] indicesArray = convertIndicesListToArray(indices);
		
		//return model
		return loader.loadToVAO(vertArray, texArray, normArray, indicesArray);
	}

	private static Vertex processVertex(String[] vert, List<Vertex> vertices, List<Integer> indices)
	{
		// index
		int i = Integer.parseInt(vert[0]) - 1;
		
		// get current vert
		Vertex v = vertices.get(i);
		
		// we need textureIndex and normalIndex
		int texIndex = Integer.parseInt(vert[1]) - 1;
		int normIndex = Integer.parseInt(vert[2]) - 1;
		
		// vert has no index
		if (!v.isSet())
		{
			// set indices
			v.setTextureIndex(texIndex);
			v.setNormalIndex(normIndex);
			indices.add(i);
			return v;
		}
		else
		{
			// process vertex
			return processedVertex(v, texIndex, normIndex, indices,	vertices);
		}
	}

	private static int[] convertIndicesListToArray(List<Integer> indices)
	{
		// List -> Array
		int[] indArray = new int[indices.size()];
		for (int i = 0; i <= indArray.length -1; i++) indArray[i] = indices.get(i);
		return indArray;
	}
	
	private static void deleteNotUsedVertices(List<Vertex> vertices)
	{
		// get rid of all not used vertices
		for (Vertex vertex : vertices)
		{
			// average
			vertex.averageTangents();
			// do we need to set?
			if (!vertex.isSet())
			{
				// delete
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}

	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures, List<Vector3f> normals, float[] verticesArray, float[] texturesArray, float[] normalsArray, float[] tangentsArray)
	{
		float farest = 0;
		for (int i = 0; i <= vertices.size()-1; i++)
		{
			// get vertex from list
			Vertex curr = vertices.get(i);
			
			// new length
			if (curr.getLength()-1 >= farest) farest = curr.getLength();
			
			//variables
			Vector3f position = curr.getPosition();
			Vector2f textureCoord = textures.get(curr.getTextureIndex());
			Vector3f normalVector = normals.get(curr.getNormalIndex());
			Vector3f tangent = curr.getAverageTangent();
			
			// fill arrays
			int dim3 = 3;
			int dim2 = 2;
			verticesArray[i * dim3] = position.x;
			verticesArray[i * dim3 + 1] = position.y;
			verticesArray[i * dim3 + 2] = position.z;
			
			texturesArray[i * dim2] = textureCoord.x;
			texturesArray[i * dim2 + 1] = 1 - textureCoord.y; //invert y
			
			normalsArray[i * dim3] = normalVector.x;
			normalsArray[i * dim3 + 1] = normalVector.y;
			normalsArray[i * dim3 + 2] = normalVector.z;
			
			tangentsArray[i * dim3] = tangent.x;
			tangentsArray[i * dim3 + 1] = tangent.y;
			tangentsArray[i * dim3 + 2] = tangent.z;

		}
		
		//return length
		return farest;
	}
	
	private static void calculateTangents(Vertex v0, Vertex v1, Vertex v2, List<Vector2f> textures)
	{
		// calculate the deltas
		Vector3f delatPosition1 = Vector3f.sub(v1.getPosition(), v0.getPosition(), null);
		Vector3f delatPosition2 = Vector3f.sub(v2.getPosition(), v0.getPosition(), null);
		
		// and uvs
		Vector2f deltaUv1 = Vector2f.sub(textures.get(v1.getTextureIndex()), textures.get(v0.getTextureIndex()), null);
		Vector2f deltaUv2 = Vector2f.sub(textures.get(v2.getTextureIndex()), textures.get(v0.getTextureIndex()), null);

		delatPosition1.scale(deltaUv2.y);
		delatPosition2.scale(deltaUv1.y);
		
		// tangent
		Vector3f t = (Vector3f.sub(delatPosition1, delatPosition2, null));
		t.scale(1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x));
		
		// we need the tangent of the vertices
		v0.addTangent(t);
		v1.addTangent(t);
		v2.addTangent(t);
	}

	private static Vertex processedVertex(Vertex prev, int newTextureIndex, int newNormalIndex, List<Integer> indices, List<Vertex> vertices)
	{
		// when they have the same
		if (prev.hasSameTextureAndNormal(newTextureIndex, newNormalIndex))
		{
			// add prev and return
			indices.add(prev.getIndex());
			return prev;
		}
		else
		{
			// else we need a new vertex
			Vertex newVertex = prev.getDuplicateVertex();
			// duplicated
			if (newVertex != null) return processedVertex(newVertex, newTextureIndex, newNormalIndex, indices, vertices);
			else
			{
				// else set the new vertex up
				Vertex duplicateVertex = new Vertex(vertices.size(), prev.getPosition());
				duplicateVertex.setNormalIndex(newNormalIndex);
				duplicateVertex.setTextureIndex(newTextureIndex);
				prev.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				// and return it
				return duplicateVertex;
			}
		}
	}
}