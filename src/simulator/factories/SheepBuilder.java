package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;
import simulator.view.Messages;

public class SheepBuilder extends Builder<Animal> {

	private Factory<SelectionStrategy> _strategies_factory;

	public SheepBuilder(Factory<SelectionStrategy> strategies_factory) {
		super(Messages.SHEEP_TAG, Messages.SHEEP_BUILDER_DESCRIPTION);
		if (strategies_factory == null)
			throw new IllegalArgumentException(Messages.INVALID_FACTORY);

		this._strategies_factory = strategies_factory;
	}

	@Override
	protected Sheep create_instance(JSONObject data) {
		if (data == null)
			throw new IllegalArgumentException(Messages.INVALID_JSON);

		SelectionStrategy mate_strategy = new SelectFirst(), danger_strategy = new SelectFirst();
		Vector2D position = null;

		if (data.has(Messages.MATE_STRATEGY_KEY))
			mate_strategy = this._strategies_factory.create_instance(data.getJSONObject(Messages.MATE_STRATEGY_KEY));

		if (data.has(Messages.DANGER_STRATEGY_KEY))
			danger_strategy = this._strategies_factory
					.create_instance(data.getJSONObject(Messages.DANGER_STRATEGY_KEY));

		if (data.has(Messages.POSITION_KEY)) {
			JSONObject jo = data.getJSONObject(Messages.POSITION_KEY);
			JSONArray jax = jo.getJSONArray(Messages.X_RANGE_KEY), jay = jo.getJSONArray(Messages.Y_RANGE_KEY);

			double x = Utils._rand.nextDouble(jax.getDouble(0), jax.getDouble(1)),
					y = Utils._rand.nextDouble(jay.getDouble(0), jay.getDouble(1));

			position = new Vector2D(x, y);
		}

		return new Sheep(mate_strategy, danger_strategy, position);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		SelectFirstBuilder b = new SelectFirstBuilder();
		JSONObject data = new JSONObject();

		data.put(Messages.MATE_STRATEGY_KEY, b.get_info());
		data.put(Messages.DANGER_STRATEGY_KEY, b.get_info());

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
