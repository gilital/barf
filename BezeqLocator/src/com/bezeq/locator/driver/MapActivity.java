package com.bezeq.locator.driver;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.EquipmentDataSource;
import com.bezeq.locator.gui.SensorsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends SensorsActivity  {

	//private static final String TAG = "MapActivity";
    static final int AR_ACTIVITY_REQUEST = 1;
	private GoogleMap map;
	private EquipmentDataSource localData = null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(localData == null) localData =  new EquipmentDataSource(this.getResources(), this);
        setContentView(R.layout.activity_map);
        selected[0] = true;
    }//end onCreate()
    
    @Override
    public void onStart(){
    	super.onStart();
        try {
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
                    

                                       
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    

                   }//end if
                }//end if
            }//end if
            
			updateMarkers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
        return true;
    }//end onCreateOptionsMenu()
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_filter:
            	showFilterDialog();
            	break;
            case R.id.map_problem_report:
            	Intent problemReportIntent = new Intent(this, ProblemReportActivity.class);
            	startActivity(problemReportIntent);
                break;
            case R.id.ar_id_selection:
            	Intent ar_intent = new Intent(this, MainActivity.class);
            	startActivityForResult(ar_intent, AR_ACTIVITY_REQUEST);
                break;
        }
        return true;
    }//end onOptionItemSelected()
    
	@Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        Location lastKnownLocation = ARData.getLastKnownLocation();
        Location currentLocation = ARData.getCurrentLocation();
        Log.i("MAP_ACTIVITY","Last location = " + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
        Log.i("MAP_ACTIVITY","Current location = " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
        Log.i("MAP_ACTIVITY","Distance : " + lastKnownLocation.distanceTo(currentLocation));
        
        if (lastKnownLocation.distanceTo(currentLocation) > 50.0){
        	localData.getUpdateFromWS(this, null);
        	ARData.setLastKnownLocation(ARData.getCurrentLocation());
        }
        
    }
    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == AR_ACTIVITY_REQUEST) {
//        		if (!compareArrays(selected, data.getBooleanArrayExtra("selected"))){
//            		selected = data.getBooleanArrayExtra("selected");
//            		try {
//    					updateMarkers();
//    				} catch (IOException e) {
//    					// TODO Auto-generated catch block
//    					e.printStackTrace();
//    				}
//            		
//            	}
//        }
//    }
    
    private void showFilterDialog(){
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle("נא לבחור סוג ציוד");
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	try {
					updateMarkers();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    	
    	builder.setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					selected[which]=isChecked;	
			}
		});
    	builder.show();
    }//end showFilterDialog()
    
    public void updateMarkers() throws IOException{
        
        ARData.addMarkers(localData.getMarkers(selected));
        
        ArrayList<MarkerOptions> markers = (ArrayList<MarkerOptions>) localData.getMapMarkers(selected);
        
        map.clear();
        
        for (MarkerOptions marker:markers){
        	map.addMarker(marker);
        } //end for
    }//end updateMarkers()
    
//    private boolean compareArrays(boolean[] first, boolean[] second){
//    	if (first.length != second.length){
//    		return false;
//    	}
//    	
//    	for (int i=0; i < first.length; i++){
//    		if (first[i] != second[i]){
//    			return false;
//    		}
//    	}
//    	
//    	return true;
//    }
}

