package Framework;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureLoader {
	private int[] textures;
	private int current;
	public TextureLoader(GL10 gl, int size) {
		textures = new int[size];
		current = 0;
		gl.glGenTextures(textures.length, textures, 0);
	}
	public void addTexture(GL10 gl, Context context, int image) {
		InputStream imagestream = context.getResources().openRawResource(image);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(imagestream);
			imagestream.close();
			imagestream = null;
		} catch(Exception e) {
			Log.d("debug1", "E1. TextureLoader + loadTexture");
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[current]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
		
		current++;
	}
	
	public int[] textures() {
		return textures;
	}
	public int getTexture(int index) {
		return textures[index];
	}
}
