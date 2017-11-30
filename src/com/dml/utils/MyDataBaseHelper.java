package com.dml.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper{
	//final String sql="create table videoplay1 (_id integer primary key autoincrement ,videotag,videoid,videourl,imgurl,videotitle,playtime)";
	final String sql1="create table videoplay (_id integer primary key autoincrement ,videotag_id VARCHAR UNIQUE,videourl,imgurl,videotitle,playtime)";
	final String sql2="create table videostore (_id integer primary key autoincrement ,videotag_id VARCHAR UNIQUE,videourl,imgurl,videotitle,playtime)";
	public MyDataBaseHelper(Context context, String name,
			 int version) {
		super(context, name, null, version);
		}//

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sql1);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
