package com.bezeq.locator.driver;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.EquipmentDataSource;
import com.bezeq.locator.draw.Marker;
import com.bezeq.locator.gui.AugmentedActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AugmentedActivity {
    private static final String TAG = "MainActivity";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateMarkers();
    }

	@Override
	public void onResume(){
		super.onResume();
		updateMarkers();
	}
	
	@Override
    public void onStart() {
        super.onStart();
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
            case R.id.addEquipment:
            	Intent intent = new Intent(this,AddEquipmentActivity.class);
            	startActivity(intent);
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
        Toast t = Toast.makeText(getApplicationContext(), marker.getComment(), Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
	}

    @Override
	protected void updateDataOnZoom() {
	    super.updateDataOnZoom();
	}
    
    private void updateMarkers(){
        EquipmentDataSource localData = new EquipmentDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers(this));
    }
}
    