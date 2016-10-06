package HighLevel;

import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Receiver {
	private AnimationManager aManager;
	private Context context;
	private int[] textures;
	private int[] num;
	private CardList myList, enemyList, deck;
	public Receiver(int[] num, Context context) {
		this.context = context;
		this.num = num;
	}
	public void init(CardList deck, CardList myList, CardList enemyList, AnimationManager am, int[] textures) {
		this.deck = deck;
		this.myList = myList;
		this.enemyList = enemyList;
		this.aManager = am;
		this.textures = textures;
	}
	public void receive(String key, String value) throws Exception{
		Log.d("debug1", "Receiver + receive " + key + "/"+ value);
		if(key.equals("visit")) {
			readVisit(Integer.parseInt(value));
		}
		else if(key.equals("read")) {
			StringTokenizer st = new StringTokenizer(value, " ");
			if(st.hasMoreElements()) {
				String order = st.nextToken();
				if(order.equals("list")) {
					readList(st);
				}		
				else if(order.equals("attack")) {
					readAttack(st);
				}
				else if(order.equals("die")) {
					readDie(st, 0);
				}
				else if(order.equals("kill")) {
					readDie(st, 1);
				}
			}
		}
	}
	/** @param type 내가 죽는 경우 0 상대가 죽는 경우 1 */
	public void readDie(StringTokenizer st, int type) {
		int index = Integer.parseInt(st.nextToken());
		if(type == 0) {
			aManager.addAnimation(null, myList.getCard(index), AnimateUnit.DESTROY, 50);
		}
		else if(type == 1) {
			aManager.addAnimation(null, enemyList.getCard(index), AnimateUnit.DESTROY, 50);
		}
	}
	public void readList(StringTokenizer st) {
		enemyList.clear();
		while(st.hasMoreElements()) {
			int id = Integer.parseInt(st.nextToken());
			int hp = Integer.parseInt(st.nextToken());
			int atk = Integer.parseInt(st.nextToken());
			enemyList.addCard(new Card(textures[id], hp, atk, id));
		}
	}
	public void readAttack(StringTokenizer st) {
		int atker = Integer.parseInt(st.nextToken());
		int defer = Integer.parseInt(st.nextToken());
		//정보가 필요한건 상대가 나를 공격할때 뿐이다
		Log.d("debug1", "readAttack " + atker + "/" + defer);
		aManager.addAnimation(enemyList.getCard(atker), myList.getCard(defer), AnimateUnit.ATTACK, 50);
	}
	public void readVisit(int value) {
		switch(this.num[0]) {
		case -1:
			Toast.makeText(context, "인원이 초과되어 입장이 불가합니다", 1);
			break;
		case 0:
			this.num[0] = value;
			Log.d("debug1", "Receiver + readVisit " + value);
			break;
		case 1:
			//2가 될때까지 기다려야 함.
			break;
		case 2:
			//특수한 case네 내가 이미 2인데 visit에서 하나 더 왔다면..? 그냥 넘겨 일단
			break;
		}		
	}
}
