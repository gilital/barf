package com.bezeq.locator.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bezeq.locator.bl.ARData;
import com.bezeq.locator.bl.AsyncSubmitReport;
import com.bezeq.locator.bl.Constants;
import com.bezeq.locator.bl.Constants.EquipmentTypes;

public class ProblemReportActivity extends Activity {

	private static final String TAG = "Report Activity";
	private EditText txtTechId;
	private EditText txtDescription;
	private EditText txtLatitude;
	private EditText txtLongitude;
	private ImageView imgPicture;
	private Button btnPicture;
	private final int CAMERA_REQUEST = 1;
	private Uri photoUri;

	protected String selected;
	protected static CharSequence[] items = { "כתובת", "תיבה", "אחר" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		initPage();
		fillLocation();
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			String objectID = getIntent().getExtras().getString("ObjectID");
			Constants.EquipmentTypes type = (EquipmentTypes) getIntent().getExtras().get("Type");
			txtDescription.setText(objectID + type.toString() + "\n");
			selected = "אחר";
			
			
		}
		else
		{
			showFilterDialog();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void initPage() {
		txtTechId = (EditText) findViewById(R.id.report_techId);
		txtDescription = (EditText) findViewById(R.id.report_description);
		txtLatitude = (EditText) findViewById(R.id.report_lat);
		txtLongitude = (EditText) findViewById(R.id.report_long);
		imgPicture = (ImageView) findViewById(R.id.imageView1);
		btnPicture = (Button)findViewById(R.id.btnPicture);
	}
	


	public void fillLocation() {
		Location current = ARData.getCurrentLocation();

		txtLatitude.setText(current.getLatitude() + "");
		txtLongitude.setText(current.getLongitude() + "");
	}

	public void save(View view) {
		String message = 
				txtTechId.getText() + "\n"
				+ txtDescription.getText() + "\n"
				+ "Lat : " + txtLatitude.getText() + "\n"
				+ "Long : " + txtLongitude.getText() + "\n";
		//id, reportType, description, picture, Lat, Lon
//		String picture = null; //TODO: getPicture as STring
//		try{
//			//this.getContentResolver().notifyChange(photoUri, null);
//		    ContentResolver cr = this.getContentResolver();
//			InputStream in = cr.openInputStream(photoUri);
//			BufferedReader r = new BufferedReader(new InputStreamReader(in));
//			StringBuilder total = new StringBuilder();
//			String line;
//			while ((line = r.readLine()) != null) {
//			    total.append(line);
//			}
//			picture = total.toString();
//		}
//		catch (Exception ex){
//			ex.printStackTrace();
//		}
		String[] params = {txtTechId.getText() + "",selected, txtDescription.getText() + "", photoUri.getPath(), txtLatitude.getText() + "", txtLongitude.getText() + "", null};
		new AsyncSubmitReport(getApplicationContext()).execute(params);
//		Intent intent = new Intent(android.content.Intent.ACTION_SEND); // it's not ACTION_SEND
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_SUBJECT, selected);
//		intent.putExtra(Intent.EXTRA_TEXT, message);
//		intent.setData(Uri.parse("mailto:silver.alex.333@gmail.com")); // or just "mailto:" for blank
//		intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "Krochit@gmail.com", "ser-leha@yandex.ru" });
//		if (photoUri != null){
//			intent.putExtra(Intent.EXTRA_STREAM, photoUri);
//		}
//		intent.setType("image/jpeg");
//		startActivity(Intent.createChooser(intent, "Send email using"));
		finish();
	}
	
	public void capture(View view){
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File photo;
	    try
	    {
	        // place where to store camera taken picture
	        photo = this.createTemporaryFile("picture", ".jpg");
	        //photo.delete();
	    }
	    catch(Exception e)
	    {
	        Log.v(TAG, "Can't create file to take picture!");
	        return;
	    }
	    photoUri = Uri.fromFile(photo);
	    cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, photoUri );
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}
	
	private File createTemporaryFile(String part, String ext) throws Exception
	{
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
	    if(!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}
	
	public void grabImage(ImageView imageView)
	{
	    this.getContentResolver().notifyChange(photoUri, null);
	    ContentResolver cr = this.getContentResolver();
	    Bitmap bitmap;
	    try
	    {
	    	InputStream in = cr.openInputStream(photoUri);
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize=8;
	        options.inJustDecodeBounds = false;
	        options.inPreferredConfig = Config.RGB_565;
	        options.inDither = true;
	        bitmap = BitmapFactory.decodeStream(in,null,options);
//	        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, photoUri);
	        imageView.setImageBitmap(bitmap);
	    }
	    catch (Exception e)
	    {
	        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
	        Log.d(TAG, "Failed to load", e);
	    }
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        	grabImage(imgPicture);
//            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//            imgPicture.setImageBitmap(photo);
//            
////            String path = Images.Media.insertImage(getContentResolver(), photo, "attachment", null);
////            photoUri = Uri.parse(path);
            btnPicture.setVisibility(8);
        }  
    } 

	private void showFilterDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("לדווח על");
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selected= (String) items[which];
			}
		});
		{

			builder.show();
		}
	}
}
