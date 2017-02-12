package tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {
	
	public static Matrix4f generateTransformationMatrix(Vector2f trans, Vector2f s) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		Matrix4f.translate(trans, mat, mat);
		Matrix4f.scale(new Vector3f(s.x, s.y, 1f), mat, mat);
		return mat;
	}

	public static Matrix4f generateTransformationMatrix(Vector3f trans, float rX, float rY,
			float rZ, float s) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		Matrix4f.translate(trans, mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rX), new Vector3f(1,0,0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rY), new Vector3f(0,1,0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rZ), new Vector3f(0,0,1), mat, mat);
		Matrix4f.scale(new Vector3f(s,s,s), mat, mat);
		return mat;
	}
	
	public static float barryCenter(Vector3f v1, Vector3f v2, Vector3f v3, Vector2f position) {
		float determinant = (v2.z - v3.z) * (v1.x - v3.x) + (v3.x - v2.x) * (v1.z - v3.z);
		float a = ((v2.z - v3.z) * (position.x - v3.x) + (v3.x - v2.x) * (position.y - v3.z)) / determinant;
		float b = ((v3.z - v1.z) * (position.x - v3.x) + (v1.x - v3.x) * (position.y - v3.z)) / determinant;
		float c = 1.0f - a - b;
		return a * v1.y + b * v2.y + c * v3.y;
	}
	
	public static Matrix4f generateViewMatrix(Camera cam) {
		Matrix4f view = new Matrix4f();
		view.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(cam.getPitch()), new Vector3f(1, 0, 0), view,
				view);
		Matrix4f.rotate((float) Math.toRadians(cam.getYaw()), new Vector3f(0, 1, 0), view, view);
		Vector3f cameraPosition = cam.getPosition();
		Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x,-cameraPosition.y,-cameraPosition.z);
		Matrix4f.translate(negativeCameraPosition, view, view);
		return view;
	}

}
