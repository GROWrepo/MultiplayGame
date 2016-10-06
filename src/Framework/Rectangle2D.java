package Framework;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Rectangle2D extends Object2D {
	public final static float[] vertices = {
			0.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
	};
	public final static float[] texture = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
	};
	public final static byte[] indices = {
			0,1,2,
			0,2,3,
	};
	protected int spriteSheet;
	protected float x, y, width, height;
	/** 텍스쳐를 쪼갤 수 없는 오브젝트다 */
	public Rectangle2D(int spriteSheet, float width, float height) {
		init(vertices, texture, indices);
		this.spriteSheet = spriteSheet;
		this.width = width;
		this.height = height;
		this.x = this.y = 0;
	}
	public Rectangle2D(int spriteSheet, float[] texture, float width, float height) {
		init(vertices, texture, indices);
		this.spriteSheet = spriteSheet;
		this.width = width;
		this.height = height;
		this.x = this.y = 0;
	}
	public void draw(GL10 gl) {
		setModelView(gl, width, height, 1f);
		setPosition(gl, x, y, 0f);
		setTexture(gl, 0f, 0f, 0f, spriteSheet); //텍스쳐를 쪼개지 않는다
	}
	public void drawSplit(GL10 gl, float tx, float ty) {
		setModelView(gl, width, height, 1f);
		setPosition(gl, x, y, 0f);
		setTexture(gl, tx, ty, 0f, spriteSheet);
	}
	/** 크기가 줄면 크기에 반비례하게 좌표계가 바뀐다
	 * ex : 크기 0.25 최대 x, y값은 4 */
	public void move(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/** 크기가 줄어도 좌표계가 그대로 유지되는 좌표설정 */
	public void moveReal(float x, float y) {
		//width는 소수점 ex .25f기 때문에 좌표계가 커지는 것을 반영한다
		this.x = x / width; 
		this.y = y / height; 
	}
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return width; }
	public float getHeight() { return height; }
}
