package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleRenderer;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);
		
		TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("person", loader), 
				new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerModel, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
		Camera camera = new Camera(player);
		
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
	//	FontType font = new FontType(loader.loadTexture("harrington"), new File("res/harrington.fnt"));
	//	GUIText text = new GUIText("This is some text!", 3f, font, new Vector2f(0f, 0f), 1f, true);
	//	text.setColour(1, 0, 0);
		
		
		/*TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************

		TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
				new ModelTexture(loader.loadTexture("rocks")));
		
		TexturedModel ship = new TexturedModel(OBJFileLoader.loadOBJ("Creature", loader), 
				new ModelTexture(loader.loadTexture("Creature")));

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);

		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
				fernTextureAtlas);

		TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));
		bobble.getTexture().setHasTransparency(true);

		fern.getTexture().setHasTransparency(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);

		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		//******************NORMAL MAP MODELS************************
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture("crate")));
		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
		crateModel.getTexture().setShineDamper(10);
		crateModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new ModelTexture(loader.loadTexture("boulder")));
		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
		boulderModel.getTexture().setShineDamper(10);
		boulderModel.getTexture().setReflectivity(0.5f);
		
		
		//************ENTITIES*******************
		
		Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
		Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
		Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
		//normalMapEntities.add(entity);
		//normalMapEntities.add(entity2);
		//normalMapEntities.add(entity3);
		
		Random random = new Random(5666778);
		for (int i = 0; i < 60; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
				} else {
					float y = terrain.getHeightOfTerrain(x, z);

				//	entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
					//		random.nextFloat() * 360, 0, 0.9f));
				}
			}
			if (i % 2 == 0) {

				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if ((x > 50 && x < 100) || (z < -50 && z > -100)) {

				} else {
					float y = terrain.getHeightOfTerrain(x, z);
				//	entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0,
					//		random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
				}
			}
		}
		//entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
		//entities.add(new Entity(ship, new Vector3f(75, 4.6f, -75), 0, 0, 0, 1.0f));
		
		*/
		
		// *********TERRAIN TEXTURE STUFF**********
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);

		// ********************MODELS*********************

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
				fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);
		
		TexturedModel pineModel = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader), 
				new ModelTexture(loader.loadTexture("pine")));
		pineModel.getTexture().setHasTransparency(true);
		
		TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
				new ModelTexture(loader.loadTexture("rocks")));


		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		//******************NORMAL MAP MODELS************************
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture("crate")));
		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
		crateModel.getTexture().setShineDamper(10);
		crateModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new ModelTexture(loader.loadTexture("boulder")));
		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
		boulderModel.getTexture().setShineDamper(10);
		boulderModel.getTexture().setReflectivity(0.5f);
		
		
		//************ENTITIES*******************
		
		Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
		Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
		Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
		normalMapEntities.add(entity);
		normalMapEntities.add(entity2);
		normalMapEntities.add(entity3);
		
		Random random = new Random(5666778);
		for (int i = 0; i < 320; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				
				float y = terrain.getHeightOfTerrain(x, z);
				if(y > 0){
					entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, 0.9f));
				}
			}
			if (i % 2 == 0) {

				float x = random.nextFloat() * 150;
				float z = random.nextFloat() * -150;
				if ((x > 50 && x < 100) || (z < -50 && z > -100)) {

				} else {
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(pineModel, 1, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.25f));
				}
			}
		}
		for (int i = 0; i < 90; i++) {
			float x =  random.nextFloat() * 800;
			float z =  random.nextFloat() * -800;
			
			float y = terrain.getHeightOfTerrain(x, z);
			normalMapEntities.add(new Entity(boulderModel, new Vector3f(x, y, z), random.nextFloat() * 360, 0, 0, 0.5f + random.nextFloat()));
		}
		//entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
		
		//*******************OTHER SETUP***************

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(10000, 15000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		entities.add(player);
		List<Entity> shadowEntities = new ArrayList<Entity>();
		shadowEntities.addAll(entities);
		shadowEntities.addAll(normalMapEntities);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), 
				new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		//guiTextures.add(shadowMap);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
	
		//**********Water Renderer Set-up************************
		
		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				waters.add(new WaterTile(i * 160, -j * 160, -2.5f));
			}
		}
		/*WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(75, -75, 0);
		waters.add(water);*/
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4,true);
		
		ParticleSystem system = new ParticleSystem(particleTexture, 155, 25, 0.2f, 2, 1f);	
		system.setLifeError(0.1f);
		system.setDirection(new Vector3f(0, 1, 0), 0.1f);
		system.setSpeedError(0.25f);
		system.setScaleError(0.5f);
		system.randomizeRotation();
		
		Fbo fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		PostProcessing.init(loader);
		
		//****************Game Loop Below*********************

		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			renderer.renderShadowMap(shadowEntities, sun);
			//system.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y+10.0f,player.getPosition().z));
			//system.generateParticles(new Vector3f(200, 10, -100));
			ParticleMaster.update(camera);
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//render reflection texture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, 
					new Vector4f(0, 1, 0, -waters.get(0).getHeight()+1));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			//render refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, 
					new Vector4f(0, -1, 0, waters.get(0).getHeight() + 0.2f));
			
			//render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();	
			
			fbo.bindFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));	
			waterRenderer.render(waters, camera, sun);
			ParticleMaster.renderParticles(camera);
			fbo.unbindFrameBuffer();
			PostProcessing.doPostProcessing(fbo.getColourTexture());
			
			guiRenderer.render(guiTextures);
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}

		//*********Clean Up Below**************
		PostProcessing.cleanUp();
		fbo.cleanUp();
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		buffers.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}


}
