package com.bezeq.locator.bl;

/**
 * Class for representing the MSAG equipment type
 * For every instance of this class the TYPE field will be "MSAG" and it not going to be changed anywhere
 * TYPE field is not saved at DB as column!!!!
 * @author Silver
 *
 */
public class Msag extends Equipment{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String exchange_num;

	
    public Msag(){ super(); this.setType("MSAG");}
    
    public Msag(int id, int area, String exnum, String settlement, String street, String bnum, String bsign, double latitude, double longitude, double altitude){
    	super(id, area, settlement, street, bnum, bsign, latitude, longitude, altitude);
    	this.setExchange_num(exnum);
    	this.setType("MSAG");    	
    }
    

	public String getExchange_num() {
		return exchange_num;
	}

	public void setExchange_num(String exchange_num) {
		this.exchange_num = exchange_num;
	}
}
