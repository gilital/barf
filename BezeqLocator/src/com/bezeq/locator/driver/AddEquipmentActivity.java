package com.bezeq.locator.driver;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.db.EquipmentDataManager;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddEquipmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_equipment);
	}
	
	@Override
    public void onStart() {
        super.onStart();
        this.fillLocation(findViewById(R.id.btn_get_location));

    }
	
	public void fillLocation(View view){
        Location current = ARData.getCurrentLocation();
        
        EditText lat = (EditText)findViewById(R.id.latitude);
        lat.setText(current.getLatitude() + "");
        EditText lon = (EditText)findViewById(R.id.longitude);
        lon.setText(current.getLongitude() + "");
        EditText alt = (EditText)findViewById(R.id.altitude);
        alt.setText(current.getAltitude() + "");
	}
	
	public void saveNewEquipment(View view){
//		EditText cnum = (EditText)findViewById(R.id.cnum);
//		EditText name = (EditText)findViewById(R.id.name);
//		EditText type = (EditText)findViewById(R.id.type);
//		EditText lat = (EditText)findViewById(R.id.latitude);
//		EditText lon = (EditText)findViewById(R.id.longitude);
//		EditText alt = (EditText)findViewById(R.id.altitude);
//		EquipmentDataManager dataManager = new EquipmentDataManager(this);
//		dataManager.open();
//		dataManager.insertEquipment(
//				cnum.getText().toString(), 
//				name.getText().toString(), 
//				" ",
//				" ",
//				type.getText().toString(),
//				Double.parseDouble(lat.getText().toString()),
//				Double.parseDouble(lon.getText().toString()),
//				Double.parseDouble(alt.getText().toString()));
//		
//		Toast t = Toast.makeText(getApplicationContext(), "Equipment added successfully", Toast.LENGTH_SHORT);
//        t.setGravity(Gravity.BOTTOM, 0, 0);
//        t.show();
//        this.finish();
		
	}
	
}
