package com.bezeq.locator.db;

import java.util.ArrayList;

import com.bezeq.locator.bl.Msag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class for Msag table management (local table for showing AR markers only)
 * Methods :
 * 	Adding new equipment
 * 	Updating existing equipment
 * 	Getting all equipments
 * 	Get equipment by its exchange number
 * @author Silver
 *
 */
public class MsagDataManager {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	private String[] allColumns = {
			DBHelper.MSAG_COLUMN_ID,
			DBHelper.MSAG_COLUMN_AREA,
			DBHelper.MSAG_COLUMN_EXCHANGE_NUMBER,
			DBHelper.MSAG_COLUMN_SETTLEMENT,
			DBHelper.MSAG_COLUMN_STREET,
			DBHelper.MSAG_COLUMN_BUILDING_NUMBER,
			DBHelper.MSAG_COLUMN_BUILDING_SIGN,
			DBHelper.MSAG_COLUMN_LATITUDE,
			DBHelper.MSAG_COLUMN_LONGITUDE,
			DBHelper.MSAG_COLUMN_ALTITUDE};
	
	public MsagDataManager(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insertMsag (int area, String exnum, String settlement, String street, String bnum, String bsign, double latitude, double longtitude, double altitude){
		Msag equip = getMsagByExnum(exnum);

		if (equip == null){
			//if equipment not exists in DB -> add it
			ContentValues cv = new ContentValues();
			
			cv.put(DBHelper.MSAG_COLUMN_AREA, area);
			cv.put(DBHelper.MSAG_COLUMN_EXCHANGE_NUMBER, exnum);
			cv.put(DBHelper.MSAG_COLUMN_SETTLEMENT, settlement);
			cv.put(DBHelper.MSAG_COLUMN_STREET, street);
			cv.put(DBHelper.MSAG_COLUMN_BUILDING_NUMBER, bnum);
			cv.put(DBHelper.MSAG_COLUMN_BUILDING_SIGN, bsign);
			cv.put(DBHelper.MSAG_COLUMN_LATITUDE, latitude);
			cv.put(DBHelper.MSAG_COLUMN_LONGITUDE, longtitude);
			cv.put(DBHelper.MSAG_COLUMN_ALTITUDE, altitude);
			database.insert(DBHelper.MSAG_TABLE_NAME, null, cv);
		}
		else{
			//if exists -> ensure that data is up to date
			this.updateMsag(area, exnum, settlement, street, bnum, bsign, latitude, longtitude, altitude);
		}
	}
	
	public void updateMsag(int area, String exnum, String settlement, String street, String bnum, String bsign, double latitude, double longtitude, double altitude){
		Msag equip = getMsagByExnum(exnum);
		
		ContentValues cv = new ContentValues();
	
		cv.put(DBHelper.MSAG_COLUMN_AREA, area);
		cv.put(DBHelper.MSAG_COLUMN_EXCHANGE_NUMBER, exnum);
		cv.put(DBHelper.MSAG_COLUMN_SETTLEMENT, settlement);
		cv.put(DBHelper.MSAG_COLUMN_STREET, street);
		cv.put(DBHelper.MSAG_COLUMN_BUILDING_NUMBER, bnum);
		cv.put(DBHelper.MSAG_COLUMN_BUILDING_SIGN, bsign);
		cv.put(DBHelper.MSAG_COLUMN_LATITUDE, latitude);
		cv.put(DBHelper.MSAG_COLUMN_LONGITUDE, longtitude);
		cv.put(DBHelper.MSAG_COLUMN_ALTITUDE, altitude);
		
		database.update(DBHelper.MSAG_TABLE_NAME, cv, DBHelper.MSAG_COLUMN_ID + "=" + equip.getId(), null);
	}
	
	public ArrayList<Msag> getAllMsags(){
		ArrayList<Msag> list = new ArrayList<Msag>();
		Cursor cursor = database.query(DBHelper.MSAG_TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	Msag msag = new Msag();
		      
		    	msag.setId(cursor.getInt(0));
		    	msag.setArea(cursor.getInt(1));
		    	msag.setExchange_num(cursor.getString(2));
		    	msag.setSettlement(cursor.getString(3));
		    	msag.setStreet(cursor.getString(4));
		    	msag.setBuilding_num(cursor.getString(5));
		    	msag.setBuilding_sign(cursor.getString(6));
		    	//msag.setType(cursor.getString(7));
		    	msag.setLatitude(cursor.getDouble(7));
		    	msag.setLongitude(cursor.getDouble(8));
		    	msag.setAltitude(cursor.getDouble(9));
		      
		      list.add(msag);
		      cursor.moveToNext();
		    }
		    
		    cursor.close();
		
		    return list;
	}
	
	public void deleteMsag(Msag msag){
		int id = msag.getId();
		database.delete(DBHelper.MSAG_TABLE_NAME, DBHelper.MSAG_COLUMN_ID + " = " + id, null);
	}
	
	public Msag getMsagByExnum(String exnum){
		Msag result = null;
		Cursor cur = database.rawQuery("SELECT * FROM "+ DBHelper.MSAG_TABLE_NAME +" WHERE "+ DBHelper.MSAG_COLUMN_EXCHANGE_NUMBER +" = '"+ exnum +"'", null);
		boolean exists = cur.moveToFirst();	
		if (exists){
			cur.moveToFirst();
			result = new Msag();
			result.setId(cur.getInt(0));
		    result.setArea(cur.getInt(1));
		    result.setExchange_num(cur.getString(2));
		    result.setSettlement(cur.getString(3));
		    result.setStreet(cur.getString(4));
		    result.setBuilding_num(cur.getString(5));
		    result.setBuilding_sign(cur.getString(6));
		    result.setLatitude(cur.getDouble(7));
		    result.setLongitude(cur.getDouble(8));
		    result.setAltitude(cur.getDouble(9));

		}
	    cur.close();
		return result;
	}
	public boolean isEmpty(){
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM " + DBHelper.MSAG_TABLE_NAME, null);
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
