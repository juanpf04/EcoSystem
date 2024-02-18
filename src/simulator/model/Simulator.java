package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.view.Messages;

import java.util.Collections;
import java.util.LinkedList;

public class Simulator implements JSONable {

	private Factory<Animal> _animals_factory;
	private Factory<Region> _regions_factory;
	private RegionManager _region_manager;
	private List<Animal> _animals;
	private double _time;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory,
			Factory<Region> regions_factory) {
		if(animals_factory == null)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		if(regions_factory == null)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		
		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
		this._region_manager = new RegionManager(cols, rows, width, height);
		this._animals = new LinkedList<Animal>();
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

	public List<? extends AnimalInfo> get_animals() {
		return Collections.unmodifiableList(this._animals);
	}

	public double get_time() {
		return this._time;
	}

	public void advance(double dt) {
		this._time += dt;
		this.remove_deaths();
		this.update_all_animals(dt);
		this._region_manager.update_all_regions(dt);
		this.update_babys();
	}

	private void update_babys() {
		for (Animal a : this._animals)
			if (a.is_pregnant()) {
				Animal baby = a.deliver_baby();
				this.add_animal(baby);
				this._animals.add(baby); // revisar 
			}
	}

	private void update_all_animals(double dt) {
		for (Animal a : this._animals) {
			a.update(dt);
			this._region_manager.update_animal_region(a);
		}
	}

	private void remove_deaths() {
		for (int i = this._animals.size() - 1; i >= 0; i--) {
			Animal a = this._animals.get(i);
			if (!a.is_alive()) {
				this._animals.remove(a);
				this._region_manager.unregister_animal(a);
			}
		}
	}

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();

		jo.put(Messages.TIME_KEY, this.get_time());
		jo.put(Messages.REGIONS_STATE_KEY, this._region_manager.as_JSON());

		return jo;
	}
}
