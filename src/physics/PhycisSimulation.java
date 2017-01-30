package physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhycisSimulation {
	
	private static DynamicsWorld dynamicsWorld;
	private BroadphaseInterface broadphase;
    private CollisionConfiguration collisionConfiguration;
    private CollisionDispatcher dispatcher;
    private ConstraintSolver solver;
    

	public void setUpPhysics(){
		
	}
	
	public void checkCollisions(float delta){
		
	}
	
	private boolean hasCollision(PhysicsModel modelA, PhysicsModel modelB){
		return false;
	}
}
