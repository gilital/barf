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
			DBHelper.EQUIPMENT_COLUMN_CNUM,
			DBHelper.EQUIPMENT_COLUMN_NAME,
			DBHelper.EQUIPMENT_COLUMN_TYPE,
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
	
	public void insertEquipment (String cnum, String name, String type, double latitude, double longtitude, double altitude){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.EQUIPMENT_COLUMN_CNUM, cnum);
		cv.put(DBHelper.EQUIPMENT_COLUMN_NAME, name);
		cv.put(DBHelper.EQUIPMENT_COLUMN_TYPE, type);
		cv.put(DBHelper.EQUIPMENT_COLUMN_LATITUDE, latitude);
		cv.put(DBHelper.EQUIPMENT_COLUMN_LONGITUDE, longtitude);
		cv.put(DBHelper.EQUIPMENT_COLUMN_ALTITUDE, altitude);
		database.insert(DBHelper.EQUIPMENT_TABLE_NAME, null, cv);
	}
	
	public ArrayList<Equipment> getAllEquipment(){
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		Cursor cursor = database.query(DBHelper.EQUIPMENT_TABLE_NAME,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Equipment equip = new Equipment();
		      
		      equip.setId(cursor.getInt(0));
		      equip.setCnum(cursor.getString(1));
		      equip.setName(cursor.getString(2));
		      equip.setType(cursor.getString(3));
		      equip.setLatitude(cursor.getDouble(4));
		      equip.setLongitude(cursor.getDouble(5));
		      equip.setAltitude(cursor.getDouble(6));
		      
		      list.add(equip);
		      cursor.moveToNext();
		    }
		    
		    cursor.close();
//		list.add(new Equipment(1,"cn1","first","A", 31.2622020, 34.806830, 0.0));
//		list.add(new Equipment(2,"cn2","second","B", 31.2622020, 34.800830, 0.0));
//		list.add(new Equipment(3,"cn3","third","C", 31.2624020, 34.800830, 0.0));
		
		    return list;
	}
	
	public void deleteEquipment(Equipment equip){
		int id = equip.getId();
		database.delete(DBHelper.EQUIPMENT_TABLE_NAME, DBHelper.EQUIPMENT_COLUMN_ID + " = " + id, null);
	}
	
	
}
