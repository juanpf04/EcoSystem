package simulator.misc;

import simulator.model.Animal;

public class Messages {

	// JSONObjects key names

	public static final String AMOUNT_KEY = "amount";
	public static final String ANIMALS_KEY = "animals";
	public static final String ANIMAL_STATE_KEY = "state";
	public static final String COLUMN_KEY = "col";
	public static final String COLUMNS_KEY = "cols";
	public static final String ROWS_KEY = "rows";
	public static final String WIDTH_KEY = "width";
	public static final String HEIGHT_KEY = "height";
	public static final String DATA_KEY = "data";
	public static final String DESCRIPTION_KEY = "desc";
	public static final String DIET_KEY = "diet";
	public static final String GENETIC_CODE_KEY = "gcode";
	public static final String POSITION_KEY = "pos";
	public static final String X_RANGE_KEY = "x_range";
	public static final String Y_RANGE_KEY = "y_range";
	public static final String ROW_KEY = "row";
	public static final String TYPE_KEY = "type";
	public static final String REGIONS_KEY = "regions";
	public static final String TIME_KEY = "time";
	public static final String REGIONS_STATE_KEY = "state";
	public static final String IN_KEY = "in";
	public static final String OUT_KEY = "out";
	public static final String SPEC_KEY = "spec";
	public static final String FOOD_KEY = "food";
	public static final String FACTOR_KEY = "factor";
	public static final String MATE_STRATEGY_KEY = "mate_strategy";
	public static final String HUNT_STRATEGY_KEY = "hunt_strategy";
	public static final String DANGER_STRATEGY_KEY = "danger_strategy";

	// Exceptions

	public static final String INVALID_GENETIC_CODE = "Invalid genetic code.";
	public static final String INVALID_SIGHT_RANGE = "Sight range must be a positive number.";
	public static final String INVALID_INIT_SPEED = "Initial speed must be a positive number.";
	public static final String INVALID_AMOUNT = "The amount of animals must be a positive number.";
	public static final String INVALID_COLS = "columns must be a positive number.";
	public static final String INVALID_COL = "column must be a between 0(inclusive) and the number of columns(exclusive).";
	public static final String INVALID_ROWS = "Rows must be a positive number.";
	public static final String INVALID_ROW = "Row must be a between 0(inclusive) and the number of rows(exclusive).";
	public static final String INVALID_WIDTH = "Width must be a positive number.";
	public static final String INVALID_HEIGHT = "Height must be a positive number.";
	public static final String INVALID_INIT_FOOD = "Initial food must be a positive number.";
	public static final String INVALID_FACTOR = "Factor cannot be a negative number.";
	public static final String INVALID_SIMULATOR = "Simulator cannot be null.";
	public static final String INVALID_STRATEGY = "Strategies cannot be null.";
	public static final String INVALID_FACTORY = "Factories cannot be null.";
	public static final String INVALID_ANIMAL = "Animal cannot be null.";
	public static final String INVALID_DIET = "Diet cannot be null.";
	public static final String INVALID_LIST = "List cannot be null.";
	public static final String INVALID_REGION_MANAGER = "Region manager cannot be null.";
	public static final String INVALID_REGION = "Region cannot be null.";
	public static final String INVALID_PREDICATE = "Predicate cannot be null.";
	public static final String INVALID_JSON = "Json object must contain any information.";
	public static final String EMPTY_DATA = "Data cannot contain any information.";
	public static final String INVALID_TYPE = "Invalid type.";
	public static final String INVALID_DESC = "Invalid desc.";
	public static final String INVALID_INFO = "'info' cannot be null.";
	public static final String INVALID_BUILDERS = "The builders list must have 1 builder at least.";
	public static final String UNRECOGNIZED_INFO = "Unrecognized 'info': %s.";
	public static final String INVALID_TIME = "Invalid value for time: %s.";
	public static final String INVALID_DELTA_TIME = "Invalid value for delta time: %s.";
	public static final String DELTA_TIME_NUMBER_ERROR = "Delta Time must be a number";
	public static final String SOMETHING_WENT_WRONG = "Something went wrong ...";
	public static final String GUI_ERROR = "GUI mode is not ready yet ...";
	public static final String IN_FILE_ERROR = "In batch mode an input configuration file is required.";
	public static final String ILLEGAL_WIDTH_OPERATION = "The width has to be divisible by the number of columns.";
	public static final String ILLEGAL_HEIGHT_OPERATION = "The height has to be divisible by the number of rows.";
	public static final String DELTA_TIME_ERROR = "Delta time must be a positive number.";
	public static final String TIME_ERROR = "Time must be a positive number.";
	public static final String ILLEGAL_STATE = "%s cannot be in %s state.";
	public static final String UNKNOWN_MODE = "Unknown execution mode %s.";

	public static final String illegal_state(String name, Animal.State state) {
		return Messages.ILLEGAL_STATE.formatted(name, state.toString().toLowerCase());
	}

	public static final String unrecognized_info(String info) {
		return Messages.UNRECOGNIZED_INFO.formatted(info);
	}

	public static final String invalid_delta_time(String delta_time) {
		return Messages.INVALID_DELTA_TIME.formatted(delta_time);
	}

	public static final String invalid_time(String time) {
		return Messages.INVALID_TIME.formatted(time);
	}

	public static final String unknown_mode(String mode_tag) {
		return Messages.UNKNOWN_MODE.formatted(mode_tag);
	}

	// Animal genetic Codes

	public static final String SHEEP_GENETIC_CODE = "Sheep";
	public static final String WOLF_GENETIC_CODE = "Wolf";

	// Region names

	public static final String DEFAULT_REGION_NAME = "Default";
	public static final String DYNAMIC_REGION_NAME = "Dynamic";

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

	public static final String COMMAND_MODE_NAME = "mode";
	public static final String COMMAND_MODE_SHORTCUT = "m";
	public static final String COMMAND_MODE_DESCRIPTION = "Execution Mode. Possible values: %s \nDefault value: 'gui'.";

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

	public static final String command_mode_description(String modes) {
		return Messages.COMMAND_MODE_DESCRIPTION.formatted(modes);
	}

	// Builder tags

	public static final String DEFAULT_REGION_TAG = "default";
	public static final String DYNAMIC_REGION_TAG = "dynamic";
	public static final String SELECT_CLOSEST_TAG = "closest";
	public static final String SELECT_FIRST_TAG = "first";
	public static final String SELECT_YOUNGEST_TAG = "youngest";
	public static final String SHEEP_TAG = "sheep";
	public static final String SIMULATOR_TAG = "simulator";
	public static final String WOLF_TAG = "wolf";

	// Builder descriptions

	public static final String WOLF_BUILDER_DESCRIPTION = "Wolf builder";
	public static final String SHEEP_BUILDER_DESCRIPTION = "Sheep builder";
	public static final String DEFAULT_REGION_BUILDER_DESCRIPTION = "Infinite food supply";
	public static final String DYNAMIC_REGION_BUILDER_DESCRIPTION = "Dynamic food supply";
	public static final String SELECT_FIRST_BUILDER_DESCRIPTION = "Select first builder";
	public static final String SELECT_CLOSEST_BUILDER_DESCRIPTION = "Select closest builder";
	public static final String SELECT_YOUNGEST_BUILDER_DESCRIPTION = "Select youngest builder";
	public static final String SIMULATOR_BUILDER_DESCRIPTION = "Simulator builder";

	// Builder data descriptions

	public static final String FACTOR_DESCRIPTION = "food increase factor (optional, default 2.0)";
	public static final String FOOD_DESCRIPTION = "initial amount of food (optional, default 100.0)";
	public static final String _DESCRIPTION = "";

	// Execute mode tags

	public static final String BATCH_TAG = "batch";
	public static final String GUI_TAG = "gui";

	// Execute mode descriptions

	public static final String BATCH_DESCRIPTION = "Batch mode";
	public static final String GUI_DESCRIPTION = "Graphical User Interface mode";

	// View

	public static final String TITLE = "[ECOSYSTEM]";
	public static final String MAP_WINDOW_TITLE = "[MAP VIEWER]";
	public static final String GUI_TITLE = "[ECOSYSTEM SIMULATOR]";
	public static final String SPECIES_TABLE_TITLE = "Species";
	public static final String REGIONS_TABLE_TITLE = "Regions";
	public static final String CHANGE_REGIONS_TITLE = "Change Regions";

	public static final String HELP_HELP_VIEWER = "h: toggle help";
	public static final String STATE_HELP_VIEWER = "s: show animals of a specific state";

	public static final String ROW = "Row";
	public static final String COL = "Col";
	public static final String DESC = "Desc.";

	public static final String KEY = "Key";
	public static final String VALUE = "Value";
	public static final String DESCRIPTION = "Description";

	public static final String REGION_TYPE = "Region type: ";
	public static final String ROW_FROM_TO = "Row from/to: ";
	public static final String COL_FROM_TO = "Column from/to: ";

	public static final String STEPS = "Steps: ";
	public static final String DELAY = "Delay: ";
	public static final String MODE = "Mode: ";
	public static final String TIME = "Time: ";
	public static final String TOTAL_ANIMALS = "Total Animals: ";
	public static final String DIMENSION = "Dimension: ";
	public static final String DELTA_TIME = "Delta-Time: ";

	public static final String CHANGE_REGIONS_DESCRIPTION = "<html><p><span style =\"font-weight:normal\">Select a region type, the rows/cols interval, and provide values for the parameters in the <b>Value column</b> (default values are used for parameters with no value).</span></p></html>";

	// Buttons

	public static final String OPEN_BUTTON_DESCRIPTION = "Load an input file into the simulator";
	public static final String VIEWER_BUTTON_DESCRIPTION = "Map Viewer";
	public static final String REGIONS_BUTTON_DESCRIPTION = "Change Regions";
	public static final String RUN_BUTTON_DESCRIPTION = "Run the simulator";
	public static final String STOP_BUTTON_DESCRIPTION = "Stop the simulator";
	public static final String EXIT_BUTTON_DESCRIPTION = "Exit";
	public static final String STEPS_SPINNER_DESCRIPTION = "Simulation steps to run: 1-10000";
	public static final String DELAY_SPINNER_DESCRIPTION = "Delay (miliseconds) between consecutive simulation steps";
	public static final String DT_TEXT_FIELD_DESCRIPTION = "Real time (seconds) corresponding to a step";

	public static final String OK_BUTTON = "OK";
	public static final String CANCEL_BUTTON = "Cancel";

	// Icons

	public static final String ICONS_DIRECTORY = "resources/icons/";

	// Other

	public static final String EXAMPLES_DIRECTORY = "resources/examples/";

}
