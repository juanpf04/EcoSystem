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
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	private Map<String, Map<Animal.State, Integer>> _species;
	private List<String> _types; // Auxiliary list

	SpeciesTableModel(Controller ctrl) {
		this._ctrl = ctrl;
		this._header = new ArrayList<>();
		this._species = new HashMap<>();
		this._types = new ArrayList<>();

		this._header.add("Species");
		for (Animal.State s : Animal.State.values())
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
		String specie = this._types.get(rowIndex);

		if (columnIndex == 0)
			return specie;

		Map<Animal.State, Integer> stats = this._species.get(specie);
		Animal.State state = Animal.State.valueOf(this.getColumnName(columnIndex));
		return stats.get(state);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setSpecies(animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._species = new HashMap<>();
		this._types = new ArrayList<>();
		this.setSpecies(animals);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		this._species = new HashMap<>();
		this._types = new ArrayList<>();
		this.setSpecies(animals);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		this._species = new HashMap<>();
		this._types = new ArrayList<>();
		this.setSpecies(animals);
	}

	private void setSpecies(List<AnimalInfo> animals) {
		for (AnimalInfo a : animals) {
			String specie = a.get_genetic_code();
			Animal.State state = a.get_state();

			Map<Animal.State, Integer> stats = this._species.get(specie);

			if (stats == null) {
				stats = new HashMap<>();
				this._types.add(specie);
				stats.put(state, 1);
			} else {
				Integer num_animals = stats.get(state);

				if (num_animals == null) {
					stats.put(state, 1);
				} else {
					num_animals++;
					stats.put(state, num_animals);
				}
			}
			this._species.put(specie, stats);
		}
	}
}
