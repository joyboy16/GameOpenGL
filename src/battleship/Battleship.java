package battleship;
/*
 * @aurthor : Diagnat Barai
 * 			  Julius 
 * */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import audio.AudioMaster;
import audio.Source;
import entities.Camera;
import entities.Entity;
import entities.Light;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import normalMappingObjLoader.NormalMappedObjLoader;
import objLoader.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.TerrainTextureBundle;
import textures.TextureModel;
import textures.TextureTerrain;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class Battleship {

	public static void main(String[] args) {
		
		/********************************************************
		 * 														*
		 * 						Variables						*
		 * 														*
		 ********************************************************/

		// create DisplayManager
		DisplayManager.createDisplay();
		// create new Loader
		Loader loader = new Loader();		
		
		// ship start
		Vector3f shipPositionStart = new Vector3f(150, -9.5f, -170);
		
		/********************************************************
		 * 														*
		 * 						ENTITIES						*
		 * 														*
		 ********************************************************/		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		// cannon
		TexturedModel canonModel = new TexturedModel(OBJFileLoader.loadOBJ("simpleCanon", loader), new TextureModel(loader.loadTexture("canonTexture")));
		Entity canon = new Entity(canonModel, new Vector3f(135, -10.5f, -110), 0, 0, 0, 0.01f);
		entities.add(canon);
		
		// ship
		TexturedModel shipModel = new TexturedModel(OBJFileLoader.loadOBJ("ship", loader), new TextureModel(loader.loadTexture("grey")));
		Entity ship = new Entity(shipModel, shipPositionStart, 0, 180, -2, 0.02f);
		entities.add(ship);
		
		// new camera
		Camera camera = new Camera(canon);

		// create MasterRenderer and init the particles
		MasterRenderer renderer = new MasterRenderer(loader,camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		TextureTerrain backgroundTexture = new TextureTerrain(loader.loadTexture("grassy2"));
		TextureTerrain rTexture = new TextureTerrain(loader.loadTexture("mud"));
		TextureTerrain gTexture = new TextureTerrain(loader.loadTexture("grassFlowers"));
		TextureTerrain bTexture = new TextureTerrain(loader.loadTexture("path"));

		TerrainTextureBundle texturePack = new TerrainTextureBundle(backgroundTexture, rTexture,
				gTexture, bTexture);
		TextureTerrain blendMap = new TextureTerrain(loader.loadTexture("blendMap"));

		TextureModel fernTextureAtlas = new TextureModel(loader.loadTexture("fern"));
		fernTextureAtlas.setNumRows(2);

		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
				fernTextureAtlas);

		TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
				new TextureModel(loader.loadTexture("pine")));
		bobble.getTexture().setIsTransparent(true);

		fern.getTexture().setIsTransparent(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "map");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);

		TexturedModel cannonBall = new TexturedModel(OBJFileLoader.loadOBJ("canonBall", loader),
				new TextureModel(loader.loadTexture("white")));
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new TextureModel(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new TextureModel(loader.loadTexture("crate")));
		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
		crateModel.getTexture().setShineDamper(10);
		crateModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new TextureModel(loader.loadTexture("boulder")));
		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
		boulderModel.getTexture().setShineDamper(10);
		boulderModel.getTexture().setReflectivity(0.5f);
		
		
		//************ENTITIES*******************
		
		Entity barrel = new Entity(barrelModel, new Vector3f(125, -10.5f, -115), 67, 0, 60, 0.3f);
		Entity boulder = new Entity(boulderModel, new Vector3f(145, -11.5f, -115), 0, 90, -20, 0.5f);
		Entity crate = new Entity(crateModel, new Vector3f(150, -10.5f, -170), 40, 60, 30, 0.02f);
		normalMapEntities.add(barrel);
		normalMapEntities.add(boulder);
		normalMapEntities.add(crate);
		
				
		Random random = new Random(5);
		float minHeight = -12;
		for (int i = 0; i < 300; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 300;
				float z = random.nextFloat() * -150 - 150;
				float y = terrain.getHeightOfTerrain(x, z);
				if (y <= minHeight) {
				} else {
					entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, 0.9f));
				}
			}
			if (i % 2 == 0) {

				float x = random.nextFloat() * 300;
				float z = random.nextFloat() * -150 - 150;
				float y = terrain.getHeightOfTerrain(x, z);
				if (y <= minHeight) {
				} else {					
					entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
				}
			}
		}
		
		
		/********************************************************
		 * 														*
		 * 						AUDIO							*
		 * 														*
		 ********************************************************/
		
		// Initialize AudioMaster and set the Listenerposition
		AudioMaster.init();
		AudioMaster.setListenerData(canon.getPosition());
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
		
		// Define all necessary sounds
		int waterSound = AudioMaster.loadSound("audio/water.wav");
		int birdSound = AudioMaster.loadSound("audio/birds.wav");
		int boatSound = AudioMaster.loadSound("audio/motorboat.wav");
		int shotSound = AudioMaster.loadSound("audio/canon.wav");
		int hitSound = AudioMaster.loadSound("audio/hit.wav");
		
		// Create Sources and specify the values of each Sound
		Source waterSource = new Source();
		waterSource.setLoop(true);
		waterSource.setPosition(new Vector3f(150, 0, -150));
		waterSource.setVelocity(new Vector3f(0, 0, 0));
		waterSource.setVolume(1);
		waterSource.setPitch(1);
		waterSource.setRolloff(0);
		waterSource.setReferenceDistance(1);
		waterSource.setMaxDistance(1000);		
		waterSource.play(waterSound);
		
		Source birdSource = new Source();
		birdSource.setLoop(true);
		birdSource.setPosition(new Vector3f(150, 0, -150));
		birdSource.setVelocity(new Vector3f(0, 0, 0));
		birdSource.setVolume(1);
		birdSource.setPitch(1);
		birdSource.setRolloff(0);
		birdSource.setReferenceDistance(1);
		birdSource.setMaxDistance(1000);
		birdSource.play(birdSound);
		
		Source boatSource = new Source();
		boatSource.setLoop(true);
		boatSource.setPosition(ship.getPosition());
		boatSource.setVelocity(new Vector3f(0, 0, 0));
		boatSource.setVolume(0.5f);
		boatSource.setPitch(1);
		boatSource.setRolloff(2);
		boatSource.setReferenceDistance(20);
		boatSource.setMaxDistance(1000);
		boatSource.play(boatSound);
		
		Source canonSource = new Source();
		canonSource.setLoop(false);
		canonSource.setPosition(canon.getPosition());
		canonSource.setVelocity(new Vector3f(0, 0, 0));
		canonSource.setVolume(1);
		canonSource.setPitch(1);
		canonSource.setRolloff(1);
		canonSource.setReferenceDistance(20);
		canonSource.setMaxDistance(1000);
		
		Source hitSource = new Source();
		hitSource.setLoop(false);
		hitSource.setPosition(new Vector3f(0, 0, 0));
		hitSource.setVelocity(new Vector3f(0, 0, 0));
		hitSource.setVolume(10);
		hitSource.setPitch(1);
		hitSource.setRolloff(5);
		hitSource.setReferenceDistance(20);
		hitSource.setMaxDistance(1000);
		
		
		
		/********************************************************
		 * 														*
		 * 						LIGHT							*
		 * 														*
		 ********************************************************/

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(-100, 1500000, 1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);
		
		/********************************************************
		 * 														*
		 * 						GUI								*
		 * 														*
		 ********************************************************/
		
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		
		GuiTexture health = new GuiTexture(loader.loadTexture("health_full"), new Vector2f(0.75f, 0.9f), new Vector2f(0.5f*0.3f, 0.2f*0.3f));
		guiTextures.add(health);
		GuiTexture title = new GuiTexture(loader.loadTexture("title"), new Vector2f(-0.65f, -0.9f), new Vector2f(0.30f, 0.30f));
		guiTextures.add(title);
		GuiTexture win = null;
				
		GuiRenderer guiRenderer = new GuiRenderer(loader);
	
		/********************************************************
		 * 														*
		 * 						WATER							*
		 * 														*
		 ********************************************************/
		
		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(150, -150, -10);
		waters.add(water);
		
		/********************************************************
		 * 														*
		 * 						PARTICLES						*
		 * 														*
		 ********************************************************/
		
		ParticleTexture smokeTexture = new ParticleTexture(loader.loadTexture("smoke"), 4,false);
		ParticleSystem smokeParticle = new ParticleSystem(smokeTexture, 800, 12, 0.5f, 1, 0.6f);
		
		ParticleTexture fireTexture = new ParticleTexture(loader.loadTexture("fire"), 7,false);
		ParticleSystem fireParticle = new ParticleSystem(fireTexture, 100, 20, 0.5f, 1, 3.6f);		 
		
		smokeParticle.setLifeError(0.5f);
		smokeParticle.setScaleError(0.15f);
		smokeParticle.setSpeedError(0.125f);
		smokeParticle.randomizeRotation();		
		smokeParticle.setDirection(new Vector3f(0, 1, 0), 0.01f);

		fireParticle.setLifeError(0.5f);
		fireParticle.setScaleError(0.15f);
		fireParticle.setSpeedError(0.125f);
		fireParticle.randomizeRotation();		
		fireParticle.setDirection(new Vector3f(0, 1, 0), 0.01f);
		
		/********************************************************
		 * 														*
		 * 						GAME-VARIABLES					*
		 * 														*
		 ********************************************************/

		Entity cb = null;
		
		// phi
		double phi = 0;
		boolean shot = false;
		boolean launched = false;
		Vector3f speed = new Vector3f(0,0,0);
		int numberOfBullets = 0;
		float boatRadius = 30;		
		int enemyLives = 3;
		boolean defeated = false;
		float delta = 0;
		float moveSpeed = 2f;
		
		/********************************************************
		 * 														*
		 * 						GAME-LOOP						*
		 * 														*
		 ********************************************************/
		
		while (!Display.isCloseRequested())
		{
			camera.updatePosition();
			delta = DisplayManager.delta();
			
			/**
			 * 	Audio update
			 * 
			 */
			
			boatSource.setPosition(ship.getPosition());			
			
			/**
			 * 	Game logics
			 * 
			 */
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				break;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_A) && canon.getRotY() < 45){
				canon.increaseRotation(0, 80f * delta, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D) && canon.getRotY() > -45){
				canon.increaseRotation(0, -80f * delta, 0);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !shot){
				if(numberOfBullets < 1)
				{
					cb = new Entity(cannonBall, 1, new Vector3f(canon.getPosition().x,canon.getPosition().y,canon.getPosition().z), 0, 0, 0, 0.008f);
					numberOfBullets++;
					entities.add(cb);
					shot = !shot;
					launched = true;
					speed = new Vector3f((float)-Math.sin(Math.toRadians(canon.getRotY()))*200,20,(float)-Math.cos(Math.toRadians(canon.getRotY()))*200);
					canonSource.play(shotSound);
				}
			}
			if(launched && cb.getPosition().y < -12)
			{
				entities.remove(entities.size()-numberOfBullets);
				numberOfBullets--;
				if(numberOfBullets == 0)
				{
					cb = null;
					launched = false;
				}
			}
			if(launched)
			{				
				cb.increasePosition(speed.x*delta, speed.y*delta, speed.z*delta);
				speed.y = speed.y - 9.81f*delta*10;
				// Collision detection
				float dist =(cb.getPosition().x - shipPositionStart.x)*(cb.getPosition().x - shipPositionStart.x) +
							(cb.getPosition().y - shipPositionStart.y)*(cb.getPosition().y - shipPositionStart.y) +
							(cb.getPosition().z - shipPositionStart.z)*(cb.getPosition().z - shipPositionStart.z);
				if(cb.getPosition().y < 8 && dist < (boatRadius+10)*(boatRadius+10) && intersect(prepareAABB(ship), ship, cb))
				{					
					if(enemyLives >= 1) enemyLives--;
					if(enemyLives == 0)
					{
						boatSource.pause();
						win = new GuiTexture(loader.loadTexture("win"), new Vector2f(-0.005f, -0.2f), new Vector2f(0.5f, 0.5f));
						guiTextures.add(win);
						defeated = true;
					}
					health.setTexture(loader.loadTexture("health_"+enemyLives));
					hitSource.setPosition(cb.getPosition());
					hitSource.play(hitSound);
					entities.remove(entities.size()-numberOfBullets);
					numberOfBullets--;
					if(numberOfBullets == 0)
					{
						cb = null;
						launched = false;
					}
				}
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && shot)
			{
				shot = !shot;
				canonSource.setPosition(canon.getPosition());
				smokeParticle.generateParticles(new Vector3f(canon.getPosition().x,canon.getPosition().y+2.8f,canon.getPosition().z+1.0f));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_N)){
				canon.setPosition(new Vector3f(135, -10.5f, -110));
				canon.setRotY(0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_L)){
				DisplayManager.setMaxFPS(10);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_H)){
				DisplayManager.setMaxFPS(60);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_M)){
				DisplayManager.setMaxFPS(30);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_R)){
				//Resetting every
				defeated = false;
				enemyLives = 3;
				health.setTexture(loader.loadTexture("health_"+enemyLives));
				boatSource.resume();
				phi = 0;
				if(win != null)
				{
					guiTextures.remove(win);
					
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				canon.increasePosition(0, 0, -moveSpeed);
				AudioMaster.setListenerData(canon.getPosition());
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				canon.increasePosition(0, 0, moveSpeed);
				AudioMaster.setListenerData(canon.getPosition());
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				canon.increasePosition(-moveSpeed, 0, 0);
				AudioMaster.setListenerData(canon.getPosition());
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				canon.increasePosition(moveSpeed, 0, 0);
				AudioMaster.setListenerData(canon.getPosition());
			}
			
			phi += 0.8f * delta;
			phi %= 360;
			if(!defeated)
			{
				// ship movement
				ship.setRotY((float)-phi*57.3f);
				ship.setPosition(new Vector3f(
						shipPositionStart.x + (float)Math.cos(phi) * 30,
						shipPositionStart.y,
						shipPositionStart.z + (float)Math.sin(phi) * 30));
			}
			else fireParticle.generateParticles(ship.getPosition());
			
			crate.setPosition(new Vector3f(shipPositionStart.x, (shipPositionStart.y - 1.75f) + (float)Math.cos(phi*3)*0.5f, shipPositionStart.z));
			
			ParticleMaster.update(camera);
			
			renderer.renderShadowMap(entities, sun);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//render reflection texture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -waters.get(0).getHeight() + 1));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			//render refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, waters.get(0).getHeight() + 0.2f));
			
			//render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();	
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));	
			waterRenderer.render(waters, camera, sun);
			
			ParticleMaster.renderParticles(camera);
			
			guiRenderer.renderGUI(guiTextures);
			
			DisplayManager.updateDisplay();
		}

		/********************************************************
		 * 														*
		 * 						CLEAN-UP						*
		 * 														*
		 ********************************************************/
		
		birdSource.delete();
		canonSource.delete();
		hitSource.delete();
		boatSource.delete();
		waterSource.delete();
		AudioMaster.cleanUP();
		ParticleMaster.clean();
		waterShader.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		buffers.cleanUp();
		guiRenderer.clean();
		DisplayManager.closeDisplay();

	}
	
	/********************************************************
	 * 														*
	 * 						COLLISION						*
	 * 														*
	 ********************************************************/
	public static boolean intersect(Vector3f[] AABB, Entity target, Entity sphere)
	{
		// Calculate position of rotated sphere
		float sX = sphere.getPosition().x;
		float sY = sphere.getPosition().y;
		float sZ = sphere.getPosition().z;
		
		float tX = target.getPosition().x;
		float tZ = target.getPosition().z;
		
		float phi = target.getRotY();
		
		float sX_ = (sX - tX)*(float)Math.cos(Math.toRadians(phi)) - (sZ - tZ)*(float)Math.sin(Math.toRadians(phi)) + tX;
		float sY_ = sY;
		float sZ_ = (sZ - tZ)*(float)Math.cos(Math.toRadians(phi)) + (sX - tX)*(float)Math.sin(Math.toRadians(phi)) + tZ;
		
		// Check if AABB intersects with rSphere
		float dist = 0;
		
		if(sX_ < AABB[0].x) dist += (sX_ - AABB[0].x)*(sX_ - AABB[0].x);
		else if(sX_ > AABB[1].x) dist += (sX_ - AABB[1].x)*(sX_ - AABB[1].x);
		
		if(sY_ < AABB[0].y) dist += (sY_ - AABB[0].y)*(sY_ - AABB[0].y);
		else if(sY_ > AABB[1].y) dist += (sY_ - AABB[1].y)*(sY_ - AABB[1].y);
		
		if(sZ_ < AABB[0].z) dist += (sZ_ - AABB[0].z)*(sZ_ - AABB[0].z);
		else if(sZ_ > AABB[1].z) dist += (sZ_ - AABB[1].z)*(sZ_ - AABB[1].z);
		
		return dist <= 4;
	}
	
	// helper method
	public static Vector3f[] prepareAABB(Entity e)
	{
		float x = e.getPosition().x;
		float y = e.getPosition().y;
		float z = e.getPosition().z;
		Vector3f[] v = {new Vector3f(x-4.1f,y-1.7f,z-10.4f),new Vector3f(x+4.1f,y+1.7f,z+10.4f)};
		return v;
	}
}
