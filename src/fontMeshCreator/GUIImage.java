package fontMeshCreator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;



public class GUIImage {

	
	public static void render(String file){
		try {
			Texture texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + file
					+ ".png"));
			texture.bind(); // or GL11.glBind(texture.getTextureID());
			
			GL11.glBegin(GL11.GL_QUADS);
			    GL11.glTexCoord2f(0,0);
			    GL11.glVertex2f(100,100);
			    GL11.glTexCoord2f(1,0);
			    GL11.glVertex2f(100+texture.getTextureWidth(),100);
			    GL11.glTexCoord2f(1,1);
			    GL11.glVertex2f(100+texture.getTextureWidth(),100+texture.getTextureHeight());
			    GL11.glTexCoord2f(0,1);
			    GL11.glVertex2f(100,100+texture.getTextureHeight());
			GL11.glEnd();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

