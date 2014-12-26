package com.bezeq.locator.db;

import java.util.ArrayList;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.Cabinet;
import com.bezeq.locator.bl.Equipment;
import com.bezeq.locator.bl.Hole;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

/**
 * Class for HOLE table management (local table for showing AR markers only)
 * Methods : Adding new equipment Updating existing equipment Getting all
 * equipments Get equipment by its exchange number
 * 
 * @author Silver
 * 
 */
public class HoleDataManager extends EquipmentDataManager {

	public HoleDataManager(Context context) {
		super(context);
	}

	public void insert(int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lat, double lon) {

		Equipment equip = getByObjectID(objectID);

		if (equip == null) {
			// if equipment not exists in DB -> add it
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

			database.insert(DBHelper.HOLE_TABLE_NAME, null, cv);
		} else {
			// if exists -> ensure that data is up to date
			this.update(equip.getId(), objectID, merkaz, featureNum, cityName,
					streetName, buildingNum, buildingLetter, lat, lon);
		}
	}

	public void update(int pkid, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lat, double lon) {

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

		database.update(DBHelper.HOLE_TABLE_NAME, cv, DBHelper.PKID_COLUMN
				+ "=" + pkid, null);
	}

	public ArrayList<Hole> getAll() {
		ArrayList<Hole> list = new ArrayList<Hole>();
		Cursor cursor = database.query(DBHelper.HOLE_TABLE_NAME, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hole hole = new Hole(cursor.getInt(0), cursor.getInt(1),
					cursor.getInt(2), cursor.getInt(3), cursor.getString(4),
					cursor.getString(5), cursor.getInt(6), cursor.getString(7),
					cursor.getDouble(8), cursor.getDouble(9));
			list.add(hole);
			cursor.moveToNext();
		}

		cursor.close();

		return list;
	}

	public ArrayList<Hole> getInRange(double range) {
		ArrayList<Hole> list = new ArrayList<Hole>();

		Location upper = getLatLong(ARData.getCurrentLocation().getLatitude(),
				ARData.getCurrentLocation().getLongitude(), range, 0);
		Location right = getLatLong(ARData.getCurrentLocation().getLatitude(),
				ARData.getCurrentLocation().getLongitude(), range, 90);
		Location bottom = getLatLong(ARData.getCurrentLocation().getLatitude(),
				ARData.getCurrentLocation().getLongitude(), range, 180);
		Location left = getLatLong(ARData.getCurrentLocation().getLatitude(),
				ARData.getCurrentLocation().getLongitude(), range, 270);

		String sql = "SELECT * FROM " + DBHelper.HOLE_TABLE_NAME + " WHERE "
				+ DBHelper.LATITUDE_COLUMN + " BETWEEN " + bottom.getLatitude()
				+ " AND " + upper.getLatitude() + " AND "
				+ DBHelper.LONGITUDE_COLUMN + " BETWEEN " + left.getLongitude()
				+ " AND " + right.getLongitude();

		Log.i("DATA_MANAGER", sql);
		Cursor cursor = database.rawQuery(sql, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hole hole = new Hole(cursor.getInt(0), cursor.getInt(1),
					cursor.getInt(2), cursor.getInt(3), cursor.getString(4),
					cursor.getString(5), cursor.getInt(6), cursor.getString(7),
					cursor.getDouble(8), cursor.getDouble(9));
			list.add(hole);
			cursor.moveToNext();
		}
		return list;
	}

	public void delete(Hole hole) {
		int id = hole.getId();
		database.delete(DBHelper.HOLE_TABLE_NAME, DBHelper.PKID_COLUMN + " = "
				+ id, null);
	}

	public Hole getByObjectID(int objectId) {
		Hole result = null;
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBHelper.HOLE_TABLE_NAME + " WHERE "
				+ DBHelper.OBJECT_ID_COLUMN + " = '" + objectId + "'", null);
		boolean exists = cursor.moveToFirst();
		if (exists) {
			cursor.moveToFirst();
			result = new Hole(cursor.getInt(0), cursor.getInt(1),
					cursor.getInt(2), cursor.getInt(3), cursor.getString(4),
					cursor.getString(5), cursor.getInt(6), cursor.getString(7),
					cursor.getDouble(8), cursor.getDouble(9));

		}
		cursor.close();
		return result;
	}

	public boolean isEmpty() {
		Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM "
				+ DBHelper.HOLE_TABLE_NAME, null);
		boolean isExists = false;
		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.getInt(0) == 0) { // Zero count means empty table.
				isExists = true;
			}
		}
		return isExists;
	}

}
