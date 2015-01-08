package com.bezeq.locator.bl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bezeq.locator.db.CabinetDataManager;
import com.bezeq.locator.db.DboxDataManager;
import com.bezeq.locator.db.HoleDataManager;
import com.bezeq.locator.db.MsagDataManager;
import com.bezeq.locator.db.PoleDataManager;
import com.bezeq.locator.driver.MainActivity;
import com.bezeq.locator.driver.MapActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncGetEquipmentInRange extends AsyncTask<String, Integer, String> {
	private ProgressDialog progressDialog;
	private Context context = null;
	private MsagDataManager mDataManager;
	private CabinetDataManager cDataManager;
	private DboxDataManager dDataManager;
	private HoleDataManager hDataManager;
	private PoleDataManager pDataManager;
	private MapActivity map;
	private MainActivity ar;
	
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
	
	public AsyncGetEquipmentInRange (Context c, MapActivity map, MainActivity ar){
		this.context = c;
		this.map = map;
		this.ar = ar;
		mDataManager = new MsagDataManager(context);
		cDataManager = new CabinetDataManager(context);
		dDataManager = new DboxDataManager(context);
		hDataManager = new HoleDataManager(context);
		pDataManager = new PoleDataManager(context);
	}
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setTitle("Wait");
		progressDialog.setMessage("Fetching information");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setCanceledOnTouchOutside(false);
		Toast t = Toast.makeText(context, "Trying get equipment from WS" , Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
        
	}
	
	@Override
	protected String doInBackground(String... params) {
		WsHelper ws = new WsHelper();
		String result = null;
		try {
			result =  ws.getEquipInRange(params[0], params[1], params[2]);
			Log.i("ASYNC_GET_EQUIP", "" + result);
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
					
					publishProgress(i,equips.length());
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		progressDialog.dismiss();
		
		if (result == null){
			Toast t = Toast.makeText(context, "Can't connect to server\nWorking offline" , Toast.LENGTH_SHORT);
	        t.setGravity(Gravity.CENTER, 0, 0);
	        t.show();
		}
		else{
			Toast t = Toast.makeText(context, "Update of equipment completed" , Toast.LENGTH_SHORT);
	        t.setGravity(Gravity.CENTER, 0, 0);
	        t.show();
	        try {
	        	if (this.map != null){
	        		map.updateMarkers();
	        	}
	        	if (this.ar != null){
	        		ar.updateMarkers();
	        	}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	@Override
	protected void onProgressUpdate(Integer... values) {
		if (values[0] == 0){
			progressDialog.setMax(values[1]);
			progressDialog.show();
		}
		
		progressDialog.setProgress(values[0]);
		
		//Log.i("PROGRESS", "Length = " + values[1]);
	}
}

