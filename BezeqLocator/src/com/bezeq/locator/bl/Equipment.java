package com.bezeq.locator.bl;

import java.io.Serializable;

public abstract class Equipment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int area;
	private String settlement;
	private String street;
	private String building_num;
	private String building_sign;
	private String type;
	private double latitude;
    private double longitude;
    private double altitude;
    
	public Equipment(){};
	
	public Equipment(int id){
		this.setId(id);
	}
	
	public Equipment(int id, double lat, double lon, double alt){
		this(id);
		this.latitude = lat;
		this.longitude = lon;
		this.altitude = alt;
	}
	
	public Equipment(int id, int area, String settlement, String street, String bnum, String bsign, double lat, double lon, double alt){
		this(id, lat, lon, alt);
		this.area = area;
		this.settlement = settlement;
		this.street = street;
		this.building_num = bnum;
		this.building_sign = bsign;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getBuilding_num() {
		return building_num;
	}

	public void setBuilding_num(String building_num) {
		this.building_num = building_num;
	}

	public String getBuilding_sign() {
		return building_sign;
	}

	public void setBuilding_sign(String building_sign) {
		this.building_sign = building_sign;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getDistanceTo(double currentLat, double currentLon) {
		return Math.sqrt(Math.pow(this.latitude-currentLat, 2) + Math.pow(this.longitude - currentLon, 2));
	}

	
	
}
