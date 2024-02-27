package simulator.model;

import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.view.Messages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
			throw new IllegalArgumentException(Messages.INVALID_COLS);
		if (rows <= 0)
			throw new IllegalArgumentException(Messages.INVALID_ROWS);
		if (width <= 0)
			throw new IllegalArgumentException(Messages.INVALID_WIDTH);
		if (height <= 0)
			throw new IllegalArgumentException(Messages.INVALID_HEIGHT);

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
		a.init(this);
		Region region = this.get_region(a);
		region.add_animal(a);
		this._animal_region.put(a, region);
	}

	public void unregister_animal(Animal a) {
		this._animal_region.remove(a).remove_animal(a);
	}

	public void update_animal_region(Animal a) {
		Region region = this.get_region(a);

		if (region != this._animal_region.get(a)) {
			region.add_animal(a);
			this._animal_region.put(a, region).remove_animal(a);
		}
	}

	private Region get_region(Animal a) {
		int i = (int) (a.get_position().getY() / this.get_region_height());
		int j = (int) (a.get_position().getX() / this.get_region_width());

		return this._regions[i][j];
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

	@Override
	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
		List<Animal> animals_in_range = new LinkedList<Animal>();

		for (Region region : this.get_regions_in_range(a))
			for (Animal animal : region.getAnimals())
				if (animal.in_sight_range(a) && filter.test(animal))
					animals_in_range.add(animal);

		animals_in_range.remove(a);

		return animals_in_range;
	}

	private List<Region> get_regions_in_range(Animal a) {
		List<Region> regions_in_range = new LinkedList<>();

		double sr = a.get_sight_range();
		double x = a.get_position().getX();
		double y = a.get_position().getY();

		int ini_i = (int) ((y - sr) / this.get_region_height());
		int ini_j = (int) ((x - sr) / this.get_region_width());
		int end_i = (int) ((y + sr) / this.get_region_height());
		int end_j = (int) ((x + sr) / this.get_region_width());

		ini_i = Math.max(ini_i, 0);
		ini_j = Math.max(ini_j, 0);
		end_i = Math.min(end_i, this.get_rows() - 1);
		end_j = Math.min(end_j, this.get_cols() - 1);

		for (int i = ini_i; i <= end_i; i++)
			for (int j = ini_j; j <= end_j; j++)
				regions_in_range.add(this._regions[i][j]);

		return regions_in_range;
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
