package com.bezeq.locator.bl;

public class Constants {
	//public static final String MAIN_REQUEST_URL = "http://192.168.2.141/testws/Service1.asmx"; //--- home
	public static final String MAIN_REQUEST_URL = "http://10.0.0.7/testws/Service1.asmx"; // ---azrieli
	public static final String NAMESPACE = "http://tempuri.org/";
	
	
	public static final String EQUIPMENT_SOAP_ACTION = "http://tempuri.org/GetEquipInRange";
    public static final String EQUIPMENT_METHOD_NAME = "GetEquipInRange";
    
    public static final String REPORT_SOAP_ACTION = "";
    public static final String REPORT_METHOD_NAME = "";
    
    public enum EquipmentTypes {MSAG, CABINET, DBOX, HOLE, POLE}
    public static final String SYSTEM_IP = "1.1.1.1";
    
}
