package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.view.Messages;

import java.util.ArrayList;

public class Simulator implements JSONable {
	
	private Factory<Animal> _animals_factory;
	private Factory<Region> _regions_factory;
	private RegionManager _region_manager;
	private List<Animal> _animals;
	private double _time;
	
	public Simulator(int cols, int rows, int width, int height,
			Factory<Animal> animals_factory, Factory<Region> animals_factory) {
		this._animals_factory = animals_factory;
		this._animals_factory = animals_factory;
		this._region_manager = new RegionManager(cols, rows, width, height);
		this._animals = new ArrayList<Animal>(); // iniciar con los animales, mirar mas alante
		this._time = 0.0;
	}

	private void set_region(int row, int col, Region r) {
		this._region_manager.set_region(row, col, r); 
	}
	
	public void set_region(int row, int col, JSONObject r_json) { 
		this.set_region(row, col, this._regions_factory.create_instance(r_json));
	}
	
	private void add_animal(Animal a) {
		this._animals.add(a);
		this._region_manager.register_animal(a);
	}
	
	public void add_animal(JSONObject a_json) {
		this.add_animal(this._animals_factory.create_instance(a_json));
	}
	
	public MapInfo get_map_info() {
		return this._region_manager;
	}
	
	public List<? extends Animalnfo> get_animals() {
		return this._animals;
	}
	
	public double get_time() {
		return this._time;
	}
	
	public void advance(double dt) {
		this._time *= dt;
		this.remove_deaths();
		this.update_all_animals(dt);
		this._region_manager.update_all_regions(dt);
		for(Animal a: this._animals)
			if(a.is_pregnant())
				this.add_animal(a.deliver_baby());
			
	}

	private void update_all_animals(double dt) {
		for(Animal a: this._animals)
			a.update(dt);
	}

	private void remove_deaths() {
		for(int i = this._animals.size() - 1; i>=0 ; i--) {
			Animal a = this._animals.get(i);
			if(!a.is_alive())
				this._animals.remove(a);
		}
	}
	
	@Override
	 public JSONObject as_JSON() { // revisar
		JSONObject jo = new JSONObject();	
		 
		jo.put(Messages.TIME, this.get_time());
		jo.put(Messages.REGIONS_STATE, this._region_manager.as_JSON());
			
		return jo;
	 }
	
}
