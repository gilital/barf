package com.bezeq.locator.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ChangesDataManager {
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	private String[] allColumns = {
			DBHelper.CHANGES_COLUMN_ID,
			DBHelper.CHANGES_COLUMN_TECH_ID,
			DBHelper.CHANGES_COLUMN_AREA,
			DBHelper.CHANGES_COLUMN_EQUIPMENT_EXCHANGE_NUMBER,
			DBHelper.CHANGES_COLUMN_SETTLEMENT,
			DBHelper.CHANGES_COLUMN_STREET,
			DBHelper.CHANGES_COLUMN_BUILDING_NUMBER,
			DBHelper.CHANGES_COLUMN_BUILDING_SIGN,
			DBHelper.CHANGES_COLUMN_EQUIPMENT_TYPE,
			DBHelper.CHANGES_COLUMN_STATUS,
			DBHelper.CHANGES_COLUMN_LATITUDE,
			DBHelper.CHANGES_COLUMN_LONGITUDE,
			DBHelper.CHANGES_COLUMN_ALTITUDE,
			DBHelper.CHANGES_COLUMN_TIME_STAMP
			};
	
	public ChangesDataManager(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insertChange (String techId, int area, String exnum, String settlement, String street,
			String bnum, String bsign, String type, String status, double latitude, double longtitude, double altitude){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.CHANGES_COLUMN_TECH_ID, techId);
		cv.put(DBHelper.CHANGES_COLUMN_AREA, area);
		cv.put(DBHelper.CHANGES_COLUMN_EQUIPMENT_EXCHANGE_NUMBER, exnum);
		cv.put(DBHelper.CHANGES_COLUMN_SETTLEMENT, settlement);
		cv.put(DBHelper.CHANGES_COLUMN_STREET, street);
		cv.put(DBHelper.CHANGES_COLUMN_BUILDING_NUMBER, bnum);
		cv.put(DBHelper.CHANGES_COLUMN_BUILDING_SIGN, bsign);
		cv.put(DBHelper.CHANGES_COLUMN_EQUIPMENT_TYPE, type);
		cv.put(DBHelper.CHANGES_COLUMN_STATUS, status);
		cv.put(DBHelper.CHANGES_COLUMN_LATITUDE, latitude);
		cv.put(DBHelper.CHANGES_COLUMN_LONGITUDE, longtitude);
		cv.put(DBHelper.CHANGES_COLUMN_ALTITUDE, altitude);
		cv.put(DBHelper.CHANGES_COLUMN_TIME_STAMP, getDateTime());

		database.insert(DBHelper.CHANGES_TABLE_NAME, null, cv);
	}
	
	
	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
}
}
