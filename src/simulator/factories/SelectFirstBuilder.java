package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.view.Messages;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

	public SelectFirstBuilder() {
		super(Messages.SELECT_FIRST_TAG, Messages.DESCRIPTION);
	}

	@Override
	protected SelectFirst create_instance(JSONObject data) {
		return new SelectFirst();
	}
}
