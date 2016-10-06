package HighLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import Framework.Scene;
import android.os.AsyncTask;
import android.util.Log;

//어떻게 하면 리스트를 적절히 밖으로 빼낼 수 있을까?
public class HttpTask extends AsyncTask<String, Void, Void> {
	private final String urlPath = "http://52.79.148.108:8080/MP3/";
	Vector<NameValuePair> nameValue;
	private LinkedList<String> list;
	private static int readLimit = 0;
	private Receiver receiver;
	private String key;
	public void visit(Receiver receiver, String state) {
		key = "visit";
		nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("state", state));
		this.receiver = receiver;
		Log.d("debug1", "HttpTask + visit" + state);
		execute(urlPath + "visit.jsp");
	}
	public void sendMessage(Receiver receiver, String message, int num) {
		nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("message", message));
		nameValue.add(new BasicNameValuePair("num", num + ""));
		this.receiver = receiver;
		Log.d("debug1", "sendMessage " + message + "/" + num);
		key = "send";
		execute(urlPath + "save.jsp");
	}
	public void readMessage(Receiver receiver, int num) {
		nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("num", num + ""));
		this.receiver = receiver;
		key = "read";
		if(HttpTask.readLimit > 5) return;
		HttpTask.readLimit++;
		execute(urlPath + "load.jsp");
	}
	@Override
	protected Void doInBackground(String... args) {
		try {
			HttpPost request = new HttpPost(args[0]);
			HttpEntity enty = new UrlEncodedFormEntity(nameValue, HTTP.UTF_8);
			
			request.setEntity(enty);
			
			HttpClient client = new DefaultHttpClient();
			HttpResponse res = client.execute(request);
			HttpEntity entityResponse = res.getEntity();
			InputStream im = entityResponse.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(im, HTTP.UTF_8));
			
			list = new LinkedList<String>();
			String tmp = "";

			while((tmp = reader.readLine()) != null) {
				if(tmp!=null) {
					list.add(tmp);
					Log.d("debug2", tmp);
				}
			}
			im.close();
		} catch(UnsupportedEncodingException e) {
			Log.d("debug1", "doInBackground E" + e.getLocalizedMessage());
		} catch(IOException e) {
			Log.d("debug1", "doInBackground E" + e.getLocalizedMessage());
		}
		return null;
	}
	public void onPostExecute(Void result) {
		if("read".equals(key)) {
			HttpTask.readLimit--;
		}
		if(list != null) {
			for(int i = 0; i < list.size(); i++) {
				if(!list.get(i).equals("")) {
					try {
						receiver.receive(key, list.get(i));
						Log.d("debug1", "HttpTask + onPostExecute" + list.get(i));
					}catch(Exception e) {
						Log.d("debug1", "httpTask + Error" + e);
					}
				}
			}
		}
	}
}
