package com.bezeq.locator.driver;

import java.util.ArrayList;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.EquipmentDataSource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MapActivity extends FragmentActivity  {

	private static final String TAG = "MapActivity";
    private final CharSequence[] items = {"MSAG","תיבות","גובים","עמודים"};
    boolean[] selected = new boolean[items.length];
	private GoogleMap map;
	private EquipmentDataSource localData = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        selected = getIntent().getBooleanArrayExtra("selected");
        
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
            	map.setMyLocationEnabled(true);
            	
            	Location location = ARData.getCurrentLocation();
                if (location != null)
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to north
                    .build();                   // Creates a CameraPosition from the builder
                

                if(localData == null) localData =  new EquipmentDataSource(this.getResources(), this);
                ArrayList<MarkerOptions> markers = (ArrayList<MarkerOptions>) localData.getMapMarkers(selected);
                
                for (MarkerOptions marker:markers){
                	map.addMarker(marker);
                }
                
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
        
    }
}

