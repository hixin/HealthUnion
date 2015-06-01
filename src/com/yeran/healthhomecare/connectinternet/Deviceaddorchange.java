package com.yeran.healthhomecare.connectinternet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

import com.example.health.MyApplication;
import com.yeran.healthhomecare.data.ShuaidaoDeviceinfor;

public class Deviceaddorchange implements Threadinterface {

	private String homeid, deviceid, name, oldphone, shuaidaophone, secret,
			time, newsecret, Responsemsg;
	private URL url = null;
	private HttpURLConnection httpurlconnection = null;
	private Context mContext;
	private String treatway = null;
	boolean upValidate = false;
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时5秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟

	private ArrayList<ShuaidaoDeviceinfor> devicelist = new ArrayList<ShuaidaoDeviceinfor>();

	public void setparams(String deviceid, String homeid, String name,
			String oldphone, String shuaidaophone, String secret,
			String newsecret, String time, String treatway) {
		// TODO Auto-generated method stub
		this.homeid = homeid;
		this.deviceid = deviceid;
		this.name = name;
		this.oldphone = oldphone;
		this.shuaidaophone = shuaidaophone;
		this.secret = secret;
		this.newsecret = newsecret;
		this.time = time;
		this.treatway = treatway;
	}

	Deviceaddorchange(Context context) {
		this.mContext = context;
	}

	@Override
	public Boolean startthread() {
		// TODO Auto-generated method stub
		Thread updevice = new Thread(new UpdeviceRunable());
		updevice.start();
		try {
			updevice.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return upValidate;
	}

	/**
	 * Runable 类，用判断是否成功连接
	 * */
	class UpdeviceRunable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// URL合法，但是这一步并不验证密码是否正确

				Message msg = mhandler.obtainMessage();
				if (downorup()) {
					if (Responsemsg.equals("success")) {
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

	@Override
	public boolean downorup() {
		// TODO Auto-generated method stub
		// String urlStr = "http://" + StartActivity.centerurl
		// + ":8080/Shuaidaoservice/servlet/Devicecontrolservlet";
		String urlStr = "http://" + "219.245.65.14/HM2/serverforzd";
		HttpPost request = new HttpPost(urlStr);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加用户名和密码
		params.add(new BasicNameValuePair("type", "shuaidao"));
		params.add(new BasicNameValuePair("Treate", treatway));
		params.add(new BasicNameValuePair("HomeID", homeid));
		params.add(new BasicNameValuePair("DevID", deviceid));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("Sdphone", shuaidaophone));
		params.add(new BasicNameValuePair("PWD", secret));
		params.add(new BasicNameValuePair("NPWD", newsecret));
		params.add(new BasicNameValuePair("Time", time));

		try {
			// 设置请求参数项
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

	@Override
	public void setparams(String[] deviceid) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("HandlerLeak")
	private Handler mhandler = new Handler() {
		@SuppressLint("SimpleDateFormat")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				Toast.makeText(mContext, Responsemsg, Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				Toast.makeText(mContext, Responsemsg, Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Toast.makeText(mContext, Responsemsg, Toast.LENGTH_SHORT)
						.show();
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

	/**
	 * 获取设备列表
	 * */
	public ArrayList<ShuaidaoDeviceinfor> getDevicelist()
			throws InterruptedException {
		// 获取列表
		Thread updevice = new Thread(new Downdevice());
		updevice.start();
		updevice.join();
		return devicelist;
	}

	class Downdevice implements Runnable {
		public void run() {
			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean upValidate = Downdevicelist();
				Message msg = mhandler.obtainMessage();
				if (upValidate) {
					if (Responsemsg.equals("success")) {
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

	@SuppressWarnings("unchecked")
	private Boolean Downdevicelist() {
		String urlStr = "http://" + MyApplication.centerurl
				+ ":8080/service/servlet/downpollocServlet";
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			httpurlconnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpurlconnection.setDoOutput(true);
		try {
			httpurlconnection.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataOutputStream dos;
		// 上传用户名
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());

			dos.writeUTF(String.valueOf(homeid));

			dos.flush();
			dos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			DataInputStream dis = new DataInputStream(
					httpurlconnection.getInputStream());

			ObjectInputStream dis2 = new ObjectInputStream(
					httpurlconnection.getInputStream());

			// 读取连接状态以及数据
			Responsemsg = dis.readUTF();
			devicelist = (ArrayList<ShuaidaoDeviceinfor>) dis2.readObject();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
