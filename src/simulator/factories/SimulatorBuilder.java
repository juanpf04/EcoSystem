package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.Simulator;
import simulator.view.Messages;

public class SimulatorBuilder extends Builder<Simulator> {
	
	private Factory<Animal> _animals_factory;
	private Factory<Region> _regions_factory;

	public SimulatorBuilder(Factory<Animal> animals_factory, Factory<Region> regions_factory) {
		super(Messages.SIMULATOR_TAG,Messages.DESCRIPTION);
		
		if(animals_factory == null)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		if(regions_factory == null)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		
		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
	}

	@Override
	public Simulator create_instance(JSONObject data) {
		try {
			
			int cols = data.getInt("cols");
			int rows = data.getInt("rows");
			int width = data.getInt("width");
			int height = data.getInt("height");
			
			return new Simulator(cols, rows, width, height, _animals_factory, _regions_factory);
			
		} catch (JSONException e) {
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO); // data tiene que contener  todas esas clves si o si
		}
	}
}
