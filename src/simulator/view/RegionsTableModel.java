package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal.Diet;
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

	private Map<RegionData, Map<Diet, Integer>> _info;
	private List<RegionData> _regions;

	RegionsTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._info = new HashMap<>();
		this._regions = new ArrayList<>();

		this._header.add("Row");
		this._header.add("Col");
		this._header.add("Desc.");
		for (Diet d : Diet.values())
			this._header.add(d.toString());

		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._info.size();
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
		RegionData r = this._regions.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return r.row();
		case 1:
			return r.col();
		case 2:
			return r.region().toString();
		default:
			Map<Diet, Integer> stats = this._info.get(r);
			Diet diet = Diet.valueOf(this.getColumnName(columnIndex));
			Integer num_animals = stats.get(diet);
			return num_animals == null ? 0 : num_animals;
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

		this.setRegions(map);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._info = new HashMap<>();
		this._regions = new ArrayList<>();
		this.setRegions(map);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		this._info = new HashMap<>();
		this._regions = new ArrayList<>();
		this.setRegions(map);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}

	private void setRegions(MapInfo map) {
		for (RegionData r : map) {
			this._regions.add(r);

			Map<Animal.Diet, Integer> stats = new HashMap<>();

			for (AnimalInfo a : r.region().getAnimalsInfo()) {
				Diet d = a.get_diet();
				Integer num_animals = stats.get(d);

				if (num_animals == null) {
					stats.put(d, 1);
				} else {
					num_animals++;
					stats.put(d, num_animals);
				}
			}

			this._info.put(r, stats);
		}
	}
}
