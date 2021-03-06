package com.example.health.bp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;












import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;
import com.example.health.util.NetTool;

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


public class FragmentDetailOneItemRight extends Fragment {
	private static final String TAG = "FragmentDetailOneItemRight";
	private EditText hp_pv;  
	private EditText lp_pv;
	private EditText et_pulse;
	private int hp;
	private int lp;
	private int pulse;
	private MyApplication app;
	private BloodPre bloodpre;
	int i = 0;
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view2 = inflater.inflate(R.layout.bp_fragment_item_detail1_manual,  container, false);
		app = (MyApplication) getActivity().getApplication();
	    hp_pv = (EditText) view2.findViewById(R.id.hp_pv);  
        lp_pv = (EditText) view2.findViewById(R.id.lp_pv);
        et_pulse = (EditText) view2.findViewById(R.id.et_pulse);
        Button btn = (Button) view2.findViewById(R.id.input_confirm);
        btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				hp = Integer.parseInt(hp_pv.getText().toString().trim());
				lp = Integer.parseInt(lp_pv.getText().toString().trim());
				pulse = Integer.parseInt(et_pulse.getText().toString().trim());
				
//				Toast.makeText(MyApplication.context, hp, Toast.LENGTH_SHORT).show();
				//打开或创建person.db.db数据库  
				SimpleDateFormat df0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String pdate=df0.format(System.currentTimeMillis()).toString();	
				bloodpre = new BloodPre();
				bloodpre.setHighp(hp);
				bloodpre.setLowp(lp);
				bloodpre.setPulse(pulse);
				bloodpre.setTime(pdate);
				bloodpre.setUserid(MainActivity.userID);
				//保存数据到服务器
				if(NetTool.isInternetConnect()) {
					new Thread(){
						@Override
						public void run() {
							boolean state = new NetTool().uploadData(bloodpre);
							if(!state) {
								app.unSavedBP++;//要用全局变量来统计未同步到服务器的数据

								LogUtil.i("unsaved",app.unSavedBP+MainActivity.userID);
							    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", 0);    
						        Editor editor = sharedPreferences.edit();
						        editor.putString("userIDBP",MainActivity.userID);
						        editor.putInt("unSavedBP", app.unSavedBP);
						        editor.commit(); 
							}
						}
						
					}.start();
				}else {
					app.unSavedBP++;//要用全局变量来统计未同步到服务器的数据
					LogUtil.i("unsaved",app.unSavedBP+MainActivity.userID);
				    SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("runInfo", 0);    
			        Editor editor = sharedPreferences.edit();
			        editor.putString("userIDBP",MainActivity.userID);
			        editor.putInt("unSavedBP", app.unSavedBP);
			        editor.commit(); 
				}					
		        if(GeneralDbHelper.getInstance(MyApplication.getContext()).save(bloodpre)!=-1) {
		        	 Toast.makeText(MyApplication.getContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
		        };
		        
			}
		});
        
        LogUtil.i("right",TAG);
		return view2;
	 }
}



