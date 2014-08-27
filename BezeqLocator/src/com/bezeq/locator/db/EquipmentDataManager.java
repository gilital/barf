package com.bezeq.locator.db;

import java.util.ArrayList;

import com.bezeq.locator.bl.Equipment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EquipmentDataManager {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	private String[] allColumns = {
			DBHelper.EQUIPMENT_COLUMN_ID,
			DBHelper.EQUIPMENT_COLUMN_AREA,
			DBHelper.EQUIPMENT_COLUMN_EXCHANGE_NUMBER,
			DBHelper.EQUIPMENT_COLUMN_SETTLEMENT,
			DBHelper.EQUIPMENT_COLUMN_STREET,
			DBHelper.EQUIPMENT_COLUMN_BUILDING_NUMBER,
			DBHelper.EQUIPMENT_COLUMN_BUILDING_SIGN,
			DBHelper.EQUIPMENT_COLUMN_LATITUDE,
			DBHelper.EQUIPMENT_COLUMN_LONGITUDE,
			DBHelper.EQUIPMENT_COLUMN_ALTITUDE};
	
	public EquipmentDataManager(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insertEquipment (int area, String exnum, String settlement, String street, String bnum, String bsign, double latitude, double longtitude, double altitude){
		Equipment equip = getEquipmentByExnum(exnum);

		if (equip == null){
			//if equipment not exists in DB -> add it
			ContentValues cv = new ContentValues();
			
			cv.put(DBHelper.EQUIPMENT_COLUMN_AREA, area);
			cv.put(DBHelper.EQUIPMENT_COLUMN_EXCHANGE_NUMBER, exnum);
			cv.put(DBHelper.EQUIPMENT_COLUMN_SETTLEMENT, settlement);
			cv.put(DBHelper.EQUIPMENT_COLUMN_STREET, street);
			cv.put(DBHelper.EQUIPMENT_COLUMN_BUILDING_NUMBER, bnum);
			cv.put(DBHelper.EQUIPMENT_COLUMN_BUILDING_SIGN, bsign);
			cv.put(DBHelper.EQUIPMENT_COLUMN_LATITUDE, latitude);
			cv.put(DBHelper.EQUIPMENT_COLUMN_LONGITUDE, longtitude);
			cv.put(DBHelper.EQUIPMENT_COLUMN_ALTITUDE, altitude);
			database.insert(DBHelper.EQUIPMENT_TABLE_NAME, null, cv);
		}
		else{
			//if exists -> ensure that data is up to date
			this.updateEquipment(area, exnum, settlement, street, bnum, bsign, latitude, longtitude, altitude);
		}
	}
	
	public void updateEquipment(int area, String exnum, String settlement, String street, String bnum, String bsign, double latitude, double longtitude, double altitude){
		Equipment equip = getEquipmentByExnum(exnum);
		
		ContentValues cv = new ContentValues();
	
		cv.put(DBHelper.EQUIPMENT_COLUMN_AREA, area);
		cv.put(DBHelper.EQUIPMENT_COLUMN_EXCHANGE_NUMBER, exnum);
		cv.put(DBHelper.EQUIPMENT_COLUMN_SETTLEMENT, settlement);
		cv.put(DBHelper.EQUIPMENT_COLUMN_STREET, street);
		cv.put(DBHelper.EQUIPMENT_COLUMN_BUILDING_NUMBER, bnum);
		cv.put(DBHelper.EQUIPMENT_COLUMN_BUILDING_SIGN, bsign);
		cv.put(DBHelper.EQUIPMENT_COLUMN_LATITUDE, latitude);
		cv.put(DBHelper.EQUIPMENT_COLUMN_LONGITUDE, longtitude);
		cv.put(DBHelper.EQUIPMENT_COLUMN_ALTITUDE, altitude);
		
		database.update(DBHelper.EQUIPMENT_TABLE_NAME, cv, DBHelper.EQUIPMENT_COLUMN_ID + "=" + equip.getId(), null);
	}
	
	public ArrayList<Equipment> getAllEquipment(){
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		Cursor cursor = database.query(DBHelper.EQUIPMENT_TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Equipment equip = new Equipment();
		      
		      equip.setId(cursor.getInt(0));
		      equip.setArea(cursor.getInt(1));
		      equip.setExchange_num(cursor.getString(2));
		      equip.setSettlement(cursor.getString(3));
		      equip.setStreet(cursor.getString(4));
		      equip.setBuilding_num(cursor.getString(5));
		      equip.setBuilding_sign(cursor.getString(6));
		      equip.setLatitude(cursor.getDouble(7));
		      equip.setLongitude(cursor.getDouble(8));
		      equip.setAltitude(cursor.getDouble(9));
		      
		      list.add(equip);
		      cursor.moveToNext();
		    }
		    
		    cursor.close();
		
		    return list;
	}
	
	public void deleteEquipment(Equipment equip){
		int id = equip.getId();
		database.delete(DBHelper.EQUIPMENT_TABLE_NAME, DBHelper.EQUIPMENT_COLUMN_ID + " = " + id, null);
	}
	
	public Equipment getEquipmentByExnum(String exnum){
		Equipment result = null;
		Cursor cur = database.rawQuery("SELECT * FROM "+ DBHelper.EQUIPMENT_TABLE_NAME +" WHERE "+ DBHelper.EQUIPMENT_COLUMN_EXCHANGE_NUMBER +" = '"+ exnum +"'", null);
		boolean exists = cur.moveToFirst();	
		if (exists){
			cur.moveToFirst();
			result = new Equipment();
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
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM " + DBHelper.EQUIPMENT_TABLE_NAME, null);
		if (cur != null) {
		    cur.moveToFirst();                     
		    if (cur.getInt (0) == 0) {               // Zero count means empty table.
		    	return true;
		    }
		}
		return false;
	}
	
	
}
