package com.bezeq.locator.bl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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
	
	public EquipmentDataSource(Resources res, Context context) {
		mDataManager = new MsagDataManager(context);
		bDataManager = new BoxDataManager(context);
		createIcons(res);
	}

	public List<Marker> getMarkers(boolean[] includes){
		ArrayList<Equipment> list = getEquipment(includes);
		
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
		ArrayList<Equipment> list = getEquipment(includes);
		
		List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
		for (Equipment equip:list){
			markers.add(
					new MarkerOptions()
					.position(new LatLng(equip.getLatitude(), equip.getLongitude()))
					.title(equip.getType() + "\n" + equip.getBuilding_num() + equip.getBuilding_sign() )
					.draggable(false)
					.icon(BitmapDescriptorFactory.fromBitmap(getIcon(equip.getType())))
					);
		}
		
		return markers;
	}
	
	private ArrayList<Equipment> getEquipment(boolean[] includes){
		ArrayList<Equipment> list = new ArrayList<Equipment>();
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
				boxes = bDataManager.getAllBoxes();
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
}
