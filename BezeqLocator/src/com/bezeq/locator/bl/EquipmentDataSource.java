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
import com.bezeq.locator.driver.MainActivity;
import com.bezeq.locator.driver.MapActivity;
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

	public void getUpdateFromWS(MapActivity map, MainActivity ar) {
		String[] params = { ARData.getCurrentLocation().getLatitude() + "",
				ARData.getCurrentLocation().getLongitude() + "", "300" };
		new AsyncGetEquipmentInRange(context, map, ar).execute(params);
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
