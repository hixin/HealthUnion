package com.yeran.healthhomecare.connectinternet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yeran.healthhomecare.data.peopinfor;

/**
 * 获取历史记录的http请求。通过HTTP连接servlet，获取servlet传过来的数据
 * */
public class ConnIntentdata implements Threadinterface {

	private ArrayList<peopinfor> peoplist;
	private String Responsemsg, homeid, deviceid;
	private Context mContext;
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时5秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	boolean upValidate = false;

	ConnIntentdata(Context context) {
		this.mContext = context;
	};

	public String getmessage() {
		return Responsemsg;
	}

	@Override
	public void setparams(String[] homeid) {
		// TODO Auto-generated method stub
		this.homeid = homeid[0];
		this.deviceid = homeid[1];
	}

	@Override
	public Boolean startthread() {
		// TODO Auto-generated method stub
		Thread downpeoplist = new Thread(new DownlistRunable());
		downpeoplist.start();
		try {
			downpeoplist.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return upValidate;
	}

	/**
	 * Runable 类，用判断是否成功连接
	 * */
	class DownlistRunable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// URL合法，但是这一步并不验证密码是否正确
				Message msg = mhandler.obtainMessage();
				if (downorup()) {
					if (Responsemsg.startsWith("success")) {
						upValidate = true;
						msg.what = 0;
						mhandler.sendMessage(msg);
					} else {
						msg.what = 1;
						mhandler.sendMessage(msg);
					}
				} else {
					msg.what = 2;
					mhandler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 发生超时,返回值区别于null与正常信息
				e.printStackTrace();
			}
		}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
	public boolean downorup() {
		// TODO Auto-generated method stub
		// String urlStr = "http://" + StartActivity.centerurl
		// + ":8080/service/servlet/downpollocServlet?homeid='"
		// + this.homeid + "'&deviceid='" + this.deviceid + "'";
		String urlStr = "http://" + "219.245.65.14/HM2/serverforzd";
		HttpPost request = new HttpPost(urlStr);

		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = data.format(new java.util.Date());

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加用户名和密码
		params.add(new BasicNameValuePair("type", "shuaidao"));
		params.add(new BasicNameValuePair("Treate", "historylocation"));
		params.add(new BasicNameValuePair("HomeID", homeid));
		params.add(new BasicNameValuePair("DevID", deviceid));
		params.add(new BasicNameValuePair("name", null));
		params.add(new BasicNameValuePair("Sdphone", null));
		params.add(new BasicNameValuePair("PWD", null));
		params.add(new BasicNameValuePair("NPWD", null));
		params.add(new BasicNameValuePair("Time", time));

		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// 执行请求返回相应
			HttpResponse response = client.execute(request);

			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获得响应信息
				Responsemsg = EntityUtils.toString(response.getEntity());

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressLint("HandlerLeak")
	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:		
				break;
			case 1:
				Toast.makeText(mContext, "接收信息失败~", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "连接服务器失败~", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
