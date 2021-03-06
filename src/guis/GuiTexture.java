package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
	
	private int texture;
	private Vector2f pos;
	private Vector2f scale;
	
	public GuiTexture(int texture, Vector2f pos, Vector2f scale) {
		this.texture = texture;
		this.pos = pos;
		this.scale = scale;
	}

	public int getTexture() {
		return texture;
	}
	
	public void setTexture(int id) {
		texture = id;
	}

	public Vector2f getPosition() {
		return pos;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	

}
