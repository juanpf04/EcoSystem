package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;
import simulator.model.SelectionStrategy;
import simulator.view.Messages;

public class SelectClosestBuilder extends Builder<SelectionStrategy> {

	public SelectClosestBuilder() {
		super(Messages.SELECT_CLOSEST_TAG, Messages.DESCRIPTION);
	}

	@Override
	protected SelectClosest create_instance(JSONObject data) {
		return new SelectClosest();
	}
}
