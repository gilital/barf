package com.bezeq.locator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "BezeqLocator.db";
	public static final String EQUIPMENT_TABLE_NAME = "Equipment";
	public static final String EQUIPMENT_COLUMN_ID = "id";
	public static final String EQUIPMENT_COLUMN_CNUM = "cnum";
	public static final String EQUIPMENT_COLUMN_NAME = "name";
	public static final String EQUIPMENT_COLUMN_TYPE = "type";
	public static final String EQUIPMENT_COLUMN_LATITUDE = "latitude";
	public static final String EQUIPMENT_COLUMN_LONGITUDE = "longitude";
	public static final String EQUIPMENT_COLUMN_ALTITUDE = "altitude";
	
	public DBHelper(Context context)
	{
	   super(context, DATABASE_NAME , null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlCreateStatement = "CREATE TABLE " + EQUIPMENT_TABLE_NAME 
				  +"(" 
			      + EQUIPMENT_COLUMN_ID + " integer primary key autoincrement, " 
			      + EQUIPMENT_COLUMN_CNUM + " text not null, " 
			      + EQUIPMENT_COLUMN_NAME + " text not null, "
			      + EQUIPMENT_COLUMN_TYPE + " text not null, "
			      + EQUIPMENT_COLUMN_LATITUDE + " real not null, "
			      + EQUIPMENT_COLUMN_LONGITUDE + " real not null, "
			      + EQUIPMENT_COLUMN_ALTITUDE + " real not null "
			      +");";
		db.execSQL(sqlCreateStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + EQUIPMENT_TABLE_NAME);
	    onCreate(db);
	}
	
	
	
}
