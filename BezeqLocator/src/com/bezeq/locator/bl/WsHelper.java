package com.bezeq.locator.bl;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WsHelper {
	
	public String getEquipInRange(String Lat, String Lon, String range) {
		String data = null;
 
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME);
        request.addProperty("Lat",Lat.toString());
        request.addProperty("Lon",Lon.toString());
        request.addProperty("RadiusInMeter",range.toString());
         
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
 
        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(Constants.SOAP_ACTION, envelope);
            SoapPrimitive resultsString = (SoapPrimitive)envelope.getResponse();

            //TODO : MOVE JSON PARSING TO HERE FROM ACTIVITY
            data = resultsString.toString();
 
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
   return data;
	}
	
	private final HttpTransportSE getHttpTransportSE() {
	    HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,Constants.MAIN_REQUEST_URL,60000);
	    ht.debug = true;
	    ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
	    return ht;
	}
	
	private final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope.dotNet = true;
	    envelope.implicitTypes = true;
	    envelope.setAddAdornments(false);
	    envelope.setOutputSoapObject(request);
	 
	    return envelope;
	}
}
