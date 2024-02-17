package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.view.Messages;

public class DefaultRegionBuilder extends Builder<DefaultRegion> { // preguntar

	public DefaultRegionBuilder() {
		super(Messages.DEFAULT_REGION_TYPE, Messages.MENSAJE_PERSONALIZADO);
	}

	@Override
	protected DefaultRegion create_instance(JSONObject data) {
		return new DefaultRegion();
	}
}
