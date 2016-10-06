package HighLevel;

import java.util.LinkedList;

import android.util.Log;

public class AnimationManager {
	public LinkedList<AnimateUnit> queue;
	public AnimationManager() {
		queue = new LinkedList<AnimateUnit>();
	}
	public void addAnimation(Card main, Card dest, int type, int time) {
		queue.addFirst(new AnimateUnit(main, dest, type, time));
	}
	/** @return 애니메이션 처리 중인 경우 true를 반환*/
	public boolean update() {
		if(queue.size() > 0) {
			if(queue.getLast().update()) {
				queue.removeLast();
				return true;
			}
			else {
				return true;
			}
		}
		return false;
	}
}
