package simulator.model;

import java.util.Map;
import java.util.function.Predicate;

import simulator.misc.Utils;

import java.util.HashMap;
import java.util.List;

public class RegionManager implements AnimalMapView {
	
	protected int _cols;
	protected int _rows;
	protected int _height;
	protected int _width;	
	protected int _region_height;
	protected int _region_width;
	protected Region[][] _regions; 
	protected Map<Animal, Region> _animal_region;
	
	public RegionManager(int cols, int rows, int width, int height) {
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		
		this._regions = new Region[this._rows][this._cols];
		
		for (int i = 0; i < this.get_rows(); i++)
			for (int j = 0; j < this.get_cols(); j++)
				this._regions[i][j] = new DefaultRegion();
		
		this._animal_region = new HashMap<Animal, Region>();// estructura sin orden
		// new TreeMap<String, Integer>()// estructura ordenada
	}
	
	void set_region(int row, int col, Region r) {
		this._regions[row][col] = r;
	}
	
	void register_animal(Animal a) { // revisar 
		for (int i = 0; i < this.get_rows(); i++)
			for (int j = 0; j < this.get_cols(); j++) {
				double x = a.get_position().getX(),
						y = a.get_position().getY(),
						minX = j * this.get_region_width(),
						minY = i * this.get_region_height(),
						maxX = (j + 1) * this.get_region_width(),
						maxY = (y + 1) * this.get_region_height();
				if (x == Utils.constrain_value_in_range(x, minX, maxX) && 
						y == Utils.constrain_value_in_range(y, minY, maxY)) { 
					this._animal_region.put(a, this._regions[i][j]);
				}
			}
	} 

	@Override
	public int get_cols() {
		return this._cols;
	}

	@Override
	public int get_rows() {
		return this._rows;
	}

	@Override
	public int get_width() {
		return this._width;
	}

	@Override
	public int get_height() {
		return this._height;
	}

	@Override
	public int get_region_width() {
		return this._region_width;
	}

	@Override
	public int get_region_height() {
		return this._region_height;
	}

	@Override
	public double get_food(Animal a, double dt) {
		//hacer
		return 0;
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		return null;
	}

}
