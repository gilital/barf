package com.bezeq.locator.bl;

public class Constants {
//	//public static final String MAIN_REQUEST_URL = "http://192.168.2.141/testws/Service1.asmx"; //--- home
//	//public static final String MAIN_REQUEST_URL = "http://10.0.0.7/testws/Service1.asmx"; // --- BEZEQ 1401
//	public static final String MAIN_REQUEST_URL = "http://10.0.0.5/testws/Service1.asmx"; // --- ACTIVATION
//	//public static final String MAIN_REQUEST_URL = "http://192.168.2.141/testws/Service1.asmx"; // -- Dani Krovadnik
//	public static final String NAMESPACE = "http://tempuri.org/";
//	public static final String EQUIPMENT_SOAP_ACTION = "http://tempuri.org/GetEquipInRange";
//    public static final String EQUIPMENT_METHOD_NAME = "GetEquipInRange";
//    
//    public static final String REPORT_SOAP_ACTION = "http://tempuri.org/SubmitReport";
//    public static final String REPORT_METHOD_NAME = "SubmitReport";
    
    
    //------------------------------------------------------------
	
	public static final String PREFS_NAME = "gala_prefs";
	
	public static final String PREFS_USER_ID_NAME = "id";
	public static final String PREFS_USER_ID_VALUE = "326958840";
	
	public static final String PREFS_URL_NAME = "url";
	public static final String PREFS_URL_VALUE = "http://10.0.0.10/testws/Service1.asmx";
	
	public static final String PREFS_NAMESPACE_NAME = "namespace";
	public static final String PREFS_NAMESPACE_VALUE = "http://tempuri.org/";
	
	public static final String PREFS_EQUIPMENT_SOAP_ACTION_NAME = "equipSoapAction";
	public static final String PREFS_EQUIPMENT_SOAP_ACTION_VALUE = "http://tempuri.org/GetEquipInRange";
	public static final String PREFS_EQUIPMENT_METHOD_NAME = "equipMethodName";
	public static final String PREFS_EQUIPMENT_METHOD_VALUE = "GetEquipInRange";
	
	public static final String PREFS_REPORT_SOAP_ACTION_NAME = "reportSoapAction";
	public static final String PREFS_REPORT_SOAP_ACTION_VALUE = "http://tempuri.org/SubmitReport";
	public static final String PREFS_REPORT_METHOD_NAME = "reportMethodName";
	public static final String PREFS_REPORT_METHOD_VALUE = "SubmitReport";
	
	public static final String PREFS_URL_TIMEOUT_NAME = "timeout";
	public static final int PREFS_URL_TIMEOUT_VALUE = 60000;
	
	public static final String PREFS_MSAGS_RANGE_NAME="msagsRange";
	public static final float PREFS_MSAGS_RANGE_VALUE = 300.0f;
	
	public static final String PREFS_CABINETS_RANGE_NAME="cabinetsRange";
	public static final float PREFS_CABINETS_RANGE_VALUE = 200.0f;
	
	public static final String PREFS_DBOXES_RANGE_NAME="dboxesRange";
	public static final float PREFS_DBOXES_RANGE_VALUE = 200.0f;
	
	public static final String PREFS_HOLES_RANGE_NAME="holesRange";
	public static final float PREFS_HOLES_RANGE_VALUE = 200.0f;
	
	public static final String PREFS_POLES_RANGE_NAME="polesRange";
	public static final float PREFS_POLES_RANGE_VALUE = 200.0f;
	

    
    public enum EquipmentTypes {MSAG, CABINET, DBOX, HOLE, POLE}
    public static final String SYSTEM_IP = "1.1.1.1";
}
