package com.bezeq.locator.driver;

import java.io.IOException;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.EquipmentDataSource;
import com.bezeq.locator.draw.Marker;
import com.bezeq.locator.gui.AugmentedActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * AR view activity window
 * 
 * @author Silver
 * 
 */
public class MainActivity extends AugmentedActivity {
	private static final String TAG = "MainActivity";

	EquipmentDataSource localData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			updateMarkers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		try {
			updateMarkers();

			Toast t = Toast.makeText(getApplicationContext(), ARData
					.getCurrentLocation().getLongitude() + "",
					Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();

			updateMarkers();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("selected", selected);
		setResult(1, returnIntent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected() item=" + item);
		switch (item.getItemId()) {
		// case R.id.showRadar:
		// showRadar = !showRadar;
		// item.setTitle(((showRadar)? "Hide" : "Show")+" Radar");
		// break;
		// case R.id.showZoomBar:
		// showZoomBar = !showZoomBar;
		// item.setTitle(((showZoomBar)? "Hide" : "Show")+" Zoom Bar");
		// zoomLayout.setVisibility((showZoomBar)?LinearLayout.VISIBLE:LinearLayout.GONE);
		// break;
		// case R.id.add_equipment:
		// Intent i = new Intent(this, AddEquipmentActivity.class);
		// i.putExtra("action", "new");
		// startActivity(i);
		// //finish();
		// break;
		case R.id.ar_search:
			// TODO: search
			break;
		case R.id.filter:
			showFilterDialog();
			break;
		case R.id.ar_problem_report:
			Intent problemReportIntent = new Intent(this,
					ProblemReportActivity.class);
			// this.startActivityForResult(problemReportIntent, 0);
			startActivity(problemReportIntent);
			break;
		case R.id.map_id_selection:
			finish();
			break;
		case R.id.about:
			showAboutDialog();
			break;
		}
		return true;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		Location lastKnownLocation = ARData.getLastKnownLocation();
		Location currentLocation = ARData.getCurrentLocation();
		Log.i("AR_ACTIVITY",
				"Last location = " + lastKnownLocation.getLatitude() + ","
						+ lastKnownLocation.getLongitude());
		Log.i("AR_ACTIVITY",
				"Current location = " + currentLocation.getLatitude() + ","
						+ currentLocation.getLongitude());
		Log.i("AR_ACTIVITY",
				"Distance : " + lastKnownLocation.distanceTo(currentLocation));

		if (lastKnownLocation.distanceTo(currentLocation) > 50.0) {
			localData.getUpdateFromWS(null, this);
			ARData.setLastKnownLocation(ARData.getCurrentLocation());
		}

	}

	@Override
	protected void markerTouched(Marker marker) {
		long secondClick = System.currentTimeMillis();

		if (secondClick - super.getFirstClick() < 1000) {
			Toast t = Toast.makeText(getApplicationContext(),
					marker.getComment(), Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} else {
			Intent i = new Intent(this, ProblemReportActivity.class);
			i.putExtra("ObjectID", marker.getName());
			i.putExtra("Type", marker.getType());
			startActivity(i);
			// TODO: open edit activity
			// MsagDataManager dataManager = new MsagDataManager(this);
			// dataManager.open();
			// Msag selected = dataManager.getMsagByExnum(marker.getName());
			// dataManager.close();
			// if (selected != null){
			// Intent i = new Intent(this, AddEquipmentActivity.class);
			// i.putExtra("action", "edit");
			// i.putExtra("selectedEquipment", selected);
			// startActivity(i);
			// }
		}
	}

	@Override
	protected void updateDataOnZoom() {
		super.updateDataOnZoom();
	}

	public void updateMarkers() throws IOException, InterruptedException {
		if (localData == null)
			localData = new EquipmentDataSource(this.getResources(), this);
		Thread t = new Thread(new Runnable() {
			public void run() {
				ARData.addMarkers(localData.getMarkers(selected));
			}
		});
		t.start();
		t.join();
	}

	private void showFilterDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View formElementsView = inflater.inflate(R.layout.filter_dialog,
				null, false);

		final CheckBox msagCheckBox = (CheckBox) formElementsView
				.findViewById(R.id.msagCheckBox);
		msagCheckBox.setChecked(selected[0]);
		final CheckBox dboxCheckBox = (CheckBox) formElementsView
				.findViewById(R.id.dboxCheckBox);
		dboxCheckBox.setChecked(selected[1]);
		final CheckBox holeCheckBox = (CheckBox) formElementsView
				.findViewById(R.id.holeCheckBox);
		holeCheckBox.setChecked(selected[2]);
		final CheckBox poleCheckBox = (CheckBox) formElementsView
				.findViewById(R.id.poleCheckBox);
		poleCheckBox.setChecked(selected[3]);
		final CheckBox cabinetCheckBox = (CheckBox) formElementsView
				.findViewById(R.id.cabinetCheckBox);
		cabinetCheckBox.setChecked(selected[4]);

		new AlertDialog.Builder(this).setView(formElementsView)
				.setTitle("נא לבחור סוג ציוד")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							selected[0] = msagCheckBox.isChecked() ? true
									: false;
							selected[1] = dboxCheckBox.isChecked() ? true
									: false;
							selected[2] = holeCheckBox.isChecked() ? true
									: false;
							selected[3] = poleCheckBox.isChecked() ? true
									: false;
							selected[4] = cabinetCheckBox.isChecked() ? true
									: false;
							updateMarkers();

							dialog.cancel();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).show();

	}
}
