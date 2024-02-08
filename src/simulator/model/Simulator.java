package simulator.model;

import java.util.List;
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

	private set_region(int row, int col, JSONObject r) {
		
	}
	
}
