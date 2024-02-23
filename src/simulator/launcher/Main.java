package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

import simulator.misc.Utils;
import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.SelectionStrategy;
import simulator.model.Simulator;
import simulator.view.Messages;
import simulator.control.Controller;
import simulator.factories.*;

public class Main {

	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

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
	}

	// default values for some parameters
	//
	private final static Double _default_time = 10.0; // in seconds
	private final static Double _default_delta_time = 0.03; // in seconds

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = null;
	private static Double _delta_time = null;
	private static String _in_file = null;
	private static String _out_file = null;
	private static ExecMode _mode = ExecMode.BATCH;
	private static boolean _sv = false;

	// factories
	//
	private static Factory<SelectionStrategy> _selection_strategy_factory;
	private static Factory<Animal> _animal_factory;
	private static Factory<Region> _region_factory;

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

		// output file
		cmdLineOptions.addOption(Option.builder(Messages.COMMAND_OUTPUT_SHORTCUT).longOpt(Messages.COMMAND_OUTPUT_NAME)
				.hasArg().desc(Messages.COMMAND_OUTPUT_DESCRIPTION).build());

		// simple viewer
		cmdLineOptions.addOption(
				Option.builder(Messages.COMMAND_SIMPLE_VIEWER_SHORTCUT).longOpt(Messages.COMMAND_SIMPLE_VIEWER_NAME)
						.hasArg().desc(Messages.COMMAND_SIMPLE_VIEWER_DESCRIPTION).build());

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
			throw new ParseException("Invalid value for delta time: " + t);
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
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}

	private static void parse_out_file_option(CommandLine line) throws ParseException {
		_out_file = line.getOptionValue(Messages.COMMAND_OUTPUT_SHORTCUT);
		if (_out_file == null) {
			throw new ParseException("HACER");
		}
	}

	private static void parse_simple_viewer_option(CommandLine line) throws ParseException {
		if (line.hasOption(Messages.COMMAND_SIMPLE_VIEWER_SHORTCUT))
			_sv = true;
	}

	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue(Messages.COMMAND_TIME_SHORTCUT, _default_time.toString());
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}

	private static void init_factories() {

		// initialize the strategies factory
		List<Builder<SelectionStrategy>> selection_strategy_builders = new LinkedList<>();
		selection_strategy_builders.add(new SelectFirstBuilder());
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		_selection_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);

		// initialize the animals factory
		List<Builder<Animal>> animal_builders = new LinkedList<>();
		animal_builders.add(new SheepBuilder(_selection_strategy_factory));
		animal_builders.add(new WolfBuilder(_selection_strategy_factory));
		_animal_factory = new BuilderBasedFactory<Animal>(animal_builders);

		// initialize the regions factory
		List<Builder<Region>> region_builders = new LinkedList<>();
		region_builders.add(new DefaultRegionBuilder());
		region_builders.add(new DynamicSupplyRegionBuilder());
		_region_factory = new BuilderBasedFactory<Region>(region_builders);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static void start_batch_mode() throws Exception {
		InputStream in = new FileInputStream(new File(_in_file));
		JSONObject data = load_JSON_file(in);
		
		// TODO
		OutputStream out = new FileOutputStream(_out_file); 
		data.get("blablbla");
		Simulator simulator = new Simulator(0, 0, 0, 0, _animal_factory, _region_factory);
		
		Controller controller = new Controller(simulator);
		controller.load_data(data);
		controller.run(_time, _delta_time, _sv, out);
		out.close();
	}

	private static void start_GUI_mode() throws Exception {
		throw new UnsupportedOperationException("GUI mode is not ready yet ...");
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
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
