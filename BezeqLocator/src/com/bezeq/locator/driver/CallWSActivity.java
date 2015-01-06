package com.bezeq.locator.driver;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.AsyncCallWS;
import com.bezeq.locator.bl.WsHelper;
import com.bezeq.locator.db.CabinetDataManager;
import com.bezeq.locator.db.DboxDataManager;
import com.bezeq.locator.db.HoleDataManager;
import com.bezeq.locator.db.MsagDataManager;
import com.bezeq.locator.db.PoleDataManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class CallWSActivity extends Activity {

	private static final String JSON_TAG_OBJECT_ID = "OBJECTID";
	private static final String JSON_TAG_ENTITY_TYPE = "ENTITY_TYPE";
	private static final String JSON_TAG_MERKAZ = "MERKAZ";
	private static final String JSON_TAG_FEATURE_NUM = "FEATURE_NUM";
	private static final String JSON_TAG_CITY_CODE = "CITY_CODE";
	private static final String JSON_TAG_CITY_NAME = "CITY_NAME";
	private static final String JSON_TAG_STREET_CODE = "STREET_CODE";
	private static final String JSON_TAG_STREET_NAME = "STREET_NAME";
	private static final String JSON_TAG_BUILDING_NUM = "BUILDING_NUM";
	private static final String JSON_TAG_BUILDING_LETTER = "BUILDING_LETTER";
	private static final String JSON_TAG_X = "X";
	private static final String JSON_TAG_Y = "Y";
	private static final String JSON_TAG_LON = "LON";
	private static final String JSON_TAG_LAT = "LAT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}// end onCreate()

	@Override
	public void onStart() {
		super.onStart();
		MsagDataManager mDataManager = new MsagDataManager(
				getApplicationContext());
		CabinetDataManager cDataManager = new CabinetDataManager(
				getApplicationContext());
		DboxDataManager dDataManager = new DboxDataManager(
				getApplicationContext());
		HoleDataManager hDataManager = new HoleDataManager(
				getApplicationContext());
		PoleDataManager pDataManager = new PoleDataManager(
				getApplicationContext());

		// String[] params = { "100" };
		// Double[] params =
		// {ARData.getCurrentLocation().getLatitude().toString(),ARData.getCurrentLocation().getLongitude().toString(),
		// "250"};
		String[] params = { "32.074278", "34.792389", "250" };
		try {
			String result = new AsyncCallWS(this).execute(params).get();
			Log.i("CALL_WS", "" + result);

			if (result != null) {
				JSONArray equips = new JSONArray(result);

				for (int i = 0; i < equips.length(); i++) {
					JSONObject equip = equips.getJSONObject(i);

					int objectID = equip.getInt(JSON_TAG_OBJECT_ID);
					String type = equip.getString(JSON_TAG_ENTITY_TYPE);
					int merkaz = equip.getString(JSON_TAG_MERKAZ)
							.equalsIgnoreCase("") ? 0 : Integer.parseInt(equip
							.getString(JSON_TAG_MERKAZ));
					int featureNum = equip.getString(JSON_TAG_FEATURE_NUM)
							.equalsIgnoreCase("") ? 0 : Integer.parseInt(equip
							.getString(JSON_TAG_FEATURE_NUM));
					String cityName = equip.getString(JSON_TAG_CITY_NAME);
					String streetName = equip.getString(JSON_TAG_STREET_NAME);
					int buildingNum = equip.getInt(JSON_TAG_BUILDING_NUM);
					String buildingLetter = equip.getString(
							JSON_TAG_BUILDING_LETTER).equalsIgnoreCase("null") ? ""
							: equip.getString(JSON_TAG_BUILDING_LETTER);
					String x_isr = equip.getString(JSON_TAG_X);
					String y_isr = equip.getString(JSON_TAG_Y);
					double lon = equip.getDouble(JSON_TAG_LON);
					double lat = equip.getDouble(JSON_TAG_LAT);

					if (type.equalsIgnoreCase("msag_mitkan")) {
						mDataManager.open();
						mDataManager.insert(objectID, merkaz, featureNum,
								cityName, streetName, buildingNum,
								buildingLetter, lon, lat);
						Log.i("CALL_WS", "Insert new MSAG : " + objectID);
						mDataManager.close();
					}

					if (type.equalsIgnoreCase("cabinet")) {
						cDataManager.open();
						cDataManager.insert(objectID, merkaz, featureNum,
								cityName, streetName, buildingNum,
								buildingLetter, lon, lat);
						Log.i("CALL_WS", "Insert new CABINET : " + objectID);
						cDataManager.close();
					}

					if (type.equalsIgnoreCase("dbox_all")) {
						dDataManager.open();
						dDataManager.insert(objectID, merkaz, featureNum,
								cityName, streetName, buildingNum,
								buildingLetter, lon, lat);
						Log.i("CALL_WS", "Insert new BOX : " + objectID);
						dDataManager.close();
					}

					if (type.equalsIgnoreCase("hole")) {
						hDataManager.open();
						hDataManager.insert(objectID, merkaz, featureNum,
								cityName, streetName, buildingNum,
								buildingLetter, lon, lat);
						Log.i("CALL_WS", "Insert new HOLE : " + objectID);
						hDataManager.close();
					}
					if (type.equalsIgnoreCase("pole")) {
						pDataManager.open();
						pDataManager.insert(objectID, merkaz, featureNum,
								cityName, streetName, buildingNum,
								buildingLetter, lon, lat);
						Log.i("CALL_WS", "Insert new POLE : " + objectID);
						pDataManager.close();
					}

				}
			}
			// Toast t = Toast.makeText(getApplicationContext(), hello,
			// Toast.LENGTH_SHORT);
			// t.setGravity(Gravity.CENTER, 0, 0);
			// t.show();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
