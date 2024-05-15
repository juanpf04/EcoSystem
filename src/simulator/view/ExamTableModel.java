package simulator.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal.Diet;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ExamTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	public record InfoTable(int herbivores, int carnivores) {
	}

	private List<InfoTable> _info;

	ExamTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._info = new LinkedList<>();

		this._header.add("Step");
		this._header.add(State.DANGER + " " + Diet.HERBIVORE);
		this._header.add(State.HUNGER + " " + Diet.CARNIVORE);

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
		InfoTable info = this._info.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			return info.herbivores();
		case 2:
			return info.carnivores();
		default:
			throw new IllegalArgumentException("Unexpected value: " + columnIndex);
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._info.clear();
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
		int herbivores = (int) animals.stream().filter((a) -> {
			return a.get_diet() == Diet.HERBIVORE && a.get_state() == State.DANGER;
		}).count();
		int carnivores = (int) animals.stream().filter((a) -> {
			return a.get_diet() == Diet.CARNIVORE && a.get_state() == State.HUNGER;
		}).count();

		this._info.add(new InfoTable(herbivores, carnivores));

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}
}
