package com.example.sinav1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SinaDB {

	private static SinaDB sinaDB;
	private SQLiteDatabase db ;
	private final String dbName ="sinaWeibo"; 
	private final int version =1; 
	
	private SinaDB(Context context) {
		db=new SinaOpenHelper(context, dbName, null, version).getReadableDatabase();
	}
	
	public synchronized static SinaDB getInstance(Context context){
		if(sinaDB==null){
			sinaDB=new SinaDB(context);
		}
		return sinaDB ;
	}
	
	public void save(String jsonStr){
		
		ContentValues values =new ContentValues();
		values.put("json_data", jsonStr );
		db.insert("weibo", null, values);
	}
	
	public String query(){
		
		String str=null ;
		Cursor cursor=db.rawQuery("select json_data from weibo", null);
		while(cursor.moveToNext()){
			str = cursor.getString(cursor.getColumnIndex("json_data"));
		}
		
		return str ;
	}
	
	public void deleteAll(){
		db.execSQL("delete from weibo");
	}

}
