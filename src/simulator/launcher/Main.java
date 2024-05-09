package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import simulator.misc.Messages;
import simulator.misc.Utils;
import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.SelectionStrategy;
import simulator.model.Simulator;
import simulator.view.MainWindow;
import simulator.control.Controller;
import simulator.factories.*;

public class Main {

	private enum ExecMode {
		BATCH(Messages.BATCH_TAG, Messages.BATCH_DESCRIPTION), GUI(Messages.GUI_TAG, Messages.GUI_DESCRIPTION);

		private String _tag;
		private String _desc;

		private ExecMode(String modeTag, String modeDesc) {
			_tag = modeTag;
			_desc = modeDesc;
		}

		public String get_tag() {
			return _tag;
		}

		public String get_desc() {
			return _desc;
		}

		public static String modes() {
			StringBuilder modes = new StringBuilder();

			for (ExecMode mode : ExecMode.values())
				modes.append("'" + mode.get_tag() + "' (" + mode.get_desc() + "), ");

			modes.deleteCharAt(modes.length() - 1);
			modes.setCharAt(modes.length() - 1, '.');

			return modes.toString();
		}

	}

	// default values for some parameters
	//
	private final static Double _default_time = 10.0; // in seconds
	private final static Double _default_delta_time = 0.03; // in seconds
	private final static ExecMode _default_mode = ExecMode.GUI; // GUI mode
	private final static int _default_cols = 20;
	private final static int _default_rows = 15;
	private final static int _default_width = 800;
	private final static int _default_height = 600;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = null;
	public static Double _delta_time = null;
	private static String _in_file = null;
	private static String _out_file = null;
	private static ExecMode _mode = null;
	private static boolean _simple_viewer = false;

	// factories
	//
	public static Factory<SelectionStrategy> _strategies_factory;
	public static Factory<Animal> _animals_factory;
	public static Factory<Region> _regions_factory;

	private static void parse_args(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = build_options();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parse_delta_time_option(line);
			parse_help_option(line, cmdLineOptions);
			parse_mode_option(line);
			parse_in_file_option(line);
			parse_out_file_option(line);
			parse_simple_viewer_option(line);
			parse_time_option(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	} 

	private static Options build_options() {
		Options cmdLineOptions = new Options();

		// delta time
		cmdLineOptions.addOption(
				Option.builder(Messages.COMMAND_DELTA_TIME_SHORTCUT).longOpt(Messages.COMMAND_DELTA_TIME_NAME).hasArg()
						.desc(Messages.command_delta_time_description(_default_delta_time)).build());

		// help
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_HELP_SHORTCUT).longOpt(Messages.COMMAND_HELP_NAME)
				.desc(Messages.COMMAND_HELP_DESCRIPTION).build());

		// input file
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_INPUT_SHORTCUT).longOpt(Messages.COMMAND_INPUT_NAME)
				.hasArg().desc(Messages.COMMAND_INPUT_DESCRIPTION).build());

		// mode
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_MODE_SHORTCUT).longOpt(Messages.COMMAND_MODE_NAME)
				.hasArg().desc(Messages.command_mode_description(ExecMode.modes())).build());

		// output file
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_OUTPUT_SHORTCUT).longOpt(Messages.COMMAND_OUTPUT_NAME)
				.hasArg().desc(Messages.COMMAND_OUTPUT_DESCRIPTION).build());

		// simple viewer
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_SIMPLE_VIEWER_SHORTCUT)
				.longOpt(Messages.COMMAND_SIMPLE_VIEWER_NAME).desc(Messages.COMMAND_SIMPLE_VIEWER_DESCRIPTION).build());

		// steps
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_TIME_SHORTCUT).longOpt(Messages.COMMAND_TIME_NAME)
				.hasArg().desc(Messages.command_time_description(_default_time)).build());

		return cmdLineOptions;
	}

	private static void parse_delta_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue(Messages.COMMAND_DELTA_TIME_SHORTCUT, _default_delta_time.toString());
		try {
			_delta_time = Double.parseDouble(t);
			assert (_delta_time >= 0);
		} catch (Exception e) {
			throw new ParseException(Messages.invalid_delta_time(t));
		}
	}

	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption(Messages.COMMAND_HELP_SHORTCUT)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parse_in_file_option(CommandLine line) throws ParseException {
		_in_file = line.getOptionValue(Messages.COMMAND_INPUT_SHORTCUT);
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException(Messages.IN_FILE_ERROR);
		}
	}

	private static void parse_mode_option(CommandLine line) throws ParseException {
		String mode_tag = line.getOptionValue(Messages.COMMAND_MODE_SHORTCUT, _default_mode.get_tag());

		for (ExecMode mode : ExecMode.values())
			if (mode_tag.equals(mode.get_tag()))
				_mode = mode;

		if (_mode == null)
			throw new ParseException(Messages.unknown_mode(mode_tag));
	}

	private static void parse_out_file_option(CommandLine line) throws ParseException {
		_out_file = line.getOptionValue(Messages.COMMAND_OUTPUT_SHORTCUT);
	}

	private static void parse_simple_viewer_option(CommandLine line) throws ParseException {
		_simple_viewer = line.hasOption(Messages.COMMAND_SIMPLE_VIEWER_SHORTCUT);
	}

	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue(Messages.COMMAND_TIME_SHORTCUT, _default_time.toString());
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException(Messages.invalid_time(t));
		}
	}

	private static void init_factories() {

		// initialize the strategies factory
		List<Builder<SelectionStrategy>> selection_strategy_builders = new LinkedList<>();
		selection_strategy_builders.add(new SelectFirstBuilder());
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		_strategies_factory = new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);

		// initialize the animals factory
		List<Builder<Animal>> animal_builders = new LinkedList<>();
		animal_builders.add(new SheepBuilder(_strategies_factory));
		animal_builders.add(new WolfBuilder(_strategies_factory));
		_animals_factory = new BuilderBasedFactory<Animal>(animal_builders);

		// initialize the regions factory
		List<Builder<Region>> region_builders = new LinkedList<>();
		region_builders.add(new DefaultRegionBuilder());
		region_builders.add(new DynamicSupplyRegionBuilder());
		_regions_factory = new BuilderBasedFactory<Region>(region_builders);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static void start_batch_mode() throws Exception {
		InputStream in = new FileInputStream(new File(_in_file));
		JSONObject data = load_JSON_file(in);

		OutputStream out = _out_file != null ? new FileOutputStream(_out_file) : System.out;

		SimulatorBuilder sb = new SimulatorBuilder(_animals_factory, _regions_factory);
		Simulator simulator = sb.create_instance(data);

		Controller controller = new Controller(simulator);
		controller.load_data(data);
		controller.run(_time, _delta_time, _simple_viewer, out);

		out.close();
	}

	private static void start_GUI_mode() throws Exception {
		Simulator simulator;
		Controller controller;

		if (_in_file != null) {
			InputStream in = new FileInputStream(new File(_in_file));
			JSONObject data = load_JSON_file(in);

			SimulatorBuilder sb = new SimulatorBuilder(_animals_factory, _regions_factory);
			simulator = sb.create_instance(data);

			controller = new Controller(simulator);
			controller.load_data(data);
		} else {
			simulator = new Simulator(_default_cols, _default_rows, _default_width, _default_height, _animals_factory,
					_regions_factory);
			controller = new Controller(simulator);
		}

		SwingUtilities.invokeAndWait(() -> new MainWindow(controller));
	}

	private static void start(String[] args) throws Exception {
		init_factories();
		parse_args(args);
		switch (_mode) {
		case BATCH:
			start_batch_mode();
			break;
		case GUI:
			start_GUI_mode();
			break;
		}
	}

	public static void main(String[] args) {
		Utils._rand.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println(Messages.SOMETHING_WENT_WRONG);
			System.err.println();
			e.printStackTrace();
		}
	}

}
