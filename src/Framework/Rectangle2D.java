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
	/** �ؽ��ĸ� �ɰ� �� ���� ������Ʈ�� */
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
		setTexture(gl, 0f, 0f, 0f, spriteSheet); //�ؽ��ĸ� �ɰ��� �ʴ´�
	}
	public void drawSplit(GL10 gl, float tx, float ty) {
		setModelView(gl, width, height, 1f);
		setPosition(gl, x, y, 0f);
		setTexture(gl, tx, ty, 0f, spriteSheet);
	}
	/** ũ�Ⱑ �ٸ� ũ�⿡ �ݺ���ϰ� ��ǥ�谡 �ٲ��
	 * ex : ũ�� 0.25 �ִ� x, y���� 4 */
	public void move(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/** ũ�Ⱑ �پ ��ǥ�谡 �״�� �����Ǵ� ��ǥ���� */
	public void moveReal(float x, float y) {
		//width�� �Ҽ��� ex .25f�� ������ ��ǥ�谡 Ŀ���� ���� �ݿ��Ѵ�
		this.x = x / width; 
		this.y = y / height; 
	}
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return width; }
	public float getHeight() { return height; }
}
