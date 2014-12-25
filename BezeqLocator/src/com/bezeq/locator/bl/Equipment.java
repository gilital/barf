package com.bezeq.locator.bl;

import java.io.Serializable;

public abstract class Equipment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int objectID;
	private Constants.EquipmentTypes type;
	private int merkaz;
	private int featureNum;
	private String cityName;
	private String streetName;
	private int buildingNum;
	private String buildingLetter;
	private double latitude;
	private double longitude;
	private double altitude;

	public Equipment() {
	};

	public Equipment(int id) {
		this.setId(id);
	}

	public Equipment(int id, double lon, double lat, double alt) {
		this(id);
		this.latitude = lat;
		this.longitude = lon;
		this.altitude = alt;
	}

	public Equipment(int id, int objectID, Constants.EquipmentTypes type,
			int merkaz, int featureNum, String cityName, String streetName,
			int buildingNum, String buildingLetter, double lon, double lat,
			double alt) {
		this(id, lon, lat, alt);
		this.setObjectID(objectID);
		this.setType(type);
		this.setMerkaz(merkaz);
		this.setFeatureNum(featureNum);
		this.setCityName(cityName);
		this.setStreetName(streetName);
		this.setBuildingNum(buildingNum);
		this.setBuildingLetter(buildingLetter);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public Constants.EquipmentTypes getType() {
		return type;
	}

	public void setType(Constants.EquipmentTypes type) {
		this.type = type;
	}

	public int getMerkaz() {
		return merkaz;
	}

	public void setMerkaz(int merkaz) {
		this.merkaz = merkaz;
	}

	public int getFeatureNum() {
		return featureNum;
	}

	public void setFeatureNum(int featureNum) {
		this.featureNum = featureNum;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public int getBuildingNum() {
		return buildingNum;
	}

	public void setBuildingNum(int buildingNum) {
		this.buildingNum = buildingNum;
	}

	public String getBuildingLetter() {
		return buildingLetter;
	}

	public void setBuildingLetter(String buildingLetter) {
		this.buildingLetter = buildingLetter;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

}
