package com.bezeq.locator.driver;

import com.bezeq.locator.db.DBHelper;
import com.bezeq.locator.gui.SensorsActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class LoadingScreenActivity extends SensorsActivity {
	private ViewSwitcher viewSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//startMapActivity();
		new LoadViewTask().execute();
	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		private TextView tv_progress;
		private ProgressBar pb_progressBar;
		DBHelper dbh;
		@Override
		protected void onPreExecute() {
			dbh = new DBHelper(getApplicationContext());
			viewSwitcher = new ViewSwitcher(LoadingScreenActivity.this);
			viewSwitcher.addView(ViewSwitcher
					.inflate(LoadingScreenActivity.this,
							R.layout.activity_loading, null));

			tv_progress = (TextView) viewSwitcher
					.findViewById(R.id.tv_progress);
			pb_progressBar = (ProgressBar) viewSwitcher
					.findViewById(R.id.pb_progressbar);

			pb_progressBar.setMax(100);

			setContentView(viewSwitcher);
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			try {
				dbh.create();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
//			try {
//				int lines = countLines();
//				synchronized (this) {
//					int counter = 0;
//					MsagDataManager mDataManager = new MsagDataManager(
//							getApplicationContext());
//					CabinetDataManager cDataManager = new CabinetDataManager(
//							getApplicationContext());
//					DboxDataManager dDataManager = new DboxDataManager(
//							getApplicationContext());
//					HoleDataManager hDataManager = new HoleDataManager(
//							getApplicationContext());
//					PoleDataManager pDataManager = new PoleDataManager(
//							getApplicationContext());
//
//					String str = "";
//					InputStream is = getResources()
//							.openRawResource(R.raw.equip);
//					try {
//
//						BufferedReader reader = new BufferedReader(
//								new InputStreamReader(is, "UTF-8"));
//						if (is != null) {
//							String date = reader.readLine();
//							if (newVersion(date)) {
//
//								// reader.readLine(); //skip the headers line
//
//								while ((str = reader.readLine()) != null) {
//									try {
//
//										String[] line = str.split("\t");
//										int objectID = Integer
//												.parseInt(line[0]);
//										String type = line[1];
//										int merkaz = line[2]
//												.equalsIgnoreCase("") ? 0
//												: Integer.parseInt(line[2]);
//										int featureNum = line[3]
//												.equalsIgnoreCase("") ? 0
//												: Integer.parseInt(line[3]);
//										String cityName = line[5];
//										String streetName = line[7];
//										int buildingNum = Integer
//												.parseInt(line[8]);
//										String buildingLetter = line[9];
//										String x_isr = line[10];
//										String y_isr = line[11];
//										double lon = Double
//												.parseDouble(line[12]);
//										double lat = Double
//												.parseDouble(line[13]);
//
//										System.out.println(objectID
//												+ " inserted\n");
//
//										if (type.equalsIgnoreCase("msag_mitkan")) {
//											mDataManager.open();
//											mDataManager.insert(objectID,
//													merkaz, featureNum,
//													cityName, streetName,
//													buildingNum,
//													buildingLetter, lon, lat);
//											mDataManager.close();
//										}
//
//										if (type.equalsIgnoreCase("cabinet")) {
//											cDataManager.open();
//											cDataManager.insert(objectID,
//													merkaz, featureNum,
//													cityName, streetName,
//													buildingNum,
//													buildingLetter, lon, lat);
//											cDataManager.close();
//										}
//
//										if (type.equalsIgnoreCase("dbox_all")) {
//											dDataManager.open();
//											dDataManager.insert(objectID,
//													merkaz, featureNum,
//													cityName, streetName,
//													buildingNum,
//													buildingLetter, lon, lat);
//											dDataManager.close();
//										}
//
//										if (type.equalsIgnoreCase("hole")) {
//											hDataManager.open();
//											hDataManager.insert(objectID,
//													merkaz, featureNum,
//													cityName, streetName,
//													buildingNum,
//													buildingLetter, lon, lat);
//											hDataManager.close();
//										}
//										if (type.equalsIgnoreCase("pole")) {
//											pDataManager.open();
//											pDataManager.insert(objectID,
//													merkaz, featureNum,
//													cityName, streetName,
//													buildingNum,
//													buildingLetter, lon, lat);
//											pDataManager.close();
//										}
//									} catch (Exception ex) {
//										is.close();
//									}
//									// check the type of equipment
//									// if (line[0].equals("MSAG")){
//									// String x_isr = line[7];
//									// String y_isr = line[8];
//									//
//									// if (x_isr == "250000" && y_isr ==
//									// "600000") continue;
//									//
//									// int area = Integer.parseInt(line[1]);
//									// String exnum = line[2];
//									// String settlement = line[3];
//									// String street = line[4];
//									// String building_num = line [5];
//									// String building_sign = line[6];
//									// double longtitude =
//									// Double.parseDouble(line[9]);
//									// double latitude =
//									// Double.parseDouble(line[10]);
//									// double altitude = 0.0;
//									//
//									// mDataManager.insertMsag(area, exnum,
//									// settlement, street, building_num,
//									// building_sign, latitude, longtitude,
//									// altitude);
//									// }//end if MSAG
//									//
//									// if (line[0].equals("BOX")){
//									// String x_isr = line[11];
//									// String y_isr = line[12];
//									//
//									// if (x_isr == "250000" && y_isr ==
//									// "600000") continue;
//									//
//									// String ufid = line[1];
//									// int area =
//									// (line[2].equals("")?0:Integer.parseInt(line[2]));
//									// String framework = (line[3] ==
//									// null?" ":line[3]);
//									// String location = line[4];
//									// String cbntType = line[5];
//									// String closer = line[6];
//									// String settlement = line[7];
//									// String street = line[8];
//									// String building_num = line [9];
//									// String building_sign = line[10];
//									// double longtitude =
//									// Double.parseDouble(line[13]);
//									// double latitude =
//									// Double.parseDouble(line[14]);
//									// double altitude = 0.0;
//									// bDataManager.insertBox(ufid, area,
//									// framework, location, cbntType, closer,
//									// settlement, street, building_num,
//									// building_sign, latitude, longtitude,
//									// altitude);
//									// }
//									counter++;
//									publishProgress((counter * 100) / lines,
//											counter, lines);
//								}// end while
//							}
//
//							// update version table
//							updateVersionTable(counter, date);
//
//						}// end if
//					}// end try
//					finally {
//						try {
//							is.close();
//						} catch (Throwable ignore) {
//						}
//					}// end finally
//
//				}// end synchronized
//			}// end try
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
		}

//		private boolean newVersion(String fileDate) {
//			VersionsDataManager vdm = new VersionsDataManager(
//					getApplicationContext());
//			try {
//				vdm.open();
//				Version v = vdm.getLastVersionDate();
//				if (v == null) {
//					return true;
//				}
//				Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fileDate);
//				Date versionDate = new SimpleDateFormat("yyyy-MM-dd").parse(v
//						.getTimeStamp());
//				if (fDate.after(versionDate)) {
//					return true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				vdm.close();
//			}
//			return false;
//		}
//
//		private void updateVersionTable(int counter, String date) {
//			VersionsDataManager vdm = new VersionsDataManager(
//					getApplicationContext());
//			try {
//				vdm.open();
//				vdm.insert(counter, date);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				vdm.close();
//			}
//
//		}

		// Update the TextView and the progress at progress bar
		@Override
		protected void onProgressUpdate(Integer... values) {
			// Update the progress at the UI if progress value is smaller than
			// 100
//			if (values[0] <= 100) {
//				tv_progress.setText("Progress: " + Integer.toString(values[1])
//						+ " / " + Integer.toString(values[2]) + " , "
//						+ Integer.toString(values[0]) + "%");
//				pb_progressBar.setProgress(values[0]);
//			}
		}

		// After executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			startMapActivity();
		}
	}

	// Override the default back key behavior
	@Override
	public void onBackPressed() {
		// Emulate the progressDialog.setCancelable(false) behavior
		// If the first view is being shown
		if (viewSwitcher.getDisplayedChild() == 0) {
			// Do nothing
			return;
		} else {
			// Finishes the current Activity
			super.onBackPressed();
		}
	}

	public void startMapActivity() {
		Intent i = new Intent(this, MapActivity.class);
		startActivity(i);
		finish();
	}

//	public int countLines() throws IOException {
//		InputStream is = getResources().openRawResource(R.raw.equip);
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
//				"UTF-8"));
//		try {
//			char[] buffer = new char[1024];
//			int count = 0;
//			int readChars = 0;
//			boolean empty = true;
//			while ((readChars = reader.read(buffer)) != -1) {
//				empty = false;
//				for (int i = 0; i < readChars; ++i) {
//					if (buffer[i] == '\n') {
//						++count;
//					}
//				}
//			}
//			return (count == 0 && !empty) ? 1 : count;
//		} finally {
//			is.close();
//		}
//	}
//
//	private Date parseDate(String date, String format) throws Exception {
//		SimpleDateFormat formatter = new SimpleDateFormat(format);
//		return formatter.parse(date);
//	}
}
