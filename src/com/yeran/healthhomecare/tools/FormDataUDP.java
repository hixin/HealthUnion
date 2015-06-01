package com.yeran.healthhomecare.tools;
/**
 * 生成指定类型的数据格式与服务器端进行识别交互
 * */
public class FormDataUDP {
	
	private String askwords=null;
	private String Askforlocation = "Askforlocation";
	private String Askforhisroute = "Askforhisroute";
	public String getAskwords(String order,Object words){
		if (order.equals(Askforlocation)) {
			//生成获取位置信息的数据格式
			askwords = "SD:AL:ID:"+words.toString()+":ED" ;
		}else if(order.equals(Askforhisroute)){
			askwords = "SD:AR:ID:"+words.toString()+":ED" ;
		}
		return askwords;
		
	}

}
