package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromSource = 5;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 15;
	private float yaw = 0;
	private float roll;
	
	private Entity entity;
	
	
	public Camera(Entity entity){
		this.entity = entity;
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateLimitedCameraPosition(horizontalDistance, verticalDistance);
		//this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		this.yaw = -entity.getRotY();
		yaw%=360;
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
	
	private void calculateLimitedCameraPosition(float horizDistance, float verticDistance){
		float theta = entity.getRotY();
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = entity.getPosition().x + offsetX;
		position.z = entity.getPosition().z + offsetZ;
		position.y = entity.getPosition().y + verticDistance + 0.5f;
	}
	
	private float calculateHorizontalDistance(){
		return (float) ((distanceFromSource) * Math.cos(Math.toRadians(pitch+45)));
	}
	
	private float calculateVerticalDistance(){
		return (float) ((distanceFromSource + 5) * Math.sin(Math.toRadians(pitch+4)));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.03f;
		distanceFromSource -= zoomLevel;
		if(distanceFromSource<5){
			distanceFromSource = 5;
		}
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.2f;
			pitch -= pitchChange;
			if(pitch < 0){
				pitch = 0;
			}else if(pitch > 90){
				pitch = 90;
			}
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.2f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	
	

}
