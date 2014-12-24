package com.bezeq.locator.db;

import java.util.ArrayList;

import com.bezeq.locator.bl.Equipment;
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
			DBHelper.PKID_COLUMN,
			DBHelper.OBJECT_ID_COLUMN,
			DBHelper.MERKAZ_COLUMN,
			DBHelper.FEATURE_NUM_COLUMN,
			DBHelper.CITY_NAME_COLUMN,
			DBHelper.STREET_NAME_COLUMN,
			DBHelper.BUILDING_NUM_COLUMN,
			DBHelper.BUILDING_LETTER_COLUMN,
			DBHelper.LONGITUDE_COLUMN,
			DBHelper.LATITUDE_COLUMN
			};
	
	public MsagDataManager(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insert (int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lat, double lon){
		
		Equipment equip = getByObjectID(objectID);

		if (equip == null){
			//if equipment not exists in DB -> add it
			ContentValues cv = new ContentValues();
			cv.put(DBHelper.OBJECT_ID_COLUMN, objectID);
			cv.put(DBHelper.MERKAZ_COLUMN, merkaz);
			cv.put(DBHelper.FEATURE_NUM_COLUMN, featureNum);
			cv.put(DBHelper.CITY_NAME_COLUMN, cityName);
			cv.put(DBHelper.STREET_NAME_COLUMN, streetName);
			cv.put(DBHelper.BUILDING_NUM_COLUMN, buildingNum);
			cv.put(DBHelper.BUILDING_LETTER_COLUMN, buildingLetter);
			cv.put(DBHelper.LONGITUDE_COLUMN, lon);
			cv.put(DBHelper.LATITUDE_COLUMN, lat);
			database.insert(DBHelper.MSAG_TABLE_NAME, null, cv);
		}
		else{
			//if exists -> ensure that data is up to date
			this.update(equip.getId(),objectID,merkaz,featureNum,
					cityName,streetName,buildingNum,
					buildingLetter,lat,lon);
		}
	}
	
	public void update(int pkid, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lat, double lon){
		
		
		ContentValues cv = new ContentValues();
	
		cv.put(DBHelper.OBJECT_ID_COLUMN, objectID);
		cv.put(DBHelper.MERKAZ_COLUMN, merkaz);
		cv.put(DBHelper.FEATURE_NUM_COLUMN, featureNum);
		cv.put(DBHelper.CITY_NAME_COLUMN, cityName);
		cv.put(DBHelper.STREET_NAME_COLUMN, streetName);
		cv.put(DBHelper.BUILDING_NUM_COLUMN, buildingNum);
		cv.put(DBHelper.BUILDING_LETTER_COLUMN, buildingLetter);
		cv.put(DBHelper.LONGITUDE_COLUMN, lon);
		cv.put(DBHelper.LATITUDE_COLUMN, lat);
		
		database.update(DBHelper.MSAG_TABLE_NAME, cv, DBHelper.PKID_COLUMN + "=" + pkid, null);
	}
	
	public ArrayList<Msag> getAll(){
		ArrayList<Msag> list = new ArrayList<Msag>();
		Cursor cursor = database.query(DBHelper.MSAG_TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	Msag msag = new Msag(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
		    			cursor.getString(4), cursor.getString(5), cursor.getInt(6),
		    			cursor.getString(7), cursor.getDouble(8),cursor.getDouble(9));
		      
//		    	msag.setId(cursor.getInt(0));
//		    	msag.setArea(cursor.getInt(1));
//		    	msag.setExchange_num(cursor.getString(2));
//		    	msag.setSettlement(cursor.getString(3));
//		    	msag.setStreet(cursor.getString(4));
//		    	msag.setBuilding_num(cursor.getString(5));
//		    	msag.setBuilding_sign(cursor.getString(6));
//		    	//msag.setType(cursor.getString(7));
//		    	msag.setLatitude(cursor.getDouble(7));
//		    	msag.setLongitude(cursor.getDouble(8));
//		    	msag.setAltitude(cursor.getDouble(9));
		      
		    	//TODO: CHECK THE RADIUS AND FILTER
		      list.add(msag);
		      cursor.moveToNext();
		    }
		    
		    cursor.close();
		
		    return list;
	}
	
	public void delete(Msag msag){
		int id = msag.getId();
		database.delete(DBHelper.MSAG_TABLE_NAME, DBHelper.PKID_COLUMN + " = " + id, null);
	}
	
	public Msag getByObjectID(int objectId){
		Msag result = null;
		Cursor cursor = database.rawQuery("SELECT * FROM "+ DBHelper.MSAG_TABLE_NAME +" WHERE "+ DBHelper.OBJECT_ID_COLUMN +" = '"+ objectId +"'", null);
		boolean exists = cursor.moveToFirst();	
		if (exists){
			cursor.moveToFirst();
			result = new Msag(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
	    			cursor.getString(4), cursor.getString(5), cursor.getInt(6),
	    			cursor.getString(7), cursor.getDouble(8),cursor.getDouble(9));

		}
	    cursor.close();
		return result;
	}
	public boolean isEmpty(){
		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBHelper.MSAG_TABLE_NAME, null);
		boolean isExists = false;
		if (cursor != null) {
		    cursor.moveToFirst();                     
		    if (cursor.getInt (0) == 0) {               // Zero count means empty table.
		    	isExists = true;
		    }
		}
		return isExists;
	}
	
	
}
