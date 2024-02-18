package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;
import simulator.view.Messages;

public class SheepBuilder extends Builder<Sheep> {

	private Factory<SelectionStrategy> _selection_strategy_factory;

	public SheepBuilder(Factory<SelectionStrategy> selection_strategy_factory) {
		super(Messages.SHEEP_TAG, Messages.DESCRIPTION);
		if (selection_strategy_factory == null)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);

		this._selection_strategy_factory = selection_strategy_factory;
	}

	@Override
	protected Sheep create_instance(JSONObject data) {
		SelectionStrategy mate_strategy = new SelectFirst(), danger_strategy = new SelectFirst();
		Vector2D position = null;

		if (data.has("mate_strategy"))
			mate_strategy = this._selection_strategy_factory.create_instance(data.getJSONObject("mate_strategy"));

		if (data.has("danger_strategy"))
			danger_strategy = this._selection_strategy_factory.create_instance(data.getJSONObject("danger_strategy"));

		if (data.has("pos")) {
			JSONObject jo = data.getJSONObject("position");
			JSONArray jax = jo.getJSONArray("x_range"), jay = jo.getJSONArray("y_range");

			double x = Utils._rand.nextDouble(jax.getDouble(0), jax.getDouble(1)),
					y = Utils._rand.nextDouble(jay.getDouble(0), jay.getDouble(1));

			position = new Vector2D(x, y);
		}

		return new Sheep(mate_strategy, danger_strategy, position);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		// no se si es hacer esto o que

	}
}
