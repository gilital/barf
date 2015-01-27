package com.bezeq.locator.bl;

public class Constants {
//	//public static  String MAIN_REQUEST_URL = "http://192.168.2.141/testws/Service1.asmx"; //--- home
//	//public static  String MAIN_REQUEST_URL = "http://10.0.0.7/testws/Service1.asmx"; // --- BEZEQ 1401
//	public static  String MAIN_REQUEST_URL = "http://10.0.0.5/testws/Service1.asmx"; // --- ACTIVATION
//	//public static  String MAIN_REQUEST_URL = "http://192.168.2.141/testws/Service1.asmx"; // -- Dani Krovadnik
//	public static  String NAMESPACE = "http://tempuri.org/";
//	public static  String EQUIPMENT_SOAP_ACTION = "http://tempuri.org/GetEquipInRange";
//    public static  String EQUIPMENT_METHOD_NAME = "GetEquipInRange";
//    
//    public static  String REPORT_SOAP_ACTION = "http://tempuri.org/SubmitReport";
//    public static  String REPORT_METHOD_NAME = "SubmitReport";
    
    
    //------------------------------------------------------------
	
	public static  String PREFS_NAME = "gala_prefs";
	
	public static  String PREFS_ADMIN_ID_NAME = "adminId";
	public static  String PREFS_ADMIN_ID_VALUE = "326958840";
	
	public static  String PREFS_USER_ID_NAME = "id";
	public static  String PREFS_USER_ID_VALUE = "326958840";
	
	public static String PREFS_IP_ADDRESS_NAME = "serverIP";
	public static String PREFS_IP_ADDRESS_VALUE = "10.0.0.10";
	
	public static  String PREFS_URL_NAME = "url";
	public static  String PREFS_URL_VALUE = "http://"+ PREFS_IP_ADDRESS_VALUE +"/testws/Service1.asmx";
	
	public static  String PREFS_NAMESPACE_NAME = "namespace";
	public static  String PREFS_NAMESPACE_VALUE = "http://tempuri.org/";
	
	public static  String PREFS_EQUIPMENT_SOAP_ACTION_NAME = "equipSoapAction";
	public static  String PREFS_EQUIPMENT_SOAP_ACTION_VALUE = "http://tempuri.org/GetEquipInRange";
	public static  String PREFS_EQUIPMENT_METHOD_NAME = "equipMethodName";
	public static  String PREFS_EQUIPMENT_METHOD_VALUE = "GetEquipInRange";
	
	public static  String PREFS_REPORT_SOAP_ACTION_NAME = "reportSoapAction";
	public static  String PREFS_REPORT_SOAP_ACTION_VALUE = "http://tempuri.org/SubmitReport";
	public static  String PREFS_REPORT_METHOD_NAME = "reportMethodName";
	public static  String PREFS_REPORT_METHOD_VALUE = "SubmitReport";
	
	public static  String PREFS_URL_TIMEOUT_NAME = "timeout";
	public static  int PREFS_URL_TIMEOUT_VALUE = 60000;
	
	public static  String PREFS_MSAGS_RANGE_NAME="msagsRange";
	public static  float PREFS_MSAGS_RANGE_VALUE = 300.0f;
	
	public static  String PREFS_CABINETS_RANGE_NAME="cabinetsRange";
	public static  float PREFS_CABINETS_RANGE_VALUE = 200.0f;
	
	public static  String PREFS_DBOXES_RANGE_NAME="dboxesRange";
	public static  float PREFS_DBOXES_RANGE_VALUE = 200.0f;
	
	public static  String PREFS_HOLES_RANGE_NAME="holesRange";
	public static  float PREFS_HOLES_RANGE_VALUE = 200.0f;
	
	public static  String PREFS_POLES_RANGE_NAME="polesRange";
	public static  float PREFS_POLES_RANGE_VALUE = 200.0f;
	

    
    public enum EquipmentTypes {MSAG, CABINET, DBOX, HOLE, POLE}
    public static  String SYSTEM_IP = "1.1.1.1";
}
