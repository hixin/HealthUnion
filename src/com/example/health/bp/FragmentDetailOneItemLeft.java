package com.example.health.bp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.UUID;

import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.util.BloodPre;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;








import com.example.health.util.NetTool;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


public class FragmentDetailOneItemLeft extends Fragment {
	private static final String TAG = "FragmentDetailOneItemLeft";
	static String BlueToothAddress = "";
	public static final String ARG_ITEM_ID = "item_id";
	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
	
	private BluetoothServerSocket mserverSocket = null;
	private clientThread clientConnectThread = null;
	private BluetoothSocket socket = null;
	private BluetoothDevice device = null;
	private readThread mreadThread = null;;	
	private BluetoothAdapter mBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private final int REQUEST_ENABLE_BT = 123;
	private StringBuffer resultbuffer;
	private boolean exist = false;
	private boolean restoreDataFlag= false;
	private boolean measureSucessFlag= false;
	private int failtime;
	private TextView tv;
	private TextView tv_hp;
	private TextView tv_lp;
	private TextView tv_pulse;
	private Button re_measure;
	
	
	private String stv="";
	private String stv_hp="";
	private String stv_lp="";
	private String stv_pulse="";
	
	private boolean bluetoothThread  = false;//用来实现退出测量界面时关闭蓝牙线程
	
	private MyApplication app;
	private BloodPre bloodpre;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (MyApplication) getActivity().getApplication();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		if (mBluetoothAdapter == null) {
			Toast.makeText(MyApplication.getContext(), "该设备没有蓝牙设备", Toast.LENGTH_LONG).show();
			return;
		}
		//---------------------------------------------------------------------
		// 打开蓝牙设备
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
            	if(device.getName().contains("BP:")){
            		BlueToothAddress = device.getAddress();
            	}
            }
        } else {
        	
        	 // 注册用以接收到已搜索到的蓝牙设备的receiver  
            IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);  
            MyApplication.getContext().registerReceiver(mReceiver, mFilter);  
            // 注册搜索完时的receiver  
            mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  
            MyApplication.getContext().registerReceiver(mReceiver, mFilter); 
        	mBluetoothAdapter.startDiscovery(); 
        }
        
        bluetoothThread  = false;
        
        device = mBluetoothAdapter.getRemoteDevice(BlueToothAddress);
	
		
		if( device.getBondState() ==  BluetoothDevice.BOND_BONDED  && !measureSucessFlag  && !bluetoothThread)
		{
			clientConnectThread = new clientThread();
			clientConnectThread.start();
		}
		
		LogUtil.i("left","onCreate");
		
      }
	
	   // Create a BroadcastReceiver for ACTION_FOUND 
		private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@SuppressLint("NewApi") public void onReceive(Context context, Intent intent) { 
				String action = intent.getAction();         
				// When discovery finds a device         
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent 
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					 if(pairedDevices.contains(device)){
		                	mBluetoothAdapter.cancelDiscovery(); 
		 					return;
		 			 }
					
					// 已经配对的则跳过
	                 if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	                	 if(device.getName().contains("BP:")){
	                 		BlueToothAddress = device.getAddress();
	                 		device.createBond();
	                 		exist = true;
	                 	}
	                 }
	                
					
				}else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {  //搜索结束
	                if (exist) {
	                    Toast.makeText(MyApplication.getContext(),"没有搜索到设备", Toast.LENGTH_SHORT).show();
	                }
	            }
			} 
		
		};
		

	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
//		 LogUtil.i("position",getParentFragment().getArguments().getString(ARG_ITEM_ID));
		View view1 = inflater.inflate(R.layout.bp_fragment_item_detail1_auto,  container, false);
		tv = (TextView) view1.findViewById(R.id.indicate);
		tv_hp = (TextView) view1.findViewById(R.id.tv_hp);
		tv_lp = (TextView) view1.findViewById(R.id.tv_lp);
		tv_pulse = (TextView) view1.findViewById(R.id.tv_pulse);
		
		re_measure = (Button) view1.findViewById(R.id.bt_remeasure);
		re_measure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				measureSucessFlag = false;
				Message msg = new Message();
				msg.obj = "请重启血压计...";
				msg.what = 3;
				LinkDetectedHandler.sendMessage(msg);
				tv_hp.setVisibility(View.INVISIBLE);
	    		tv_lp.setVisibility(View.INVISIBLE);
	    		tv_pulse.setVisibility(View.INVISIBLE);
	    		re_measure.setVisibility(View.GONE);
	    	/*	//！！！一定不要忘了重新建立StringBuffer,因为程序是靠它的位数判断读取是否成功的
	    	 * 觉得在读线程里面new更合理
	    	 * 
	    		resultbuffer = new StringBuffer();*/

			}
		});
		
		if(measureSucessFlag) {
			re_measure.setVisibility(View.VISIBLE);
    		tv_hp.setVisibility(View.VISIBLE);
    		tv_lp.setVisibility(View.VISIBLE);
    		tv_pulse.setVisibility(View.VISIBLE);
    		tv.setText("测量成功");
    		tv_hp.setText("收缩压："+ stv_hp);
    		tv_lp.setText("舒张压："+ stv_lp);
    		tv_pulse.setText("脉搏："+ stv_pulse);
		}else {
			tv_hp.setVisibility(View.INVISIBLE);
    		tv_lp.setVisibility(View.INVISIBLE);
    		tv_pulse.setVisibility(View.INVISIBLE);
    		re_measure.setVisibility(View.GONE);
		}
		
		LogUtil.i("left",TAG);
		return view1;
	 }
	 
	@Override
	public void onDestroyView() {
		
		if(stv.contains("A000")) {
			restoreDataFlag = true;
		}	
		bluetoothThread = true;
		super.onDestroyView();
	}

	 
	 
	//开启客户端
		private class clientThread extends Thread { 
			public void run() {
				try {
//					listenUsingRfcommWithServiceRecord() 是在android设备作为服务端时使用的；createRfcommSocketToServiceRecord是作为客户端时使用的。
					if(bluetoothThread) {
						return;
					}
					
					if(device != null ) {
						socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
						//连接
					    socket.connect();
					}
				    
				  if(socket.isConnected()) {
				    Message msg= new Message();
					msg.obj = "已经连接上血压计，请稍候...";
					msg.what = 0;
					LinkDetectedHandler.sendMessage(msg);
				  }
			
				} 
				catch (IOException e) 
				{
					Log.d("connect fail", "", e);//血压计关闭时报错信息
					if(!measureSucessFlag) {
						try {
							Thread.sleep(1000);
							Message msg = new Message();
							msg.obj = "连接异常！正在重连...";
							msg.what = 1;
							LinkDetectedHandler.sendMessage(msg);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					}

				} 
			}
		};
		
		private Handler LinkDetectedHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	//Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
	        	if(msg.what==0)
	        	{
	        		tv.setText(msg.obj.toString());	        		
	        		//启动接受数据
	        		if(socket.isConnected()) {
						mreadThread = new readThread();
						mreadThread.start();	  
	        		}
	        	}
	        	else if(msg.what==1)//连接失败时，自动重连
	        	{
	        		tv.setText(msg.obj.toString());
	        		
	        		try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		clientConnectThread = new clientThread();
					clientConnectThread.start();
	        		
	
	        	}
	        	
	        	else if(msg.what==2)
	        	{
	        		stv = msg.obj.toString();
	        		tv.setText("测量成功");
	        		measureSucessFlag = true;
	        		re_measure.setVisibility(View.VISIBLE);
	        		tv_hp.setVisibility(View.VISIBLE);
	        		tv_lp.setVisibility(View.VISIBLE);
	        		tv_pulse.setVisibility(View.VISIBLE);
	        		
	        		stv_hp = Integer.parseInt(stv.substring(4,8), 16)+"";
	        		stv_lp = Integer.parseInt(stv.substring(8,10), 16)+"";
	        		stv_pulse = Integer.parseInt(stv.substring(10,12), 16)+"";
	        	
	        		tv_hp.setText("收缩压："+ stv_hp);
	        		tv_lp.setText("舒张压："+ stv_lp);
	        		tv_pulse.setText("脉搏："+ stv_pulse);
	        		
	        		//打开或创建person.db.db数据库  
					SimpleDateFormat df0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String pdate=df0.format(System.currentTimeMillis()).toString();	
					bloodpre = new BloodPre();
					bloodpre.setHighp(Integer.parseInt(stv_hp));
					bloodpre.setLowp(Integer.parseInt(stv_lp));
					bloodpre.setPulse(Integer.parseInt(stv_pulse));
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
						LogUtil.i("unsaved",app.unSavedBP+"");
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
	        	
	        	else if(msg.what==3)//重新测量
	        	{
	        		measureSucessFlag = false;
	        		tv.setText(msg.obj.toString());
	        		clientConnectThread = new clientThread();
					clientConnectThread.start();		
	        	}
				
	        }
	        
	    };    
	    
	  //读取数据
	    private class readThread extends Thread { 
	    	
	        public void run() {
	        	resultbuffer = new StringBuffer();
	            byte[] buffer = new byte[1024];
	            int bytes;
	            InputStream mmInStream = null;
	            
				try {
					mmInStream = socket.getInputStream();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				while(true) {
					// Read from the InputStream
	                try {
						if( (bytes = mmInStream.read(buffer)) > 0 )  {
							resultbuffer.append(new String(buffer,0,bytes));
						}
						
						if(resultbuffer.length() == 32) {
                    	    String s = resultbuffer.toString();
                    	    LogUtil.i(TAG,s);
							Message msg = new Message();
							msg.obj = s;
							msg.what = 2;
							LinkDetectedHandler.sendMessage(msg);
							return;
					   }
	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//如果在已连接的情况下，血压计突然关闭，会导致读不到数据
//						 e.printStackTrace();
						 if(!measureSucessFlag) {
							 Message msg = new Message();
							 msg.obj = "连接异常！正在重连...";
							 msg.what = 1;
							 LinkDetectedHandler.sendMessage(msg);
							 return;
						 }
						 else {
							 return;
						 }

					}
				}
            
	        }
	    }

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			bluetoothThread = true;
			super.onPause();
		}

}
