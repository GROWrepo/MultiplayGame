package Framework;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Framework.Scene;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class GameRenderer implements Renderer {
	private static final int GAME_THREAD_FPS_SLEEP = 1000/60;
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	private Context context;
	private Scene current;
	public GameRenderer(Context context, Scene firstScene) {
		this.context = context;
		this.current = firstScene;
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		loopStart = System.currentTimeMillis();
		try {
			if(loopRunTime < GAME_THREAD_FPS_SLEEP) {
				Thread.sleep(GAME_THREAD_FPS_SLEEP - loopRunTime);
			}
		} catch(InterruptedException e) {
			Log.d("Catch", "GameRenderer + onDrawFrame()");
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//여기에 추가하기
		current.update();
		current.draw(gl);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		loopEnd = System.currentTimeMillis();
		loopRunTime = (loopEnd - loopStart);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
		
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
		
		current.init(gl, context);
	}

}
