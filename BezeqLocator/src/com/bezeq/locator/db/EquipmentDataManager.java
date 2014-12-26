package com.bezeq.locator.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

public class EquipmentDataManager {
	protected SQLiteDatabase database;
	protected DBHelper dbHelper;
	private static final double RAD = 6372.8; // using in haversine formula
	
	protected String[] allColumns = {
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
	
	public EquipmentDataManager(Context context)
	{
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	protected Location getLatLong(double lat1, double lon1, double distance,
			double angle) {
		final double RAD = 6372.8;
		distance = distance /17.4;
		angle = Math.toRadians(angle);
		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);

		double alfa = Math.toRadians(distance / RAD);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(alfa)
				+ Math.cos(lat1) * Math.sin(alfa) * Math.cos(angle));

		double lon2 = lon1
				+ Math.atan2(Math.sin(angle) * Math.sin(alfa) * Math.cos(lat1),
						Math.cos(alfa) - Math.sin(lat1) * Math.sin(lat2));

		Location result = new Location("NEW");
		result.setLatitude(Math.toDegrees(lat2));
		result.setLongitude(Math.toDegrees(lon2));

		return result;
	}
	
	protected double haversine(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return RAD * c;
	}
}
