package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;
import simulator.view.Messages;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super(Messages.DYNAMIC_REGION_TAG, Messages.DESCRIPTION);
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {
		double food = data.optDouble(Messages.FOOD_KEY, 1000.0);
		double factor = data.optDouble(Messages.FACTOR_KEY, 2.0);

		return new DynamicSupplyRegion(food, factor);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		o.put(Messages.FACTOR_KEY, 2.5);
		o.put(Messages.FOOD_KEY, 1250.0);
	}
}
