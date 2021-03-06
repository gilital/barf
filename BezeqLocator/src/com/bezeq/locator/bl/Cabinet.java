package com.bezeq.locator.bl;

/**
 * Class for representing the CABINET equipment type For every instance of this
 * class the TYPE field will be "CABINET" and it not going to be changed anywhere
 * TYPE field is not saved at DB as column!!!!
 * 
 * @author Silver
 * 
 */
public class Cabinet extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Cabinet() {
		super();
		this.setType(Constants.EquipmentTypes.CABINET);
	}

	public Cabinet(int id, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lon, double lat) {
		super(id, objectID, Constants.EquipmentTypes.CABINET, merkaz, featureNum,
				cityName, streetName, buildingNum, buildingLetter, lon, lat,
				1.0);
	}

}
