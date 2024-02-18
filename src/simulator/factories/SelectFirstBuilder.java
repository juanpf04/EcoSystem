package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

	private static final String TYPE = "first";
	private static final String DESCRIPTION = "descripcion rechulona";

	public SelectFirstBuilder() {
		super(TYPE, DESCRIPTION);
	}

	@Override
	protected SelectFirst create_instance(JSONObject data) {
		return new SelectFirst();
	}
}
