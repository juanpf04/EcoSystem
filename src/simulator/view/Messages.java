package simulator.view;

public class Messages {
	
	public static final String MENSAJE_PERSONALIZADO = "personalizar mensaje";
	
	
	// JSONObjects key names
	
	public static final String POSITION_KEY = "pos";
	public static final String GENETIC_CODE_KEY = "gcode";
	public static final String DIET_KEY = "diet";
	public static final String ANIMAL_STATE_KEY = "state";
	public static final String COLUMN_KEY = "col";
	public static final String ROW_KEY = "row";
	public static final String DATA_KEY = "data";
	public static final String TYPE_KEY = "type";
	public static final String ANIMALS_KEY = "animals";
	public static final String REGIONS_KEY = "regiones";
	public static final String TIME_KEY = "time";
	public static final String REGIONS_STATE_KEY = "state";
	public static final String IN_KEY = "in";
	public static final String OUT_KEY = "out";


	// Exceptions
	
	public static final String INVALID_TYPE = "Invalid type";
	public static final String INVALID_DESC = "Invalid desc";
	public static final String INVALID_INFO = "’info’ cannot be null";

	private static final String UNRECOGNIZED_INFO = "Unrecognized ‘info’: %s";	
	
	public static final String unrecognized_info(String info) {
		return Messages.UNRECOGNIZED_INFO.formatted(info);
	}
	
	
}
