package textures;

public class TerrainTextureBundle {
	
	private TextureTerrain bgTex;
	private TextureTerrain redTex;
	private TextureTerrain greenTex;
	private TextureTerrain blueTex;
	
	public TerrainTextureBundle(TextureTerrain bgTex, TextureTerrain redTex,
			TextureTerrain greenTex, TextureTerrain blueTex) {
		this.bgTex = bgTex;
		this.redTex = redTex;
		this.greenTex = greenTex;
		this.blueTex = blueTex;
	}

	public TextureTerrain getBackgroundtexture() {
		return bgTex;
	}

	public TextureTerrain getRedTexture() {
		return redTex;
	}

	public TextureTerrain getGreenTexture() {
		return greenTex;
	}

	public TextureTerrain getBlueTexture() {
		return blueTex;
	}


}
