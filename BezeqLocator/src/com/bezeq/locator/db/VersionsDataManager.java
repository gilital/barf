package com.bezeq.locator.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bezeq.locator.bl.Version;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class for Versions table management (For control purposes)
 * @author Silver
 *
 */
public class VersionsDataManager {

	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	private String[] allColumns = {
			DBHelper.VERSIONS_COLUMN_ID,
			DBHelper.VERSIONS_COLUMN_TIME_STAMP,
			DBHelper.VERSIONS_NUMBER_OF_RECORDS
			};
	
	public VersionsDataManager(Context context){
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insert(int counter, String date){
		ContentValues cv = new ContentValues();
		
		//no changes for today -> insert new row
		cv.put(DBHelper.VERSIONS_COLUMN_TIME_STAMP, date);
		cv.put(DBHelper.VERSIONS_NUMBER_OF_RECORDS, counter);

		database.insert(DBHelper.VERSIONS_TABLE_NAME, null, cv);
	}
	
	/**
	 * 
	 * @param date must be formatted String yyyy-MM-dd
	 * @return
	 */
	public Version getVersionByDate(String date){
		Version v = null;
		Cursor cur = database.rawQuery("SELECT * FROM "+ DBHelper.VERSIONS_TABLE_NAME + " WHERE "+ DBHelper.VERSIONS_COLUMN_TIME_STAMP + " = '"+ date +"'", null);
		boolean exists = cur.moveToFirst();
		if (exists){
			v = new Version();
			v.setId(cur.getInt(0));
			v.setTimeStamp(cur.getString(1));
			v.setNumberOfRecords(2);
		}
		
		cur.close();
		return v;
	}

	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public Version getLastVersionDate(){
		Version v = null;
		Cursor cur = database.rawQuery("SELECT * FROM "+ DBHelper.VERSIONS_TABLE_NAME + " ORDER BY " + DBHelper.VERSIONS_COLUMN_ID + " DESC ", null);
		boolean exists = cur.moveToFirst();
		if (exists){
			v = new Version();
			v.setId(cur.getInt(0));
			v.setTimeStamp(cur.getString(1));
			v.setNumberOfRecords(2);
		}
		
		cur.close();
		return v;
	}

	private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}
}
