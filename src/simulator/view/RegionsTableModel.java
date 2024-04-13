package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.misc.Messages;
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

	private Map<RegionData, Map<Diet, Integer>> _regions;
	private List<RegionData> _regions_data; // Auxiliary list

	RegionsTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._regions = new HashMap<>();
		this._regions_data = new ArrayList<>();

		this._header.add(Messages.ROW);
		this._header.add(Messages.COL);
		this._header.add(Messages.DESC);
		for (Diet d : Diet.values())
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
		RegionData r = this._regions_data.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return r.row();
		case 1:
			return r.col();
		case 2:
			return r.region().toString();
		default:
			Map<Diet, Integer> stats = this._regions.get(r);
			Diet diet = Diet.valueOf(this.getColumnName(columnIndex));
			Integer num_animals = stats.get(diet);
			return num_animals == null ? 0 : num_animals;
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setRegions(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setRegions(map);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO
		this.setRegions(map);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		this.setRegions(map);
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO
		this.setRegions(map);
	}

	private void setRegions(MapInfo map) {
		this._regions.clear();
		this._regions_data.clear();

		for (RegionData r : map)
			this.addRegion(r);

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	private void addRegion(RegionData r) {
		this._regions_data.add(r);

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

		this._regions.put(r, stats);
	}
}
