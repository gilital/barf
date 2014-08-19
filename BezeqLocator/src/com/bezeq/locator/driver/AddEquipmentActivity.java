package com.bezeq.locator.driver;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.Equipment;
import com.bezeq.locator.db.ChangesDataManager;
import com.bezeq.locator.db.EquipmentDataManager;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddEquipmentActivity extends Activity {

	private String action = "new";
	
	private EditText txtTechId;
	private EditText txtArea;
	private EditText txtExnum;
	private EditText txtSettlement;
	private EditText txtStreet;
	private EditText txtBnum;
	private EditText txtBsign;
	private EditText txtType;
	private EditText txtStatus;
	private EditText txtLatitude;
	private EditText txtLongitude;
	private EditText txtAltitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_equipment);
		initPage();
	}
	
	@Override
    public void onStart() {
        super.onStart();
        Intent i = getIntent();
        action = i.getExtras().getString("action");
        
        if (action.equals("new")){
        	txtArea.setVisibility(0);
        	txtStatus.setText("בהקמה");
        }
        if (action.equals("edit")){
            Equipment equip = (Equipment)i.getSerializableExtra("selectedEquipment");
            txtExnum.setText(equip.getExchange_num());
            txtExnum.setEnabled(false);
            txtArea.setText(equip.getArea() + " ");
            txtSettlement.setText(equip.getSettlement());
            txtStreet.setText(equip.getStreet());
            txtBnum.setText(equip.getBuilding_num());
            txtBsign.setText(equip.getBuilding_sign());
            txtType.setText("מרכזת");
        }

        fillLocation();
    }
	
	private void initPage(){
		txtTechId = (EditText)findViewById(R.id.techId);
		txtArea = (EditText)findViewById(R.id.area);
		txtExnum = (EditText)findViewById(R.id.equipExnum);
		txtSettlement = (EditText)findViewById(R.id.settlement);
		txtStreet = (EditText)findViewById(R.id.street);
		txtBnum = (EditText)findViewById(R.id.bnum);
		txtBsign = (EditText)findViewById(R.id.bsign);
		txtType = (EditText)findViewById(R.id.equipType);
		txtStatus = (EditText)findViewById(R.id.equipStatus);
		txtLatitude = (EditText)findViewById(R.id.lat);
		txtLongitude = (EditText)findViewById(R.id.lon);
		txtAltitude = (EditText)findViewById(R.id.alt);
	}
	
	public void fillLocation(){
        Location current = ARData.getCurrentLocation();
        
        txtLatitude.setText(current.getLatitude() + "");
        txtLongitude.setText(current.getLongitude() + "");
        txtAltitude.setText(current.getAltitude() + "");
	}

	
	public void save(View view){
		if (action.equals("new")){
			EquipmentDataManager eDataManager = new EquipmentDataManager(this);
			eDataManager.open();
			eDataManager.insertEquipment(
					Integer.parseInt(txtArea.getText().toString()),
					txtExnum.getText().toString(),
					txtSettlement.getText().toString(),
					txtStreet.getText().toString(),
					txtBnum.getText().toString(),
					txtBsign.getText().toString(),
					Double.parseDouble(txtLatitude.getText().toString()),
					Double.parseDouble(txtLongitude.getText().toString()),
					Double.parseDouble(txtAltitude.getText().toString()));
			eDataManager.close();
			
			ChangesDataManager cDataManager = new ChangesDataManager(this);
			cDataManager.open();
			cDataManager.insertChange(
					txtTechId.getText().toString(),
					Integer.parseInt(txtArea.getText().toString()),
					txtExnum.getText().toString(),
					txtSettlement.getText().toString(),
					txtStreet.getText().toString(),
					txtBnum.getText().toString(),
					txtBsign.getText().toString(),
					txtType.getText().toString(),
					txtStatus.getText().toString(),
					Double.parseDouble(txtLatitude.getText().toString()),
					Double.parseDouble(txtLongitude.getText().toString()),
					Double.parseDouble(txtAltitude.getText().toString()));
			cDataManager.close();			
			
			Toast t = Toast.makeText(getApplicationContext(), "ציוד הוסף בהצלחה", Toast.LENGTH_SHORT);
	        t.setGravity(Gravity.BOTTOM, 0, 0);
	        t.show();

        }
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
		finish();
	}
	
}
