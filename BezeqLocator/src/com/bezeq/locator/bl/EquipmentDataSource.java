package com.bezeq.locator.bl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bezeq.locator.db.CabinetDataManager;
import com.bezeq.locator.db.DboxDataManager;
import com.bezeq.locator.db.HoleDataManager;
import com.bezeq.locator.db.MsagDataManager;
import com.bezeq.locator.db.PoleDataManager;
import com.bezeq.locator.draw.IconMarker;
import com.bezeq.locator.draw.Marker;
import com.bezeq.locator.driver.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

/**
 * Class for conversion of Equipment data row to ARData marker
 * 
 * @author Silver
 * 
 */
public class EquipmentDataSource {
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

	private MsagDataManager mDataManager;
	private CabinetDataManager cDataManager;
	private DboxDataManager dDataManager;
	private HoleDataManager hDataManager;
	private PoleDataManager pDataManager;

	private Hashtable<Constants.EquipmentTypes, Bitmap> icons = new Hashtable<Constants.EquipmentTypes, Bitmap>();
	private List<Msag> msags;
	private List<Dbox> boxes;
	private List<Cabinet> cabinets;
	private List<Hole> holes;
	private List<Pole> poles;
	private Context context;
	private final static double RADAR_MAX_DISTANCE = 0.5;
	private final static double MAP_MAX_DISTANCE = 0.5;

	public EquipmentDataSource(Resources res, Context context) {
		mDataManager = new MsagDataManager(context);
		cDataManager = new CabinetDataManager(context);
		dDataManager = new DboxDataManager(context);
		hDataManager = new HoleDataManager(context);
		pDataManager = new PoleDataManager(context);
		createIcons(res);
		this.context = context;
	}

	public void getUpdateFromWS() {
		String[] params = {ARData.getCurrentLocation().getLatitude() + "",ARData.getCurrentLocation().getLongitude() + "", "250"};
		//String[] params = { "32.074278", "34.792389", "250" };
		try {
			String result = new AsyncGetEquipmentInRange(context).execute(
					params).get();
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
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Marker> getMarkers(boolean[] includes) {
		List<Equipment> list = getEquipment(includes, RADAR_MAX_DISTANCE);

		List<Marker> markers = new ArrayList<Marker>();
		for (Equipment equip : list) {
			String comment = equip.getCityName() + ", "
					+ equip.getBuildingNum() + equip.getBuildingLetter() + " "
					+ equip.getStreetName();

			Marker temp = new IconMarker(equip.getId() + "\n", comment,
					equip.getLatitude(), equip.getLongitude(),
					equip.getAltitude(), Color.DKGRAY, getIcon(equip.getType()));
			markers.add(temp);
		}

		return markers;
	}

	public List<MarkerOptions> getMapMarkers(boolean[] includes) {
		List<Equipment> list = getEquipment(includes, MAP_MAX_DISTANCE);

		List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
		for (Equipment equip : list) {
			markers.add(new MarkerOptions()
					.position(
							new LatLng(equip.getLatitude(), equip
									.getLongitude()))
					.title(equip.getType() + "\n" + equip.getBuildingNum()
							+ equip.getBuildingLetter() + " "
							+ equip.getStreetName())
					.draggable(false)
					.icon(BitmapDescriptorFactory.fromBitmap(getIcon(equip
							.getType()))));
		}

		return markers;
	}

	/**
	 * 
	 * @param includes
	 *            - type of equipment to be included in search
	 * @param range
	 *            - max distance to equipment from current location
	 * @return
	 */
	private List<Equipment> getEquipment(boolean[] includes, double range) {
		List<Equipment> list = new CopyOnWriteArrayList<Equipment>();
		if (includes[0]) {
			if (msags == null) {
				mDataManager.open();
				// msags = mDataManager.getAll();
				msags = mDataManager.getInRange(500.0);
				mDataManager.close();
			}
			list.addAll(msags);
		}
		if (includes[1]) {
			if (boxes == null) {
				dDataManager.open();
				// boxes = dDataManager.getAll();
				boxes = dDataManager.getInRange(250.0);
				dDataManager.close();
			}
			list.addAll(boxes);
		}
		if (includes[2]) {
			if (holes == null) {
				hDataManager.open();
				// holes = hDataManager.getAll();
				holes = hDataManager.getInRange(250.0);
				hDataManager.close();
			}
			list.addAll(holes);

		}
		if (includes[3]) {
			if (poles == null) {
				pDataManager.open();
				// poles = pDataManager.getAll();
				poles = pDataManager.getInRange(250.0);
				pDataManager.close();
			}
			list.addAll(poles);
		}

		// TODO: CABINET

		Log.i("EDS", list.size() + "");
		int counter = 0;
		for (Equipment equip : list) {
			// if (haversine(ARData.getCurrentLocation().getLatitude(), ARData
			// .getCurrentLocation().getLongitude(), equip.getLatitude(),
			// equip.getLongitude()) > range){
			// Log.i("EDS", equip.getObjectID() + " " + equip.getStreetName() +
			// " " + equip.getBuildingNum());
			// list.remove(equip);
			counter++;
			// }
		}
		Log.i("EDS", "Total " + counter);
		return list;
	}

	private Bitmap getIcon(Constants.EquipmentTypes type) {
		Bitmap icon = icons.get(type);
		return (icon == null) ? icons.get(Constants.EquipmentTypes.CABINET)
				: icon;
	}

	protected void createIcons(Resources res) {
		if (res == null)
			throw new NullPointerException();
		icons.put(Constants.EquipmentTypes.MSAG,
				BitmapFactory.decodeResource(res, R.drawable.msag));
		icons.put(Constants.EquipmentTypes.DBOX,
				BitmapFactory.decodeResource(res, R.drawable.box));
		icons.put(Constants.EquipmentTypes.HOLE,
				BitmapFactory.decodeResource(res, R.drawable.pit));
		icons.put(Constants.EquipmentTypes.POLE,
				BitmapFactory.decodeResource(res, R.drawable.pole));
		// TODO create icon for cabinet
		icons.put(Constants.EquipmentTypes.CABINET,
				BitmapFactory.decodeResource(res, R.drawable.pole));
		// TODO create default icon
	}
}
