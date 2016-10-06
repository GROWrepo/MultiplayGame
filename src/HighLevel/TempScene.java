package HighLevel;

import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;

import Framework.Number;
import Framework.Scene;
import Framework.TextureLoader;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.multiplaygame.R;

public class TempScene extends Touchable implements Scene {
	private CardList deck, myList, enemyList;
	private ViewCard viewCard;
	private AnimationManager aManager;
	private Receiver receiver;
	private int[] textures;
	private int[] num = {0};
	@Override
	public void init(GL10 gl, Context context) {
		TextureLoader tLoader = new TextureLoader(gl, 4);
		tLoader.addTexture(gl, context, R.drawable.number);
		tLoader.addTexture(gl, context, R.drawable.card1);
		tLoader.addTexture(gl, context, R.drawable.card2);
		tLoader.addTexture(gl, context, R.drawable.card3);
		aManager = new AnimationManager();
		viewCard = new ViewCard();
		receiver = new Receiver(num, context);
		Log.d("debug1", "TempScene + num : " + num[0]);
		deck = new CardList(viewCard, receiver, 0f, 0f, 1f, .25f);
		myList = new CardList(viewCard, receiver, 0f, .25f, 1f, .25f);
		enemyList = new CardList(viewCard, receiver, 0f, .75f, 1f, .25f);
		textures = tLoader.textures();
		
		Number.spriteSheet =textures[0];
		deck.addCard(new Card(textures[1], 0, 1, 1));
		deck.addCard(new Card(textures[2], 1, 3, 2));
		deck.addCard(new Card(textures[3], 2, 3, 3));
		deck.addCard(new Card(textures[1], 2, 3, 1));
		deck.addCard(new Card(textures[2], 3, 2, 2));		

		receiver.init(deck, myList, enemyList, aManager, textures);
		HttpTask task = new HttpTask();
		task.visit(receiver, "in");
	}

	@Override
	public void draw(GL10 gl) {
		gl.glClearColor(1, 1, 1, 1);
		deck.draw(gl);
		enemyList.draw(gl);
		myList.draw(gl);
		viewCard.draw(gl);
	}

	@Override
	public void update() {
		if(!aManager.update()) {
			deck.update();
			myList.update();
			enemyList.update();
			HttpTask task = new HttpTask();
			task.readMessage(receiver, num[0]);
		}
	}

	@Override
	public void onFinish() {
		HttpTask task = new HttpTask();
		task.visit(receiver, "out");
	}

	@Override
	public void onTouchEvent(int action, float x, float y) {
		myList.take(deck, action, x, y, num[0]);
		enemyList.attack(aManager, myList, action, x, y, num[0]);
		deck.onTouchEvent(action, x, y);
		myList.onTouchEvent(action, x, y);
		enemyList.onTouchEvent(action, x, y);
	}

}
