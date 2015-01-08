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

		SoapObject request = new SoapObject(Constants.NAMESPACE,
				Constants.EQUIPMENT_METHOD_NAME);
		request.addProperty("Lat", Lat.toString());
		request.addProperty("Lon", Lon.toString());
		request.addProperty("RadiusInMeter", range.toString());

		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

		HttpTransportSE ht = getHttpTransportSE();
		try {
			ht.call(Constants.EQUIPMENT_SOAP_ACTION, envelope);
			SoapPrimitive resultsString = (SoapPrimitive) envelope
					.getResponse();
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

	public void submitReport(String id, String reportType, String description,
			String picture, String Lat, String Lon) {
		SoapObject request = new SoapObject(Constants.NAMESPACE,
				Constants.REPORT_METHOD_NAME);
		request.addProperty("", id);
		request.addProperty("", reportType);
		request.addProperty("", description);
		request.addProperty("", picture);
		request.addProperty("", Lat);
		request.addProperty("", Lon);
		

		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

		HttpTransportSE ht = getHttpTransportSE();
		try {
			ht.call(Constants.REPORT_SOAP_ACTION, envelope);
			//SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			// TODO : MOVE JSON PARSING TO HERE FROM ACTIVITY
		} catch (SocketTimeoutException t) {
			t.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (Exception q) {
			q.printStackTrace();
		}
	}

	private final HttpTransportSE getHttpTransportSE() {
		HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,
				Constants.MAIN_REQUEST_URL, 60000);
		ht.debug = true;
		ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
		return ht;
	}

	private final SoapSerializationEnvelope getSoapSerializationEnvelope(
			SoapObject request) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		envelope.setOutputSoapObject(request);

		return envelope;
	}
}
