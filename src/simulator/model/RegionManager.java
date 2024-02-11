package simulator.model;

import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.view.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RegionManager implements AnimalMapView {
	
	private int _cols;
	private int _rows;
	private int _height;
	private int _width;	
	private int _region_height;
	private int _region_width;
	private Region[][] _regions; 
	private Map<Animal, Region> _animal_region;
	
	public RegionManager(int cols, int rows, int width, int height) {
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		this._region_width = this._width / this._cols;
		this._region_height = this._height / this._rows;
		
		this._regions = new Region[this._rows][this._cols];
		
		for (int i = 0; i < this.get_rows(); i++)
			for (int j = 0; j < this.get_cols(); j++)
				this._regions[i][j] = new DefaultRegion();
		
		this._animal_region = new HashMap<Animal, Region>();
	}
	
	void set_region(int row, int col, Region r) {
		this._regions[row][col] = r;
	}
	
	void register_animal(Animal a) { 
		boolean registered = false;
		
		for (int i = 0; i < this.get_rows() && !registered; i++)
			for (int j = 0; j < this.get_cols() && !registered; j++) {
				
				double x = a.get_position().getX(),
						y = a.get_position().getY(),
						minX = j * this.get_region_width(),
						minY = i * this.get_region_height(),
						maxX = (j + 1) * this.get_region_width(),
						maxY = (y + 1) * this.get_region_height();
				
				if (x == Utils.constrain_value_in_range(x, minX, maxX) && 
						y == Utils.constrain_value_in_range(y, minY, maxY)) { 
					
					this._regions[i][j].add_animal(a);
					this._animal_region.put(a, this._regions[i][j]);
					a.init(this);
					registered = true;
				}
			}
	} 
	
	void unregister_animal(Animal a) {
		boolean unregistered = false;
		for (int i = 0; i < this.get_rows() && !unregistered; i++)
			for (int j = 0; j < this.get_cols() && !unregistered; j++) 
				if (this._regions[i][j].contains(a)) {
					this._regions[i][j].remove_animal(a);
					this._animal_region.remove(a);
					unregistered = true;
				}
	}
	
	void update_animal_region(Animal a) { // revisar
		boolean updated = false;
		
		for (int i = 0; i < this.get_rows() && !updated; i++)
			for (int j = 0; j < this.get_cols() && !updated; j++) {
				
				double x = a.get_position().getX(),
						y = a.get_position().getY(),
						minX = j * this.get_region_width(),
						minY = i * this.get_region_height(),
						maxX = (j + 1) * this.get_region_width(),
						maxY = (y + 1) * this.get_region_height();
				
				if (x == Utils.constrain_value_in_range(x, minX, maxX) && 
						y == Utils.constrain_value_in_range(y, minY, maxY)) { 
					this._regions[i][j].add_animal(a);
					this._animal_region.put(a, this._regions[i][j]);
					// utilizar mapa para saber en que region esta en el animal
					updated = true;
				}
			}
	}
	
	void update_all_regions(double dt) {
		for(Region[] regions: this._regions)
			for(Region region: regions)
				region.update(dt);
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
		return this._animal_region.get(a).get_food(a, dt);
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		return null;
	}

	@Override
	 public JSONObject as_JSON() { // revisar
		JSONObject jo = new JSONObject();
		 
		List<JSONObject> regions = new ArrayList<JSONObject>();
		
		for (int i = 0; i < this.get_rows(); i++) 
			for (int j = 0; j < this.get_cols(); j++) {
				JSONObject jo1 = new JSONObject();
				jo1.put(Messages.ROW_KEY, i);
				jo1.put(Messages.COLUMN_KEY, j);
				jo1.put(Messages.DATA_KEY, this._regions[i][j]);
				regions.add(jo1);
			}			
		 
		jo.put(Messages.REGIONS_KEY, regions);
			
		return jo;
	 }
}
