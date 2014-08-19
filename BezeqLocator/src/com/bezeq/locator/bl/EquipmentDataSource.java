package com.bezeq.locator.bl;

import java.util.ArrayList;
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

public class EquipmentDataSource extends LocalDataSource{

	private EquipmentDataManager data;
	
	public EquipmentDataSource(Resources res) {
		super(res);
		createIcons(res);
	}
	
	@Override
	public List<Marker> getMarkers(Context context){
		if (data == null) data = new EquipmentDataManager(context);
		data.open();
		ArrayList<Equipment> list = data.getAllEquipment();
		
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

	@Override
	protected void createIcons(Resources res) {
		if (res==null) throw new NullPointerException();
		icons.put("A", BitmapFactory.decodeResource(res, R.drawable.ic_action_good));
        icons.put("B", BitmapFactory.decodeResource(res, R.drawable.ic_action_accept));
        icons.put("C", BitmapFactory.decodeResource(res, R.drawable.ic_action_share));
        icons.put("default", BitmapFactory.decodeResource(res, R.drawable.ic_action_good));
		
	}
}
