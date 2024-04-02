package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Messages;
import simulator.model.SelectYoungest;
import simulator.model.SelectionStrategy;

public class SelectYoungestBuilder extends Builder<SelectionStrategy> {

	public SelectYoungestBuilder() {
		super(Messages.SELECT_YOUNGEST_TAG, Messages.SELECT_YOUNGEST_BUILDER_DESCRIPTION);
	}

	@Override
	protected SelectYoungest create_instance(JSONObject data) {
		if (data != null && !data.isEmpty())
			throw new IllegalArgumentException(Messages.EMPTY_DATA);

		return new SelectYoungest();
	}
}
