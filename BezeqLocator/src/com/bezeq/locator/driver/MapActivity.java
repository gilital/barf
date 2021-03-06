package com.bezeq.locator.driver;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.Constants;
import com.bezeq.locator.bl.EquipmentDataSource;
import com.bezeq.locator.gui.SensorsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends SensorsActivity {

	// private static final String TAG = "MapActivity";
	static final int AR_ACTIVITY_REQUEST = 1;
	private GoogleMap map;
	private EquipmentDataSource localData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (localData == null)
			localData = new EquipmentDataSource(this.getResources(), this);
		setContentView(R.layout.activity_map);
		selected[0] = true;
	}// end onCreate()

	@Override
	public void onStart() {
		super.onStart();
		try {
			if (map == null) {
				map = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				// Check if we were successful in obtaining the map.
				if (map != null) {
					map.setMyLocationEnabled(true);

					Location location = ARData.getCurrentLocation();
					if (location != null) {
						map.animateCamera(CameraUpdateFactory.newLatLngZoom(
								new LatLng(location.getLatitude(), location
										.getLongitude()), 13));

						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(location.getLatitude(),
										location.getLongitude())) // Sets the
																	// center of
																	// the map
																	// to
																	// location
																	// user
								.zoom(17) // Sets the zoom
								.bearing(0) // Sets the orientation of the
											// camera to north
								.build(); // Creates a CameraPosition from the
											// builder

						map.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));

					}// end if
				}// end if
			}// end if

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

		SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME,
				0);
		String userId = settings.getString(Constants.PREFS_USER_ID_NAME, "123");
		String adminId = settings.getString(Constants.PREFS_ADMIN_ID_NAME,
				"326958840");

		if (userId.compareToIgnoreCase(adminId) != 0) {
			menu.getItem(5).setVisible(false);
		}
		return true;
	}// end onCreateOptionsMenu()

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_search:
			// TODO: search
			break;
		case R.id.map_filter:
			showFilterDialog();
			break;
		case R.id.map_problem_report:
			Intent problemReportIntent = new Intent(this,
					ProblemReportActivity.class);
			startActivity(problemReportIntent);
			break;
		case R.id.ar_id_selection:
			Intent ar_intent = new Intent(this, MainActivity.class);
			startActivityForResult(ar_intent, AR_ACTIVITY_REQUEST);
			break;
		case R.id.about:
			showAboutDialog();
			break;
		case R.id.admin:
			showAdminDialog();
			break;
		}

		return true;
	}// end onOptionItemSelected()

	private void showAdminDialog() {
		SharedPreferences settings = getSharedPreferences(
				Constants.PREFS_NAME, 0);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("WS URL");
		alert.setMessage("Set url of WS");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setText(settings.getString(Constants.PREFS_URL_NAME, ""));
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				
				String value = input.getText().toString();

				SharedPreferences inSettings = MapActivity.this.getSharedPreferences(
						Constants.PREFS_NAME, 0);
				
				SharedPreferences.Editor editor = inSettings.edit();

				Constants.PREFS_URL_VALUE = value;
				editor.putString(Constants.PREFS_URL_NAME, value);

				editor.commit();
			}
		});

		
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		Location lastKnownLocation = ARData.getLastKnownLocation();
		Location currentLocation = ARData.getCurrentLocation();
		Log.i("MAP_ACTIVITY",
				"Last location = " + lastKnownLocation.getLatitude() + ","
						+ lastKnownLocation.getLongitude());
		Log.i("MAP_ACTIVITY",
				"Current location = " + currentLocation.getLatitude() + ","
						+ currentLocation.getLongitude());
		Log.i("MAP_ACTIVITY",
				"Distance : " + lastKnownLocation.distanceTo(currentLocation));

		if (lastKnownLocation.distanceTo(currentLocation) > 50.0) {
			try {
				localData.getUpdateFromWS(this, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ARData.setLastKnownLocation(ARData.getCurrentLocation());
		}

	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // Check which request we're responding to
	// if (requestCode == AR_ACTIVITY_REQUEST) {
	// if (!compareArrays(selected, data.getBooleanArrayExtra("selected"))){
	// selected = data.getBooleanArrayExtra("selected");
	// try {
	// updateMarkers();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// }
	// }

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
				.setTitle("�� ����� ��� ����")
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
						}
					}
				}).show();
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("�� ����� ��� ����");
		// builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// try {
		// updateMarkers();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
		//
		// builder.setMultiChoiceItems(items, selected,
		// new DialogInterface.OnMultiChoiceClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which,
		// boolean isChecked) {
		// selected[which] = isChecked;
		// }
		// });
		// builder.show();

		// TextView messageText =
		// (TextView)alert.findViewById(android.R.id.message);
		// messageText.setGravity(Gravity.CENTER);

	}// end showFilterDialog()

	public void updateMarkers() throws IOException {

		ARData.addMarkers(localData.getMarkers(selected));

		ArrayList<MarkerOptions> markers = (ArrayList<MarkerOptions>) localData
				.getMapMarkers(selected);

		map.clear();

		for (MarkerOptions marker : markers) {
			map.addMarker(marker);
		} // end for
	}// end updateMarkers()

	// private boolean compareArrays(boolean[] first, boolean[] second){
	// if (first.length != second.length){
	// return false;
	// }
	//
	// for (int i=0; i < first.length; i++){
	// if (first[i] != second[i]){
	// return false;
	// }
	// }
	//
	// return true;
	// }
}
