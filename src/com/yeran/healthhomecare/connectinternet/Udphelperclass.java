package com.yeran.healthhomecare.connectinternet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

public class Udphelperclass implements Runnable{

	public Boolean IsThreadDisable = false;// 指示监听线程是否终止
	private WifiManager.MulticastLock lock;
	InetAddress mInetAddress;
	static String IP;
	Handler mhandler = new Handler();
	// UDP服务器监听的端口
	Integer port = 9001;
	DatagramSocket datagramSocket;
	
	public Udphelperclass(WifiManager manager) {
		this.lock = manager.createMulticastLock("UDPwifi");
	}

	public void setInetaddress(String Ip) {
		this.IP = Ip;
	}

	public void StartListen() {

		// 接收的字节大小，客户端发送的数据不能超过这个大小
		byte[] message = new byte[1024];
		try {
			// 建立Socket连接
		//	datagramSocket = new DatagramSocket(port);
			System.out.println("datagramSocket"+datagramSocket.getLocalPort());
			datagramSocket.setBroadcast(true);
			DatagramPacket datagramPacket = new DatagramPacket(message,
					message.length);
			try {
				while (!IsThreadDisable) {
					// 准备接收数据
					Log.d("UDP Demo", "准备接受");
					this.lock.acquire();

					datagramSocket.receive(datagramPacket);
					String strMsg = new String(datagramPacket.getData()).trim();
					Log.d("UDP Demo", datagramPacket.getAddress()
							.getHostAddress().toString()
							+ ":" + strMsg);
					this.lock.release();
				}
			} catch (IOException e) {// IOException
				e.printStackTrace();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public  void send(String message) {
		message = (message == null ? "Hello IdeasAndroid!" : message);
		// 客户端端口号
		int server_port = 12345;
		Log.d("UDP Demo", "UDP发送数据:" + message);
		
		try {
			this.datagramSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		InetAddress local = null;
		try {
			local = InetAddress.getByName("219.245.64.1");
			System.out.println("local" + local.toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		int msg_length = message.length();
		byte[] messageByte = message.getBytes();
		DatagramPacket p = new DatagramPacket(messageByte, msg_length, local,
				server_port);
		try {

			datagramSocket.send(p);
			datagramSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		StartListen();
	}
	
}