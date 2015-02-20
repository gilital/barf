package com.bezeq.locator.driver;

import java.io.IOException;

import com.bezeq.locator.bl.Constants;
import com.bezeq.locator.db.DBHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LoadingScreenActivity extends Activity {
//	private ViewSwitcher viewSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//new LoadViewTask().execute();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		DBHelper dbh = new DBHelper(getApplicationContext());
		try {
			initPreferences();
			dbh.create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startMapActivity();
		
	}

	private void initPreferences() {
		SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.PREFS_USER_ID_NAME, Constants.PREFS_USER_ID_VALUE);
		editor.putString(Constants.PREFS_ADMIN_ID_NAME, Constants.PREFS_ADMIN_ID_VALUE);
		editor.putFloat(Constants.PREFS_MSAGS_RANGE_NAME, Constants.PREFS_MSAGS_RANGE_VALUE);
		editor.putFloat(Constants.PREFS_CABINETS_RANGE_NAME, Constants.PREFS_CABINETS_RANGE_VALUE);
		editor.putFloat(Constants.PREFS_DBOXES_RANGE_NAME, Constants.PREFS_DBOXES_RANGE_VALUE);
		editor.putFloat(Constants.PREFS_HOLES_RANGE_NAME, Constants.PREFS_HOLES_RANGE_VALUE);
		editor.putFloat(Constants.PREFS_POLES_RANGE_NAME, Constants.PREFS_POLES_RANGE_VALUE);
		
		editor.putString(Constants.PREFS_IP_ADDRESS_NAME, Constants.PREFS_IP_ADDRESS_VALUE);
		editor.putString(Constants.PREFS_URL_NAME, Constants.PREFS_URL_VALUE);
		editor.putString(Constants.PREFS_NAMESPACE_NAME, Constants.PREFS_NAMESPACE_VALUE);
		editor.putString(Constants.PREFS_EQUIPMENT_SOAP_ACTION_NAME, Constants.PREFS_EQUIPMENT_SOAP_ACTION_VALUE);
		editor.putString(Constants.PREFS_EQUIPMENT_METHOD_NAME, Constants.PREFS_EQUIPMENT_METHOD_VALUE);
		editor.putString(Constants.PREFS_REPORT_SOAP_ACTION_NAME, Constants.PREFS_REPORT_SOAP_ACTION_VALUE);
		editor.putString(Constants.PREFS_REPORT_METHOD_NAME, Constants.PREFS_REPORT_METHOD_VALUE);
		editor.putInt(Constants.PREFS_URL_TIMEOUT_NAME, Constants.PREFS_URL_TIMEOUT_VALUE);
		
		editor.commit();
	}
	
	public void startMapActivity() {
		Intent i = new Intent(this, MapActivity.class);
		startActivity(i);
		this.finish();
	}
}
//	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
//		//private TextView tv_progress;
//		private ProgressBar pb_progressBar;
//		DBHelper dbh;
//		@Override
//		protected void onPreExecute() {
//			//dbh = new DBHelper(getApplicationContext());
//			viewSwitcher = new ViewSwitcher(LoadingScreenActivity.this);
//			viewSwitcher.addView(ViewSwitcher
//					.inflate(LoadingScreenActivity.this,
//							R.layout.activity_loading, null));
//
////			tv_progress = (TextView) viewSwitcher
////					.findViewById(R.id.tv_progress);
//			pb_progressBar = (ProgressBar) viewSwitcher
//					.findViewById(R.id.pb_progressbar);
//
//			pb_progressBar.setMax(100);
//
//			setContentView(viewSwitcher);
//		}
//
//		// The code to be executed in a background thread.
//		@Override
//		protected Void doInBackground(Void... params) {
//			try {
//				initPreferences();
//				dbh.create();
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//
//		}
//		
//
////		private boolean newVersion(String fileDate) {
////			VersionsDataManager vdm = new VersionsDataManager(
////					getApplicationContext());
////			try {
////				vdm.open();
////				Version v = vdm.getLastVersionDate();
////				if (v == null) {
////					return true;
////				}
////				Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fileDate);
////				Date versionDate = new SimpleDateFormat("yyyy-MM-dd").parse(v
////						.getTimeStamp());
////				if (fDate.after(versionDate)) {
////					return true;
////				}
////			} catch (Exception e) {
////				e.printStackTrace();
////			} finally {
////				vdm.close();
////			}
////			return false;
////		}
////
////		private void updateVersionTable(int counter, String date) {
////			VersionsDataManager vdm = new VersionsDataManager(
////					getApplicationContext());
////			try {
////				vdm.open();
////				vdm.insert(counter, date);
////			} catch (Exception e) {
////				e.printStackTrace();
////			} finally {
////				vdm.close();
////			}
////
////		}
//
//		// Update the TextView and the progress at progress bar
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			// Update the progress at the UI if progress value is smaller than
//			// 100
////			if (values[0] <= 100) {
////				tv_progress.setText("Progress: " + Integer.toString(values[1])
////						+ " / " + Integer.toString(values[2]) + " , "
////						+ Integer.toString(values[0]) + "%");
////				pb_progressBar.setProgress(values[0]);
////			}
//		}
//
//		// After executing the code in the thread
//		@Override
//		protected void onPostExecute(Void result) {
//
//		}
//	}
//
//	// Override the default back key behavior
//	@Override
//	public void onBackPressed() {
//		// Emulate the progressDialog.setCancelable(false) behavior
//		// If the first view is being shown
//		if (viewSwitcher.getDisplayedChild() == 0) {
//			// Do nothing
//			return;
//		} else {
//			// Finishes the current Activity
//			super.onBackPressed();
//		}
//	}
//
//
//
////	public int countLines() throws IOException {
////		InputStream is = getResources().openRawResource(R.raw.equip);
////		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
////				"UTF-8"));
////		try {
////			char[] buffer = new char[1024];
////			int count = 0;
////			int readChars = 0;
////			boolean empty = true;
////			while ((readChars = reader.read(buffer)) != -1) {
////				empty = false;
////				for (int i = 0; i < readChars; ++i) {
////					if (buffer[i] == '\n') {
////						++count;
////					}
////				}
////			}
////			return (count == 0 && !empty) ? 1 : count;
////		} finally {
////			is.close();
////		}
////	}
////
////	private Date parseDate(String date, String format) throws Exception {
////		SimpleDateFormat formatter = new SimpleDateFormat(format);
////		return formatter.parse(date);
////	}
//}
