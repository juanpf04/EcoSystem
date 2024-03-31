package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

/*class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
	/**
	 * 
	 
	private static final long serialVersionUID = 1L;

	//dos mapas map(map(numero, stado), especie)
	
	private Controller _ctrl;
	private List<String> _header;
	private List<int> _species;

	SpeciesTableModel(Controller ctrl) {
		this._ctrl = ctrl;
		this._header = new ArrayList<>();
		this._species = new ArrayList<>();
		
		this._header.add("Species");
		for(Animal.State s:Animal.State.values())
			this._header.add(s.toString());
	}

	@Override
	public int getRowCount() {
		return _species.size();
	}

	@Override
	public int getColumnCount() {
		return _header.size();
	}

	@Override
	public String getColumnName(int index) {
		return _header.get(index);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Animal.State s = Animal.State.valueOf(this.getColumnName(columnIndex));

		return null;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		
		for(AnimalInfo a: animals)
			if(!this._species.contains(a.get_genetic_code()))
				this._species.add(a.get_genetic_code());


	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {

		if(!this._species.contains(a.get_genetic_code()))
			this._species.add(a.get_genetic_code());

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub

	}
}*/
