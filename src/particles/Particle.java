package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import renderEngine.DisplayManager;

public class Particle {

	private Vector3f pos;
	private Vector3f velocity;
	private float gravity;
	private float life;
	private float rot;
	private float scale;
	
	private ParticleTexture particletexture;
	
	private Vector2f textureOffset1 = new Vector2f();
	private Vector2f textureOffset2 = new Vector2f();
	private float blendFactor; 
	
	private float timeElapsed = 0;
	private float dist;
	
	private Vector3f reuse = new Vector3f();
	
	private boolean alive = false;

	public Particle(ParticleTexture particletexture, Vector3f pos, Vector3f velocity, float gravity, float life, float rot,
			float scale) {
		alive = true;
		this.particletexture = particletexture;
		this.pos = pos;
		this.velocity = velocity;
		this.gravity = gravity;
		this.life = life;
		this.rot = rot;
		this.scale = scale;
		ParticleMaster.addParticles(this);
	}

	
	public float getDistance() {
		return dist;
	}

	public Vector3f getPosition() {
		return pos;
	}

	public float getRotation() {
		return rot;
	}

	public float getScale() {
		return scale;
	}
	
	public ParticleTexture getTexture() {
		return particletexture;
	}
	
	
	public Vector2f getTexOffset1() {
		return textureOffset1;
	}

	public Vector2f getTexOffset2() {
		return textureOffset2;
	}

	public float getBlend() {
		return blendFactor;
	}

	protected boolean updateParticle(Camera cam){
		velocity.y += Entity.GRAVITY * gravity * DisplayManager.getFrameTimeSeconds();
		reuse.set(velocity);
		reuse.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reuse, pos, pos);
		updateTextureCoordinates();
		dist = Vector3f.sub(cam.getPosition(), pos, null).lengthSquared();
		timeElapsed += DisplayManager.getFrameTimeSeconds();
		return timeElapsed < life;
	}
	
	private void updateTextureCoordinates(){
		float life_factor = timeElapsed / life;
		int stage = particletexture.getNumberOfRows() * particletexture.getNumberOfRows();
		float atlas_progress = life_factor * stage;
		int idx1 = (int)Math.floor(atlas_progress);
		int idx2 = idx1 < stage -1 ? idx1 +1 : idx1;
		this.blendFactor = atlas_progress % 1; 
		setTextureOffset(textureOffset1,idx1);
		setTextureOffset(textureOffset2,idx2);
		
	}
	
	private void setTextureOffset(Vector2f offset, int idx){
		int col = idx % particletexture.getNumberOfRows();
		int row = idx / particletexture.getNumberOfRows();
		offset.x = (float) col / particletexture.getNumberOfRows();
		offset.y = (float) row / particletexture.getNumberOfRows();
	}
}
