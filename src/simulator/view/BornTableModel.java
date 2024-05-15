package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class BornTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	private List<Integer> _babys;

//	private int _numAnimals;

	BornTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._babys = new ArrayList<>();

		this._header.add("Step");
		this._header.add("Babys");

		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._babys.size();
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
		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			return this._babys.get(rowIndex);
		default:
			throw new IllegalArgumentException("Unexpected value: " + columnIndex);
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
//		this._numAnimals = (int) animals.stream().filter((a) -> a.get_state() != State.DEAD).count();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
//		this._numAnimals = (int) animals.stream().filter((a) -> a.get_state() != State.DEAD).count();
		this._babys.clear();
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		this._babys.add((int) animals.stream().filter((a) -> a.get_age() == 0).count());
//		int new_numAnimals = (int) animals.stream().filter((a) -> a.get_state() != State.DEAD).count();
//		this._babys.add(new_numAnimals - this._numAnimals);
//		this._numAnimals = (int) animals.stream().filter((a) -> a.get_state() != State.DEAD).count();
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}

}
