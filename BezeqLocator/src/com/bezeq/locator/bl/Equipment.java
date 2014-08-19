package com.bezeq.locator.bl;

import java.io.Serializable;

public class Equipment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int area;
	private String exchange_num;
	private String settlement;
	private String street;
	private String building_num;
	private String building_sign;
	private double latitude;
    private double longitude;
    private double altitude;
	
    public Equipment(){}
    
    public Equipment(int id, int area, String exnum, String settlement, String street, String bnum, String bsign, double latitude, double longitude, double altitude){
    	this.id = id;
    	this.setArea(area);
    	this.setExchange_num(exnum);
    	this.setSettlement(settlement);
    	this.setStreet(street);
    	this.setBuilding_num(bnum);
    	this.setBuilding_sign(bsign);
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.altitude = altitude;    	
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

	public String getExchange_num() {
		return exchange_num;
	}

	public void setExchange_num(String exchange_num) {
		this.exchange_num = exchange_num;
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

	/**
	 * Calculating distance from current coordinate to a given coordinate 
	 * by using haversine formula
	 * 
	 * @param latitude of a given coordinate
	 * @param longtitude of a given coordinate
	 * @return distance in km
	 */
	public double getDistance(double latitude, double longtitude){
		double R = 6371;
		double dLat = degree2radian(latitude - this.latitude);
		double dLon = degree2radian(longtitude - this.longitude);
		double a = 
				Math.sin(dLat/2) * Math.sin(dLat/2) +
			    Math.cos(degree2radian(latitude)) * Math.cos(degree2radian(this.latitude)) * 
			    Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return R*c;
	}
	
	/**
	 * Converting degrees to radians
	 * 
	 * @param degree - angle in degree measure
	 * @return angle in radian measure
	 */
	private double degree2radian(double degree){
		return degree * (Math.PI/180);
	}
    
}
