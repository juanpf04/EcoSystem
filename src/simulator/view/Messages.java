package simulator.view;

import simulator.model.State;

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
	public static final String SOMETHING_WENT_WRONG = "Something went wrong ...";
	public static final String GUI_ERROR = "GUI mode is not ready yet ...";
	public static final String IN_FILE_ERROR = "In batch mode an input configuration file is required.";
	public static final String ILLEGAL_WIDTH_OPERATION = "The width has to be divisible by the number of columns.";
	public static final String ILLEGAL_HEIGHT_OPERATION = "The height has to be divisible by the number of rows.";
	public static final String DELTA_TIME_ERROR = "Delta time must be a positive number.";
	public static final String TIME_ERROR = "Time must be a positive number.";
	public static final String ILLEGAL_STATE = "%s cannot be in %s state.";

	public static final String illegal_state(String name, State state) {
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

	// Animal genetic Codes

	public static final String SHEEP_GENETIC_CODE = "Sheep";
	public static final String WOLF_GENETIC_CODE = "Wolf";

	// Region names

	public static final String DEFAULT_REGION_NAME = "Default region";
	public static final String DYNAMIC_REGION_NAME = "Dynamic region";

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
	public static final String DEFAULT_REGION_BUILDER_DESCRIPTION = "Default region builder";
	public static final String DYNAMIC_REGION_BUILDER_DESCRIPTION = "Dynamic region builder";
	public static final String SELECT_FIRST_BUILDER_DESCRIPTION = "Select first builder";
	public static final String SELECT_CLOSEST_BUILDER_DESCRIPTION = "Select closest builder";
	public static final String SELECT_YOUNGEST_BUILDER_DESCRIPTION = "Select youngest builder";
	public static final String SIMULATOR_BUILDER_DESCRIPTION = "Simulator builder";

	// Execute mode tags

	public static final String BATCH_TAG = "batch";
	public static final String GUI_TAG = "gui";

	// Execute mode descriptions

	public static final String BATCH_DESCRIPTION = "Batch mode";
	public static final String GUI_DESCRIPTION = "Graphical User Interface mode";

	// View

	public static final String TITLE = "[ECOSYSTEM]";
}
