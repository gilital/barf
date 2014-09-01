package com.bezeq.locator.bl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import com.bezeq.locator.db.EquipmentDataManager;
import com.bezeq.locator.draw.IconMarker;
import com.bezeq.locator.draw.Marker;
import com.bezeq.locator.driver.R;

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

	private EquipmentDataManager data;
	private Dictionary<String,Bitmap> icons = new Hashtable<String,Bitmap>();
	private List<Equipment> msags;
	private List<Equipment> boxes;
	private List<Equipment> pits;
	private List<Equipment> poles;
	
	public EquipmentDataSource(Resources res, Context context) {
		data = new EquipmentDataManager(context);
		createIcons(res);
		populateLists();
	}

	private void populateLists(){
		msags = new ArrayList<Equipment>();
		boxes = new ArrayList<Equipment>();
		pits = new ArrayList<Equipment>();
		poles = new ArrayList<Equipment>();
		
		data.open();
		ArrayList<Equipment> list = data.getAllEquipment();
		
		for (Equipment equip:list){
			if (equip.getType().equals("MSAG")){
				msags.add(equip);
				continue;
			}
			else if (equip.getType().equals("box")){
				boxes.add(equip);
				continue;
			}
			else if (equip.getType().equals("pit")){
				pits.add(equip);
				continue;
			}
			else if (equip.getType().equals("pole")){
				poles.add(equip);
			}
		}
		
		data.close();
		
	}
	
	public List<Marker> getMarkers(boolean[] includes){
		//boolean includeMsags, boolean includeBoxes, boolean includePits, boolean includePoles
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		if (includes[0]){
			list.addAll(msags);
		}
		if (includes[1]){
			list.addAll(boxes);
		}
		if (includes[2]){
			list.addAll(pits);
		}
		if (includes[3]){
			list.addAll(poles);
		}
		
		List<Marker> markers = new ArrayList<Marker>();
		for (Equipment equip:list){
			String comment = 
					equip.getExchange_num() + "\n"
					+ equip.getSettlement() + ", "
					+ equip.getBuilding_num() 
					+ equip.getBuilding_sign() + " "
					+ equip.getStreet();	
					
			Marker temp = new IconMarker(equip.getExchange_num(),comment, equip.getLatitude(), equip.getLongitude(),equip.getAltitude() , Color.DKGRAY, getIcon("A"));
			markers.add(temp);
		}
		
		return markers;
	}
	
	private Bitmap getIcon(String type){
		Bitmap icon = icons.get(type); 
		return (icon == null)?icons.get("default"):icon;
	}

	protected void createIcons(Resources res) {
		if (res==null) throw new NullPointerException();
		icons.put("A", BitmapFactory.decodeResource(res, R.drawable.ic_action_good));
        icons.put("B", BitmapFactory.decodeResource(res, R.drawable.ic_action_accept));
        icons.put("C", BitmapFactory.decodeResource(res, R.drawable.ic_action_share));
        icons.put("default", BitmapFactory.decodeResource(res, R.drawable.ic_action_good));
		
	}
}
