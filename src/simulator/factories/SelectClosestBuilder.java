package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;

public class SelectClosestBuilder extends Builder<SelectClosest> {

	private static final String TYPE = "closest";
	private static final String DESCRIPTION = "descripcion rechulona";

	public SelectClosestBuilder() {
		super(TYPE, DESCRIPTION);

	}

	@Override
	protected SelectClosest create_instance(JSONObject data) {
		return new SelectClosest();
	}
}
