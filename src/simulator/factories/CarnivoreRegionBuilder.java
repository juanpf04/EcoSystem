package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Messages;
import simulator.model.CarnivoreRegion;
import simulator.model.Region;

public class CarnivoreRegionBuilder extends Builder<Region> {

	public CarnivoreRegionBuilder() {
		super("carnivore", Messages.DEFAULT_REGION_BUILDER_DESCRIPTION);
	}

	@Override
	protected CarnivoreRegion create_instance(JSONObject data) {
		if (data != null && !data.isEmpty())
			throw new IllegalArgumentException(Messages.EMPTY_DATA);

		return new CarnivoreRegion();
	}
}
