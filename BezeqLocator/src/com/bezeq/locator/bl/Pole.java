package com.bezeq.locator.bl;

/**
 * Class for representing the POLE equipment type For every instance of this
 * class the TYPE field will be "POLE" and it not going to be changed anywhere
 * TYPE field is not saved at DB as column!!!!
 * 
 * @author Silver
 * 
 */
public class Pole extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Pole() {
		super();
		this.setType(Constants.EquipmentTypes.POLE);
	}

	public Pole(int id, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lon, double lat) {
		super(id, objectID, Constants.EquipmentTypes.POLE, merkaz, featureNum,
				cityName, streetName, buildingNum, buildingLetter, lon, lat,
				3.0);
	}

}
