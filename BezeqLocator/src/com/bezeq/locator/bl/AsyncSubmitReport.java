package com.bezeq.locator.bl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class AsyncSubmitReport extends AsyncTask<String, String, String>{
//	private ProgressDialog progressDialog;
	private Context context = null;
	
	public AsyncSubmitReport (Context c){
		this.context = c;
	}

	@Override
	protected String doInBackground(String... params) {
		WsHelper ws = new WsHelper();
		//id, reportType, description, picture, Lat, Lon
		Log.i("ASYNC_SUBMIT_REPORT","Params : ");
		Log.i("ASYNC_SUBMIT_REPORT","id : " + params[0]);
		Log.i("ASYNC_SUBMIT_REPORT","reportType : " + params[1]);
		Log.i("ASYNC_SUBMIT_REPORT","description : " + params[2]);
		Log.i("ASYNC_SUBMIT_REPORT","picture : " + params[3]);
		Log.i("ASYNC_SUBMIT_REPORT","Lat, Lon : " + params[4] + "," + params[5]);
		
		String result = ws.submitReport(params[0], params[1], params[2],params[3],params[4],params[5]);
		Log.i("SUBMIT",result);
		return result;
	}
	
	protected void onPostExecute(String result) {

		Toast t = Toast.makeText(context, "Submitted" , Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
//		progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
//		progressDialog = new ProgressDialog(this.context);
//		progressDialog.setTitle("Wait");
//		progressDialog.setMessage("Submitting report");
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.show();
	}
	
}
