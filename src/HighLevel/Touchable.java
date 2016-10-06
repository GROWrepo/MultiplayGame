package HighLevel;

import android.view.MotionEvent;
import Framework.Counter;


public class Touchable {
	public static final int TOUCH_CANCEL_TIME = 5;
	private Counter counter;
	private float prevX, prevY;
	/** android의 touchup 관리 부족을 대신 처리해준다 */
	public Touchable() {
		counter = new Counter(TOUCH_CANCEL_TIME);
		prevX = 0;
		prevY = 0;
	}
	public void update( ){
		if(counter.release()) {
			onTouchEvent(MotionEvent.ACTION_UP, prevX, prevY);
		}
	}
	public void onTouchEvent(int action, float x, float y) {
		counter.counterInit();
		prevX = x;
		prevY = y;
	}
}
