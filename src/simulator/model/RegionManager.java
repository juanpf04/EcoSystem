package simulator.model;

import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.view.Messages;

import java.util.HashMap;
import java.util.LinkedList;
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

		if (cols <= 0)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		if (rows <= 0)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		if (width <= 0)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		if (height <= 0)
			throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);

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

	public void set_region(int row, int col, Region r) {
		for (Animal a : this._regions[row][col].getAnimals()) {
			r.add_animal(a);
			this._animal_region.put(a, r);
		}
		this._regions[row][col] = r;
	}

	public void register_animal(Animal a) {
		int j = (int) (a.get_position().getX() / this.get_region_width()) - 1,
				i = (int) (a.get_position().getY() / this.get_region_height()) - 1;
		Region region = this._regions[i][j];
		region.add_animal(a);
		this._animal_region.put(a, region);
		a.init(this);
	}

	public void unregister_animal(Animal a) {
		this._animal_region.remove(a).remove_animal(a);
	}

	public void update_animal_region(Animal a) {
		int j = (int) (a.get_position().getX() / this.get_region_width()) - 1,
				i = (int) (a.get_position().getY() / this.get_region_height()) - 1;
		Region new_region = this._regions[i][j];
		if (!new_region.contains(a)) {
			new_region.add_animal(a);
			Region old_region = this._animal_region.put(a, new_region);
			old_region.remove_animal(a);
		}
	}

	public void update_all_regions(double dt) {
		for (Region[] regions : this._regions)
			for (Region region : regions)
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

	@Override // terminar
	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
		List<Animal> animals_in_range = new LinkedList<Animal>();
		double sr = a.get_sight_range(), x = a.get_position().getX(), y = a.get_position().getY();
		int init_j = (int) (x - sr / this.get_region_width()) - 1,
				init_i = (int) (y - sr / this.get_region_height()) - 1;
		int end_j = (int) (x + sr / this.get_region_width()), end_i = (int) (y + sr / this.get_region_height());

		for (int i = init_i; i < end_i; i++)
			for (int j = init_j; j < end_j; j++)
				for (Animal animal : this._regions[i][j].getAnimals())
					if (animal.in_sight_range(a) && filter == null)
						animals_in_range.add(animal);

		return animals_in_range;
	}

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (int i = 0; i < this.get_rows(); i++)
			for (int j = 0; j < this.get_cols(); j++) {
				JSONObject jo1 = new JSONObject();
				jo1.put(Messages.ROW_KEY, i);
				jo1.put(Messages.COLUMN_KEY, j);
				jo1.put(Messages.DATA_KEY, this._regions[i][j].as_JSON());
				ja.put(jo1);
			}

		jo.put(Messages.REGIONS_KEY, ja);

		return jo;
	}
}
