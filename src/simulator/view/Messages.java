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
	public static final String REGIONS_KEY = "regions";
	public static final String TIME_KEY = "time";
	public static final String REGIONS_STATE_KEY = "state";
	public static final String IN_KEY = "in";
	public static final String OUT_KEY = "out";
	public static final String AMOUNT_KEY = "amount";
	public static final String SPEC_KEY = "spec";

	// Exceptions

	public static final String INVALID_TYPE = "Invalid type";
	public static final String INVALID_DESC = "Invalid desc";
	public static final String INVALID_INFO = "’info’ cannot be null";

	private static final String UNRECOGNIZED_INFO = "Unrecognized ‘info’: %s";

	public static final String unrecognized_info(String info) {
		return Messages.UNRECOGNIZED_INFO.formatted(info);
	}

	// Genetic code of Animals

	public static final String SHEEP_GENETIC_CODE = "Sheep";
	public static final String WOLF_GENETIC_CODE = "Wolf";

	// revisar nombre type / tag
	public static final String DEFAULT_REGION_TYPE = "dynamic";
	public static final String SHEEP_TAG = "Sheep";

	public static final String DESCRIPTION = "Descripcion personalizada";

	public static final String WOLF_TAG = "Wolf";

	// Commands

	public static final String COMMAND_DELTA_TIME_NAME = "delta-time";
	public static final String COMMAND_DELTA_TIME_SHORTCUT = "dt";
	public static final String COMMAND_DELTA_TIME_DESCRIPTION = "A double representing actual time, in seconds, per simulation step. Default value: %s.";

	public static final String COMMAND_HELP_NAME = "help";
	public static final String COMMAND_HELP_SHORTCUT = "h";
	public static final String COMMAND_HELP_DESCRIPTION = "Print this message.";

	public static final String COMMAND_INPUT_NAME = "input";
	public static final String COMMAND_INPUT_SHORTCUT = "i";
	public static final String COMMAND_INPUT_DESCRIPTION = "Initial configuration file.";

	public static final String COMMAND_OUTPUT_NAME = "output";
	public static final String COMMAND_OUTPUT_SHORTCUT = "o";
	public static final String COMMAND_OUTPUT_DESCRIPTION = "Output file, where output is written.";

	public static final String COMMAND_SIMPLE_VIEWER_NAME = "simple-viewer";
	public static final String COMMAND_SIMPLE_VIEWER_SHORTCUT = "sv";
	public static final String COMMAND_SIMPLE_VIEWER_DESCRIPTION = "Show the viewer window in console mode.";

	public static final String COMMAND_TIME_NAME = "time";
	public static final String COMMAND_TIME_SHORTCUT = "t";
	public static final String COMMAND_TIME_DESCRIPTION = "An real number representing the total simulation time in seconds. Default value: %s.";
	
	public static final String command_delta_time_description(Double delta_time) {
		return Messages.COMMAND_DELTA_TIME_DESCRIPTION.formatted(delta_time);
	}
	
	public static final String command_time_description(Double time) {
		return Messages.COMMAND_TIME_DESCRIPTION.formatted(time);
	}
	
}
