package com.bezeq.locator.bl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bezeq.locator.db.BoxDataManager;
import com.bezeq.locator.db.MsagDataManager;
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

/**
 * Class for conversion of Equipment data row to ARData marker
 * @author Silver
 *
 */
public class EquipmentDataSource{

	private MsagDataManager mDataManager;
	private BoxDataManager bDataManager;
	private Dictionary<String,Bitmap> icons = new Hashtable<String,Bitmap>();
	private List<Msag> msags;
	private List<Box> boxes;
	private List<Msag> pits;
	private List<Msag> poles;
	private final static double RADAR_MAX_DISTANCE = 0.5;
	private final static double MAP_MAX_DISTANCE = 3.0;
	private static final double RAD = 6372.8; // using in haversine formula
	
	public EquipmentDataSource(Resources res, Context context) {
		mDataManager = new MsagDataManager(context);
		bDataManager = new BoxDataManager(context);
		createIcons(res);
	}

	public List<Marker> getMarkers(boolean[] includes){
		List<Equipment> list = getEquipment(includes, RADAR_MAX_DISTANCE);
		
		List<Marker> markers = new ArrayList<Marker>();
		for (Equipment equip:list){
			String comment = 
					equip.getType() + "\n"
					+ equip.getSettlement() + ", "
					+ equip.getBuilding_num() 
					+ equip.getBuilding_sign() + " "
					+ equip.getStreet();	
					
			Marker temp = new IconMarker(equip.getType()+ " " + equip.getId() + "\n",comment, equip.getLatitude(), equip.getLongitude(),equip.getAltitude() , Color.DKGRAY, getIcon(equip.getType()));
			markers.add(temp);
		}
		
		return markers;
	}
	
	public List<MarkerOptions> getMapMarkers(boolean[] includes){
		List<Equipment> list = getEquipment(includes, MAP_MAX_DISTANCE);
		
		List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
		for (Equipment equip:list){
			markers.add(
					new MarkerOptions()
					.position(new LatLng(equip.getLatitude(), equip.getLongitude()))
					.title(equip.getType() + "\n" + equip.getBuilding_num() + equip.getBuilding_sign() + " " + equip.getStreet())
					.draggable(false)
					.icon(BitmapDescriptorFactory.fromBitmap(getIcon(equip.getType())))
					);
		}
		
		return markers;
	}
	
	/**
	 * 
	 * @param includes - type of equipment to be included in search
	 * @param range - max distance to equipment from current location
	 * @return
	 */
	private List<Equipment> getEquipment(boolean[] includes, double range){
		List<Equipment> list = new CopyOnWriteArrayList<Equipment>();
		if (includes[0]){
			if (msags == null){
				mDataManager.open();
				msags = mDataManager.getAllMsags();
				mDataManager.close();
			}
			list.addAll(msags);
		}
		if (includes[1]){
			if (boxes == null){
				bDataManager.open();
				boxes = new ArrayList<Box>();
				boxes.addAll(bDataManager.getAllBoxes());
						
				bDataManager.close();
			}
			list.addAll(boxes);
		}
		if (includes[2]){
			list.addAll(pits);
		}
		if (includes[3]){
			list.addAll(poles);
		}
		
		for (Equipment equip:list){
			if (haversine(ARData.getCurrentLocation().getLatitude(),
				  	  ARData.getCurrentLocation().getLongitude(),
				  	  equip.getLatitude(),
				  	  equip.getLongitude()) > range)
			list.remove(equip);
		}
		return list;
	}
	
	private Bitmap getIcon(String type){
		Bitmap icon = icons.get(type); 
		return (icon == null)?icons.get("default"):icon;
	}

	protected void createIcons(Resources res) {
		if (res==null) throw new NullPointerException();
		icons.put("MSAG", BitmapFactory.decodeResource(res, R.drawable.ic_action_good));
        icons.put("BOX", BitmapFactory.decodeResource(res, R.drawable.ic_action_accept));
        icons.put("PIT", BitmapFactory.decodeResource(res, R.drawable.ic_action_share));
        icons.put("POLE", BitmapFactory.decodeResource(res, R.drawable.ic_action_good));
	}
	
	private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return RAD * c;
    }
}
