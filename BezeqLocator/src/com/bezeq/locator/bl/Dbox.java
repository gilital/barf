package com.bezeq.locator.bl;

/**
 * Class for representing the DBOX equipment type For every instance of this
 * class the TYPE field will be "DBOX" and it not going to be changed anywhere
 * TYPE field is not saved at DB as column!!!!
 * 
 * @author Silver
 * 
 */
public class Dbox extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Dbox() {
		super();
		this.setType(Constants.EquipmentTypes.DBOX);
	}

	public Dbox(int id, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lon, double lat) {
		super(id, objectID, Constants.EquipmentTypes.DBOX, merkaz, featureNum,
				cityName, streetName, buildingNum, buildingLetter, lon, lat,
				2.0);
	}

}
