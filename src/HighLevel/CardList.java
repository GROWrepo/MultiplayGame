package HighLevel;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import Framework.Scene;
import android.util.Log;
import android.view.MotionEvent;

public class CardList {
	public static final int TOUCHWAIT = 0, FOCUSNOTME = 1, FOCUSME = 2;
	public static final int CARD_BASE_SIZE = 10, CARD_MAX_VIEW = 4, ATTACK_DEFAULT_TIME = 50;
	private float x, y, width, height, wheelX, prevX;
	private int isFocus = TOUCHWAIT;
	private ArrayList<Card> cards;
	private Card selectedCard;
	private ViewCard viewCard;
	private int selectedIndex;
	private Receiver receiver;
	
	/* 기본적인 함수들 */
	public CardList(ViewCard viewCard,  Receiver receiver, float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.viewCard = viewCard;
		cards = new ArrayList<Card>(CARD_BASE_SIZE);
		this.receiver = receiver;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public boolean isIn(float x, float y) {
		return (this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height);
	}
	/** 자신이 focusing 되었는지 확인한다 */
	public void setIsFocus(int action, float x, float y) {
		switch(action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if(isFocus == TOUCHWAIT) {
				if(isIn(x, y))	isFocus = FOCUSME;
				else isFocus = FOCUSNOTME;
			}
			break;
		case MotionEvent.ACTION_UP:
			isFocus = TOUCHWAIT;
			break;
		}
	}
	public int isFocus() { return isFocus; }
	/** @return 못찾으면 null을 반환한다 */
	public Card findSelectedCard(float x) {
		x *= CARD_MAX_VIEW;
		for(int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			if(c.getX() <= x && x <= c.getX() + c.getWidth() * CARD_MAX_VIEW) {
				selectedIndex = i;
				return c;
			}
		}
		selectedIndex = -1;
		return null;
	}
	/** card_max_view - cards.size()*/
	public float wheelCoef() {
		return 1.0f * CARD_MAX_VIEW - cards.size();
	}
	public void addCard(Card card) {
		cards.add(card);
	}
	/* 응용 함수들 */
	/** action = action_move라는 가정하에 실행되어야 한다 */
	public void wheel(float x, float y) {
		if(isIn(x, y)) {
			float coef = wheelCoef();
			if(coef < 0) {
				if(prevX >= 0) wheelX += (x-prevX) * CARD_MAX_VIEW;
				if(wheelX < coef) wheelX = coef;
				else if(wheelX > 0) wheelX = 0;
				prevX = x;
			}
		}
	}
	/** action = action_move라는 가정하에 실행되어야 한다 */
	public void drag(float x, float y) {
		if(isIn(x, y) && isFocus == FOCUSME) {
			selectedCard = findSelectedCard(x);
			if(selectedCard != null) {
				//drag 안함
				selectedCard.setVisible(true);
				viewCard.setVisible(false);
			}
		}
		else if(selectedCard != null && isFocus == FOCUSME) {
			//drag
			selectedCard.setVisible(false);
			viewCard.changeSprite(selectedCard);
			viewCard.setVisible(true);
			viewCard.moveReal(x, y);
		}
	}
	
	public void take(CardList prev, int action, float x, float y, int num) {
		if(action == MotionEvent.ACTION_UP && isIn(x, y) 
				&& prev.isFocus == FOCUSME) {
			Card selectedCard = prev.selectedCard;
			if(selectedCard != null) {
				this.cards.add(selectedCard);
				prev.cards.remove(selectedCard);
				HttpTask sender = new HttpTask();
				sender.sendMessage(receiver, listMessage(), num);
				selectedIndex = -1;
			}
		}
	}
	
	public String listMessage() {
		String msg = "list ";
		for(int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			msg += c.getId() + " ";
			msg += c.getHp() + " ";
			msg += c.getAtk() + " ";
		}
		return msg;
	}
	
	public String attackMessage(int atker, int dmger) {
		return "attack " + atker + " " + dmger;
	}
	
	public void attack(AnimationManager aManager, CardList prev, int action, float x, float y, int num) {
		if(action == MotionEvent.ACTION_UP && prev.isFocus == FOCUSME && isIn(x, y)) {
			Card damagedCard = this.findSelectedCard(x);
			Card attackCard = prev.selectedCard;
			if(damagedCard != null && attackCard != null) {
				HttpTask sender = new HttpTask();
				sender.sendMessage(receiver, attackMessage(prev.selectedIndex, selectedIndex), num);
				attack(aManager, attackCard, damagedCard);
			}
		}
	}
	public Card getCard(int i) {
		return cards.get(i);
	}
	public void attack(AnimationManager aManager, Card attackCard, Card damagedCard) {
		aManager.addAnimation(attackCard, damagedCard, AnimateUnit.ATTACK, 
				ATTACK_DEFAULT_TIME);
		aManager.addAnimation(attackCard, damagedCard, AnimateUnit.DAMAGE_VIEW, 
				ATTACK_DEFAULT_TIME);
	}
	/* 실제 사용 함수들 */
	public void onTouchEvent(int action, float x, float y) {
		setIsFocus(action, x, y);
		switch(action) {
		case  MotionEvent.ACTION_MOVE:
			wheel(x, y);
			drag(x, y);
			break;
		case MotionEvent.ACTION_UP:
			touchUp();
			break;
		}
	}
	/** 터치를 떼었을 때 기본적으로 일어나는 것들에 대해 처리한다 */
	public void touchUp() {
		Iterator<Card> it = cards.iterator();
		while(it.hasNext()) {
			it.next().setVisible(true);
		}
		viewCard.setVisible(false);
		prevX = -1;
		selectedIndex = -1;
	}
	public void update() {
		if(wheelCoef() >= 0) {
			wheelX = wheelCoef()/2.0f;
		}
		for(int i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			c.move(wheelX + i, this.y * CARD_MAX_VIEW);
			if(c.isDeath()) cards.remove(i);
		}
	}
	public void draw(GL10 gl) {
		for(int i = 0; i < cards.size(); i++) {
			cards.get(i).draw(gl);
		}
	}
	public void clear() {
		cards.clear();
	}
}
