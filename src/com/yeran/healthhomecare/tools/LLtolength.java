package com.yeran.healthhomecare.tools;

/**
 * http://www.cnblogs.com/ycsfwhh/archive/2010/12/20/1911232.html 经纬度转距离
 * */

public class LLtolength {
	private final double Earth_Radius = 6378137.0;

	public double getLength(double latitude1, double longitude1,
			double latitude2, double longitude2) {
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * Earth_Radius;
		s = Math.round(s * 10000) / 10000;
		return s;

	}

	private double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/***
	 * 地球是一个近乎标准的椭球体，它的赤道半径为6378.140千米，极半径为
	 * 6356.755千米，平均半径6371.004千米。如果我们假设地球是一个完美的球体，那么它的半径就是地球的平均半径，记为R。如果以0度经线为基
	 * 准，那么根据地球表面任意两点的经纬度就可以计算出这两点间的地表距离（这里忽略地球表面地形对计算带来的误差，仅仅是理论上的估算值）。设第一点A的经
	 * 纬度为(LonA, LatA)，第二点B的经纬度为(LonB,
	 * LatB)，按照0度经线的基准，东经取经度的正值(Longitude)，西经取经度负值(-Longitude)，北纬取90-纬度值(90-
	 * Latitude)，南纬取90+纬度值(90+Latitude)，则经过上述处理过后的两点被计为(MLonA, MLatA)和(MLonB,
	 * MLatB)。那么根据三角推导，可以得到计算两点距离的如下公式：
	 * 
	 * C = sin(MLatA)*sin(MLatB)*cos(MLonA-MLonB) + cos(MLatA)*cos(MLatB)
	 * 
	 * Distance = R*Arccos(C)*Pi/180
	 * 
	 * 这里，R和Distance单位是相同，如果是采用6371.004千米作为半径，那么Distance就是千米为单位，如果要使用其他单位，比如mile
	 * ，还需要做单位换算，1千米=0.621371192mile
	 * 
	 * 如果仅对经度作正负的处理，而不对纬度作90-Latitude(假设都是北半球，南半球只有澳洲具有应用意义)的处理，那么公式将是：
	 * 
	 * C = sin(LatA)*sin(LatB) + cos(LatA)*cos(LatB)*cos(MLonA-MLonB)
	 * 
	 * Distance = R*Arccos(C)*Pi/180
	 * 
	 * 以上通过简单的三角变换就可以推出。
	 * 
	 * 如果三角函数的输入和输出都采用弧度值，那么公式还可以写作：
	 * 
	 * C = sin(LatA*Pi/180)*sin(LatB*Pi/180) +
	 * cos(LatA*Pi/180)*cos(LatB*Pi/180)*cos((MLonA-MLonB)*Pi/180)
	 * 
	 * Distance = R*Arccos(C)*Pi/180
	 * 
	 * 也就是：
	 * 
	 * C = sin(LatA/57.2958)*sin(LatB/57.2958) +
	 * cos(LatA/57.2958)*cos(LatB/57.2958)*cos((MLonA-MLonB)/57.2958)
	 * 
	 * Distance = R*Arccos(C) = 6371.004*Arccos(C) kilometer =
	 * 0.621371192*6371.004*Arccos(C) mile = 3958.758349716768*Arccos(C) mile
	 * 
	 * 
	 * */
}
