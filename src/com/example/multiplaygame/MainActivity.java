package com.example.multiplaygame;

import Framework.GameRenderer;
import Framework.GameView;
import Framework.Scene;
import HighLevel.TempScene;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {
	public static DisplayMetrics display;
	public static float touchX, touchY;
	private Scene current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        current = new TempScene();
        GameView gv = new GameView(this);
        gv.setRenderer(new GameRenderer(this, current));
        setContentView(gv);
    }
    public boolean onTouchEvent(MotionEvent event) {
    	touchX = event.getX() / display.widthPixels;
    	touchY = 1 - event.getY() / display.heightPixels;
    	current.onTouchEvent(event.getAction(), touchX, touchY);
    	return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	current.onFinish();
    }
}
