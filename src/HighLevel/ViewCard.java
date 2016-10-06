package HighLevel;

import javax.microedition.khronos.opengles.GL10;

import Framework.Number;
import Framework.Rectangle2D;
import android.util.Log;

public class ViewCard extends Rectangle2D {
	private Number hpNumber, atkNumber;
	public ViewCard() {
		super(0, .5f, .5f);
		hpNumber = new Number();
		atkNumber = new Number();
		setVisible(false);
	}
	
	public void changeSprite(Card card) {
		this.spriteSheet = card.getSpriteSheet();
		hpNumber.setNumber(card.getHp());
		atkNumber.setNumber(card.getAtk());
	}
	public void draw(GL10 gl) {
		super.draw(gl);
		hpNumber.draw(gl);
		atkNumber.draw(gl);
	}
	public void setVisible(boolean value) {
		if(value == false || spriteSheet != 0) {
			super.setVisible(value);
			hpNumber.setVisible(value);
			atkNumber.setVisible(value);
		}
	}
	
	public void moveReal(float x, float y) {
		x -= 1f / CardList.CARD_MAX_VIEW;
		y -= .5f / CardList.CARD_MAX_VIEW;
		super.moveReal(x, y);
		x *= CardList.CARD_MAX_VIEW;
		y *= CardList.CARD_MAX_VIEW;
		hpNumber.move(x, y, 3f, 0f);
		atkNumber.move(x, y, 0f, 0f);
	}
}
