package com.example.health.bo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;





import java.text.SimpleDateFormat;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.util.BloodPre;
import com.example.health.util.BloodOx;
import com.example.health.util.ConvertHelper;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.io.SerialPort;
import android.io.SerialPort.OnReceiveListener;

import com.example.health.util.NetTool;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentDetailOneItemLeftBO extends Fragment {
	private static final String TAG = "FragmentDetailOneItemLeft";
	
	private TextView et_sp;//血氧
	private TextView et_bp;//脉搏
	private Button re_measure;
/*	private DatagramSocket socket;
	// 定义发送数据报的目的地（UDP协议）IP+端口号
	public static final int DEST_PORT = 12236;
	public static final String DEST_IP = "219.245.64.1";
	// 定义一个用于发送的DatagramPacket对象
	private DatagramPacket outPacket = null;
	// 定义每个数据报的最大大小为4K
	private static final int DATA_LEN = 4096;	
	// 定义接收网络数据的字节数组
	byte[] inBuff = new byte[DATA_LEN];*/
	private int count;
	private boolean measureSucessFlag= false;
	private String str1;
	private String str2;
	private int bpnum;//血压
	private int spnum;//血氧
	private int bpnum_success;//血压
	private int spnum_success;//血氧
	private String[] ss;
	private static SerialPort mSerialPort;
	private StringBuffer  sb = new StringBuffer(80);
		
	private MyApplication app;
	private BloodOx bloodox;
	private boolean onceOver;
	
	Handler myHandler= new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.d("Test", "执行handler");
			if(msg.what==0x123){
				 et_sp.setText(spnum+"%"+"\r\n");
				 et_bp.setText(bpnum+"/min"+"\r\n");
				if(measureSucessFlag) {	    			
	        		//打开或创建person.db.db数据库  
					re_measure.setVisibility(View.VISIBLE);
					SimpleDateFormat df0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String pdate=df0.format(System.currentTimeMillis()).toString();	
					bloodox = new BloodOx();
					bloodox.setOx(spnum);
					bloodox.setPulse(bpnum);
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
				
			}
		}   
	};
	
	private  OnReceiveListener mListener = new OnReceiveListener() {
		public void onReceive(final byte[] data, boolean completed) {
			//以下进行数据解析
    		// TODO Auto-generated method stub
			if(!measureSucessFlag){
				str2 = ConvertHelper.byte2HexString(data);
				System.out.println("receive："+str2);
				sb.append(str2);
				 if(sb.length()==30){
					 str1=sb.substring(0);
					 System.out.println("接收的一帧数据为："+str1);
					 if(str1.length()==30&&str1.charAt(0)=='C'&&str1.charAt(1)=='C'&&str1.charAt(2)=='A'&&
								str1.charAt(3)=='A'&&str1.charAt(5)=='2'&&str1.charAt(28)=='F'&&str1.charAt(29)=='F'){
						 count++;
						 ss = str1.split("",30);					
						 bpnum= Integer.parseInt(ss[25],16)*16 + Integer.parseInt(ss[26],16);
	        			 spnum= Integer.parseInt(ss[27],16)*16 + Integer.parseInt(ss[28],16);
	        			 
	        			 //只要返回数据就会发送handler消息，但会通过 measureSucessFlag来保证只保存一组数据
	        			 Message msg = new Message();
    					 msg.what= 0x123;
    					 myHandler.sendMessage(msg);
	        			
	    				 if(count%5 == 0 && bpnum !=0 && spnum !=0){
	    					 measureSucessFlag = true;
	    					 bpnum_success = bpnum;
	    					 spnum_success = spnum;
	    				 }else{
	    					 measureSucessFlag = false;
	    				 }
	    					 
						
					 }
					 Log.i("RECEIVE","接收到的一帧数据为："+str1);
					 sb.delete(0, 30);//2015年5月13日，这种每次用完清理值得学习，不用每次都新建。
				 }
			}
		
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (MyApplication) getActivity().getApplication();
		open();
	}
		

	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view1 = inflater.inflate(R.layout.bo_fragment_item_detail1_auto,  container, false);
	    str1="";
        str2="";
        et_sp=(TextView)view1.findViewById(R.id.et_sp); 
        et_bp=(TextView)view1.findViewById(R.id.et_bp); 
        re_measure = (Button) view1.findViewById(R.id.bt_remeasure);
     
    	re_measure = (Button) view1.findViewById(R.id.bt_remeasure);
		re_measure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				measureSucessFlag = false;
				count = 0;
	    		re_measure.setVisibility(View.GONE);
	    	/*	//！！！一定不要忘了重新建立StringBuffer,因为程序是靠它的位数判断读取是否成功的
	    	 * 觉得在读线程里面new更合理
	    	 * 
	    		resultbuffer = new StringBuffer();*/

			}
		});
		
		if(measureSucessFlag) {
			re_measure.setVisibility(View.VISIBLE);
			 et_sp.setText(spnum_success+"%"+"\r\n");
			 et_bp.setText(bpnum_success+"/min"+"\r\n");
    		
		}else {
    		re_measure.setVisibility(View.INVISIBLE);
		}
		return view1;
	 }
	 
	
	 private void open() {
			Log.d("Test", "打开串口");
			if (mSerialPort != null) {
				Log.d("Test", "串口不为空，先关闭");
				mSerialPort.close();//关闭串口
			}
			mSerialPort = SerialPort.open("/dev/ttyS3", 1024, true);
			if (mSerialPort != null) {
				Log.d("Test", "打开串口，并进行相应波特率、奇偶校验位，数据位，停止位的设置");
				mSerialPort.setSettings(9600,'n',8,1);
				mSerialPort.setOnReceiveListener(mListener);
			}
	 }

}
