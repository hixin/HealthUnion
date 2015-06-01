package com.example.health.util;

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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.bp.ItemListActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
if(NetTool.isInternetConnect()) {
	
}else {
	 Toast.makeText(MyApplication.getContext(), "请保持网络连接", Toast.LENGTH_SHORT).show();
}
*/

public class NetTool {
	
	public static boolean isInternetConnect() {
		ConnectivityManager connectionManager=(ConnectivityManager)MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo(); 
		if(networkInfo != null && networkInfo.isAvailable()){
			return true;
		}else{
			return false;
		}
	}
	
	/*
	另外需要权限
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	*/
//http://219.245.64.1:8080/health_union/Servlet?type=login&zhongduan_id=11223344
	
	public boolean addUser(String userName, String userId,  String password) {
		   HttpClient client = new DefaultHttpClient();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			NameValuePair pair0 = new BasicNameValuePair("type","Add");
			NameValuePair pair1 = new BasicNameValuePair("shenfennum", userId);
			NameValuePair pair2 = new BasicNameValuePair("username", userName);
			NameValuePair pair3 = new BasicNameValuePair("password", password);
			NameValuePair pair4 = new BasicNameValuePair("zhongduanid", MyApplication.zhongduan_id);
			list.add(pair0);
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
			list.add(pair4);
			HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/receiveservlet");
//			HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//			HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//			HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
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
			
			if(response == null) {
				LogUtil.i("fail","与服务器连接失败");
				return false;
			}else{
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
							LogUtil.i("add_state", s1);													
							if(s1.equals("Add")) {	
								MyApplication.sAdduser = s2;							       						       								        
						        LogUtil.i("user",MyApplication.sAdduser);
						       	return true;	
							}else{
								MyApplication.sAdduserToast = s1;								
								return false;
							}
						}else {				
							LogUtil.i("Add", "fail");
							return false;
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
		   return false;

	}
	
	
	public boolean delUser(String deviceId,String userId) {
		   HttpClient client = new DefaultHttpClient();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			NameValuePair pair0 = new BasicNameValuePair("type","Del");
			NameValuePair pair1 = new BasicNameValuePair("zhongduan_id", deviceId);
			NameValuePair pair2 = new BasicNameValuePair("zhongduanuser_id", userId);
			list.add(pair0);
			list.add(pair1);
			list.add(pair2);
			HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/receiveservlet");
//			HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//			HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//			HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
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
			
			if(response == null) {
				LogUtil.i("fail","与服务器连接失败");
				return false;
			}else{
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
					        LogUtil.i("s1",s1);
							LogUtil.i("del_state", s1);							
							if(s1.equals("Del")) {							       						       								        
						       	return true;	
							}
						}else {				
							LogUtil.i("login", "fail");
							return false;
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
		   return false;

	}
	
	
	public  boolean verifyOfID(String ID) {				 
	    HttpClient client = new DefaultHttpClient();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair0 = new BasicNameValuePair("type","login");
		NameValuePair pair1 = new BasicNameValuePair("zhongduan_id", ID);
		list.add(pair0);
		list.add(pair1);
		HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/receiveservlet");
//					HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//		HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
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
		
		if(response == null) {
			LogUtil.i("fail","与服务器连接失败");
			return false;
		}else{
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
							MyApplication.sData	= s2;							       						       								        
					        LogUtil.i("user",MyApplication.sData);
					       	return true;	
						}
					}else {				
						LogUtil.i("login", "fail");
						return false;
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
	   return false;

	}
	
	/* handler.sendEmptyMessage(12);
	 * Gson gson = new Gson();
				        Type type = new TypeToken<List<User>>(){}.getType();								       
				        MyApplication.userList = gson.fromJson(s2, type);
     //创建数据库，保存用户信息到本地										        
     for (User suser : app.userList) { 
         GeneralDbHelper.getInstance(MyApplication.getContext()).save(suser);
     }
     
		MyApplication.zhongduan_id = login_number.getText().toString();
     //只要登录成功，以后就不再进入登录界面啦
     SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", MODE_PRIVATE);    
     Editor editor = sharedPreferences.edit();
     editor.putBoolean("isFirstRun", false);
     editor.commit();
     app.isFirstRun = false;		*/

	public boolean uploadData(BloodPre obj) {
		HttpClient client = new DefaultHttpClient();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Gson gson = new Gson();
		String s1= gson.toJson(obj);
		NameValuePair pair0 = new BasicNameValuePair("type", "bloodpre");
		NameValuePair pair1 = new BasicNameValuePair("userid",obj.getUserid());
		NameValuePair pair2 = new BasicNameValuePair("data", s1);
		list.add(pair0);
		list.add(pair1);
		list.add(pair2);
		// HttpPost post = new
		HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//		HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
		HttpResponse response = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");
			post.setEntity(entity);
			response = client.execute(post);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(response == null) {
			LogUtil.i("fail","与服务器连接失败");
			return false;
		}else{
			if (response.getStatusLine().getStatusCode() == 200) {

				try {
					InputStream in = response.getEntity().getContent();
					StringBuffer resultbuffer = new StringBuffer();
					byte[] buffer = new byte[1024];
					int bytes;

					try {
						if ((bytes = in.read(buffer)) > 0) {
							resultbuffer.append(new String(buffer, 0, bytes));
						}
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					in.close();
					if(resultbuffer.length()>3) {
						String searchvalue = "!!!";
						String s = resultbuffer.toString();
						int start = 0;
			        	start = s.indexOf(searchvalue, 0);
				
				        String s2 = s.substring(0, start);				      					
						LogUtil.i("save_state", s2);
						if(s2.equals("bloodpre")){
							return true;//只有返回bloodpre才表示真正插入成功
						}else {
							return false;
						}
					  
					}else{
						return false;
					}
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				return false;	
			}
			
		}
		return false;			
	}
	
	
	
	public boolean uploadData(BloodOx obj) {
		HttpClient client = new DefaultHttpClient();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Gson gson = new Gson();
		String s1= gson.toJson(obj);
		NameValuePair pair0 = new BasicNameValuePair("type", "bloodox");
		NameValuePair pair1 = new BasicNameValuePair("userid",obj.getUserid());
		NameValuePair pair2 = new BasicNameValuePair("data", s1);
		list.add(pair0);
		list.add(pair1);
		list.add(pair2);
		// HttpPost post = new
		HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//		HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
		HttpResponse response = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");
			post.setEntity(entity);
			response = client.execute(post);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(response == null) {
			LogUtil.i("fail","与服务器连接失败");
			return false;
		}else{
			if (response.getStatusLine().getStatusCode() == 200) {

				try {
					InputStream in = response.getEntity().getContent();
					StringBuffer resultbuffer = new StringBuffer();
					byte[] buffer = new byte[1024];
					int bytes;

					try {
						if ((bytes = in.read(buffer)) > 0) {
							resultbuffer.append(new String(buffer, 0, bytes));
						}
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					in.close();
					if(resultbuffer.length()>3) {
						String searchvalue = "!!!";
						String s = resultbuffer.toString();
						int start = 0;
			        	start = s.indexOf(searchvalue, 0);
				
				        String s2 = s.substring(0, start);				      					
						LogUtil.i("save_state", s2);
						if(s2.equals("bloodox")){
							return true;//只有返回bloodox才表示真正插入成功
						}else {
							return false;
						}
					  
					}else{
						return false;
					}
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				return false;	
			}
			
		}
		return false;			
	}

	public boolean downloadData(BloodPre obj,int index) {
		HttpClient client = new DefaultHttpClient();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair0 = new BasicNameValuePair("type", "bloodpre");
		NameValuePair pair1 = new BasicNameValuePair("shenfennum",obj.getUserid());
		NameValuePair pair2 = new BasicNameValuePair("index", String.valueOf(index));
		list.add(pair0);
		list.add(pair1);
		list.add(pair2);
		HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/serverforzd");
//		HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//		HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
		HttpResponse response = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");
			post.setEntity(entity);
			response = client.execute(post);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(response == null) {
			LogUtil.i("fail","与服务器连接失败");
			return false;
		}else{
			if (response.getStatusLine().getStatusCode() == 200) {

				try {
					InputStream in = response.getEntity().getContent();
					StringBuffer resultbuffer = new StringBuffer();
					byte[] buffer = new byte[1024];
					int bytes;

					try {
						if ((bytes = in.read(buffer)) > 0) {
							resultbuffer.append(new String(buffer, 0, bytes));
						}
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					in.close();
					if(resultbuffer.length()>3) {
						String searchvalue = "!!!";
						String s = resultbuffer.toString();
						int start = 0;
			        	start = s.indexOf(searchvalue, 0);				
				        String s1 = s.substring(0, start);
				        String s2 = s.substring(start+3, s.length());					
						LogUtil.i("download_state", s1);		
						if(s1.equals("bloodpre")){
							MyApplication.sDownBloodpre = s2;							       						       								        
					        LogUtil.i("bloodpre_down",MyApplication.sDownBloodpre);
							return true;//只有返回bloodpre才表示接收血压数据成功
						}else {
							return false;
						}
					  
					}else{
						return false;
					}
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				return false;	
			}
			
		}
		return false;		
	}
	
	
	public boolean downloadData(BloodOx obj,int index) {
		HttpClient client = new DefaultHttpClient();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair pair0 = new BasicNameValuePair("type", "bloodox");
		NameValuePair pair1 = new BasicNameValuePair("shenfennum",obj.getUserid());
		NameValuePair pair2 = new BasicNameValuePair("index", String.valueOf(index));
		list.add(pair0);
		list.add(pair1);
		list.add(pair2);
		HttpPost post = new HttpPost("http://117.34.95.207:8077/HM2/serverforzd");
//		HttpPost post = new HttpPost("http://219.245.65.14/HM2/serverforzd");
//		HttpPost post = new HttpPost("http://219.245.65.14/HM2/receiveservlet");
//		HttpPost post = new HttpPost("http://219.245.64.92/server/Servlet");
//		HttpPost post = new HttpPost("http://219.245.64.1:8080/health_union/Servlet");
		HttpResponse response = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");
			post.setEntity(entity);
			response = client.execute(post);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(response == null) {
			LogUtil.i("fail","与服务器连接失败");
			return false;
		}else{
			if (response.getStatusLine().getStatusCode() == 200) {

				try {
					InputStream in = response.getEntity().getContent();
					StringBuffer resultbuffer = new StringBuffer();
					byte[] buffer = new byte[1024];
					int bytes;

					try {
						if ((bytes = in.read(buffer)) > 0) {
							resultbuffer.append(new String(buffer, 0, bytes));
						}
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					in.close();
					if(resultbuffer.length()>3) {
						String searchvalue = "!!!";
						String s = resultbuffer.toString();
						int start = 0;
			        	start = s.indexOf(searchvalue, 0);				
				        String s1 = s.substring(0, start);
				        String s2 = s.substring(start+3, s.length());					
						LogUtil.i("download_state", s1);		
						if(s1.equals("bloodox")){
							MyApplication.sDownBloodox = s2;							       						       								        
					        LogUtil.i("bloodox_down",MyApplication.sDownBloodox);
							return true;//只有返回bloodpre才表示接收血压数据成功
						}else {
							return false;
						}
					  
					}else{
						return false;
					}
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				return false;	
			}
			
		}
		return false;		
	}
}
