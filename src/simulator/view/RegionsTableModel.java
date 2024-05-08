package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.Animal.Diet;
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

	private List<String> _header;

	private Map<RegionData, Map<String, Integer>> _regions;
	private List<RegionData> _regions_data; // Auxiliary list

	RegionsTableModel(Controller ctrl) {
		this._header = new ArrayList<>();
		this._regions = new HashMap<>();
		this._regions_data = new ArrayList<>();

		this._header.add(Messages.ROW);
		this._header.add(Messages.COL);
		this._header.add(Messages.DESC);
		for (Diet d : Diet.values())
			this._header.add(d.toString());

		ctrl.addObserver(this);
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

		Object value;
		switch (columnIndex) {
		case 0:
			value = r.row();
			break;
		case 1:
			value = r.col();
			break;
		case 2:
			value = r.region().toString();
			break;
		default:
			Map<String, Integer> stats = this._regions.get(r);
			Integer num_animals = stats.get(this.getColumnName(columnIndex));
			value = num_animals == null ? 0 : num_animals;
			break;
		}

		return value;
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
		SwingUtilities.invokeLater(() -> {
			int row = (int) (a.get_position().getY() / map.get_region_height());
			int col = (int) (a.get_position().getX() / map.get_region_width());
			int index = row * map.get_cols() + col;

			RegionData r = this._regions_data.get(index);
			String diet = a.get_diet().toString();
			Map<String, Integer> stats = this._regions.get(r);

			Integer num_animals = stats.get(diet);
			num_animals = num_animals == null ? 1 : num_animals + 1;

			stats.replace(diet, num_animals);
			this._regions.replace(r, stats);

			this.fireTableDataChanged();
			this.fireTableStructureChanged();
		});
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		int index = row * map.get_cols() + col;

		RegionData new_r = new RegionData(row, col, r);
		RegionData old_r = this._regions_data.set(index, new_r);

		this._regions.put(new_r, this._regions.remove(old_r));

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		SwingUtilities.invokeLater(() -> {
			this.setRegions(map);
		});
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

		Map<String, Integer> stats = new HashMap<>();

		for (Diet d : Diet.values())
			stats.put(d.toString(),
					(int) r.region().getAnimalsInfo().stream().filter((a) -> a.get_diet() == d).count());

//		for (AnimalInfo a : r.region().getAnimalsInfo()) {
//			String diet = a.get_diet().toString();
//			Integer num_animals = stats.get(diet);
//
//			if (num_animals == null) {
//				stats.put(diet, 1);
//			} else {
//				num_animals++;
//				stats.replace(diet, num_animals);
//			}
//		}

		this._regions.put(r, stats);
	}
}
