package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;
import simulator.view.Messages;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super(Messages.DEFAULT_REGION_TYPE, Messages.DESCRIPTION);
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {
		double factor = 2.0, food = 1000.0;

		if (data.has("factor"))
			factor = data.getDouble("factor");

		if (data.has("food"))
			food = data.getDouble("food");

		return new DynamicSupplyRegion(food, factor);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		o.put("factor", 2.5);
		o.put("food", 1250.0);
	}
}
