package particles;
 
import java.util.Random;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
 
import renderEngine.DisplayManager;
 
public class ParticleSystem {
 
    private float pps, avgSpeed, gravity, avgLife, avgScale;
 
    private float speedEr, lifeEr, scaleEr = 0;
    private boolean randomRotation = false;
    private Vector3f dir;
    private float dirDeviation = 0;
 
    private ParticleTexture particleTexture;
    
    private Random random = new Random();
 
    public ParticleSystem(ParticleTexture particleTexture, float pps, float speed, float gravity, float avgLife, float scale) {
        this.particleTexture = particleTexture;
    	this.pps = pps;
        this.avgSpeed = speed;
        this.gravity = gravity;
        this.avgLife = avgLife;
        this.avgScale = scale;
    }
 
    public void setDirection(Vector3f dir, float dev) {
        this.dir = new Vector3f(dir);
        this.dirDeviation = (float) (dev * Math.PI);
    }
 
    public void randomizeRotation() {
        randomRotation = true;
    }
 
    public void setSpeedError(float error) {
        this.speedEr = error * avgSpeed;
    }
 
    public void setLifeError(float error) {
        this.lifeEr = error * avgLife;
    }
 
     
    public void setScaleError(float error) {
        this.scaleEr = error * avgScale;
    }
 
    public void generateParticles(Vector3f center) {
        float delta = DisplayManager.getFrameTimeSeconds();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for (int i = 0; i < count; i++) {
            emitParticle(center);
        }
        if (Math.random() < partialParticle) {
            emitParticle(center);
        }
    }
 
    private void emitParticle(Vector3f center) {
        Vector3f velocity = null;
        if(dir!=null){
            velocity = generateRandomUnitVectorWithinCone(dir, dirDeviation);
        }else{
            velocity = generateRandomUnitVector();
        }
        velocity.normalise();
        velocity.scale(generateValue(avgSpeed, speedEr));
        float scale = generateValue(avgScale, scaleEr);
        float lifeLength = generateValue(avgLife, lifeEr);
        new Particle(particleTexture, new Vector3f(center), velocity, gravity, lifeLength, generateRotation(), scale);
    }
 
    private float generateValue(float avg, float errorMargin) {
        float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
        return avg + offset;
    }
 
    private float generateRotation() {
        if (randomRotation) {
            return random.nextFloat() * 360f;
        } else {
            return 0;
        }
    }
 
    private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDir, float angle) {
        float cosAngle = (float) Math.cos(angle);
        Random random = new Random();
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
 
        Vector4f dir = new Vector4f(x, y, z, 1);
        if (coneDir.x != 0 || coneDir.y != 0 || (coneDir.z != 1 && coneDir.z != -1)) {
            Vector3f rotateAxis = Vector3f.cross(coneDir, new Vector3f(0, 0, 1), null);
            rotateAxis.normalise();
            float rotateAngle = (float) Math.acos(Vector3f.dot(coneDir, new Vector3f(0, 0, 1)));
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Matrix4f.transform(rotationMatrix, dir, dir);
        } else if (coneDir.z == -1) {
            dir.z *= -1;
        }
        return new Vector3f(dir);
    }
     
    private Vector3f generateRandomUnitVector() {
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = (random.nextFloat() * 2) - 1;
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
        return new Vector3f(x, y, z);
    }
 
}