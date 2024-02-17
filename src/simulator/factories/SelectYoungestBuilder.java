package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectYoungest;

public class SelectYoungestBuilder extends Builder<SelectYoungest> {

	private static final String TYPE = "youngest";
	private static final String DESCRIPTION = "descripcion rechulona";

	public SelectYoungestBuilder() {
		super(TYPE, DESCRIPTION);

	}

	@Override
	protected SelectYoungest create_instance(JSONObject data) {
		return new SelectYoungest();
	}
}
