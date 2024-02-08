package simulator.model;

import java.util.List;
import java.util.ArrayList;

public class Region implements Entity, FoodSupplier, RegionInfo {
	
	protected List<Animal> _animal_in_region;
	
	public Region() {
		this._animal_in_region = new ArrayList<Animal>();
	}

	@Override
	public double get_food(Animal a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
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
}
