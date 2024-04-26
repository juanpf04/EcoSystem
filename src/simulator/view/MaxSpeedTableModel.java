package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.Animal.Diet;
import simulator.model.MapInfo.RegionData;

public class MaxSpeedTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private List<String> _header;

	private Map<RegionData, AnimalInfo> _fastest_animals;
	private List<RegionData> _regiones; // Auxiliary list

	MaxSpeedTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._header = new ArrayList<>();
		this._fastest_animals = new HashMap<>();
		this._regiones = new ArrayList<>();

		this._header.add(Messages.ROW);
		this._header.add(Messages.COL);
		this._header.add("Fastest specie");
		this._header.add("Max speed");

		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._fastest_animals.size();
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
		RegionData r = this._regiones.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return r.row();
		case 1:
			return r.col();
		case 2:
			if(this._fastest_animals.get(r) == null)
				return "no hay";
			return this._fastest_animals.get(r).get_genetic_code();
		case 3:
			if(this._fastest_animals.get(r) == null)
				return 0;			
			return this._fastest_animals.get(r).get_speed();
		default:
			throw new UnsupportedOperationException("queee");
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setFastestAnimal(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setFastestAnimal(map);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
//		int row = (int) (a.get_position().getY() / map.get_region_height());
//		int col = (int) (a.get_position().getX() / map.get_region_width());
//		int index = row * map.get_cols() + col;
//
//		RegionData r = this._regions_data.get(index);
//
//		AnimalInfo fastest_animal = this._regions.get(r);
//
//		if (a.get_speed() > fastest_animal.get_speed())
//			this._regions.replace(r, a);
//
//		this.fireTableDataChanged();
//		this.fireTableStructureChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		this.setFastestAnimal(map);
	}

	private void setFastestAnimal(MapInfo map) {
		this._fastest_animals.clear();
		this._regiones.clear();

		for (RegionData r : map)
			this.addMaxAnimal(r);

		this.fireTableDataChanged();
		this.fireTableStructureChanged();
	}

	private void addMaxAnimal(RegionData r) {
		this._regiones.add(r);

		Optional<AnimalInfo> fastest_animal = r.region().getAnimalsInfo().stream().max((a1,a2) -> 
			Double.compare(a1.get_speed(), a2.get_speed())
		);
		
		if (fastest_animal.isPresent())
			this._fastest_animals.put(r, fastest_animal.get());
		else 
			this._fastest_animals.put(r, null);
	}
}
