package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Messages;
import simulator.model.DefaultRegion;
import simulator.model.Region;

public class DefaultRegionBuilder extends Builder<Region> {

	public DefaultRegionBuilder() {
		super(Messages.DEFAULT_REGION_TAG, Messages.DEFAULT_REGION_BUILDER_DESCRIPTION);
	}

	@Override
	protected DefaultRegion create_instance(JSONObject data) {
		if (data != null && !data.isEmpty())
			throw new IllegalArgumentException(Messages.EMPTY_DATA);

		return new DefaultRegion();
	}
}
