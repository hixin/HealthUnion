package com.example.health;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;






import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;
import com.example.health.util.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{

	private Button load_btn;	
	private EditText login_number;
	private MyApplication app;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			/*if (msg.what == 10) {//当服务器返回给客户端的标记为10时，说明注册成功
				Intent login_main = new Intent(LoginActivity.this, MainActivity.class);
				LogUtil.i("register_main----------------->", "success");
				startActivity(login_main);
				finish();
			}else if (msg.what == 11) {//当服务器返回给客户端的标记为11时，说明注册失败
				Toast.makeText(MyApplication.getContext(), "注册失败", Toast.LENGTH_SHORT)
				.show();
			}else */
			if (msg.what == 12) {//当服务器返回给客户端的标记为12时，说明登录成功
				progressDialog.dismiss();			
				Intent login_main = new Intent(LoginActivity.this, UserListActivity.class);			
				startActivity(login_main);
				finish();
			}else if (msg.what == 13) {//当服务器返回给客户端的标记为13时，说明登录失败
				progressDialog.dismiss();
				Toast.makeText(MyApplication.getContext(), "设备验证失败", Toast.LENGTH_SHORT).show();
			}else {
				//忘记密码
				LogUtil.i("register_main----------------->", "forget password");
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		app = (MyApplication) getApplication();		
		init();
			
	  load_btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(NetTool.isInternetConnect()) {
					String myUser = login_number.getText().toString();
					if(myUser.length()>2) {
						 progressDialog = new ProgressDialog(LoginActivity.this);					
						 progressDialog.setMessage("正在验证设备，请稍候...");
						 progressDialog.setCancelable(false);
						 progressDialog.show();			
						  new Thread(){
							  public void run() { 
								    HttpClient client = new DefaultHttpClient();
									List<NameValuePair> list = new ArrayList<NameValuePair>();
									NameValuePair pair0 = new BasicNameValuePair("type","login");
									NameValuePair pair1 = new BasicNameValuePair("zhongduan_id", login_number.getText().toString().trim());
									list.add(pair0);
									list.add(pair1);
									HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/receiveservlet");
//									HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
					//				HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
					//				HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
									HttpResponse response = null;
									try {
										UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");							
										post.setEntity(entity);
										response = client.execute(post);
									} catch (UnsupportedEncodingException e1) {
										e1.printStackTrace();
									} catch (ClientProtocolException e1) {
										e1.printStackTrace();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									if (response.getStatusLine().getStatusCode() == 200 ) {
										try {
					
											    InputStream in = response.getEntity().getContent();								
											    StringBuffer resultbuffer = new StringBuffer();
								                byte[] buffer = new byte[1024];
								                int bytes;
								                
								                try {
								                    if( (bytes = in.read(buffer)) > 0 )  {
								                        resultbuffer.append(new String(buffer,0,bytes));
								                    }
								                } catch (IOException e2) {
								                    // TODO Auto-generated catch block
								                    e2.printStackTrace();
								                }
												in.close();										
												LogUtil.i("response",resultbuffer.toString());
												if(resultbuffer.length()>3) {
													String searchvalue = "!!!";
													String s = resultbuffer.toString();
													int start = 0;
										        	start = s.indexOf(searchvalue, 0);
											
											        String s1 = s.substring(0, start);
											        String s2 = s.substring(start+3, s.length());
											        LogUtil.i("s1",s1);
											        LogUtil.i("s2",s2);						
													LogUtil.i("login_state", s1);							
													
													if(s1.equals("login")) {
														handler.sendEmptyMessage(12);
														Gson gson = new Gson();
												        Type type = new TypeToken<List<User>>(){}.getType();								       
												        app.userList = gson.fromJson(s2, type);								       						       								        
//												        LogUtil.i("user", app.userList.get(0).toString());
												        //创建数据库，保存用户信息到本地										        
												        for (User suser :  app.userList) { 
					                                      GeneralDbHelper.getInstance(MyApplication.getContext()).save(suser);
					                                  }
												        
														app.zhongduan_id = login_number.getText().toString().trim();
												        //只要登录成功，以后就不再进入登录界面啦
												        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", MODE_PRIVATE);    
												        Editor editor = sharedPreferences.edit();
												        editor.putString("userID", app.zhongduan_id);
												        editor.putBoolean("isFirstRun", false);
												        editor.commit();
												        app.isFirstRun = false;				
													}
												}else {
													try {
														Thread.sleep(2000);
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													handler.sendEmptyMessage(13);
													LogUtil.i("login", "fail");
												}
												
												
											
										} catch (IllegalStateException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}							
										
									}
									
							  }
						  }.start();	
					}
					else{
						  Toast.makeText(MyApplication.getContext(), "设备号不存在！", Toast.LENGTH_SHORT).show();
					}
				}else {
					 Toast.makeText(MyApplication.getContext(), "请保持网络连接", Toast.LENGTH_SHORT).show();
				}
				
			  
			}
	  });										
	}			
	private void init(){
			
		load_btn = (Button) findViewById(R.id.loadBtn);		
		login_number = (EditText) findViewById(R.id.login_number);
	
	}
	

}

