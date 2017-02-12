package models;

import textures.TextureModel;

public class TexturedModel
{
	// declare variables
	private RawModel rModel;
	private TextureModel modTex;

	// Constructor
	public TexturedModel(RawModel model, TextureModel texture)
	{
		this.rModel = model;
		this.modTex = texture;
	}

	/********************************************************
	 * 														*
	 * 						GETTER							*
	 * 														*
	 ********************************************************/
	
	public RawModel getRawModel()
	{
		return rModel;
	}

	public TextureModel getTexture()
	{
		return modTex;
	}
}
