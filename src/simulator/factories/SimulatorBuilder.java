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
		super(Messages.MENSAJE_PERSONALIZADO,Messages.MENSAJE_PERSONALIZADO);
		this._animal_factory = animal_factory;
		this._region_factory = region_factory;
	}

	@Override
	public Simulator create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return new Simulator(0, 0, 0, 0, _animal_factory, _region_factory);
	}

}
