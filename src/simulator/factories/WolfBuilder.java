package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;
import simulator.view.Messages;

public class WolfBuilder extends Builder<Wolf> {
	
	private Factory<SelectionStrategy> _factory;
	
	public WolfBuilder(Factory<SelectionStrategy> factory) {
		super(Messages.WOLF_TAG, Messages.DESCRIPTION);
		this._factory = factory;
	}

	@Override
	protected Wolf create_instance(JSONObject data) {		
		SelectionStrategy mate_strategy = new SelectFirst();
		SelectionStrategy hunt_strategy = new SelectFirst();
		Vector2D position = null;
		
		if(data.has("mate_strategy"))
			mate_strategy = this._factory.create_instance(data.getJSONObject("mate_strategy"));
		
		if(data.has("hunt_strategy"))
			hunt_strategy = this._factory.create_instance(data.getJSONObject("hunt_strategy"));
		
		if(data.has("pos")) { 
			JSONObject jo = data.getJSONObject("position");
			JSONArray jax = jo.getJSONArray("x_range");
			JSONArray jay = jo.getJSONArray("y_range");
			
			double x = Utils._rand.nextDouble(jax.getDouble(0), jax.getDouble(1)),
					y = Utils._rand.nextDouble(jay.getDouble(0), jay.getDouble(1));
			
			position = new Vector2D(x, y);
		}
		
		return new Wolf(mate_strategy, hunt_strategy, position);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		// no se si es hacer esto o que 
		
	}
}
