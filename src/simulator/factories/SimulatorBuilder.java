package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Messages;
import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.Simulator;

public class SimulatorBuilder extends Builder<Simulator> {

	private Factory<Animal> _animals_factory;
	private Factory<Region> _regions_factory;

	public SimulatorBuilder(Factory<Animal> animals_factory, Factory<Region> regions_factory) {
		super(Messages.SIMULATOR_TAG, Messages.SIMULATOR_BUILDER_DESCRIPTION);

		if (animals_factory == null || regions_factory == null)
			throw new IllegalArgumentException(Messages.INVALID_FACTORY);

		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
	}

	@Override
	public Simulator create_instance(JSONObject data) {
		if (data == null || data.isEmpty())
			throw new IllegalArgumentException(Messages.INVALID_JSON);

		int cols = data.getInt(Messages.COLUMNS_KEY);
		int rows = data.getInt(Messages.ROWS_KEY);
		int width = data.getInt(Messages.WIDTH_KEY);
		int height = data.getInt(Messages.HEIGHT_KEY);

		return new Simulator(cols, rows, width, height, _animals_factory, _regions_factory);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		// TODO
	}
}
