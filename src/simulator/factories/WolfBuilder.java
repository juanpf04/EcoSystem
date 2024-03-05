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
		super(Messages.WOLF_TAG, Messages.WOLF_BUILDER_DESCRIPTION);

		if (selection_strategy_factory == null)
			throw new IllegalArgumentException(Messages.INVALID_FACTORY);

		this._selection_strategy_factory = selection_strategy_factory;
	}

	@Override
	protected Wolf create_instance(JSONObject data) {
		if (data == null)
			throw new IllegalArgumentException(Messages.INVALID_JSON);

		SelectionStrategy mate_strategy = new SelectFirst(), hunt_strategy = new SelectFirst();
		Vector2D position = null;

		if (data.has(Messages.MATE_STRATEGY_KEY))
			mate_strategy = this._selection_strategy_factory
					.create_instance(data.getJSONObject(Messages.MATE_STRATEGY_KEY));

		if (data.has(Messages.HUNT_STRATEGY_KEY))
			hunt_strategy = this._selection_strategy_factory
					.create_instance(data.getJSONObject(Messages.HUNT_STRATEGY_KEY));

		if (data.has(Messages.POSITION_KEY)) {
			JSONObject jo = data.getJSONObject(Messages.POSITION_KEY);
			JSONArray jax = jo.getJSONArray(Messages.X_RANGE_KEY), jay = jo.getJSONArray(Messages.Y_RANGE_KEY);

			double x = Utils._rand.nextDouble(jax.getDouble(0), jax.getDouble(1)),
					y = Utils._rand.nextDouble(jay.getDouble(0), jay.getDouble(1));

			position = new Vector2D(x, y);
		}

		return new Wolf(mate_strategy, hunt_strategy, position);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		SelectFirstBuilder b = new SelectFirstBuilder();
		JSONObject data = new JSONObject();

		data.put(Messages.MATE_STRATEGY_KEY, b.get_info());
		data.put(Messages.HUNT_STRATEGY_KEY, b.get_info());

		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		ja.put(100.0);
		ja.put(200.0);

		jo.put(Messages.X_RANGE_KEY, ja);
		jo.put(Messages.Y_RANGE_KEY, ja);

		data.put(Messages.POSITION_KEY, jo);

		o = new JSONObject(data.toString());
	}
}
