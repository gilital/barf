package com.bezeq.locator.bl;

/**
 * Class for representing the MSAG equipment type For every instance of this
 * class the TYPE field will be "MSAG" and it not going to be changed anywhere
 * TYPE field is not saved at DB as column!!!!
 * 
 * @author Silver
 * 
 */
public class Msag extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Msag() {
		super();
		this.setType(Constants.EquipmentTypes.MSAG);
	}

	public Msag(int id, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lat, double lon) {
		super(id, objectID, Constants.EquipmentTypes.MSAG, merkaz, featureNum,
				cityName, streetName, buildingNum, buildingLetter, lat, lon,
				0.0);
	}

}
