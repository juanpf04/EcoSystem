package simulator.model;

import java.util.Map;

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
		
		// hacer mapa
	}
}
