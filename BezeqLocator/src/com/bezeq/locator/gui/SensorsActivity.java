package com.bezeq.locator.gui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.draw.LowPassFilter;
import com.bezeq.locator.draw.Matrix;
import com.bezeq.locator.driver.R;
import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SensorsActivity extends FragmentActivity implements
		SensorEventListener, LocationListener,
		ConnectionCallbacks, OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {
	private static final String TAG = "SensorsActivity";

	private static final AtomicBoolean computing = new AtomicBoolean(false);
	private static final int MIN_TIME = 30 * 1000;
	private static final int MIN_DISTANCE = 10;

	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

	private static GoogleApiClient mGoogleApiClient;
	protected LocationRequest mLocationRequest;
	protected String mLastUpdateTime;

	private static final float temp[] = new float[9];
	private static final float rotation[] = new float[9];
	private static final float grav[] = new float[3];
	private static final float mag[] = new float[3];

	private static final Matrix worldCoord = new Matrix();
	private static final Matrix magneticCompensatedCoord = new Matrix();
	private static final Matrix xAxisRotation = new Matrix();
	private static final Matrix magneticNorthCompensation = new Matrix();

	private static GeomagneticField gmf = null;
	private static float smooth[] = new float[3];
	private static SensorManager sensorMgr = null;
	private static List<Sensor> sensors = null;
	private static Sensor sensorGrav = null;
	private static Sensor sensorMag = null;
	private static LocationManager locationMgr = null;
	// private static LocationClient locationClnt = null;

	protected static boolean[] selected = new boolean[5];
	protected static CharSequence[] items = { "MSAG", "תיבות", "גובים",
			"עמודים", "ארונות" };

	Location google_play_client = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// locationClnt = new LocationClient(this, this, this);
		locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		buildGoogleApiClient();
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		createLocationRequest();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		// Sets the desired interval for active location updates. This interval
		// is
		// inexact. You may not receive updates at all if no location sources
		// are available, or
		// you may receive them slower than requested. You may also receive
		// updates faster than
		// requested if other applications are requesting location at a faster
		// interval.
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		// Sets the fastest rate for active location updates. This interval is
		// exact, and your
		// application will never receive updates faster than this value.
		mLocationRequest
				.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	protected void startLocationUpdates() {
		// The final argument to {@code requestLocationUpdates()} is a
		// LocationListener
		// (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	protected void stopLocationUpdates() {
		// It is a good practice to remove location requests when the activity
		// is in a paused or
		// stopped state. Doing so helps battery performance and is especially
		// recommended in applications that request frequent location updates.
		// The final argument to {@code requestLocationUpdates()} is a
		// LocationListener
		// (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		// locationClnt.connect();
		mGoogleApiClient.connect(); // connect using google api
		double angleX = Math.toRadians(-90);
		double angleY = Math.toRadians(-90);

		// Counter-clockwise rotation at -90 degrees around the x-axis
		// [ 1, 0, 0 ]
		// [ 0, cos, -sin ]
		// [ 0, sin, cos ]
		xAxisRotation.set(1f, 0f, 0f, 0f, (float) Math.cos(angleX),
				(float) -Math.sin(angleX), 0f, (float) Math.sin(angleX),
				(float) Math.cos(angleX));

		try {
			sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);

			if (sensors.size() > 0) {
				sensorGrav = sensors.get(0);
			}

			sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);

			if (sensors.size() > 0) {
				sensorMag = sensors.get(0);
			}
			// sensorMgr.registerListener(this, sensorGrav,
			// SensorManager.SENSOR_DELAY_NORMAL);
			// sensorMgr.registerListener(this, sensorMag,
			// SensorManager.SENSOR_DELAY_NORMAL);
			sensorMgr.registerListener(this, sensorGrav,
					SensorManager.SENSOR_DELAY_FASTEST);
			sensorMgr.registerListener(this, sensorMag,
					SensorManager.SENSOR_DELAY_FASTEST);

			locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					MIN_TIME, MIN_DISTANCE, this);
			locationMgr.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
					this);

			try {

				try {
					Location gps = null;
					Location network = null;
					if (locationMgr
							.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						// if gps is turned on
						if (System.currentTimeMillis()
								- locationMgr.getLastKnownLocation(
										LocationManager.GPS_PROVIDER).getTime() > (10 * 60 * 1000)) { // 10
																										// minutes
							// if last location not up to date
							if (locationMgr
									.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
								locationMgr.requestSingleUpdate(
										LocationManager.NETWORK_PROVIDER, this,
										null);
								network = locationMgr
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								onLocationChanged(network);
							} else {
								throw new Exception(
										"Connection is down and GPS location is not up to date");
							}
						} else {
							gps = locationMgr
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							onLocationChanged(gps);
						}
					} else {
						buildAlertMessageNoGps();
						if (locationMgr
								.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
							locationMgr.requestSingleUpdate(
									LocationManager.NETWORK_PROVIDER, this,
									null);
							network = locationMgr
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							onLocationChanged(network);
						} else {
							throw new Exception(
									"Connection is down and GPS location is not up to date");
						}
					}
				} catch (Exception ex2) {
					ex2.printStackTrace();
					onLocationChanged(ARData.hardFix);
					Toast t = Toast.makeText(getApplicationContext(),
							ex2.getMessage(), Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER, 0, 0);
					t.show();
					finish();
				}

				gmf = new GeomagneticField((float) ARData.getCurrentLocation()
						.getLatitude(), (float) ARData.getCurrentLocation()
						.getLongitude(), (float) ARData.getCurrentLocation()
						.getAltitude(), System.currentTimeMillis());
				angleY = Math.toRadians(-gmf.getDeclination());

				synchronized (magneticNorthCompensation) {

					magneticNorthCompensation.toIdentity();

					magneticNorthCompensation.set((float) Math.cos(angleY), 0f,
							(float) Math.sin(angleY), 0f, 1f, 0f,
							(float) -Math.sin(angleY), 0f,
							(float) Math.cos(angleY));

					magneticNorthCompensation.prod(xAxisRotation);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex1) {
			try {
				if (sensorMgr != null) {
					sensorMgr.unregisterListener(this, sensorGrav);
					sensorMgr.unregisterListener(this, sensorMag);
					sensorMgr = null;
				}
				if (locationMgr != null) {
					locationMgr.removeUpdates(this);
					locationMgr = null;
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mGoogleApiClient.isConnected()) {
			startLocationUpdates();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Stop location updates to save battery, but don't disconnect the
		// GoogleApiClient object.
		if (mGoogleApiClient.isConnected()) {
			stopLocationUpdates();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		// locationClnt.disconnect();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
		try {
			try {
				sensorMgr.unregisterListener(this, sensorGrav);
				sensorMgr.unregisterListener(this, sensorMag);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sensorMgr = null;

			try {
				locationMgr.removeUpdates(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			locationMgr = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onSensorChanged(SensorEvent evt) {
		if (!computing.compareAndSet(false, true))
			return;

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			smooth = LowPassFilter.filter(0.5f, 1.0f, evt.values, grav);
			grav[0] = smooth[0];
			grav[1] = smooth[1];
			grav[2] = smooth[2];
		} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			smooth = LowPassFilter.filter(2.0f, 4.0f, evt.values, mag);
			mag[0] = smooth[0];
			mag[1] = smooth[1];
			mag[2] = smooth[2];
		}

		// Find real world position relative to phone location
		// Get rotation matrix given the gravity and geomagnetic matrices
		SensorManager.getRotationMatrix(temp, null, grav, mag);

		SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y,
				SensorManager.AXIS_MINUS_X, rotation);

		worldCoord
				.set(rotation[0], rotation[1], rotation[2], rotation[3],
						rotation[4], rotation[5], rotation[6], rotation[7],
						rotation[8]);

		magneticCompensatedCoord.toIdentity();

		synchronized (magneticNorthCompensation) {
			// Cross product the matrix with the magnetic north compensation
			magneticCompensatedCoord.prod(magneticNorthCompensation);
		}

		// Cross product with the world coordinates to get a mag north
		// compensated coords
		magneticCompensatedCoord.prod(worldCoord);

		magneticCompensatedCoord.invert();

		// Set the rotation matrix (used to translate all object from lat/lon to
		// x/y/z)
		ARData.setRotationMatrix(magneticCompensatedCoord);

		computing.set(false);
	}

	public void onProviderDisabled(String provider) {
		// Not Used
	}

	public void onProviderEnabled(String provider) {
		// Not Used
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Not Used
	}

	public void onLocationChanged(Location location) {
		ARData.setCurrentLocation(location);
		mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
		gmf = new GeomagneticField((float) ARData.getCurrentLocation()
				.getLatitude(), (float) ARData.getCurrentLocation()
				.getLongitude(), (float) ARData.getCurrentLocation()
				.getAltitude(), System.currentTimeMillis());

		double angleY = Math.toRadians(-gmf.getDeclination());

		synchronized (magneticNorthCompensation) {
			magneticNorthCompensation.toIdentity();

			magneticNorthCompensation.set((float) Math.cos(angleY), 0f,
					(float) Math.sin(angleY), 0f, 1f, 0f,
					(float) -Math.sin(angleY), 0f, (float) Math.cos(angleY));

			magneticNorthCompensation.prod(xAxisRotation);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor == null)
			throw new NullPointerException();

		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			Log.e(TAG, "Compass data unreliable");
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		google_play_client = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		if (google_play_client != null) {
			mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
			onLocationChanged(google_play_client);
		}
		
		startLocationUpdates();
	}

//	@Override
//	public void onDisconnected() {
//		// TODO Auto-generated method stub
//		Toast.makeText(this, "Disconnected. Please re-connect.",
//				Toast.LENGTH_SHORT).show();
//	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Yout GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	protected void showAboutDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View aboutFormView = inflater.inflate(R.layout.about_dialog,
				null, false);
		final TextView aboutText = (TextView) aboutFormView
				.findViewById(R.id.about_text);
		String version = null;
		PackageInfo pInfo;
		ApplicationInfo appInfo;
		String message = "";
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
			appInfo = getPackageManager().getApplicationInfo(
					"com.bezeq.locator.driver", 0);
			String appFile = appInfo.sourceDir;
			long installed = new File(appFile).lastModified();
			Date versionDate = new Date(installed);
			String format = "HH:mm dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

			message += "תאריך גרסה : ";
			message += sdf.format(versionDate) + "\n";
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message += "לדווח על תקלה באפליקציה\n";
		message += "גיל דהרי : ";
		message += "gil.dahari@bezeq.co.il\n";
		message += "אלכסיי סרבריאני : ";
		message += "silver.alex.333@gmail.com";
		aboutText.setText(message);

		new AlertDialog.Builder(this).setView(aboutFormView)
				.setTitle("GALA, " + "גרסה " + version)
				.setIcon(R.drawable.bezeq)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();

	}
}