package Framework;

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public interface Scene {
	public void init(GL10 gl, Context context);
	public void draw(GL10 gl);
	public void update();
	public void onFinish();
	public void onTouchEvent(int action, float x, float y);
}
