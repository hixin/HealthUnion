package com.yeran.healthhomecare.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 本地摔倒信息数据库
 * */
public class LocalDB extends SQLiteOpenHelper{
	private final static int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "shuaidaodata.db";
	private final static String DATABASE_TABLE="sddevices";
	private final static String DATABASE_SDD="sdinfors";
	public LocalDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public LocalDB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
/**
 * 创建数据库
 * */
	@Override
	public void onCreate(SQLiteDatabase shuaidaodb) {
		// TODO Auto-generated method stub
		//创建摔倒设备表
		String sql = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "(" 
				+ "[_id] AUTOINC,"
				+ "[name] VARCHAR(20) NOT NULL ON CONFLICT FAIL,"
				+ "[deviceid] int(10) NOT NULL ON CONFLICT FAIL,"
				+ "[oldphone] int(11) NOT NULL ON CONFLICT FAIL,"
				+ "[shuaidaophone] int(11) NOT NULL ON CONFLICT FAIL,"
				+ "[secret] VARCHAR(8) NOT NULL ON CONFLICT FAIL,"
				+ "[createtime] VARCHAR(17) NOT NULL ON CONFLICT FAIL,"
				+ "CONSTRAINT [sqlite_autoindex_sddevices_1] PRIMARY KEY ([_id]))";
		
		//sql2创建摔倒位置表
		String sql2 = "CREATE TABLE IF NOT EXISTS " + DATABASE_SDD + "(" 
				+ "[_id] AUTOINC,"
				+ "[name] VARCHAR(20) NOT NULL ON CONFLICT FAIL,"
				+ "[deviceid] int(10) NOT NULL ON CONFLICT FAIL,"
				+ "[weidu] double(10,6) NOT NULL ON CONFLICT FAIL,"
				+ "[jingdu] double(10,6) NOT NULL ON CONFLICT FAIL,"
				+ "[weizhi] VARCHAR(30) NOT NULL ON CONFLICT FAIL,"
				+ "[time] VARCHAR(17) NOT NULL ON CONFLICT FAIL,"
				+ "CONSTRAINT [sqlite_autoindex_sdinfors_1] PRIMARY KEY ([_id]))";

		shuaidaodb.execSQL(sql);
		shuaidaodb.execSQL(sql2);
	}

	/**
	 * 版本更新
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase  db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	// ִ��select���
		public Cursor query(String sql, String[] args)
		{
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, args);
			return cursor;
		}
		
		//ִ��insert��update��delete��SQL���
		public void execSQL(String sql,Object[] args){
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(sql, args);
		}
		//ִ��insert��update��delete��SQL���
		public void execSQL(String sql){
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(sql);
		}

}
