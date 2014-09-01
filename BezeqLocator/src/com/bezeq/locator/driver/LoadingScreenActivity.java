package com.bezeq.locator.driver;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bezeq.locator.bl.Version;
import com.bezeq.locator.db.BoxDataManager;
import com.bezeq.locator.db.MsagDataManager;
import com.bezeq.locator.db.VersionsDataManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class LoadingScreenActivity extends Activity 
{
	//creates a ViewSwitcher object, to switch between Views
	private ViewSwitcher viewSwitcher;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

		//Initialize a LoadViewTask object and call the execute() method
        new LoadViewTask().execute();
    }
    
    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
    	//A TextView object and a ProgressBar object
    	private TextView tv_progress;
    	private ProgressBar pb_progressBar;
    	
    	//Before running code in the separate thread
		@Override
		protected void onPreExecute() 
		{
			//Initialize the ViewSwitcher object
	        viewSwitcher = new ViewSwitcher(LoadingScreenActivity.this);
	        /* Initialize the loading screen with data from the 'loadingscreen.xml' layout xml file. 
	         * Add the initialized View to the viewSwitcher.*/
			viewSwitcher.addView(ViewSwitcher.inflate(LoadingScreenActivity.this, R.layout.activity_loading, null));
			
			//Initialize the TextView and ProgressBar instances - IMPORTANT: call findViewById() from viewSwitcher.
			tv_progress = (TextView) viewSwitcher.findViewById(R.id.tv_progress);
			pb_progressBar = (ProgressBar) viewSwitcher.findViewById(R.id.pb_progressbar);
			//Sets the maximum value of the progress bar to 1000		
			pb_progressBar.setMax(100);
			
			//Set ViewSwitcher instance as the current View.
			setContentView(viewSwitcher);
		}

		//The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) 
		{
			try 
			{
				int lines = countLines();
				//Get the current thread's token
				synchronized (this) 
				{
					int counter = 0;
					MsagDataManager mDataManager = new MsagDataManager(getApplicationContext());
					BoxDataManager bDataManager = new BoxDataManager(getApplicationContext());
					mDataManager.open();
					bDataManager.open();
					
					String str="";
					InputStream is = getResources().openRawResource(R.raw.equip);
					try {
					
				        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				        if (is!=null) {
				        	String date = reader.readLine();
				        	if(newVersion(date)){
					        	
					        	//reader.readLine(); //skip the headers line
					        	
					            while ((str = reader.readLine()) != null) {
					                String[] line = str.split("\t");
					                
					                //check the type of equipment
					                if (line[0].equals("MSAG")){
					                	String x_isr = line[7];
						                String y_isr = line[8];
						                
						                if (x_isr == "250000" && y_isr == "600000") continue;
						                
						                int area = Integer.parseInt(line[1]);
						                String exnum = line[2];
						                String settlement = line[3];
						                String street = line[4];
						                String building_num = line [5];
						                String building_sign = line[6];
						                double longtitude = Double.parseDouble(line[9]);
						                double latitude = Double.parseDouble(line[10]);
						                double altitude = 0.0;
							                
						                mDataManager.insertMsag(area, exnum, settlement, street, building_num, building_sign, latitude, longtitude, altitude);
					                }//end if MSAG
					                
					                if (line[0].equals("BOX")){
					                	String x_isr = line[11];
						                String y_isr = line[12];
						                
						                if (x_isr == "250000" && y_isr == "600000") continue;
						                
						                String ufid = line[1];
						                int area = (line[2].equals("")?0:Integer.parseInt(line[2]));
						                String framework = (line[3] == null?" ":line[3]);
						                String location = line[4];
						                String cbntType = line[5];
						                String closer = line[6];
						                String settlement = line[7];
							            String street = line[8];
							            String building_num = line [9];
							            String building_sign = line[10];
							            double longtitude = Double.parseDouble(line[13]);
							            double latitude = Double.parseDouble(line[14]);
							            double altitude = 0.0;
						                bDataManager.insertBox(ufid, area, framework, location, cbntType, closer, settlement, street, building_num, building_sign, latitude, longtitude, altitude);
					                }
					                counter++;
					                publishProgress((counter*100)/lines);
				            }//end while
				        	}
				            
				            //update version table
				            updateVersionTable(counter, date);
				            
				        }//end if
				    }//end try
						finally {
					        try { is.close(); } catch (Throwable ignore) {}
					    }//end finally

					mDataManager.close();
					bDataManager.close();
				}//end synchronized
			}//end try
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private boolean newVersion(String fileDate) {
			VersionsDataManager vdm = new VersionsDataManager(getApplicationContext());
			try {				
				vdm.open();
				Version v = vdm.getLastVersionDate();
				if(v == null){
					return true;
				}
				Date fDate = parseDate(fileDate, "yyyy-MM-dd");
				Date versionDate = parseDate(v.getTimeStamp(), "yyyy-MM-dd");
				if(fDate.after(versionDate)){
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				vdm.close();
			}
			return false;
		}

		private void updateVersionTable(int counter, String date) {
			VersionsDataManager vdm = new VersionsDataManager(getApplicationContext());
			try {				
				vdm.open();
				vdm.insert(counter, date);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				vdm.close();
			}
			
		}

		//Update the TextView and the progress at progress bar
		@Override
		protected void onProgressUpdate(Integer... values) 
		{
			//Update the progress at the UI if progress value is smaller than 100
			if(values[0] <= 100)
			{
				tv_progress.setText("Progress: " + Integer.toString(values[0]) + "%");
				pb_progressBar.setProgress(values[0]);
			}
		}
		
		//After executing the code in the thread
		@Override
		protected void onPostExecute(Void result) 
		{
			startMainActivity();
		}
    }
    
    //Override the default back key behavior
    @Override
    public void onBackPressed() 
    {
    	//Emulate the progressDialog.setCancelable(false) behavior
    	//If the first view is being shown
    	if(viewSwitcher.getDisplayedChild() == 0)
    	{
    		//Do nothing
    		return;
    	}
    	else
    	{
    		//Finishes the current Activity
    		super.onBackPressed();
    	}
    }
    
    public void startMainActivity(){
		Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

	public int countLines() throws IOException {
		InputStream is = getResources().openRawResource(R.raw.equip);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	    try {
	        char[] buffer = new char[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = reader.read(buffer)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (buffer[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	
	private Date parseDate(String date, String format) throws Exception
	{
	    SimpleDateFormat formatter = new SimpleDateFormat(format);
	    return formatter.parse(date);
	}
}

