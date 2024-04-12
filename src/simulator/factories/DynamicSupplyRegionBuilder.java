package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Messages;
import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super(Messages.DYNAMIC_REGION_TAG, Messages.DYNAMIC_REGION_BUILDER_DESCRIPTION);
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {
		if (data == null)
			throw new IllegalArgumentException(Messages.INVALID_JSON);

		double food = data.optDouble(Messages.FOOD_KEY, 1000.0);
		double factor = data.optDouble(Messages.FACTOR_KEY, 2.0);

		return new DynamicSupplyRegion(food, factor);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		o.put(Messages.FACTOR_KEY, Messages.FACTOR_DESCRIPTION);
		o.put(Messages.FOOD_KEY, Messages.FOOD_DESCRIPTION);
	}
}
