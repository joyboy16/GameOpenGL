package particles;

public class ParticleTexture {

	private int iD;
	private int numRows;
	private boolean additive;
	
	public ParticleTexture(int iD, int numRows, boolean additive) {
		this.iD = iD;
		this.numRows = numRows;
		this.additive = additive;
	}

	public int getTextureID() {
		return iD;
	}

	public int getNumberOfRows() {
		return numRows;
	}
	
	protected boolean useAdditiveBlending() {
		return additive;

	}
}
