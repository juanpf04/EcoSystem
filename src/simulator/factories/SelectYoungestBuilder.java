package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectYoungest;
import simulator.model.SelectionStrategy;
import simulator.view.Messages;

public class SelectYoungestBuilder extends Builder<SelectionStrategy> {

	public SelectYoungestBuilder() {
		super(Messages.SELECT_YOUNGEST_TAG, Messages.DESCRIPTION);
	}

	@Override
	protected SelectYoungest create_instance(JSONObject data) {
		return new SelectYoungest();
	}
}
