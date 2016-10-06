package Framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Object2D {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	private int indicesLength;
	private boolean visible;
	private float red, green, blue, alpha;
	public void init(float[] vertices, float[] texture, byte[] indices) {
		vertexBuffer = makeBuffer(vertices);
		textureBuffer = makeBuffer(texture);
		indexBuffer = makeBuffer(indices);
		indicesLength = indices.length;
		visible = true;
		red = green = blue = alpha = 1;
	}
	public void init(float[] vertices, float[] texture, byte[] indices, 
			float textureWidth, float textureHeight) {
		for(int i = 0; i < texture.length; i+=2)
		{
			texture[i] *= textureWidth;
			texture[i+1] *= textureWidth;
		}
		init(vertices, texture, indices);
	}
	public FloatBuffer makeBuffer(float[] array) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);
		return floatBuffer;
	}
	public ByteBuffer makeBuffer(byte[] array) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length);
		byteBuffer.put(array);
		byteBuffer.position(0);
		return byteBuffer;
	}
	public void rgb(float red, float green, float blue) {
		rgba(red, green, blue, 1f);
	}
	public void rgba(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	/** setModelView -> setPosition -> setTexture */
	public void setModelView(GL10 gl, float scaleX, float scaleY, float scaleZ) {
		gl.glColor4f(red, green, blue, alpha);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(scaleX, scaleY, scaleZ);
	}
	/** setModelView -> setPosition -> setTexture */
	public void setPosition(GL10 gl, float x, float y, float z) {
		gl.glTranslatef(x, y, z);
	}
	/** @순서 setModelView -> setPosition -> setTexture
	 * OpenGl은 텍스쳐가 거꾸로 받아들이기 때문에 회전을 시켜준다 */
	public void setTexture(GL10 gl, float x, float y, float z, int spriteSheet) {
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(.5f, .5f, 0f);
		gl.glRotatef(180f, 1f, 0f, 0f);
		gl.glTranslatef(-0.5f, -0.5f, 0f);
		gl.glTranslatef(x, y, z);
		draw(gl, spriteSheet);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	/** spriteSheet에 textureloader를 인덱싱하여 구한 실질적인 텍스쳐 인덱스를 넣는다 */
	public void draw(GL10 gl, int spriteSheet) {
		if(visible) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, spriteSheet);
			
			gl.glFrontFace(GL10.GL_CCW);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_BACK);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			
			gl.glDrawElements(GL10.GL_TRIANGLES, indicesLength, GL10.GL_UNSIGNED_BYTE, indexBuffer);
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	}
	public void setVisible(boolean value) {
		visible = value;
	}
}
