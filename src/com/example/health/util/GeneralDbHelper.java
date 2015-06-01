package com.example.health.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author wuzhixin
 * 
 * 目的在于用一个SQLiteOpenHelper子类创建多个表
 */
public class GeneralDbHelper extends SQLiteOpenHelper{

	    private static final String DB_NAME = "person.db"; //数据库名称
	    private static final int version = 1; //数据库版本
	    private static GeneralDbHelper instance = null;
	    private SQLiteDatabase db;

	    //单例模式
	   
	    public static GeneralDbHelper getInstance(Context context) {
	        if(instance == null) {
	            instance = new GeneralDbHelper(context);
	        }
	        return instance;
	    }
	    private void openDatabase() {
	        if(db == null) {
	            db = this.getReadableDatabase();
	        }
	    }
	    public GeneralDbHelper (Context context) {
	        super(context, DB_NAME, null, version);        
	    }
	 
	    
	    /* (non-Javadoc)
	     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	     */
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        // TODO Auto-generated method stub
	        /* StringBuffer tableCreate = new StringBuffer();
	         tableCreate.append("create table user (_id integer primary key autoincrement,")
	         .append("hp int,")
	         .append("lp int)");
	         db.execSQL(tableCreate.toString());
	         */
   		   

	    		 String tableCreate1 = "CREATE TABLE zhongduanuser (shenfennum varchar(255) primary key,name varchar(64),regtime varchar(255),address varchar(255), birthdate varchar(255))";
	    		 db.execSQL(tableCreate1);
	    		 String tableCreate2="create table xueya2 (_id integer primary key autoincrement,userid varchar(255),regdate varchar(64),shousuo int(11),shuzhang int(11),maibo int(11))";
	             db.execSQL(tableCreate2);
	             String tableCreate3="create table xueyang2 (_id integer primary key autoincrement,userid varchar(255),regdate varchar(64),xueyang int(11),maibo int(11))";
	             db.execSQL(tableCreate3);

	         
//	         Toast.makeText(MyApplication.getContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // TODO Auto-generated method stub
	    
			String sql1 = "drop table if exists zhongduanuser";
 	        db.execSQL(sql1);

    		String sql2 = "drop table if exists xueya2";
 	        db.execSQL(sql2);
 	        
 	        String sql3 = "drop table if exists xueyang2";
	        db.execSQL(sql3);
	        onCreate(db);
	    }
	   /* Sqlite清空表数据 
	    delete from TableName;  //清空数据
	    update sqlite_sequence SET seq = 0 where name ='TableName';//自增长ID为0
*/
	    public boolean deleteUserTable() {
	    	String sql4 = "delete from zhongduanuser";
	    	try {
				db.execSQL(sql4);
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	    }
	    
	    public boolean deleteBloodPreTable() {
	    	String sql5 = "delete from xueya2";
	    	try {
				db.execSQL(sql5);
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	    }
	    
	    public boolean deleteBloodOxTable() {
	    	String sql6 = "delete from xueyang2";
	    	try {
				db.execSQL(sql6);
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	    }
	    
	    public long deleteUser(String ID) {
	    	openDatabase();
	    	return db.delete("zhongduanuser", "shenfennum=?", new String[]{ID});  	
	    }
	    public boolean isUserExist(String ID) {
	    	   openDatabase();		       
			   Cursor cursor = db.query("zhongduanuser", new String[]{"shenfennum"},"shenfennum=?", new String[]{ID}, null, null, null);
			    if(cursor.moveToNext()) {
			    	return true;
			    }else{
			    	return false;
			    }
		    
	    }
	    
	    public long save(User user) {
	    	 openDatabase();
		     ContentValues value = new ContentValues();
		     /*private String shenfennum;
	        	private String name;
	        	private String regtime;
	        	private String address;
	        	private String birthdate;*/
	        	
	        	value.put("shenfennum", user.getShenfennum());
	        	value.put("name", user.getName());
	        	value.put("regtime", user.getRegtime());
	        	value.put("address", user.getAddress());
	        	value.put("birthdate", user.getBirthdate());
	        	return db.insert("zhongduanuser", null, value);
        	
	    }

	     public List<User> getBeanList(User obj) {
	        openDatabase();
	       
			 Cursor cursor = db.query("zhongduanuser", null, null, null, null, null, null);
			 List<User> list = new ArrayList<User>();	        	 
			 while (cursor.moveToNext()) {
				 User user = new User();
				 user.setShenfennum(cursor.getString(cursor.getColumnIndex("shenfennum")));
				 user.setName(cursor.getString(cursor.getColumnIndex("name")));
				 user.setRegtime(cursor.getString(cursor.getColumnIndex("regtime")));
				 user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
				 user.setBirthdate(cursor.getString(cursor.getColumnIndex("birthdate")));				
				 list.add(user);		              
			 }
	        return list;
	     }
 
	    public long save(BloodPre obj) {
	    	openDatabase();
		    ContentValues value = new ContentValues();
	
        	/*private String userid;  
     	    private String time;
     	    private int highp;  
     	    private int lowp;
     	    private int pulse;*/
        	value.put("userid", obj.getUserid());
        	value.put("regdate", obj.getTime());
        	value.put("shousuo", obj.getHighp());
        	value.put("shuzhang", obj.getLowp());
        	value.put("maibo", obj.getPulse());
        	return db.insert("xueya2", null, value);
	    
	    }
	    public List<BloodPre> getBeanList(BloodPre obj,String userID) {
	        openDatabase();
	      		        		        	 
//	         String query = "select userid,regdate,shousuo,shuzhang,maibo from xueya2 where userid = "+userID;
        	 Cursor cursor = db.query("xueya2", new String[]{" userid,regdate,shousuo,shuzhang,maibo"},"userid=?", new String[]{userID}, null, null, null);
        	 List<BloodPre> list = new ArrayList<BloodPre>();	        	 
        	 while (cursor.moveToNext()) {
        		 BloodPre bloodpre = new BloodPre();
        		 bloodpre.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
        		 bloodpre.setTime(cursor.getString(cursor.getColumnIndex("regdate")));
        		 bloodpre.setHighp(cursor.getInt(cursor.getColumnIndex("shousuo")));
        		 bloodpre.setLowp(cursor.getInt(cursor.getColumnIndex("shuzhang")));
        		 bloodpre.setPulse(cursor.getInt(cursor.getColumnIndex("maibo")));									
				 list.add(bloodpre);		              
	         }
	        	return list;
	      }
	    public List<BloodPre> getBeanList(BloodPre obj,String userID,int num) {
	        openDatabase();
	       	        		        	 
//	        	 String query = "select userid,regdate,shousuo,shuzhang,maibo from xueya2 order by id desc limit (0,"+num+") where userid = "+userID;
        	 Cursor cursor = db.query("xueya2", new String[]{" userid,regdate,shousuo,shuzhang,maibo"},"userid=?", new String[]{userID}, null, null, null);
        	 List<BloodPre> list = new ArrayList<BloodPre>();
        	 cursor.moveToLast();
        	 for(int i=0; i<num; i++) {
        		 BloodPre bloodpre = new BloodPre();
        		 bloodpre.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
        		 bloodpre.setTime(cursor.getString(cursor.getColumnIndex("regdate")));
        		 bloodpre.setHighp(cursor.getInt(cursor.getColumnIndex("shousuo")));
        		 bloodpre.setLowp(cursor.getInt(cursor.getColumnIndex("shuzhang")));
        		 bloodpre.setPulse(cursor.getInt(cursor.getColumnIndex("maibo")));									
				 list.add(bloodpre);
				 cursor.moveToPrevious();
        	 }        	
        	return list;
	     }
	    
	
	    public long save(BloodOx obj) {
	    	openDatabase();
		    ContentValues value = new ContentValues();
	
        	value.put("userid", obj.getUserid());
        	value.put("regdate", obj.getTime());
        	value.put("xueyang", obj.getOx());
        	value.put("maibo", obj.getPulse());
        	return db.insert("xueyang2", null, value);
	       
	    }

	     public List<BloodOx> getBeanList(BloodOx obj,String userID) {
		        openDatabase();
		       
	//		      String query = "select userid,regdate,xueyang,maibo from xueyang2 where userid = "+userID;
	        	 Cursor cursor = db.query("xueyang2", new String[]{" userid,regdate,xueyang,maibo"},"userid=?", new String[]{userID}, null, null, null);
	        	 List<BloodOx> list = new ArrayList<BloodOx>();	        	 
	        	 while (cursor.moveToNext()) {	        		 
	        		 BloodOx bloodox = new BloodOx();
	        		 bloodox.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
	        		 bloodox.setTime(cursor.getString(cursor.getColumnIndex("regdate")));
	        		 bloodox.setOx(cursor.getInt(cursor.getColumnIndex("xueyang")));
	        		 bloodox.setPulse(cursor.getInt(cursor.getColumnIndex("maibo")));
								
					 list.add(bloodox);		              
		        }
	        	return list;
		       

		   }
	     
	     
	     public List<BloodOx> getBeanList(BloodOx obj,String userID,int num) {
		        openDatabase();
		        
//		        String query = "select userid,regdate,xueyang,maibo from xueyang2 where userid = "+userID;
	        	 Cursor cursor = db.query("xueyang2", new String[]{" userid,regdate,xueyang,maibo"},"userid=?", new String[]{userID}, null, null, null);
	        	 List<BloodOx> list = new ArrayList<BloodOx>();
	        	 cursor.moveToLast();
	        	 for(int i=0; i<num; i++) {
	        		 BloodOx bloodox = new BloodOx();
	        		 bloodox.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
	        		 bloodox.setTime(cursor.getString(cursor.getColumnIndex("regdate")));
	        		 bloodox.setOx(cursor.getInt(cursor.getColumnIndex("xueyang")));
	        		 bloodox.setPulse(cursor.getInt(cursor.getColumnIndex("maibo")));
								
					 list.add(bloodox);		  
					 cursor.moveToPrevious();
	        	 }        	
	        	return list;
	        	
		      
		 }
}



/*  public long save(Object obj) {
openDatabase();
ContentValues value = new ContentValues();
if(obj  instanceof User) {
	private String shenfennum;
	private String name;
	private String regtime;
	private String address;
	private String birthdate;
	
	value.put("shenfennum", ((User)obj).getShenfennum());
	value.put("name", ((User)obj).getName());
	value.put("regtime", ((User)obj).getRegtime());
	value.put("address", ((User)obj).getAddress());
	value.put("birthdate", ((User)obj).getBirthdate());
	return db.insert("zhongduanuser", null, value);
}else if(obj instanceof BloodPre){	        	
	private String userid;  
	    private String time;
	    private int highp;  
	    private int lowp;
	    private int pulse;
	value.put("userid", ((BloodPre)obj).getUserid());
	value.put("regdate", ((BloodPre)obj).getTime());
	value.put("shousuo", ((BloodPre)obj).getHighp());
	value.put("shuzhang", ((BloodPre)obj).getLowp());
	value.put("maibo", ((BloodPre)obj).getPulse());
	return db.insert("xueya2", null, value);
}else if(obj instanceof BloodOx){
	value.put("userid", ((BloodOx)obj).getUserid());
	value.put("regdate", ((BloodOx)obj).getTime());
	value.put("xueyang", ((BloodOx)obj).getOx());
	value.put("maibo", ((BloodOx)obj).getPulse());
	return db.insert("xueyang2", null, value);
}else{
	return 0;
}

}*/