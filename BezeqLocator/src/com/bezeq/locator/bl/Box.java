package com.bezeq.locator.bl;

public class Box extends Equipment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ufid;
	private String framework;
	private String location;
	private String cbntType;
	private String closer;
	
	public Box(){super(); this.setType("BOX");}
	
	public Box(int id, String ufid, int area, String framework, String location, String cbntType, String closer, String settlement, String street, String bnum, String bsign, double latitude, double longitude, double altitude){
		super(id, area, settlement, street, bnum, bsign, latitude, longitude, altitude);
		this.setUfid(ufid);
		this.setFramework(framework);
		this.setLocation(location);
		this.setCbntType(cbntType);
		this.setCloser(closer);
		this.setType("BOX");
	}

	public String getUfid() {
		return ufid;
	}

	public void setUfid(String ufid) {
		this.ufid = ufid;
	}

	public String getFramework() {
		return framework;
	}

	public void setFramework(String framework) {
		this.framework = framework;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCbntType() {
		return cbntType;
	}

	public void setCbntType(String cbntType) {
		this.cbntType = cbntType;
	}

	public String getCloser() {
		return closer;
	}

	public void setCloser(String closer) {
		this.closer = closer;
	}
	
}
