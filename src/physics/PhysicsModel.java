package physics;

import javax.sound.midi.ControllerEventListener;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;

public class PhysicsModel {

	private final RigidBody physicsBody;
	private boolean applyForce;
	private MotionState bodyMotion;
	private CollisionShape collisionShape;
	
	
		
	public PhysicsModel(RigidBody physicsBody, MotionState bodyMotion) {
		
		this.physicsBody = physicsBody;
		this.bodyMotion = bodyMotion;
	}


	public boolean isApplyForce() {
		return applyForce;
	}


	public void setApplyForce(boolean applyForce) {
		this.applyForce = applyForce;
	}


	public RigidBody getPhysicsBody() {
		return physicsBody;
	}


	public CollisionShape getCollisionShape() {
		return collisionShape;
	}


	public void setCollisionShape(CollisionShape collisionShape) {
		this.collisionShape = collisionShape;
	}


	public MotionState getBodyMotion() {
		return bodyMotion;
	}

	
}
