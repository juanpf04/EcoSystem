package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;
import simulator.view.Messages;

public class SheepBuilder extends Builder<Sheep> {

	private Factory<SelectionStrategy> _factory;
	
	public SheepBuilder(Factory<SelectionStrategy> factory) {
		super(Messages.SHEEP_TAG, Messages.DESCRIPTION);
		this._factory = factory;
	}

	@Override
	protected Sheep create_instance(JSONObject data) {		
		SelectionStrategy mate_strategy = new SelectFirst();
		SelectionStrategy danger_strategy = new SelectFirst();
		
		
		return null;
	}

}
