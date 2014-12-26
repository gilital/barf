package com.bezeq.locator.bl;

/**
 * Class for representing the HOLE equipment type For every instance of this
 * class the TYPE field will be "HOLE" and it not going to be changed anywhere
 * TYPE field is not saved at DB as column!!!!
 * 
 * @author Silver
 * 
 */
public class Hole extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Hole() {
		super();
		this.setType(Constants.EquipmentTypes.HOLE);
	}

	public Hole(int id, int objectID, int merkaz, int featureNum,
			String cityName, String streetName, int buildingNum,
			String buildingLetter, double lon, double lat) {
		super(id, objectID, Constants.EquipmentTypes.HOLE, merkaz, featureNum,
				cityName, streetName, buildingNum, buildingLetter, lon, lat,
				-1.0);
	}

}
