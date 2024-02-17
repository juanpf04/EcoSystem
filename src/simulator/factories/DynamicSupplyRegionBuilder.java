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
		double factor = 2.0,
				food = 1000.0;
		
		if(data.has("factor"))
			factor = data.getDouble("factor");
		
		if(data.has("food"))
			food = data.getDouble("food");		
		
		return new DynamicSupplyRegion(food, factor);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		// no se si es hacer esto o que 
		
	}
}
