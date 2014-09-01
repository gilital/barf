package com.bezeq.locator.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bezeq.locator.bl.Box;

public class BoxDataManager {
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	private String[] allColumns = {
		      DBHelper.BOX_COLUMN_ID,
		      DBHelper.BOX_COLUMN_UFID,
		      DBHelper.BOX_COLUMN_AREA,
		      DBHelper.BOX_COLUMN_FRAMEWORK_NUMBER,
		      DBHelper.BOX_COLUMN_LOCATION,
		      DBHelper.BOX_COLUMN_CBNT_TYPE,
		      DBHelper.BOX_COLUMN_CLOSER,
		      DBHelper.BOX_COLUMN_SETTLEMENT,
		      DBHelper.BOX_COLUMN_STREET,
		      DBHelper.BOX_COLUMN_BUILDING_NUMBER,
		      DBHelper.BOX_COLUMN_BUILDING_SIGN,
		      DBHelper.BOX_COLUMN_LATITUDE,
		      DBHelper.BOX_COLUMN_LONGITUDE,
		      DBHelper.BOX_COLUMN_ALTITUDE
			};
	
	public BoxDataManager(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insertBox (String ufid, int area, String framework, String location, String cbntType, String closer,  String settlement, String street, String bnum, String bsign, double latitude, double longtitude, double altitude){
		Box box = getBoxByUfid(ufid);

		if (box == null){
			//if equipment not exists in DB -> add it
			ContentValues cv = new ContentValues();

			cv.put(DBHelper.BOX_COLUMN_UFID, ufid);
			cv.put(DBHelper.BOX_COLUMN_AREA, area);
			cv.put(DBHelper.BOX_COLUMN_FRAMEWORK_NUMBER, framework);
			cv.put(DBHelper.BOX_COLUMN_LOCATION, location);
			cv.put(DBHelper.BOX_COLUMN_CBNT_TYPE, cbntType);
			cv.put(DBHelper.BOX_COLUMN_CLOSER, closer);
			cv.put(DBHelper.BOX_COLUMN_SETTLEMENT, settlement);
			cv.put(DBHelper.BOX_COLUMN_STREET, street);
			cv.put(DBHelper.BOX_COLUMN_BUILDING_NUMBER, bnum);
			cv.put(DBHelper.BOX_COLUMN_BUILDING_SIGN, bsign);
			cv.put(DBHelper.BOX_COLUMN_LATITUDE, latitude);
			cv.put(DBHelper.BOX_COLUMN_LONGITUDE, longtitude);
			cv.put(DBHelper.BOX_COLUMN_ALTITUDE, altitude);
			database.insert(DBHelper.BOX_TABLE_NAME, null, cv);
		}
		else{
			//if exists -> ensure that data is up to date
			this.updateBox(ufid, area, framework, location, cbntType, closer, settlement, street, bnum, bsign, latitude, longtitude, altitude);
		}
	}
	
	private Box getBoxByUfid(String ufid) {
		Box result = null;
		Cursor cur = database.rawQuery("SELECT * FROM "+ DBHelper.BOX_TABLE_NAME +" WHERE "+ DBHelper.BOX_COLUMN_UFID +" = '"+ ufid +"'", null);
		boolean exists = cur.moveToFirst();	
		if (exists){
			cur.moveToFirst();
			result = new Box();
			result.setId(cur.getInt(0));
			result.setUfid(cur.getString(1));
		    result.setArea(cur.getInt(2));
		    result.setFramework(cur.getString(3));
		    result.setLocation(cur.getString(4));
		    result.setCbntType(cur.getString(5));
		    result.setCloser(cur.getString(6));
		    result.setSettlement(cur.getString(7));
		    result.setStreet(cur.getString(8));
		    result.setBuilding_num(cur.getString(9));
		    result.setBuilding_sign(cur.getString(10));
		    result.setLatitude(cur.getDouble(11));
		    result.setLongitude(cur.getDouble(12));
		    result.setAltitude(cur.getDouble(13));

		}
	    cur.close();
		return result;
	}

	public void updateBox(String ufid, int area, String framework, String location, String cbntType, String closer,  String settlement, String street, String bnum, String bsign, double latitude, double longtitude, double altitude){
		Box box = getBoxByUfid(ufid);

		if (box != null){
			
			ContentValues cv = new ContentValues();

			cv.put(DBHelper.BOX_COLUMN_UFID, ufid);
			cv.put(DBHelper.BOX_COLUMN_AREA, area);
			cv.put(DBHelper.BOX_COLUMN_FRAMEWORK_NUMBER, framework);
			cv.put(DBHelper.BOX_COLUMN_LOCATION, location);
			cv.put(DBHelper.BOX_COLUMN_CBNT_TYPE, cbntType);
			cv.put(DBHelper.BOX_COLUMN_CLOSER, closer);
			cv.put(DBHelper.BOX_COLUMN_SETTLEMENT, settlement);
			cv.put(DBHelper.BOX_COLUMN_STREET, street);
			cv.put(DBHelper.BOX_COLUMN_BUILDING_NUMBER, bnum);
			cv.put(DBHelper.BOX_COLUMN_BUILDING_SIGN, bsign);
			cv.put(DBHelper.BOX_COLUMN_LATITUDE, latitude);
			cv.put(DBHelper.BOX_COLUMN_LONGITUDE, longtitude);
			cv.put(DBHelper.BOX_COLUMN_ALTITUDE, altitude);
			database.update(DBHelper.BOX_TABLE_NAME, cv, DBHelper.BOX_COLUMN_ID + "=" + box.getId(), null);
		}
		
		
	}
	
	public ArrayList<Box> getAllBoxes(){
		ArrayList<Box> list = new ArrayList<Box>();
		Cursor cur = database.query(DBHelper.BOX_TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cur.moveToFirst();
		    while (!cur.isAfterLast()) {
		    	Box box = new Box();
		    	box.setId(cur.getInt(0));
		    	box.setUfid(cur.getString(1));
		    	box.setArea(cur.getInt(2));
		    	box.setFramework(cur.getString(3));
		    	box.setLocation(cur.getString(4));
		    	box.setCbntType(cur.getString(5));
		    	box.setCloser(cur.getString(6));
		    	box.setSettlement(cur.getString(7));
		    	box.setStreet(cur.getString(8));
		    	box.setBuilding_num(cur.getString(9));
		    	box.setBuilding_sign(cur.getString(10));
		    	box.setLatitude(cur.getDouble(11));
		    	box.setLongitude(cur.getDouble(12));
		    	box.setAltitude(cur.getDouble(13));
		      
		      list.add(box);
		      cur.moveToNext();
		    }
		    
		    cur.close();
		
		    return list;
	}
	
	public void deleteBox(Box box){
		int id = box.getId();
		database.delete(DBHelper.BOX_TABLE_NAME, DBHelper.BOX_COLUMN_ID + " = " + id, null);
	}
	public boolean isEmpty(){
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM " + DBHelper.BOX_TABLE_NAME, null);
		boolean isExists = false;
		if (cur != null) {
		    cur.moveToFirst();                     
		    if (cur.getInt (0) == 0) {               // Zero count means empty table.
		    	isExists = true;
		    }
		}
		return isExists;
	}
	
	
}
