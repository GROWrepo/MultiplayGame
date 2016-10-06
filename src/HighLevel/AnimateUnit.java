package HighLevel;

import android.util.Log;


public class AnimateUnit {
	public static final int ATTACK = 1, DAMAGE_VIEW = 2, DESTROY = 3;
	private Card main, dest;
	private int type, time, count;
	private float startX, startY;
	public AnimateUnit(Card main, Card dest, int type, int time) {
		this.main = main;
		this.dest = dest;
		this.type = type;
		this.time = time;
		this.count = 0;
		if(type == DAMAGE_VIEW) {
			this.startX = dest.getX();
			this.startY = dest.getY();
		}
		else {
			this.startX = main.getX();
			this.startY = main.getY();
		}
	}
	/** @return 모든 애니메이션이 끝난 경우 true를 반환한다 */
	public boolean update() {
		switch(type) {
		case ATTACK:
			return moveArc();
		case DAMAGE_VIEW:
			count++;
			return count > time;
		case DESTROY:
			return destroy();
		default:
			throw new IllegalArgumentException("AnimateUnit type 잘못 지정 : " + type);
		}
	}
	public int getType() {
		return type;
	}
	/** @return 모든 애니메이션이 끝난 경우 true를 반환한다 */
	public boolean destroy() {
		float move = 0.05f;
		if(count < time/2) {
			dest.rgb(0.1f, 0.1f, 0.1f);
			if(count % 3 == 0)	dest.move(startX, startY - move);
			else if(count % 3 == 1) dest.move(startX, startY);
			else dest.move(startX, startY + move);
			count++;
			return false;
		}
		else if(count < time){
			float alpha = 1.0f - 1.0f * (count - time/2) / (time/2);
			dest.rgba(0.1f, 0.1f, 0.1f, alpha);
			count++;
			return false;
		}
		dest.makeDeath();
		return true;
	}
	/** @return 모든 애니메이션이 끝난 경우 true를 반환한다 */
	public boolean moveArc() {
		if(count < time / 2) {
			main.move(arcPosition(startX, dest.getX(), 0), arcPosition(startY, dest.getY(), 0));
			count++;
			return false;
		}
		else if(count < time) {
			main.move(arcPosition(startX, dest.getX(), time), arcPosition(startY, dest.getY(), time));
			count++;
			return false;
		}
		return true;
	}
	public float arcPosition(float origin, float dir, int plusTime) {
		return origin + 4.0f * (dir - origin)/ (time * time) * (count - plusTime) * (count - plusTime);
	}
}
