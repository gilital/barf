package com.bezeq.locator.bl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncCallWS extends AsyncTask<String, Void, String> {
	private ProgressDialog progressDialog;
	private Context context = null;
	
	public AsyncCallWS (Context c){
		this.context = c;
		
	}
	@Override
	protected String doInBackground(String... params) {
		WsHelper ws = new WsHelper();
		return ws.callWS(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {

		progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setTitle("Wait");
		progressDialog.setMessage("Fetching information");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
