package com.example.health.bo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;







import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.util.BloodOx;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;
import com.example.health.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FragmentDetailOneItemRightBO extends Fragment {
	private static final String TAG = "FragmentDetailOneItemRightBO";
	private EditText et_ox;  
	private EditText et_pulse;
	private int ox;
	private int pulse;
	private MyApplication app;
	private BloodOx bloodox;
	int i = 0;
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view2 = inflater.inflate(R.layout.bo_fragment_item_detail1_manual,  container, false);
		app = (MyApplication) getActivity().getApplication(); 
        et_ox = (EditText) view2.findViewById(R.id.et_ox);
        et_pulse = (EditText) view2.findViewById(R.id.et_pulse);
        Button btn = (Button) view2.findViewById(R.id.input_confirm);
        btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ox = Integer.parseInt(et_ox.getText().toString().trim());
				pulse = Integer.parseInt(et_pulse.getText().toString().trim());
				
//				Toast.makeText(MyApplication.context, hp, Toast.LENGTH_SHORT).show();
				//打开或创建person.db.db数据库  
				SimpleDateFormat df0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String pdate=df0.format(System.currentTimeMillis()).toString();	
				bloodox = new BloodOx();
				bloodox.setPulse(pulse);
				bloodox.setOx(ox);
				bloodox.setTime(pdate);
				bloodox.setUserid(MainActivity.userID);
				//保存数据到服务器
				if(NetTool.isInternetConnect()) {
					new Thread(){
						@Override
						public void run() {
							boolean state = new NetTool().uploadData(bloodox);
							if(!state) {
								app.unSavedBO++;//要用全局变量来统计未同步到服务器的数据

								LogUtil.i("unsaved",app.unSavedBO+MainActivity.userID);
							    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", 0);    
						        Editor editor = sharedPreferences.edit();
						        editor.putString("userIDBO",MainActivity.userID);
						        editor.putInt("unSavedBO", app.unSavedBO);
						        editor.commit(); 
							}
						}
						
					}.start();
				}else {
					app.unSavedBO++;//要用全局变量来统计未同步到服务器的数据
					LogUtil.i("unsaved",app.unSavedBO+"");
				    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", 0);    
			        Editor editor = sharedPreferences.edit();
			        editor.putString("userIDBO",MainActivity.userID);
			        editor.putInt("unSavedBO", app.unSavedBO);
			        editor.commit(); 
				}					
		        if(GeneralDbHelper.getInstance(MyApplication.getContext()).save(bloodox)!=-1) {
		        	 Toast.makeText(MyApplication.getContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
		        };
		        
		        
			}
		});
        
        LogUtil.i("right",TAG);
		return view2;
	 }
}



