/*package com.example.health.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.health.util.BloodPre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
//早期版本，没有参考性，最终版本的程序中没有使用这个
public class DatabaseHelper extends SQLiteOpenHelper {
	 
    private static final String DB_NAME = "person.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private static DatabaseHelper instance = null;
    private SQLiteDatabase db0;

    //单例模式
   
    public static DatabaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }
    private void openDatabase() {
        if(db0 == null) {
            db0 = this.getReadableDatabase();
        }
    }
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }
 
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
         StringBuffer tableCreate = new StringBuffer();
         tableCreate.append("create table user (_id integer primary key autoincrement,")
         .append("hp int,")
         .append("lp int)");
         db.execSQL(tableCreate.toString());
         
    	 String tableCreate = new String();
    	 tableCreate="create table bloodpre (_id integer primary key autoincrement,name varchar(16),pdate text,hp int,lp int,pulse int)";
         db.execSQL(tableCreate);
         
//         Toast.makeText(MyApplication.getContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "drop table if exists bloodpre";
        db.execSQL(sql);
        onCreate(db);
    }
    
    public long save(BloodPre user) {
        openDatabase();
        ContentValues value = new ContentValues();
        value.put("name", user.name);
        value.put("pdate",user.time);
        value.put("hp", user.highp);
        value.put("lp", user.lowp);
        value.put("pulse", user.pulse);
        return db0.insert("bloodpre", null, value);
    }
    
     *//**
     * @早期版本，封装的不是很合理。应该简洁返回ArrayList<BloodPre>，更合理的请参考GeneralDbHelper
     *//*
    public ArrayList getUserList() {
        openDatabase();
        Cursor cursor = db0.query("bloodpre", null, null, null, null, null, null);
        ArrayList<Map> list = new ArrayList();
        while (cursor.moveToNext()) {
                HashMap map = new HashMap();
                map.put("name", cursor.getInt(cursor.getColumnIndex("name")));
                map.put("pdate", cursor.getString(cursor.getColumnIndex("pdate")));
                map.put("hp", cursor.getInt(cursor.getColumnIndex("hp")));
                map.put("lp", cursor.getInt(cursor.getColumnIndex("lp")));
                map.put("pulse", cursor.getInt(cursor.getColumnIndex("pulse")));
                list.add(map);
        }
        return list;
    }
}*/