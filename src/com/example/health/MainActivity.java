package com.example.health;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.health.util.CircleMenuLayout;
import com.example.health.util.CircleMenuLayout.OnMenuItemClickListener;
import com.example.health.util.CircleMenuLayout.OnMenuItemTouchListener;
import com.example.health.util.LogUtil;
import com.wiipu.voice.domain.VoiceConstant;
import com.wiipu.voice.ui.CanSpeechActivity;
import com.yeran.healthhomecare.ItemListActivity;
public class MainActivity extends Activity{

	
		private CircleMenuLayout mCircleMenuLayout;
		private ImageView mImageView;
		private MyApplication app;
		public static String userID;
		private String userName;
		private String[] mItemTexts = new String[] {  "摔倒","血压", "血氧" };
		private int[] mItemImgs = new int[] { R.drawable.home_mbank_3_normal,
				R.drawable.home_mbank_4_normal,R.drawable.home_mbank_6_normal };
		
		private int[] mItemImgsClicked = new int[] { R.drawable.home_mbank_3_clicked,
				R.drawable.home_mbank_4_clicked,R.drawable.home_mbank_6_clicked};
	
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			//自已切换布局文件看效果
			setContentView(R.layout.activity_main);
		    ActionBar actionBar = getActionBar();  
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
			app = (MyApplication) getApplication();
			Intent intent = getIntent();
	    	userID = intent.getStringExtra("userID");
		
			userName = intent.getStringExtra("userNAME");
			LogUtil.i("user", userID+userName);
			mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
			mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
			
//			Intent intent2 = new Intent("UdpService");
//			intent.putExtra("askwords", "startwork");
//			startService(intent2);
			
			mCircleMenuLayout.setOnMenuItemTouchListener(new OnMenuItemTouchListener(){

				@Override
				public void itemCenterTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean itemTouch(View v, MotionEvent event, int pos) {
					 if(event.getAction()==MotionEvent.ACTION_UP){  
						 ((ImageView) v).setImageResource(mItemImgs[pos]);  
		             } else {  
		            	 ((ImageView) v).setImageResource(mItemImgsClicked[pos]); 
		             } 
					 if(mCircleMenuLayout.isDisClick()) {
						 return true;
					 }else {
						 return false;
					 }	
				}
				
			});
			mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener()
			{
				
				@Override
				public void itemClick(View view, int pos)
				{
					/*Toast.makeText(MainActivity.this, mItemTexts[pos],
							Toast.LENGTH_SHORT).show();*/
					switch(pos) {
						case 0: 
							/*Intent intent2 = new Intent(MainActivity.this,
									ItemListActivity.class);
							startActivity(intent2);*/
							break;
						case 1:				
							   Intent bp = new Intent(MainActivity.this,com.example.health.bp.ItemListActivity.class);
							   bp.putExtra("userID", userID);
							   startActivity(bp);
							   break;
						case 2: Intent bo = new Intent(MainActivity.this,com.example.health.bo.ItemListActivityBO.class);
							   bo.putExtra("userID", userID);
							   startActivity(bo);
							   break;						
						
					}

				}
				
				@Override
				public void itemCenterClick(View view)
				{
					Toast.makeText(MainActivity.this, userName, Toast.LENGTH_SHORT).show();
				}
			});
			
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:return true;
			}
		}

	
}
