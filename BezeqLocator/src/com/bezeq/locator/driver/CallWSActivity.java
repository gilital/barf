package com.bezeq.locator.driver;

import java.util.concurrent.ExecutionException;

import com.bezeq.locator.bl.AsyncCallWS;
import com.bezeq.locator.bl.WsHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class CallWSActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}// end onCreate()

	@Override
	public void onStart() {
		super.onStart();
		String[] params = { "100" };
		try {
			String hello = new AsyncCallWS(this).execute(params).get();
			Toast t = Toast.makeText(getApplicationContext(), hello,
					Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
