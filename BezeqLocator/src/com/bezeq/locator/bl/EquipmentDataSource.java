package com.bezeq.locator.bl;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bezeq.locator.db.EquipmentDataManager;
import com.bezeq.locator.draw.IconMarker;
import com.bezeq.locator.draw.Marker;
import com.bezeq.locator.driver.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.Toast;

public class EquipmentDataSource extends LocalDataSource{

	private EquipmentDataManager data;
	private Resources resources;
	
	public EquipmentDataSource(Resources res) {
		super(res);
		createIcons(res);
		
		this.resources = res;
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
					+ equip.getSettlement() + ","
					+ equip.getBuilding_num() + " "
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
	
	public void loadFromFile(Context context) throws IOException{
		if (data == null) data = new EquipmentDataManager(context);
		
		data.open();
		
		if (data.isEmpty()){
			String str="";
			InputStream is = resources.openRawResource(R.raw.msag);
			try {
				
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		        if (is!=null) {       
		        	reader.readLine(); //skip the headers line
		            while ((str = reader.readLine()) != null) { 
		                String[] line = str.split("\t");
		                String x_isr = line[6];
		                String y_isr = line[7];
		                
		                if (x_isr == "250000" && y_isr == "600000") continue;
		                
		                int area = Integer.parseInt(line[0]);
		                String exnum = line[1];
		                String settlement = line[2];
		                String street = line[3];
		                String buildNum = (line[5] == null?" ":line[5]);
		                String building = line [4] + " " + buildNum;
		                double longtitude = Double.parseDouble(line[8]);
		                double latitude = Double.parseDouble(line[9]);
		                double altitude = 0.0;
		                
		                data.insertEquipment(area, exnum, settlement, street, building, latitude, longtitude, altitude);
		                
		            }               
		        }
		    } finally {
		        try { is.close(); } catch (Throwable ignore) {}
		    }
		}
		data.close();
	}

}
