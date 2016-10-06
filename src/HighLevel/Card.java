package HighLevel;

import javax.microedition.khronos.opengles.GL10;

import Framework.Number;
import Framework.Rectangle2D;

public class Card extends Rectangle2D{
	public static final float CARDSIZE = 1f / CardList.CARD_MAX_VIEW;
	private int hp, atk, id;
	private Number hpNumber, atkNumber;
	private boolean isDeath;
	public Card(int spriteSheet, int atk, int hp, int id) {
		this(spriteSheet, atk, hp, CARDSIZE, id);
	}
	public Card(int spriteSheet, int atk, int hp, float size, int id) {
		super(spriteSheet, size, size);
		this.hp = hp;
		this.atk = atk;
		this.isDeath = false;
		this.id = id;
		hpNumber = new Number();
		atkNumber = new Number();
		hpNumber.setNumber(hp);
		atkNumber.setNumber(atk);
	}
	public int getId() {
		return this.id;
	}
	public boolean isDeath() {
		return isDeath;
	}
	public void makeDeath() {
		this.isDeath = true;
	}
	public int getSpriteSheet() {
		return spriteSheet;
	}
	public String toString(int[] textures) {
		for(int i = 0; i < textures.length; i++) {
			if(textures[i] == spriteSheet) {
				return i + " " + hp + " " + atk;
			}
		}
		throw new IllegalArgumentException("Card's spritesheet is wrong");
	}

	public boolean attack(Card card) {
		card.hp -= this.atk;
		card.hpNumber.setNumber(card.hp);
		return card.hp <= 0;
	}
	public void move(float x, float y) {
		super.move(x, y);
		hpNumber.move(x, y, 3f, 0f);
		atkNumber.move(x, y, 0f, 0f);
	}
	public void draw(GL10 gl) {
		super.draw(gl);
		hpNumber.draw(gl);
		atkNumber.draw(gl);
	}
	public void setVisible(boolean value) {
		super.setVisible(value);
		hpNumber.setVisible(value);
		atkNumber.setVisible(value);
	}
	public void rgb(float red, float green, float blue) {
		super.rgb(red, green, blue);
		hpNumber.rgb(red, green, blue);
		atkNumber.rgb(red, green, blue);
	}
	public void rgba(float red, float green, float blue, float alpha) {
		super.rgba(red, green, blue, alpha);
		hpNumber.rgba(red, green, blue, alpha);
		atkNumber.rgba(red, green, blue, alpha);
	}
	public int getHp() {
		return hp;
	}
	public int getAtk() {
		return atk;
	}
}
