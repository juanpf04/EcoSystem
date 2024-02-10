package simulator.factories;

import org.json.JSONObject;

import simulator.view.Messages;

public class SelectFirstBuilder<SelectFirst> extends Builder<SelectFirst> {

	private static final String TYPE = "first";
	private static final String DESCRIPTION = "descripcion rechulona";
	
	public SelectFirstBuilder() {
		super(TYPE, DESCRIPTION);
		
	}

	@Override
	protected SelectFirst create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
