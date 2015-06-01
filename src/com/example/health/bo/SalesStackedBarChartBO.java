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
package com.example.health.bo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;












import com.example.health.MainActivity;
import com.example.health.MyApplication;
import com.example.health.bp.ItemListActivity;
import com.example.health.util.BloodOx;
import com.example.health.util.BloodPre;
import com.example.health.util.DataModel;
import com.example.health.util.GeneralDbHelper;
import com.example.health.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Sales demo bar chart.
 */
public class SalesStackedBarChartBO extends AbstractDemoChartBO {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Sales stacked bar chart";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "The monthly sales for the last 2 years (stacked bar chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
    String[] titles = new String[] { "血氧", "脉搏" };
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500,
        12600, 14000 });
    values.add(new double[] { 5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, 9500, 10500,
        11600, 13500 });
    int[] colors = new int[] { Color.BLUE, Color.CYAN };
    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
    setChartSettings(renderer, "Monthly sales in the last 2 years", "Month", "Units sold", 0.5,
        12.5, 0, 24000, Color.GRAY, Color.LTGRAY);
    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setXLabelsAlign(Align.LEFT);
    renderer.setYLabelsAlign(Align.LEFT);
    renderer.setPanEnabled(true, false);
    // renderer.setZoomEnabled(false);
    renderer.setZoomRate(1.1f);
    renderer.setBarSpacing(0.5f);
    return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
        Type.STACKED);
  }
  
  	public GraphicalView getView(Context context,int format) {
		boolean setflag;
		boolean defaultFlag = false;
	    String[] titles = new String[] { "血氧(%)", "脉搏"};
		List<BloodOx> GraphicalData = new DataModelBO().getGraphicalData(format);
	  	int size = GraphicalData.size();
	  	String [] xValue = new String[format];
	  	double [] yhpValue = new double[format];
	  	double [] ylpValue = new double[format];
//	  	double [] ypulse = new double[format];
	  	
	  	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	SimpleDateFormat df2 = new SimpleDateFormat("M/dd");
	  	SimpleDateFormat df3 = new SimpleDateFormat("dd");
	  	
	 	for(int i=0; i<format; i++) {
	  		Date time;
	  		Date temp;

			try {
				/*
				 * 从最后一个有效日期开始，向前推算，数据库中有该日期的数据则加载，没有则数据设置为零，表示当天没有进行测量
				*/
				setflag = false;
				if(size > 0)
				{
					time = df1.parse((String) GraphicalData.get(size-1).getTime());
					Calendar cal=Calendar.getInstance();
					cal.setTime(time);
					cal.add(Calendar.DAY_OF_MONTH, -(format-1)+i);
					String sTime;
					if(format==7) {
						sTime = df2.format(cal.getTime());
						LogUtil.i("sTime ",sTime );
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
							yhpValue[i] = Double.parseDouble(GraphicalData.get(j).getOx()+"");
							ylpValue[i] = Double.parseDouble(GraphicalData.get(j).getPulse()+"");
//							ypulse[i] = Double.parseDouble(GraphicalData.get(j).get("pulse").toString());
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
//						ypulse[i] =0;
						defaultFlag = true;
					}
				}
	

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		
	  	}
	  	
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(yhpValue);
	    values.add(ylpValue);
//	    values.add(ypulse);
	    
	    int[] colors = new int[] { Color.CYAN,Color.BLUE};
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	  
	    
	    setChartSettings(renderer, "血氧和脉搏", "", "", 0.5,
	        format+0.5, 0, 250, Color.BLACK, Color.BLACK);
	    if(format <= 7) {
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(18f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(18f);
//	    	 renderer.getSeriesRendererAt(2).setChartValuesTextSize(18f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(false, false);
	         renderer.setZoomEnabled(false, false);
	  	}
	    else if(format == 30){
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(15f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(15f);
//	    	 renderer.getSeriesRendererAt(2).setChartValuesTextSize(15f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(true, false);
	         renderer.setZoomEnabled(true, false);
	    }
	 
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
//	    renderer.getSeriesRendererAt(2).setDisplayChartValues(true);
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

	    // renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);

	    //设置是否显示背景色
	    renderer.setApplyBackgroundColor(true);
	    //设置背景色
	    renderer.setBackgroundColor(Color.argb(0, 220, 228, 234) );
	    //设置报表周边颜色
	    renderer.setMarginsColor(Color.argb(0, 220, 228, 234));
	    

		//虽然这个帖子很久了 但是作为一个程序员 也许其他人也会找到这里 我把找了2天才找到的解决方法贴上来
		//renderer.setLabelsColor(Color.RED);//坐标标题颜色 不是刻度
		renderer.setXLabelsColor(Color.BLACK);//设置X轴刻度颜色
		renderer.setYLabelsColor(0, Color.BLACK);//设置Y轴刻度颜色
	     
	
		 GraphicalView mChartView = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
			      Type.DEFAULT);
		  return mChartView;
	
	   
	  }

  
  

  		public GraphicalView getView(Context context) {
		String[] titles = new String[] { "血氧(%)", "脉搏"};
		BloodOx bloodox = new BloodOx();
    	List<BloodOx> GraphicalData  = (ArrayList<BloodOx>) GeneralDbHelper.getInstance(MyApplication.getContext()).getBeanList(bloodox,MainActivity.userID);
	  	int size = GraphicalData.size();	
//		LogUtil.i("rr",String.valueOf(size));
	  	String [] xValue = new String[size];
	  	double [] yhpValue = new double[size];
		double [] ypulse = new double[size];
	  
	  	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	SimpleDateFormat df2 = new SimpleDateFormat("M月dd日");
	  
	  	
	  	for(int i=0; i<size; i++) {
	  		Date time;
			try {		
				time = df1.parse((String) GraphicalData.get(i).getTime());
				String sTime = df2.format(time);		
				xValue[i] = sTime;
				yhpValue[i] = Double.parseDouble(GraphicalData.get(i).getOx()+"");
				ypulse[i] = Double.parseDouble(GraphicalData.get(i).getPulse()+"");

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		
	  	}
	  	
	  	
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(yhpValue);
	    values.add(ypulse);
	    
	    int[] colors = new int[] {Color.CYAN,Color.BLUE};
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	  
	    
	    setChartSettings(renderer, "血氧和脉搏", "", "", 0.5,
	        size+0.5, 0, 250, Color.BLACK, Color.BLACK);
	     
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);

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
	    
	    if(size <= 10) {
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(20f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(20f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(false, false);
	         renderer.setZoomEnabled(false, false);
	  	}
	    else {
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(15f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(15f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(true, false);
	         renderer.setZoomEnabled(true, false);
	    }

	    // renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);
	  
	 
	  
	  	
	  
  
	    //设置是否显示背景色
	    renderer.setApplyBackgroundColor(true);
	    //设置背景色
	    renderer.setBackgroundColor(Color.argb(0, 220, 228, 234) );
	    //设置报表周边颜色
	    renderer.setMarginsColor(Color.argb(0, 220, 228, 234));
	    

		//虽然这个帖子很久了 但是作为一个程序员 也许其他人也会找到这里 我把找了2天才找到的解决方法贴上来
		//renderer.setLabelsColor(Color.RED);//坐标标题颜色 不是刻度
		renderer.setXLabelsColor(Color.BLACK);//设置X轴刻度颜色
		renderer.setYLabelsColor(0, Color.BLACK);//设置Y轴刻度颜色
	     
	 
	    GraphicalView mChartView = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
        Type.DEFAULT);
	    return mChartView;
	  }
  
  
  	/*	public GraphicalView getView(Context context,int format) {
		boolean setflag;
		boolean defaultFlag = false;
	    String[] titles = new String[] { "收缩压(高压)", "舒张压(低压)","脉搏"};
		ArrayList<Map> GraphicalData = DataModel.getGraphicalData(format);
	  	int size = GraphicalData.size();
	  	String [] xValue = new String[format];
	  	double [] yhpValue = new double[format];
	  	double [] ylpValue = new double[format];
	  	double [] ypulse = new double[format];
	  	
	  	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	SimpleDateFormat df2 = new SimpleDateFormat("M/dd");
	  	SimpleDateFormat df3 = new SimpleDateFormat("dd");
	  	
	 	for(int i=0; i<format; i++) {
	  		Date time;
	  		Date temp;

			try {
				
				 * 从最后一个有效日期开始，向前推算，数据库中有该日期的数据则加载，没有则数据设置为零，表示当天没有进行测量
				
				setflag = false;
				if(size > 0)
				{
					time = df1.parse((String) GraphicalData.get(size-1).get("pdate"));
					Calendar cal=Calendar.getInstance();
					cal.setTime(time);
					cal.add(Calendar.DAY_OF_MONTH, -(format-1)+i);
					String sTime;
					if(format==7) {
						sTime = df2.format(cal.getTime());
						LogUtil.i("sTime ",sTime );
					}
					else {
						sTime = df3.format(cal.getTime());	
					}
					
					for(int j=0; j < size; j++) {
						
						temp = df1.parse((String) GraphicalData.get(j).get("pdate"));
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
							yhpValue[i] = Double.parseDouble(GraphicalData.get(j).get("hp").toString());
							ylpValue[i] = Double.parseDouble(GraphicalData.get(j).get("lp").toString());
							ypulse[i] = Double.parseDouble(GraphicalData.get(j).get("pulse").toString());
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
						ypulse[i] =0;
						defaultFlag = true;
					}
				}
	

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		
	  	}
	  	
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(yhpValue);
	    values.add(ylpValue);
	    values.add(ypulse);
	    
	    int[] colors = new int[] { Color.DKGRAY, Color.CYAN,Color.BLUE};
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	  
	    
	    setChartSettings(renderer, "Blood pressure", "", "", 0.5,
	        format+0.5, 0, 250, Color.BLACK, Color.BLACK);
	    if(format <= 7) {
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(18f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(18f);
	    	 renderer.getSeriesRendererAt(2).setChartValuesTextSize(18f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(false, false);
	         renderer.setZoomEnabled(false, false);
	  	}
	    else if(format == 30){
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(15f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(15f);
	    	 renderer.getSeriesRendererAt(2).setChartValuesTextSize(15f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(true, false);
	         renderer.setZoomEnabled(true, false);
	    }
	 
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(2).setDisplayChartValues(true);
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

	    // renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);

	    //设置是否显示背景色
	    renderer.setApplyBackgroundColor(true);
	    //设置背景色
	    renderer.setBackgroundColor(Color.argb(0, 220, 228, 234) );
	    //设置报表周边颜色
	    renderer.setMarginsColor(Color.argb(0, 220, 228, 234));
	    

		//虽然这个帖子很久了 但是作为一个程序员 也许其他人也会找到这里 我把找了2天才找到的解决方法贴上来
		//renderer.setLabelsColor(Color.RED);//坐标标题颜色 不是刻度
		renderer.setXLabelsColor(Color.BLACK);//设置X轴刻度颜色
		renderer.setYLabelsColor(0, Color.BLACK);//设置Y轴刻度颜色
	     
	 
	    GraphicalView mChartView = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
        Type.DEFAULT);
	    return mChartView;
	  }

    
    
  
  public GraphicalView getView(Context context) {
		String[] titles = new String[] { "收缩压(高压)", "舒张压(低压)", "脉搏"};
		ArrayList<Map> GraphicalData = DatabaseHelper.getInstance(MyApplication.getContext()).getUserList();
	  	int size = GraphicalData.size();	
//		LogUtil.i("rr",String.valueOf(size));
	  	String [] xValue = new String[size];
	  	double [] yhpValue = new double[size];
	  	double [] ylpValue = new double[size];
		double [] ypulse = new double[size];
	  
	  	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	SimpleDateFormat df2 = new SimpleDateFormat("M月dd日");
	  
	  	
	  	for(int i=0; i<size; i++) {
	  		Date time;
			try {		
				time = df1.parse((String) GraphicalData.get(i).get("pdate"));
				String sTime = df2.format(time);		
				xValue[i] = sTime;
				yhpValue[i] = Double.parseDouble(GraphicalData.get(i).get("hp").toString());
				ylpValue[i] = Double.parseDouble(GraphicalData.get(i).get("lp").toString());
				ypulse[i] = Double.parseDouble(GraphicalData.get(i).get("pulse").toString());

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		
	  	}
	  	
	  	
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(yhpValue);
	    values.add(ylpValue);
	    values.add(ypulse);
	    
	    int[] colors = new int[] { Color.DKGRAY, Color.CYAN,Color.BLUE};
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	  
	    
	    setChartSettings(renderer, "Blood pressure", "", "", 0.5,
	        size+0.5, 0, 250, Color.BLACK, Color.BLACK);
	     
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(2).setDisplayChartValues(true);

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
	    
	    if(size <= 10) {
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(20f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(20f);
	    	 renderer.getSeriesRendererAt(2).setChartValuesTextSize(20f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(false, false);
	         renderer.setZoomEnabled(false, false);
	  	}
	    else {
	    	 renderer.getSeriesRendererAt(0).setChartValuesTextSize(15f);
	    	 renderer.getSeriesRendererAt(1).setChartValuesTextSize(15f);
	    	 renderer.getSeriesRendererAt(2).setChartValuesTextSize(15f);
	    	 renderer.setClickEnabled(false);
	         renderer.setPanEnabled(true, false);
	         renderer.setZoomEnabled(true, false);
	    }

	    // renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);
	  
	 
	  
	  	
	  
    
	    //设置是否显示背景色
	    renderer.setApplyBackgroundColor(true);
	    //设置背景色
	    renderer.setBackgroundColor(Color.argb(0, 220, 228, 234) );
	    //设置报表周边颜色
	    renderer.setMarginsColor(Color.argb(0, 220, 228, 234));
	    

		//虽然这个帖子很久了 但是作为一个程序员 也许其他人也会找到这里 我把找了2天才找到的解决方法贴上来
		//renderer.setLabelsColor(Color.RED);//坐标标题颜色 不是刻度
		renderer.setXLabelsColor(Color.BLACK);//设置X轴刻度颜色
		renderer.setYLabelsColor(0, Color.BLACK);//设置Y轴刻度颜色
	     
	 
	    GraphicalView mChartView = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
        Type.STACKED);
	    return mChartView;
	  }*/



}
