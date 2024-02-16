package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectionStrategy;
import simulator.model.Sheep;
import simulator.view.Messages;

public class SheepBuilder extends Builder<Sheep> {

	public SheepBuilder() {
		super(Messages.SHEEP_TAG, Messages.DESCRIPTION);
	}

	@Override
	protected Sheep create_instance(JSONObject data) {
		
		
		SelectionStrategy ms = create_instance(data.getJSONObject("mate_strategy"));
		SelectionStrategy ds = create_instance(data.getJSONObject("danger_strategy"));		
	
		
		
		return null;
	}

}
