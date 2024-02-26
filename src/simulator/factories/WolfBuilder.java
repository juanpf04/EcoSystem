package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;
import simulator.view.Messages;

public class WolfBuilder extends Builder<Animal> {

	private Factory<SelectionStrategy> _selection_strategy_factory;

	public WolfBuilder(Factory<SelectionStrategy> selection_strategy_factory) {
		super(Messages.WOLF_TAG, Messages.DESCRIPTION);
		if (selection_strategy_factory == null)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);

		this._selection_strategy_factory = selection_strategy_factory;
	}

	@Override
	protected Wolf create_instance(JSONObject data) {
		SelectionStrategy mate_strategy = new SelectFirst(), hunt_strategy = new SelectFirst();
		Vector2D position = null;

		if (data.has("mate_strategy"))
			mate_strategy = this._selection_strategy_factory.create_instance(data.getJSONObject("mate_strategy"));

		if (data.has("hunt_strategy"))
			hunt_strategy = this._selection_strategy_factory.create_instance(data.getJSONObject("hunt_strategy"));

		if (data.has("pos")) {
			JSONObject jo = data.getJSONObject("pos");
			JSONArray jax = jo.getJSONArray("x_range"), jay = jo.getJSONArray("y_range");

			double x = Utils._rand.nextDouble(jax.getDouble(0), jax.getDouble(1)),
					y = Utils._rand.nextDouble(jay.getDouble(0), jay.getDouble(1));

			position = new Vector2D(x, y);
		}

		return new Wolf(mate_strategy, hunt_strategy, position);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		SelectFirstBuilder b = new SelectFirstBuilder();
		
		o.put("mate_strategy", b.get_info());
		o.put("hunt_strategy", b.get_info());
		
		JSONObject jo = new JSONObject();
		
		JSONArray ja = new JSONArray();
		ja.put(100.0);
		ja.put(200.0);
		
		jo.put("x_range", ja);
		jo.put("y_range", ja);		
		
		o.put("pos", jo);
	}
}
