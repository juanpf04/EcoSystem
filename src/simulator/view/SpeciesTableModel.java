package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	private Map<String, Map<State, Integer>> _species;
	private List<String> _gcodes; // Auxiliary list

	SpeciesTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._species = new HashMap<>();
		this._gcodes = new ArrayList<>();

		this._header.add(Messages.SPECIES_TABLE_TITLE);
		for (State s : State.values())
			this._header.add(s.toString());

		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._species.size();
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
		String specie = this._gcodes.get(rowIndex);

		if (columnIndex == 0)
			return specie;

		Map<State, Integer> stats = this._species.get(specie);
		State state = State.valueOf(this.getColumnName(columnIndex));
		Integer num_animals = stats.get(state);
		return num_animals == null ? 0 : num_animals;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setSpecies(animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setSpecies(animals);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		this.addSpecie(a);
		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		this.setSpecies(animals);
	}

	private void setSpecies(List<AnimalInfo> animals) {
		this._species.clear();
		this._gcodes.clear();

		for (AnimalInfo a : animals)
			this.addSpecie(a);

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	private void addSpecie(AnimalInfo a) {
		String specie = a.get_genetic_code();
		State state = a.get_state();

		Map<State, Integer> stats = this._species.get(specie);

		if (stats == null) {
			stats = new HashMap<>();
			this._gcodes.add(specie);
			stats.put(state, 1);
		} else {
			Integer num_animals = stats.get(state);

			if (num_animals == null) {
				stats.put(state, 1);
			} else {
				num_animals++;
				stats.replace(state, num_animals);
			}
		}
		this._species.put(specie, stats);
	}
}
