package textures;

import java.nio.ByteBuffer;

public class DataTexture {
	
	private int width;
	private int height;
	private ByteBuffer buff;
	
	public DataTexture(ByteBuffer buff, int width, int height){
		this.buff = buff;
		this.width = width;
		this.height = height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public ByteBuffer getBuffer(){
		return buff;
	}

}
