package simulator.factories;

import org.json.JSONObject;

import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.Simulator;
import simulator.view.Messages;

public class SimulatorBuilder extends Builder<Simulator> {
	
	private Factory<Animal> _animal_factory;
	private Factory<Region> _region_factory;

	public SimulatorBuilder(Factory<Animal> animal_factory, Factory<Region> region_factory) {
		super(Messages.SIMULATOR_TAG,Messages.DESCRIPTION);
		this._animal_factory = animal_factory;
		this._region_factory = region_factory;
	}

	@Override
	public Simulator create_instance(JSONObject data) {
		int cols, rows, width, height;
		
		cols = data.getInt("cols");
		rows = data.getInt("rows");
		width = data.getInt("width");
		height = data.getInt("height");
		
		return new Simulator(cols, rows, width, height, _animal_factory, _region_factory);
	}

}
