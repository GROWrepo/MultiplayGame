package Framework;

import javax.microedition.khronos.opengles.GL10;

import HighLevel.Card;
import android.util.Log;

public class Number extends Rectangle2D {
	private static final float NUMBERRATE = 4, NUMBERSIZE = Card.CARDSIZE / NUMBERRATE;
	public static int spriteSheet;
	private int number;
	private static final float[] texture = {
			0.0f, 0.0f,
			0.2f, 0.0f,
			0.2f, 0.2f,
			0.0f, 0.2f,
	};
	/** �����ϱ����� Number.spriteSheet�� �ʱ�ȭ����� �Ѵ� */
	public Number() {
		this(1);
	}
	public Number(float size) {
		super(spriteSheet, texture, size * NUMBERSIZE, size * NUMBERSIZE);
		number = 0;
	}
	public void move(float baseX, float baseY, float addX, float addY) {
		super.move(baseX * NUMBERRATE + addX, baseY * NUMBERRATE + addY);
	}
	public void setNumber(int number) {
		this.number = number;
		if(number > 10) throw new IllegalArgumentException("number is over 10");
	}
	public int getNumber() {
		return this.number;
	}
	public void draw(GL10 gl) {
		if(number < 5) { //ù��° �� 0 1 2 3 4
			 drawSplit(gl, number / 5f, .8f);
		} else {
			 drawSplit(gl, (number - 5)/5f, .6f);
		}
	}
}
