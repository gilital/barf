package com.bezeq.locator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class for creating and upgrading the "BezeqLocator.db" Data base
 * @author Silver
 *
 */
public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "BezeqLocator.db";
	
	public static final String EQUIPMENT_TABLE_NAME = "Equipment";
	public static final String EQUIPMENT_COLUMN_ID = "id";
	public static final String EQUIPMENT_COLUMN_AREA = "area";
	public static final String EQUIPMENT_COLUMN_EXCHANGE_NUMBER = "exnum";
	public static final String EQUIPMENT_COLUMN_SETTLEMENT = "settlement";
	public static final String EQUIPMENT_COLUMN_STREET = "street";
	public static final String EQUIPMENT_COLUMN_BUILDING_NUMBER = "bnum";
	public static final String EQUIPMENT_COLUMN_BUILDING_SIGN = "bsign";
	public static final String EQUIPMENT_COLUMN_EQUIPMENT_TYPE = "equip_type";
	public static final String EQUIPMENT_COLUMN_LATITUDE = "latitude";
	public static final String EQUIPMENT_COLUMN_LONGITUDE = "longitude";
	public static final String EQUIPMENT_COLUMN_ALTITUDE = "altitude";
	
	public static final String CHANGES_TABLE_NAME = "Changes";
	public static final String CHANGES_COLUMN_ID = "id";
	public static final String CHANGES_COLUMN_TECH_ID = "tech_id";
	public static final String CHANGES_COLUMN_AREA = "area";
	public static final String CHANGES_COLUMN_EQUIPMENT_EXCHANGE_NUMBER = "exnum";
	public static final String CHANGES_COLUMN_SETTLEMENT = "settlement";
	public static final String CHANGES_COLUMN_STREET = "street";
	public static final String CHANGES_COLUMN_BUILDING_NUMBER = "bnum";
	public static final String CHANGES_COLUMN_BUILDING_SIGN = "bsign";
	public static final String CHANGES_COLUMN_EQUIPMENT_TYPE = "equip_type";
	public static final String CHANGES_COLUMN_STATUS = "status";
	public static final String CHANGES_COLUMN_LATITUDE = "latitude";
	public static final String CHANGES_COLUMN_LONGITUDE = "longtitude";
	public static final String CHANGES_COLUMN_ALTITUDE = "altitude";
	public static final String CHANGES_COLUMN_TIME_STAMP = "time_stamp";
	
	public static final String VERSIONS_TABLE_NAME = "Versions";
	public static final String VERSIONS_COLUMN_ID = "id";
	public static final String VERSIONS_COLUMN_TIME_STAMP = "time_stamp";
	public static final String VERSIONS_NUMBER_OF_RECORDS = "num_of_records";
	
	public DBHelper(Context context)
	{
	   super(context, DATABASE_NAME , null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlCreateStatement = "CREATE TABLE " + EQUIPMENT_TABLE_NAME 
				  +"(" 
			      + EQUIPMENT_COLUMN_ID + " integer primary key autoincrement, "
			      + EQUIPMENT_COLUMN_AREA + " integer not null, "
			      + EQUIPMENT_COLUMN_EXCHANGE_NUMBER + " text not null, "
			      + EQUIPMENT_COLUMN_SETTLEMENT + " text not null, "
			      + EQUIPMENT_COLUMN_STREET + " text not null, "
			      + EQUIPMENT_COLUMN_BUILDING_NUMBER + " text not null, "
			      + EQUIPMENT_COLUMN_BUILDING_SIGN + " text, "
			      + EQUIPMENT_COLUMN_EQUIPMENT_TYPE + " text not null, "
			      + EQUIPMENT_COLUMN_LATITUDE + " real not null, "
			      + EQUIPMENT_COLUMN_LONGITUDE + " real not null, "
			      + EQUIPMENT_COLUMN_ALTITUDE + " real not null "
			      +");";
		db.execSQL(sqlCreateStatement);
		
		sqlCreateStatement = "CREATE TABLE " + CHANGES_TABLE_NAME 
				  +"(" 
			      + CHANGES_COLUMN_ID + " integer primary key autoincrement, "
			      + CHANGES_COLUMN_TECH_ID + " text not null, "
			      + CHANGES_COLUMN_AREA + " integer not null, "
			      + CHANGES_COLUMN_EQUIPMENT_EXCHANGE_NUMBER + " text not null, "
			      + CHANGES_COLUMN_SETTLEMENT + " text not null, "
			      + CHANGES_COLUMN_STREET + " text not null, "
			      + CHANGES_COLUMN_BUILDING_NUMBER + " text not null, "
			      + CHANGES_COLUMN_BUILDING_SIGN + " text, "
			      + CHANGES_COLUMN_EQUIPMENT_TYPE + " text not null, "
			      + CHANGES_COLUMN_STATUS + " text, "
			      + CHANGES_COLUMN_LATITUDE + " real not null, "
			      + CHANGES_COLUMN_LONGITUDE + " real not null, "
			      + CHANGES_COLUMN_ALTITUDE + " real not null, "
			      + CHANGES_COLUMN_TIME_STAMP + " int not null "
			      +");";
		db.execSQL(sqlCreateStatement);
		
		sqlCreateStatement = "CREATE TABLE " + VERSIONS_TABLE_NAME
				  +"(" 
			      + VERSIONS_COLUMN_ID + " integer primary key autoincrement, "
			      + VERSIONS_COLUMN_TIME_STAMP + " text not null, "
			      + VERSIONS_NUMBER_OF_RECORDS + " int not null "
			      +");";
		db.execSQL(sqlCreateStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + EQUIPMENT_TABLE_NAME);
	    onCreate(db);
	}
	
	
	
}
