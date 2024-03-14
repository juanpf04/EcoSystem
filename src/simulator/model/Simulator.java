package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.view.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Simulator implements JSONable, Observable<EcoSysObserver> {

	private Factory<Animal> _animals_factory;
	private Factory<Region> _regions_factory;
	private RegionManager _region_manager;
	private List<Animal> _animals;
	private List<EcoSysObserver> _observers;
	private double _time;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory,
			Factory<Region> regions_factory) {

		if (cols <= 0)
			throw new IllegalArgumentException(Messages.INVALID_COLS);
		if (rows <= 0)
			throw new IllegalArgumentException(Messages.INVALID_ROWS);
		if (width <= 0)
			throw new IllegalArgumentException(Messages.INVALID_WIDTH);
		if (height <= 0)
			throw new IllegalArgumentException(Messages.INVALID_HEIGHT);
		if (animals_factory == null || regions_factory == null)
			throw new IllegalArgumentException(Messages.INVALID_FACTORY);

		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
		this._animals = new LinkedList<Animal>();
		this._region_manager = new RegionManager(cols, rows, width, height);
		this._time = 0.0;
		this._observers = new LinkedList<EcoSysObserver>();
	}

	public void reset(int cols, int rows, int width, int height) {
		if (cols <= 0)
			throw new IllegalArgumentException(Messages.INVALID_COLS);
		if (rows <= 0)
			throw new IllegalArgumentException(Messages.INVALID_ROWS);
		if (width <= 0)
			throw new IllegalArgumentException(Messages.INVALID_WIDTH);
		if (height <= 0)
			throw new IllegalArgumentException(Messages.INVALID_HEIGHT);

		this._animals = new LinkedList<Animal>();
		this._region_manager = new RegionManager(cols, rows, width, height);
		this._time = 0.0;

		this.notify_on_reset();
	}

	private void set_region(int row, int col, Region r) {
		if (row < 0 || row >= this.get_map_info().get_rows())
			throw new IllegalArgumentException(Messages.INVALID_ROW);
		if (col < 0 || col >= this.get_map_info().get_cols())
			throw new IllegalArgumentException(Messages.INVALID_COL);
		if (r == null)
			throw new IllegalArgumentException(Messages.INVALID_REGION);

		this._region_manager.set_region(row, col, r);

		this.notify_on_region_set(row, col, r);
	}

	public void set_region(int row, int col, JSONObject r_json) {
		if (row < 0 || row >= this.get_map_info().get_rows())
			throw new IllegalArgumentException(Messages.INVALID_ROW);
		if (col < 0 || col >= this.get_map_info().get_cols())
			throw new IllegalArgumentException(Messages.INVALID_COL);
		if (r_json == null || r_json.isEmpty())
			throw new IllegalArgumentException(Messages.INVALID_JSON);

		this.set_region(row, col, this._regions_factory.create_instance(r_json));
	}

	private void add_animal(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		this._animals.add(a);
		this._region_manager.register_animal(a);

		this.notify_on_animal_added(a);
	}

	public void add_animal(JSONObject a_json) {
		if (a_json == null || a_json.isEmpty())
			throw new IllegalArgumentException(Messages.INVALID_JSON);

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
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		this._time += dt;
		this.remove_deaths();
		this.update_all_animals(dt);
		this._region_manager.update_all_regions(dt);
		this.update_babys();
		this.notify_on_advance(dt);
	}

	private void update_babys() {
		List<Animal> babies = new LinkedList<Animal>();

		for (Animal a : this._animals)
			if (a.is_pregnant()) {
				Animal baby = a.deliver_baby();
				babies.add(baby);
				this._region_manager.register_animal(baby);
			}

		this._animals.addAll(babies);
	}

	private void update_all_animals(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

//		for (Animal a : this._animals) { TODO
//			a.update(dt);
//			this._region_manager.update_animal_region(a);
//		}
		this._animals.forEach((Animal a) -> {
			a.update(dt);
			this._region_manager.update_animal_region(a);
		});
	}

	private void remove_deaths() {
		Iterator<Animal> it = this._animals.iterator();

		while (it.hasNext()) {
			Animal a = it.next();
			if (a.dead()) {
				it.remove();
				this.remove_animal(a);
			}
		}
	}

	private void remove_animal(Animal a) { // FIXME revisar remove a
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		this._animals.remove(a);
		this._region_manager.unregister_animal(a);
	}

	// JSONable

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();

		jo.put(Messages.TIME_KEY, this.get_time());
		jo.put(Messages.REGIONS_STATE_KEY, this._region_manager.as_JSON());

		return jo;
	}

	// Observable

	@Override
	public void addObserver(EcoSysObserver o) {
		if (!this._observers.contains(o))
			this._observers.add(o);

		this.notify_on_register(o);
	}

	@Override
	public void removeObserver(EcoSysObserver o) {
		this._observers.remove(o);
	}

	// Notifications

	private void notify_on_register(EcoSysObserver o) {
		o.onRegister(this.get_time(), this.get_map_info(), new ArrayList<>(this.get_animals()));
	}

	private void notify_on_reset() {
		List<AnimalInfo> animals = new ArrayList<>(this.get_animals());

		for (EcoSysObserver o : this._observers)
			o.onReset(this.get_time(), this.get_map_info(), animals);
	}

	private void notify_on_animal_added(Animal a) {
		List<AnimalInfo> animals = new ArrayList<>(this.get_animals());

		for (EcoSysObserver o : this._observers)
			o.onAnimalAdded(this.get_time(), this.get_map_info(), animals, a);
	}

	private void notify_on_region_set(int row, int col, Region r) {
		for (EcoSysObserver o : this._observers)
			o.onRegionSet(row, col, this.get_map_info(), r);
	}

	private void notify_on_advance(double dt) {
		List<AnimalInfo> animals = new ArrayList<>(this.get_animals());

		for (EcoSysObserver o : this._observers)
			o.onAvanced(this.get_time(), this.get_map_info(), animals, dt);
	}

}
