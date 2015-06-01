package com.yeran.healthhomecare.connectinternet;

public interface Threadinterface {
	/**
	 * 设置参数
	 * */
	public void setparams(String[] deviceid);
	/**
	 * 开启线程，和服务器进行交互
	 * @return 
	 * */
	public Boolean startthread();
	/**
	 * 具体的和servlet的连接和传值
	 * */
	public boolean downorup();

}
