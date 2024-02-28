package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;
import simulator.model.SelectionStrategy;
import simulator.view.Messages;

public class SelectClosestBuilder extends Builder<SelectionStrategy> {

	public SelectClosestBuilder() {
		super(Messages.SELECT_CLOSEST_TAG, Messages.SELECT_CLOSEST_BUILDER_DESCRIPTION);
	}

	@Override
	protected SelectClosest create_instance(JSONObject data) {
		if(data != null && !data.isEmpty())
			throw new IllegalArgumentException(Messages.EMPTY_DATA);
		
		return new SelectClosest();
	}
}
