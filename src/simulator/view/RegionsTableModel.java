package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	private Map<RegionData, Map<Animal.Diet, Integer>> _regions;

	private int _cols;
	private int _rows;

	RegionsTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._regions = new HashMap<>();

		this._header.add("Row");
		this._header.add("Col");
		this._header.add("Desc.");
		for (Animal.Diet d : Animal.Diet.values())
			this._header.add(d.toString());

		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._regions.size();
	}

	@Override
	public int getColumnCount() {
		return this._header.size();
	}

	@Override
	public String getColumnName(int index) {
		return this._header.get(index);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int col = 0;
		int row = 0;
		while (col + row < rowIndex) {
			col++;
			if (col == this._cols) {
				row++;
				col = 0;
			}
		}

		if (columnIndex == 0) {
			return row;
		} else if (columnIndex == 1) {
			return col;
		} else {

		}

		return 0;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this._cols = map.get_cols();
		this._rows = map.get_rows();

		this.setRegions(map);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._cols = map.get_cols();
		this._rows = map.get_rows();

		this._regions = new HashMap<>();
		this.setRegions(map);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		this._regions = new HashMap<>();
		this.setRegions(map);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}

	private void setRegions(MapInfo map) {
		for (RegionData r : map) {

		}
	}
}
