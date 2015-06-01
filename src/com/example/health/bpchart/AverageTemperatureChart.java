package com.example.health.bpchart;


/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;











import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.util.BloodPre;
import com.example.health.util.DataModel;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Average temperature demo chart.
 */
public class AverageTemperatureChart extends AbstractDemoChart {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Average temperature";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "The average temperature in 4 Greek islands (line chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
	String[] titles = new String[] { "Crete", "Corfu", "Thassos", "Skiathos" };
    
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
        13.9 });
    values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
    values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
    values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });
    int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW };
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
        PointStyle.TRIANGLE, PointStyle.SQUARE };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, -10, 40,
        Color.LTGRAY, Color.LTGRAY);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
    XYSeries series = dataset.getSeriesAt(0);
    series.addAnnotation("Vacation", 6, 30);
    Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
        "Average temperature");
    GraphicalView mChartView = ChartFactory.getLineChartView(context, dataset, renderer);
    return intent;
  }
  
  public GraphicalView getView(Context context,int format) {
	  	String[] titles = new String[] { "收缩压(高压)", "舒张压(低压)" };
		List<BloodPre> GraphicalData = new DataModel().getGraphicalData(format);
	  	int size = GraphicalData.size();
	  	String [] xValue = new String[format];
	  	double [] yhpValue = new double[format];
	  	double [] ylpValue = new double[format];
	  
	  	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	SimpleDateFormat df2 = new SimpleDateFormat("MM月dd日");
		SimpleDateFormat df3 = new SimpleDateFormat("dd");
	  	
	  	for(int i=0; i<format; i++) {
	  		Date time;
	  		Date temp;
	  		boolean setflag;
			try {
				/*
				 * 从最后一个有效日期开始，向前推算，数据库中有该日期的数据则加载，没有则数据设置为零，表示当天没有进行测量
				*/
				setflag = false;
				time = df1.parse((String) GraphicalData.get(size-1).getTime());
				Calendar cal=Calendar.getInstance();
				cal.setTime(time);
				cal.add(Calendar.DAY_OF_MONTH, -(format-1)+i);
				String sTime;
				if(format==7) {
					sTime = df2.format(cal.getTime());	
				}
				else {
					sTime = df3.format(cal.getTime());	
				}
				
				for(int j=0; j < size; j++) {
					
					temp = df1.parse((String) GraphicalData.get(j).getTime());
					String sTemp;
					if(format==7) {
						sTemp = df2.format(temp);	
					}
					else {
						sTemp = df3.format(temp);	
					}
			
					if(sTime.equals(sTemp)) {
						setflag = true;
						xValue[i] = sTime;
						yhpValue[i] = Double.parseDouble(GraphicalData.get(j).getHighp()+"");
						ylpValue[i] = Double.parseDouble(GraphicalData.get(j).getLowp()+"");
						break;
					}
					else {
						continue;
					}	
			  	}

				if(!setflag) {
					xValue[i] = sTime;
					yhpValue[i] =0;
					ylpValue[i] =0;	
				}
				
				

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		
	  	}
	  	
	  	List<double[]> x = new ArrayList<double[]>();
	  	if(format == 7) {
	  		 for (int i = 0; i < titles.length; i++) {
	  	       x.add(new double[] { 1, 2, 3, 4, 5, 6, 7 });
	  	     }
	  	}
	  	else if(format == 30) {
	  		for (int i = 0; i < titles.length; i++) {
		  	       x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 
		  	    		  16, 17, 18, 19, 20, 21, 22, 23, 24 ,25, 26,27,28,29,30 });
		  	 }
	  	}
	  	else if(format == 90) {
	  		double[] x1= new double[format];
	  		for(int j=0; j<90; j++) {
	  			x1[j] = j+1;
	  		}	  		
	  		for (int i = 0; i < titles.length; i++) {
		  	       x.add(x1);
		  	 }
	  	}
	  	 
	    
	    
	    List<double[]> values = new ArrayList<double[]>();
	      
	    values.add(yhpValue);
	    values.add(ylpValue);
	   
	    int[] colors = new int[] { Color.RED, Color.BLUE };
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,PointStyle.SQUARE };
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartSettings(renderer, "Blood pressure", "", "", 0, format, 0, 250,
	        Color.LTGRAY, Color.LTGRAY);
	    renderer.setXLabels(0);
	    renderer.setYLabels(10);
	    for(int i=0;i<format;i++)
	       {
	         renderer.addXTextLabel(i+1, xValue[i]); //
	       }
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setZoomButtonsVisible(false);
	    
	    if(format == 7) {
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(false, false);
	         renderer.setZoomEnabled(false, false);
	  	}
	  	else {
	  	    renderer.setClickEnabled(false);
	        renderer.setPanEnabled(true, false);
	        renderer.setZoomEnabled(true, false);
	  	}
	  
        
	    //设置是否显示背景色
	    renderer.setApplyBackgroundColor(true);
	    //设置背景色
	    renderer.setBackgroundColor(Color.argb(0, 220, 228, 234) );
	    //设置报表周边颜色
	    renderer.setMarginsColor(Color.argb(0, 220, 228, 234));


	    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
	    XYSeries series = dataset.getSeriesAt(0);
//	    series.addAnnotation("Vacation", 6, 30);
	    GraphicalView mChartView = ChartFactory.getLineChartView(context, dataset, renderer);
	    return mChartView;
	  }
  
  
  public GraphicalView getView(Context context) {
	  	String[] titles = new String[] { "收缩压(高压)", "舒张压(低压)" };
	  	BloodPre bloodpre = new BloodPre();
    	List<BloodPre> GraphicalData  = (ArrayList<BloodPre>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodpre,MainActivity.userID);
	  	int size = GraphicalData.size();
	  	LogUtil.i("rr",String.valueOf(size));
	  	String [] xValue = new String[size];
	  	double [] yhpValue = new double[size];
	  	double [] ylpValue = new double[size];
	  
	  	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	SimpleDateFormat df2 = new SimpleDateFormat("MM月dd日");
	  
	  	
	  	for(int i=0; i<size; i++) {
	  		Date time;
			try {		
				time = df1.parse((String) GraphicalData.get(i).getTime());
				String sTime = df2.format(time);		
				xValue[i] = sTime;
				yhpValue[i] = Double.parseDouble(GraphicalData.get(i).getHighp()+"");
				ylpValue[i] = Double.parseDouble(GraphicalData.get(i).getLowp()+"");

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		
	  	}
	  	
	  	List<double[]> x = new ArrayList<double[]>();
	  	
  		double[] x1= new double[size];
  		for(int j=0; j<size; j++) {
  			x1[j] = j+1;
  		}	  		
  		for (int i = 0; i < titles.length; i++) {
	  	       x.add(x1);
	  	}
	  
	  	 
	    
	    
	    List<double[]> values = new ArrayList<double[]>();
	      
	    values.add(yhpValue);
	    values.add(ylpValue);
	   
	    int[] colors = new int[] { Color.RED, Color.BLUE };
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,PointStyle.SQUARE };
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartSettings(renderer, "Blood pressure", "", "", 0, size, 0, 250,
	        Color.LTGRAY, Color.LTGRAY);
	    renderer.setXLabels(0);
	    renderer.setYLabels(10);
	    for(int i=0;i<size;i++)
	       {
	         renderer.addXTextLabel(i+1, xValue[i]); //
	       }
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setZoomButtonsVisible(false);
	    
	    if(size <= 7) {
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(false, false);
	         renderer.setZoomEnabled(false, false);
	  	}
	  	else {
	  	    renderer.setClickEnabled(false);
	        renderer.setPanEnabled(true, false);
	        renderer.setZoomEnabled(true, false);
	  	}
	  
      
	    //设置是否显示背景色
	    renderer.setApplyBackgroundColor(true);
	    //设置背景色
	    renderer.setBackgroundColor(Color.argb(0, 220, 228, 234) );
	    //设置报表周边颜色
	    renderer.setMarginsColor(Color.argb(0, 220, 228, 234));


	    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
	    XYSeries series = dataset.getSeriesAt(0);
//	    series.addAnnotation("Vacation", 6, 30);
	    GraphicalView mChartView = ChartFactory.getLineChartView(context, dataset, renderer);
	    return mChartView;
	  }
  
  
  
 
  
  

}
