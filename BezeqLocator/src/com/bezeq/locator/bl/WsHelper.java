package com.bezeq.locator.bl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Proxy;
import android.content.Context;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.SharedPreferences;

public class WsHelper {

	private static WsHelper instance = null;
	private Context context;

	private WsHelper(Context context) {
		this.context = context;
	}

	public static WsHelper getInstance(Context context) {
		if (instance == null) {
			instance = new WsHelper(context);
		}
		return instance;
	}

	public String getEquipInRange(String Lat, String Lon, String range) {
		String data = null;

		SharedPreferences settings = context.getSharedPreferences(
				Constants.PREFS_NAME, 0);

		SoapObject request = new SoapObject(settings.getString(
				Constants.PREFS_NAMESPACE_NAME, null), settings.getString(
				Constants.PREFS_EQUIPMENT_METHOD_NAME, null));
		request.addProperty("Lat", Lat.toString());
		request.addProperty("Lon", Lon.toString());
		request.addProperty("RadiusInMeter", range.toString());

		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

		HttpTransportSE ht = getHttpTransportSE();
		try {
			ht.call(settings.getString(
					Constants.PREFS_EQUIPMENT_SOAP_ACTION_NAME, null), envelope);
			SoapPrimitive resultsString = (SoapPrimitive) envelope
					.getResponse();
			data = resultsString.toString();
		} catch (Exception q) {
			q.printStackTrace();
		}
		return data;
	}

	public String submitReport(int pkid, String id, String reportType,
			String description, String picture, String Lat, String Lon,
			String timeStamp) {

		SharedPreferences settings = context.getSharedPreferences(
				Constants.PREFS_NAME, 0);
		// IF pkid == 0 --> new report, else, trying submit old report
		String data = null;
		SoapObject request = new SoapObject(settings.getString(
				Constants.PREFS_NAMESPACE_NAME, null), settings.getString(
				Constants.PREFS_REPORT_METHOD_NAME, null));

		InputStream is = null;
		byte[] array = null;
		if (picture != null) {
			try {
				is = new FileInputStream(picture);
				if (is != null) {
					try {
						array = streamToBytes(is);
					} finally {
						is.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		request.addProperty("id", id);
		request.addProperty("reportType", reportType);
		request.addProperty("description", description);
		request.addProperty("picture", array);
		request.addProperty("Lat", Lat);
		request.addProperty("Lon", Lon);
		request.addProperty("timeStamp", timeStamp);

		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

		HttpTransportSE ht = getHttpTransportSE();
		try {
			ht.call(settings.getString(
					Constants.PREFS_REPORT_SOAP_ACTION_NAME, null), envelope);
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			data = result.toString();
		} catch (Exception q) {
			q.printStackTrace();
		}

		if (data == null && pkid != 0) {
			// TODO : error to connect, save to local DB
		}
		return data;
	}

	private final HttpTransportSE getHttpTransportSE() {
		SharedPreferences settings = context.getSharedPreferences(
				Constants.PREFS_NAME, 0);
		
		HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,
				settings.getString(
						Constants.PREFS_URL_NAME, null), settings.getInt(
								Constants.PREFS_URL_TIMEOUT_NAME, 60000));
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
