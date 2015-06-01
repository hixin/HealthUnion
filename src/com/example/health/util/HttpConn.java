package com.example.health.util;
/*
血氧专用
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpConn {
	private DataInputStream dis;
    private DataOutputStream dos;
    private HttpURLConnection hc;
	private static final String POST_URL = "http://219.245.65.143:8080/2embed-servlets/servlet/TestServlet";
	private ArrayList<String> PostList = new ArrayList<String>();
	boolean checking = false;
	public HttpConn()
	{
		hc = null;
		dos = null;
		dis = null;
	}

	//建立连接
	public boolean creatConn(){
		try{ 
		URL url= new URL(POST_URL);
		hc = (HttpURLConnection)url.openConnection();	
		hc.setUseCaches(false);  
		hc.setRequestProperty("Content-Language", "en-US" ); 
		hc.setRequestProperty("Accept-Charset", "UTF-8" );
		hc.setRequestProperty("Content-type", "application/x-java-serialized-object");
		hc.setDoOutput(true);
        hc.setDoInput(true);
        hc.setReadTimeout(20000);   
        hc.setConnectTimeout(20000); 
        hc.setRequestMethod("POST"); 
		}      
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//关闭连接
	public void close() 
	{ 
		try {
			if(dis!=null)dis.close();
			if(dos!=null)dos.close();
			if(hc!=null){
				hc.disconnect();
				hc=null;
			}
		} catch (IOException e) {
		}
	} 
	//POST方式传递字符串
	public void post(String arg){
		PostList.add(arg);        //添加到发送缓冲区
		if(checking == false){
         	checking = true;
         	new Thread(new postRunnable()).start();
         }
	}
	class postRunnable implements Runnable {
		String arg;
    
		public void run(){ 
			while(PostList.size()>0){
				arg = (String)PostList.get(0);
				PostList.remove(0);
				try{
					if(hc==null){
						creatConn();
					}
					dos =  new DataOutputStream(hc.getOutputStream()); 
					dos.writeUTF(arg);
					dos.flush();
					dis = new DataInputStream(hc.getInputStream());   
					close();                    //关闭所有连接
				}catch(EOFException e){
					e.printStackTrace();
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}catch (ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
				}   //异常响应，长度过长
				finally{ 
					
				}
			}
			checking = false;
		}
	}
	
}