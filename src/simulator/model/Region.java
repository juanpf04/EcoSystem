package simulator.model;

import java.util.List;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {
	
	protected static final double FOOD = 60.0;
	protected static final double NOMBRE_RECHULON = 5.0;
	protected static final double NOMBRE_RECHULON2 = 2.0;
	
	protected List<Animal> _animal_in_region;
	
	public Region() {
		this._animal_in_region = new ArrayList<Animal>();
	}

	// revisar visibilidad
	public final void add_animal(Animal a) {
		this._animal_in_region.add(a);
	}
	
	public final void remove_animal(Animal a) {
		this._animal_in_region.remove(a);
	}
	
	// revisar si es constante
	public final List<Animal> getAnimals(){
		return this._animal_in_region;
	}
	
	 @Override
	 public JSONObject as_JSON() { // revisar
		 JSONObject jo = new JSONObject();
		 
		jo.put("animals", this._animal_in_region);
			
		 return jo;
	 }
	 
	 protected int count(Diet d) {
		 int i = 0;
		 
		 for (Animal a: this._animal_in_region) 
			 if(d.equals(a.get_diet()))
				 i++;
			 
		return i;
	 }

	public boolean contains(Animal a) {
		return this._animal_in_region.contains(a);
	}
}
