package com.bezeq.locator.bl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
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

	public String submitReport(String id, String reportType, String description,
			String picture, String Lat, String Lon) {

		String data = null;
		SoapObject request = new SoapObject(Constants.NAMESPACE,
				Constants.REPORT_METHOD_NAME);

		InputStream is = null;
		byte[] array = null;
		try {
			is = new FileInputStream(picture);
			if (picture != null) {
				try {
					array = streamToBytes(is);
				} finally {
					is.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.addProperty("id", id);
		request.addProperty("reportType", reportType);
		request.addProperty("description", description);
		request.addProperty("picture", array);
		request.addProperty("Lat", Lat);
		request.addProperty("Lon", Lon);

		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

		HttpTransportSE ht = getHttpTransportSE();
		try {
			ht.call(Constants.REPORT_SOAP_ACTION, envelope);
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			data =  result.toString();
			// TODO : MOVE JSON PARSING TO HERE FROM ACTIVITY
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
		new MarshalBase64().register(envelope);
		envelope.dotNet = true;
		envelope.implicitTypes = true;
		envelope.setAddAdornments(false);
		envelope.setOutputSoapObject(request);

		return envelope;
	}

	public static byte[] streamToBytes(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = is.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (java.io.IOException e) {
		}
		return os.toByteArray();
	}
}
