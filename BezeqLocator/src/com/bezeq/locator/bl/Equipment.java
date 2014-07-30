package com.bezeq.locator.bl;

public class Equipment {

	private int id;
	private String cnum;
	private String name;
	private String type;
	private double latitude;
    private double longitude;
    private double altitude;
	
    public Equipment(){}
    
    public Equipment(int id, String cnum, String name, String type, double latitude, double longitude, double altitude){
    	this.id = id;
    	this.cnum = cnum;
    	this.name = name;
    	this.type = type;
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
	public String getCnum() {
		return cnum;
	}
	public void setCnum(String cnum) {
		this.cnum = cnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
