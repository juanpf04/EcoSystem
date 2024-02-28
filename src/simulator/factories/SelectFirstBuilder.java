package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.view.Messages;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

	public SelectFirstBuilder() {
		super(Messages.SELECT_FIRST_TAG, Messages.SELECT_FIRST_BUILDER_DESCRIPTION);
	}

	@Override
	protected SelectFirst create_instance(JSONObject data) {
		if(data != null && !data.isEmpty())
			throw new IllegalArgumentException(Messages.EMPTY_DATA);
		
		return new SelectFirst();
	}
}
