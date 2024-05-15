package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatesTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	private List<List<AnimalInfo>> _animals;

	private State _state = State.NORMAL;

	StatesTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._animals = new ArrayList<>();

		this._header.add("Step");
		this._header.add("numAnimals in " + this._state);

		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._animals.size();
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
			return this._animals.get(rowIndex).stream().filter((a) -> a.get_state() == _state).count();
		default:
			throw new IllegalArgumentException("Unexpected value: " + columnIndex);
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._animals.clear();
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
		this._animals.add(animals);
		this.fireTableDataChanged();
	}

	public void setState(State state) {
		this._state = state;
		this.fireTableDataChanged();
		this._header.set(1, "numAnimals in " + this._state);
		this.fireTableStructureChanged();
	}

}
