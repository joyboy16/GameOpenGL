package textures;

public class TextureModel {
	
	private int texID;
	private int normalMap;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean isTransparent = false;
	private boolean useLighting = false;
	
	private int numRows = 1;
	
	public TextureModel(int texture){
		this.texID = texture;
	}
		
	public int getnumRows() {
		return numRows;
	}

	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public boolean isuseLighting() {
		return useLighting;
	}


	public void setUseLighting(boolean useLighting) {
		this.useLighting = useLighting;
	}

	public void setIsTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
	}


	public int getID(){
		return texID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
