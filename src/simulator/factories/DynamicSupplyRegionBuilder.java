package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.DynamicSupplyRegion;
import simulator.view.Messages;

public class DynamicSupplyRegionBuilder extends Builder<DynamicSupplyRegion> {
	
	public DynamicSupplyRegionBuilder() {
		super(Messages.DEFAULT_REGION_TYPE, Messages.MENSAJE_PERSONALIZADO);
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {
		// terminar 
		
		return new DynamicSupplyRegion(0, 0);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		// no se si es hacer esto o que 
		
	}
}
