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

import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;
import com.example.health.util.User;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.wiipu.voice.domain.VoiceConstant;
import com.wiipu.voice.ui.CanSpeechActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//public class UserListActivity extends CanSpeechActivity
public class UserListActivity extends Activity {
	
	private ListView userList;
	private MyApplication app;
	private static final String TAG = "UserListActivity";
	private UserListAdapter mAdapter;
	private Boolean verify = null;
	private ProgressDialog progressDialog;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			User user = new User();
			switch(msg.what){
			   
				

                  case 3:
                	  Gson gson = new Gson();
  			          Type type = new TypeToken<List<User>>(){}.getType();
		      
                      List<User>  tempList = gson.fromJson(MyApplication.sData, type);
                      app.userList.clear();
                      app.userList.addAll(tempList);
		              progressDialog.dismiss();
                      userList.setAdapter(new UserListAdapter(UserListActivity.this, app.userList));	
	
				break;
				case 30:  
					progressDialog.dismiss();
					Toast.makeText(MyApplication.getContext(), "刷新失败,稍后重试", Toast.LENGTH_SHORT).show();
                                 break;
				case 20://添加失败
					   progressDialog.dismiss();
					   break;
				default:break;
			}
		}
		
	};
/*	static 
	{
		//使用讯飞语音开源框架播放语音，appid为key值。
		VoiceConstant.APPID = "5553fe48";
	}
	*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		app = (MyApplication) getApplication();
		if(app.userList.size()>0) {
			LogUtil.i(TAG ,app.userList.get(0).toString());
		}
		userList = (ListView) findViewById(R.id.data_list);
		mAdapter = new UserListAdapter(this, app.userList);
		userList.setAdapter(mAdapter);
	
//		readContent.read("欢迎使用尊博停车");
		userList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent mainIntent = new Intent();
				mainIntent.setClass(UserListActivity.this, MainActivity.class);
				mainIntent.putExtra("userID",app.userList.get(position).getShenfennum());
				mainIntent.putExtra("userNAME",app.userList.get(position).getName());
				startActivity(mainIntent);
			}
		});
		
		/*userList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserListActivity.this);
				builder.setTitle("删除用户");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if(NetTool.isInternetConnect()) {
									new Thread(){
										@Override
										public void run() {
											Looper.prepare();
											String userId = app.userList.get(position).getShenfennum();
											if(new NetTool().delUser(app.zhongduan_id, userId)){
												 GeneralDbHelper.getInstance(MyApplication.getContext()).deleteUser(userId);
												 mHandler.sendEmptyMessage(1);									 
											}else{
												Toast.makeText(MyApplication.getContext(), "删除失败,稍后重试", Toast.LENGTH_SHORT).show();
											}
											Looper.loop();
										}
										
									}.start();
									
								
								}else {
									 Toast.makeText(MyApplication.getContext(), "请保持网络连接", Toast.LENGTH_SHORT).show();
								}
								 								 
							}
						}

				);
				builder.setNegativeButton("取消", null);
				builder.create().show();
				return true;
			}
		});*/

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_refresh:
			LogUtil.i("zongduanID",app.zhongduan_id);
			
			if(NetTool.isInternetConnect()) {
				 progressDialog = new ProgressDialog(UserListActivity.this);					
				 progressDialog.setMessage("正在刷新，请稍候...");
				 progressDialog.setCancelable(false);
				 progressDialog.show();		
			       new Thread(){
					@Override
					public void run() {
                     if(new NetTool().verifyOfID(app.zhongduan_id)) {
					      Gson gson = new Gson();
			              Type type = new TypeToken<List<User>>(){}.getType();
			                       
			              app.userList = gson.fromJson(MyApplication.sData, type);
			               //身份证ID为主键，所以要先清空，再创建数据库，保存用户信息到本地	
				       	if(GeneralDbHelper.getInstance(MyApplication.getContext()).deleteUserTable()){
				    	   for (User suser :  app.userList) { 
		                               GeneralDbHelper.getInstance(MyApplication.getContext()).save(suser);
		                            }
				       }else{
				    	   LogUtil.i("deleteUserTable", "fail");
				       }				
						     mHandler.sendEmptyMessage(3);
						}else{
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						      mHandler.sendEmptyMessage(30);
                                                } 
							
					}
				}.start();
		
				
				
			 }else{
				 Toast.makeText(UserListActivity.this, "请保持网络连接！" ,Toast.LENGTH_SHORT).show();
			 }
			 return true;   		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class UserListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext;
		private List<User> mDatas;

		public UserListAdapter(Context context, List<User> mDatas)
		{
			mInflater = LayoutInflater.from(context);
			this.mContext = context;
			this.mDatas = mDatas;
		}

		@Override
		public int getCount()
		{
			return mDatas.size();
		}

		@Override
		public Object getItem(int position)
		{
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;  
	        if (convertView == null)  
	        {  
	            convertView = mInflater.inflate(R.layout.userlistitem, parent, false);  
	            viewHolder = new ViewHolder();  
	            viewHolder.mTextView1 = (TextView) convertView.findViewById(R.id.userinfo1);
	            viewHolder.mTextView2 = (TextView) convertView.findViewById(R.id.userinfo2);
	            viewHolder.mTextView3 = (TextView) convertView.findViewById(R.id.userinfo3);
	            /*viewHolder.mTextView4 = (TextView) convertView.findViewById(R.id.userinfo4);
	            viewHolder.mTextView5 = (TextView) convertView.findViewById(R.id.userinfo5);*/
	            convertView.setTag(viewHolder);  
	        } else  
	        {  
	            viewHolder = (ViewHolder) convertView.getTag();  
	        }  
	        
	        viewHolder.mTextView1.setText(mDatas.get(position).getShenfennum());
	        viewHolder.mTextView2.setText(mDatas.get(position).getName());
	        viewHolder.mTextView3.setText(mDatas.get(position).getRegtime().substring(0,11));
	      /*  viewHolder.mTextView4.setText(mDatas.get(position).getAddress());
	        viewHolder.mTextView5.setText(mDatas.get(position).getBirthdate());*/
	        return convertView;  
		}

	}
	
	private final class ViewHolder  
	{  
		TextView mTextView1;
		TextView mTextView2;
		TextView mTextView3;
	/*	TextView mTextView4;
		TextView mTextView5;	*/
	}

}




 
