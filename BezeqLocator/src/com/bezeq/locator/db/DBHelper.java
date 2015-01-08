package com.bezeq.locator.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class for creating and upgrading the "BezeqLocator.db" Data base
 * @author Silver
 *
 */
public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "bezeqlocator.db";
	
	public static final String MSAG_TABLE_NAME = "Msags";
	public static final String CABINET_TABLE_NAME = "Cabinets";
	public static final String DBOX_TABLE_NAME = "Dboxes";
	public static final String HOLE_TABLE_NAME = "Holes";
	public static final String POLE_TABLE_NAME = "Poles";
	
	public static final String PKID_COLUMN = "pkid";
	public static final String OBJECT_ID_COLUMN = "object_id";
	public static final String MERKAZ_COLUMN = "merkaz";
	public static final String FEATURE_NUM_COLUMN = "feture_num";
	public static final String CITY_NAME_COLUMN = "city";
	public static final String STREET_NAME_COLUMN = "street";
	public static final String BUILDING_NUM_COLUMN = "building_num";
	public static final String BUILDING_LETTER_COLUMN = "building_letter";
	public static final String LONGITUDE_COLUMN = "longitude";
	public static final String LATITUDE_COLUMN = "latitude";
	
	public static final String CHANGES_TABLE_NAME = "Changes";
	public static final String CHANGES_COLUMN_ID = "id";
	public static final String CHANGES_COLUMN_TECH_ID = "tech_id";
	public static final String CHANGES_COLUMN_AREA = "area";
	public static final String CHANGES_COLUMN_EQUIPMENT_EXCHANGE_NUMBER = "exnum";
	public static final String CHANGES_COLUMN_SETTLEMENT = "settlement";
	public static final String CHANGES_COLUMN_STREET = "street";
	public static final String CHANGES_COLUMN_BUILDING_NUMBER = "bnum";
	public static final String CHANGES_COLUMN_BUILDING_SIGN = "bsign";
	public static final String CHANGES_COLUMN_EQUIPMENT_TYPE = "equip_type";
	public static final String CHANGES_COLUMN_STATUS = "status";
	public static final String CHANGES_COLUMN_LATITUDE = "latitude";
	public static final String CHANGES_COLUMN_LONGITUDE = "longtitude";
	public static final String CHANGES_COLUMN_ALTITUDE = "altitude";
	public static final String CHANGES_COLUMN_TIME_STAMP = "time_stamp";
	
	public static final String VERSIONS_TABLE_NAME = "Versions";
	public static final String VERSIONS_COLUMN_ID = "id";
	public static final String VERSIONS_COLUMN_TIME_STAMP = "time_stamp";
	public static final String VERSIONS_NUMBER_OF_RECORDS = "num_of_records";
	
	private static String DATABASE_PATH;
	private Context context;
	private SQLiteDatabase db;
	
	public DBHelper(Context context)
	{
	   super(context, DATABASE_NAME , null, 1);
	   this.context = context;
	   DATABASE_PATH = context.getFilesDir().getParentFile().getPath() + "/databases/";
	}
	
	public void create() throws IOException{

	    boolean dbExist = checkDataBase();

	    if(dbExist){
	      //do nothing - database already exist
	    }else{

	      //By calling this method and empty database will be created into the default system path
	      //of your application so we are gonna be able to overwrite that database with our database.
	      this.getReadableDatabase();

	      try {

	        copyDataBase();

	      } catch (IOException e) {

	        throw new Error("Error copying database");

	      }
	    }

	  }
	private boolean checkDataBase(){

	    SQLiteDatabase checkDB = null;

	    try{


	      String path = DATABASE_PATH + DATABASE_NAME;
	      checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

	    }catch(SQLiteException e){

	      // database don't exist yet.
	      e.printStackTrace();

	    }

	    if(checkDB != null){

	      checkDB.close();

	    }

	    return checkDB != null ? true : false;
	  }

	  // copy your assets db to the new system DB
	  private void copyDataBase() throws IOException{

	    //Open your local db as the input stream
	    InputStream myInput = context.getAssets().open(DATABASE_NAME);

	    // Path to the just created empty db
	    String outFileName = DATABASE_PATH + DATABASE_NAME;

	    //Open the empty db as the output stream
	    OutputStream myOutput = new FileOutputStream(outFileName);

	    //transfer bytes from the inputfile to the outputfile
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = myInput.read(buffer))>0){
	      myOutput.write(buffer, 0, length);
	    }

	    //Close the streams
	    myOutput.flush();
	    myOutput.close();
	    myInput.close();

	  }

	  //Open the database
	  public boolean open() {
	    try {
	      String myPath = DATABASE_PATH + DATABASE_NAME;
	      db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	      return true;
	    } catch(SQLException sqle) {
	      db = null;
	      return false;
	    }
	  }

	  @Override
	  public synchronized void close() {
	    if(db != null)
	      db.close();
	    super.close();
	  }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
//		//CREATING MSAGS TABLE
//		String sqlCreateStatement = "CREATE TABLE " + MSAG_TABLE_NAME 
//				  +"(" 
//			      + PKID_COLUMN + " integer primary key autoincrement, "
//			      + OBJECT_ID_COLUMN + " integer not null, "
//			      + MERKAZ_COLUMN + " integer not null, "
//			      + FEATURE_NUM_COLUMN + " integer not null, "
//			      + CITY_NAME_COLUMN + " text not null, "
//			      + STREET_NAME_COLUMN + " text not null, "
//			      + BUILDING_NUM_COLUMN + " integer, "
//			      + BUILDING_LETTER_COLUMN + " text, "
//			      + LONGITUDE_COLUMN + " real not null, "
//			      + LATITUDE_COLUMN + " real not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
//		
//		//CREATING CABINET TABLE
//		sqlCreateStatement = "CREATE TABLE " + CABINET_TABLE_NAME 
//				  +"(" 
//			      + PKID_COLUMN + " integer primary key autoincrement, "
//			      + OBJECT_ID_COLUMN + " integer not null, "
//			      + MERKAZ_COLUMN + " integer not null, "
//			      + FEATURE_NUM_COLUMN + " integer not null, "
//			      + CITY_NAME_COLUMN + " text not null, "
//			      + STREET_NAME_COLUMN + " text not null, "
//			      + BUILDING_NUM_COLUMN + " integer, "
//			      + BUILDING_LETTER_COLUMN + " text, "
//			      + LONGITUDE_COLUMN + " real not null, "
//			      + LATITUDE_COLUMN + " real not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
//		
//		//CREATING DBOX TABLE
//		sqlCreateStatement = "CREATE TABLE " + DBOX_TABLE_NAME 
//				  +"(" 
//			      + PKID_COLUMN + " integer primary key autoincrement, "
//			      + OBJECT_ID_COLUMN + " integer not null, "
//			      + MERKAZ_COLUMN + " integer not null, "
//			      + FEATURE_NUM_COLUMN + " integer not null, "
//			      + CITY_NAME_COLUMN + " text not null, "
//			      + STREET_NAME_COLUMN + " text not null, "
//			      + BUILDING_NUM_COLUMN + " integer, "
//			      + BUILDING_LETTER_COLUMN + " text, "
//			      + LONGITUDE_COLUMN + " real not null, "
//			      + LATITUDE_COLUMN + " real not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
//		
//		//CREATING HOLES TABLE
//		sqlCreateStatement = "CREATE TABLE " + HOLE_TABLE_NAME 
//				  +"(" 
//			      + PKID_COLUMN + " integer primary key autoincrement, "
//			      + OBJECT_ID_COLUMN + " integer not null, "
//			      + MERKAZ_COLUMN + " integer not null, "
//			      + FEATURE_NUM_COLUMN + " integer not null, "
//			      + CITY_NAME_COLUMN + " text not null, "
//			      + STREET_NAME_COLUMN + " text not null, "
//			      + BUILDING_NUM_COLUMN + " integer, "
//			      + BUILDING_LETTER_COLUMN + " text, "
//			      + LONGITUDE_COLUMN + " real not null, "
//			      + LATITUDE_COLUMN + " real not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
//		
//		//CREATING POLES TABLE
//		sqlCreateStatement = "CREATE TABLE " + POLE_TABLE_NAME 
//				  +"(" 
//			      + PKID_COLUMN + " integer primary key autoincrement, "
//			      + OBJECT_ID_COLUMN + " integer not null, "
//			      + MERKAZ_COLUMN + " integer not null, "
//			      + FEATURE_NUM_COLUMN + " integer not null, "
//			      + CITY_NAME_COLUMN + " text not null, "
//			      + STREET_NAME_COLUMN + " text not null, "
//			      + BUILDING_NUM_COLUMN + " integer, "
//			      + BUILDING_LETTER_COLUMN + " text, "
//			      + LONGITUDE_COLUMN + " real not null, "
//			      + LATITUDE_COLUMN + " real not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
//		
//		//CREATE CHANGES TABLE
//		sqlCreateStatement = "CREATE TABLE " + CHANGES_TABLE_NAME 
//				  +"(" 
//			      + CHANGES_COLUMN_ID + " integer primary key autoincrement, "
//			      + CHANGES_COLUMN_TECH_ID + " text not null, "
//			      + CHANGES_COLUMN_AREA + " integer not null, "
//			      + CHANGES_COLUMN_EQUIPMENT_EXCHANGE_NUMBER + " text not null, "
//			      + CHANGES_COLUMN_SETTLEMENT + " text not null, "
//			      + CHANGES_COLUMN_STREET + " text not null, "
//			      + CHANGES_COLUMN_BUILDING_NUMBER + " text not null, "
//			      + CHANGES_COLUMN_BUILDING_SIGN + " text, "
//			      + CHANGES_COLUMN_EQUIPMENT_TYPE + " text not null, "
//			      + CHANGES_COLUMN_STATUS + " text, "
//			      + CHANGES_COLUMN_LATITUDE + " real not null, "
//			      + CHANGES_COLUMN_LONGITUDE + " real not null, "
//			      + CHANGES_COLUMN_ALTITUDE + " real not null, "
//			      + CHANGES_COLUMN_TIME_STAMP + " int not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
//		
//		
//		//CREATE VERSIONS TABLE
//		sqlCreateStatement = "CREATE TABLE " + VERSIONS_TABLE_NAME
//				  +"(" 
//			      + VERSIONS_COLUMN_ID + " integer primary key autoincrement, "
//			      + VERSIONS_COLUMN_TIME_STAMP + " text not null, "
//			      + VERSIONS_NUMBER_OF_RECORDS + " int not null "
//			      +");";
//		db.execSQL(sqlCreateStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MSAG_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CABINET_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBOX_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HOLE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + POLE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CHANGES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + VERSIONS_TABLE_NAME);
	    onCreate(db);
	}
	
	
	
}
