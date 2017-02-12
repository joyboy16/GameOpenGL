package shadows;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;

public class ShadowBox {

	private static final float OFFSET = 15;
	private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
	private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);
	private static final float SHADOW_DIST = 150;

	private float min_X, max_X;
	private float min_Y, max_Y;
	private float min_Z, max_Z;
	private Matrix4f lightView;
	private Camera cam;

	private float farH, farW, nearH, nearW;

	protected ShadowBox(Matrix4f lightView, Camera cam) {
		this.lightView = lightView;
		this.cam = cam;
		calculateWidthHeight();
	}

	protected void update() {
		Matrix4f rotation = calculateCameraRotation();
		Vector3f forwardV = new Vector3f(Matrix4f.transform(rotation, FORWARD, null));

		Vector3f toFar = new Vector3f(forwardV);
		toFar.scale(SHADOW_DIST);
		Vector3f toNear = new Vector3f(forwardV);
		toNear.scale(MasterRenderer.NEAR_PLANE);
		Vector3f center_Near = Vector3f.add(toNear, cam.getPosition(), null);
		Vector3f center_Far = Vector3f.add(toFar, cam.getPosition(), null);

		Vector4f[] points = calculateFrustumVertices(rotation, forwardV, center_Near,
				center_Far);

		boolean first = true;
		for (Vector4f point : points) {
			if (first) {
				min_X = point.x;
				max_X = point.x;
				min_Y = point.y;
				max_Y = point.y;
				min_Z = point.z;
				max_Z = point.z;
				first = false;
				continue;
			}
			if (point.x > max_X) {
				max_X = point.x;
			} else if (point.x < min_X) {
				min_X = point.x;
			}
			if (point.y > max_Y) {
				max_Y = point.y;
			} else if (point.y < min_Y) {
				min_Y = point.y;
			}
			if (point.z > max_Z) {
				max_Z = point.z;
			} else if (point.z < min_Z) {
				min_Z = point.z;
			}
		}
		max_Z += OFFSET;

	}

	protected Vector3f getCenter() {
		float x = (min_X + max_X) / 2f;
		float y = (min_Y + max_Y) / 2f;
		float z = (min_Z + max_Z) / 2f;
		Vector4f center = new Vector4f(x, y, z, 1);
		Matrix4f invertedLight = new Matrix4f();
		Matrix4f.invert(lightView, invertedLight);
		return new Vector3f(Matrix4f.transform(invertedLight, center, null));
	}


	protected float getWidth() {
		return max_X - min_X;
	}

	protected float getHeight() {
		return max_Y - min_Y;
	}

	protected float getLength() {
		return max_Z - min_Z;
	}

	private Vector4f[] calculateFrustumVertices(Matrix4f rot, Vector3f forward,
			Vector3f centerN, Vector3f centerF) {
		Vector3f up = new Vector3f(Matrix4f.transform(rot, UP, null));
		Vector3f right = Vector3f.cross(forward, up, null);
		Vector3f down = new Vector3f(-up.x, -up.y, -up.z);
		Vector3f left = new Vector3f(-right.x, -right.y, -right.z);
		Vector3f farTop = Vector3f.add(centerF, new Vector3f(up.x * farH,
				up.y * farH, up.z * farH), null);
		Vector3f farBottom = Vector3f.add(centerF, new Vector3f(down.x * farH,
				down.y * farH, down.z * farH), null);
		Vector3f nearTop = Vector3f.add(centerN, new Vector3f(up.x * nearH,
				up.y * nearH, up.z * nearH), null);
		Vector3f nearBottom = Vector3f.add(centerN, new Vector3f(down.x * nearH,
				down.y * nearH, down.z * nearH), null);
		Vector4f[] points = new Vector4f[8];
		points[0] = calculateLightSpaceFrustum(farTop, right, farW);
		points[1] = calculateLightSpaceFrustum(farTop, left, farW);
		points[2] = calculateLightSpaceFrustum(farBottom, right, farW);
		points[3] = calculateLightSpaceFrustum(farBottom, left, farW);
		points[4] = calculateLightSpaceFrustum(nearTop, right, nearW);
		points[5] = calculateLightSpaceFrustum(nearTop, left, nearW);
		points[6] = calculateLightSpaceFrustum(nearBottom, right, nearW);
		points[7] = calculateLightSpaceFrustum(nearBottom, left, nearW);
		return points;
	}

	private Vector4f calculateLightSpaceFrustum(Vector3f start, Vector3f dir,
			float width) {
		Vector3f point = Vector3f.add(start,
				new Vector3f(dir.x * width, dir.y * width, dir.z * width), null);
		Vector4f point4f = new Vector4f(point.x, point.y, point.z, 1f);
		Matrix4f.transform(lightView, point4f, point4f);
		return point4f;
	}

	private Matrix4f calculateCameraRotation() {
		Matrix4f rot = new Matrix4f();
		rot.rotate((float) Math.toRadians(-cam.getYaw()), new Vector3f(0, 1, 0));
		rot.rotate((float) Math.toRadians(-cam.getPitch()), new Vector3f(1, 0, 0));
		return rot;
	}

	private void calculateWidthHeight() {
		farW = (float) (SHADOW_DIST * Math.tan(Math.toRadians(MasterRenderer.FOV)));
		nearW = (float) (MasterRenderer.NEAR_PLANE
				* Math.tan(Math.toRadians(MasterRenderer.FOV)));
		farH = farW / getAspectRatio();
		nearH = nearW / getAspectRatio();
	}

	private float getAspectRatio() {
		return (float) Display.getWidth() / (float) Display.getHeight();
	}

}
