package com.bezeq.locator.driver;

import java.io.IOException;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.EquipmentDataSource;
import com.bezeq.locator.bl.Msag;
import com.bezeq.locator.db.MsagDataManager;
import com.bezeq.locator.draw.Marker;
import com.bezeq.locator.gui.AugmentedActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * AR view activity window
 * @author Silver
 *
 */
public class MainActivity extends AugmentedActivity {
    private static final String TAG = "MainActivity";
    private CharSequence[] items = {"MSAG","תיבות","גובים","עמודים"};
    final boolean[] selected = new boolean[items.length];
    EquipmentDataSource localData = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selected[0] = true;
        try {
			updateMarkers();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void onResume(){
		super.onResume();
		try {
			updateMarkers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public void onStart() {
        super.onStart();
        try {
			updateMarkers();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected() item="+item);
        switch (item.getItemId()) {
            case R.id.showRadar:
                showRadar = !showRadar;
                item.setTitle(((showRadar)? "Hide" : "Show")+" Radar");
                break;
            case R.id.showZoomBar:
                showZoomBar = !showZoomBar;
                item.setTitle(((showZoomBar)? "Hide" : "Show")+" Zoom Bar");
                zoomLayout.setVisibility((showZoomBar)?LinearLayout.VISIBLE:LinearLayout.GONE);
                break;
            case R.id.add_equipment:
            	Intent i = new Intent(this, AddEquipmentActivity.class);
				i.putExtra("action", "new");
		        startActivity(i);
		        //finish();
            	break;
            case R.id.filter:
            	showFilterDialog();
            	break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

	@Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
    }

	@Override
	protected void markerTouched(Marker marker) {
		long secondClick = System.currentTimeMillis();
		
		if (secondClick - super.getFirstClick() < 1000){
			Toast t = Toast.makeText(getApplicationContext(), marker.getComment(), Toast.LENGTH_SHORT);
	        t.setGravity(Gravity.CENTER, 0, 0);
	        t.show();	
		}
		else
		{
			//TODO: open edit activity
			MsagDataManager dataManager = new MsagDataManager(this); 
			dataManager.open();
			Msag selected = dataManager.getMsagByExnum(marker.getName());
			dataManager.close();
			if (selected != null){
				Intent i = new Intent(this, AddEquipmentActivity.class);
				i.putExtra("action", "edit");
				i.putExtra("selectedEquipment", selected);
		        startActivity(i);
			}
		}       
	}
	
	@Override
	protected void updateDataOnZoom() {
	    super.updateDataOnZoom();
	}
    
    private void updateMarkers() throws IOException{
        if(localData == null) localData =  new EquipmentDataSource(this.getResources(), this);
        ARData.addMarkers(localData.getMarkers(selected));
    }
    
    private void showFilterDialog(){
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle("נא לבחור סוג ציוד");
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	try {
					updateMarkers();
				} catch (IOException e) {
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
    }
}
    