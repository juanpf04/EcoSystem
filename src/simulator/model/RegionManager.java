package simulator.model;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Messages;

import java.util.HashMap;
import java.util.Iterator;
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

		if (this.get_width() % this.get_cols() != 0)
			throw new ArithmeticException(Messages.ILLEGAL_WIDTH_OPERATION);
		if (this.get_height() % this.get_rows() != 0)
			throw new ArithmeticException(Messages.ILLEGAL_HEIGHT_OPERATION);

		this._region_width = this._width / this._cols;
		this._region_height = this._height / this._rows;

		this._regions = new Region[this._rows][this._cols];

		for (int i = 0; i < this.get_rows(); i++)
			for (int j = 0; j < this.get_cols(); j++)
				this._regions[i][j] = new DefaultRegion();

		this._animal_region = new HashMap<Animal, Region>();
	}

	public void set_region(int row, int col, Region r) {
		if (row < 0 || row >= this.get_rows())
			throw new IllegalArgumentException(Messages.INVALID_ROW);
		if (col < 0 || col >= this.get_cols())
			throw new IllegalArgumentException(Messages.INVALID_COL);
		if (r == null)
			throw new IllegalArgumentException(Messages.INVALID_REGION);

		for (Animal a : this.get_region(row, col).getAnimals()) {
			r.add_animal(a);
			this._animal_region.put(a, r);
		}

		this._regions[row][col] = r;
	}

	public void register_animal(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		a.init(this);
		Region region = this.get_region(a);
		region.add_animal(a);
		this._animal_region.put(a, region);
	}

	public void unregister_animal(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		this._animal_region.remove(a).remove_animal(a);
	}

	public void update_animal_region(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		Region region = this.get_region(a);

		if (region != this._animal_region.get(a)) {
			region.add_animal(a);
			this._animal_region.put(a, region).remove_animal(a);
		}
	}

	private Region get_region(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		int i = (int) (a.get_position().getY() / this.get_region_height());
		int j = (int) (a.get_position().getX() / this.get_region_width());

		return this.get_region(i, j);
	}

	private Region get_region(int row, int col) {
		return this._regions[row][col];
	}

	public void update_all_regions(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		for (Region[] regions : this._regions)
			for (Region region : regions)
				region.update(dt);
	}

	// MapInfo

	@Override
	public Iterator<RegionData> iterator() {
		return new Iterator<RegionData>() {

			private int _row = 0;
			private int _col = 0;

			@Override
			public RegionData next() {
				if (!this.hasNext())
					throw new NoSuchElementException("mensaje"); // TODO message
				
				RegionData r = new RegionData(_row, _col, get_region(_row, _col));

				this._col++;

				if (this._col == get_cols()) {
					this._col = 0;
					this._row++;
				}

				return r;
			}

			@Override
			public boolean hasNext() {
				return this._row < get_rows() && this._col < get_cols();
			}
		};
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

	// FoodSupplier

	@Override
	public double get_food(Animal a, double dt) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		return this._animal_region.get(a).get_food(a, dt);
	}

	// AnimalMapView

	@Override
	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (filter == null)
			throw new IllegalArgumentException(Messages.INVALID_PREDICATE);

		List<Animal> animals_in_range = new LinkedList<Animal>();

		for (Region region : this.get_regions_in_range(a))
			animals_in_range.addAll(region.get_animals(animal -> animal.in_sight_range(a) && filter.test(animal)));

		animals_in_range.remove(a);

		return animals_in_range;
	}

	// Auxiliary

	private List<Region> get_regions_in_range(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		List<Region> regions_in_range = new LinkedList<>();

		double sr = a.get_sight_range();
		double x = a.get_position().getX();
		double y = a.get_position().getY();

		int ini_i = (int) Math.max((y - sr) / this.get_region_height(), 0);
		int ini_j = (int) Math.max((x - sr) / this.get_region_width(), 0);
		int end_i = (int) Math.min((y + sr) / this.get_region_height(), this.get_rows() - 1);
		int end_j = (int) Math.min((x + sr) / this.get_region_width(), this.get_cols() - 1);

		for (int i = ini_i; i <= end_i; i++)
			for (int j = ini_j; j <= end_j; j++)
				regions_in_range.add(this.get_region(i, j));

		return regions_in_range;
	}

	// JSONable

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (int i = 0; i < this.get_rows(); i++)
			for (int j = 0; j < this.get_cols(); j++) {
				JSONObject jo1 = new JSONObject();
				jo1.put(Messages.ROW_KEY, i);
				jo1.put(Messages.COLUMN_KEY, j);
				jo1.put(Messages.DATA_KEY, this.get_region(i, j).as_JSON());
				ja.put(jo1);
			}

		jo.put(Messages.REGIONS_KEY, ja);

		return jo;
	}

}
