package com.example.health.bo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.bp.ItemListActivity;
import com.example.health.util.BloodOx;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;
import com.example.health.util.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ItemListActivityBO extends FragmentActivity
        implements ItemListFragmentBO.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private FragmentDetailOneBO fragment1;
    private FragmentDetailTwoBO fragment2;
    private FragmentDetailThreeBO fragment3;
    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 2:	//从服务器下载数据成功						
			        	progressDialog.dismiss();			      
						break;
				case 20://从服务器下载数据失败
					   progressDialog.dismiss();
					   Toast.makeText(MyApplication.getContext(), "加载数据失败,稍后重试", Toast.LENGTH_SHORT).show();
					   break;
				default:break;
			}
		}
		
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  /*      
        WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
       Display display = windowManager.getDefaultDisplay();

       int width = display.getWidth();
       int height = display.getHeight();

       //得到设备的大小

        if(width > height) {
        	setContentView(R.layout.bo_activity_item_twopane);
        }
        else {
        	setContentView(R.layout.bo_activity_item_list);
        }*/
        
        setContentView(R.layout.bo_activity_item_list);
        if (findViewById(R.id.item_detail_containerbo) != null) {
     
            mTwoPane = true;
            
            ((ItemListFragmentBO) getSupportFragmentManager()
                    .findFragmentById(R.id.item_listbo))
                    .setActivateOnItemClick(true);
        }
        Intent intent = getIntent();
        ActionBar actionBar = getActionBar();  
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
   /* @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();  
		inflater.inflate(R.menu.bp_main, menu);  
		  
		return super.onCreateOptionsMenu(menu);
	}*/
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bloodox, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
    
    
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
			
		case R.id.action_add30:		
		case R.id.action_add90:	
		case R.id.action_addall:
			if(NetTool.isInternetConnect()) {
				 progressDialog = new ProgressDialog(ItemListActivityBO.this);					
				 progressDialog.setMessage("正在加载数据，请稍候...");
				 progressDialog.setCancelable(false);
				 progressDialog.show();		
				new Thread(){
					@Override
					public void run() {
						BloodOx obj1 = new BloodOx();
						obj1.setUserid(MainActivity.userID);
						if(new NetTool().downloadData(obj1, 1)) {
							Gson gson = new Gson();
					        Type type = new TypeToken<List<BloodOx>>(){}.getType();
					        List<BloodOx>  bloodOxList = new ArrayList<BloodOx>();
					        bloodOxList = gson.fromJson(MyApplication.sDownBloodox, type);								       						       								        
					        //创建数据库，保存用户信息到本地	
					        if(GeneralDbHelper.getInstance(MyApplication.getContext()).deleteBloodOxTable()){
					        	for (BloodOx bo  :  bloodOxList) { 
					                  GeneralDbHelper.getInstance(MyApplication.getContext()).save(bo);
					               }		
						    }else{
						    	   LogUtil.i("deleteBloodOxTable", "fail");
						    }	
					        mHandler.sendEmptyMessage(2);
					        			
						}else{
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mHandler.sendEmptyMessage(20);	
						}
					}
					
				}.start();
				
			}else {
				 Toast.makeText(MyApplication.getContext(), "请保持网络连接", Toast.LENGTH_SHORT).show();
			}
			
			return true;
		default:return true;
		
		}
	}
	
	
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {

            Bundle arguments = new Bundle();
          
            switch(Integer.parseInt(id)) {
            case 1:   
            	arguments.putString(FragmentDetailOneBO.ARG_ITEM_ID, id);
            		if(fragment1 == null) {
            			fragment1 = new FragmentDetailOneBO();
            			fragment1.setArguments(arguments);
            		}
                 
                   if(!fragment1.isAdded()) {
	                   getSupportFragmentManager().beginTransaction()
	                           .replace(R.id.item_detail_containerbo, fragment1,"Detail11")
	                           .commit();
                   }
            	break;
            case 2:
            	  arguments.putString(FragmentDetailOneBO.ARG_ITEM_ID, id);
            	if(fragment2 == null) {
            		fragment2 = new FragmentDetailTwoBO();
            		fragment2.setArguments(arguments);
        		}
               
                
                if(!fragment2.isAdded()) {
	                getSupportFragmentManager().beginTransaction()
	                        .replace(R.id.item_detail_containerbo, fragment2,"Detail22")
	                        .commit();
                }
            	
            	break;
            case 3:
            	arguments.putString(FragmentDetailOneBO.ARG_ITEM_ID, id);
            	if(fragment3 == null) {
            		fragment3 = new FragmentDetailThreeBO();
            		fragment3.setArguments(arguments);
        		}
                if(!fragment3.isAdded()){
                	 getSupportFragmentManager().beginTransaction()
                     .replace(R.id.item_detail_containerbo, fragment3,"Detail33")
                     .commit();
                }
               
            	break;
            }
         

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivityBO.class);
            detailIntent.putExtra(FragmentDetailOneBO.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }



}
