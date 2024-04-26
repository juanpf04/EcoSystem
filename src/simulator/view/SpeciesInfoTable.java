package simulator.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

public class SpeciesInfoTable extends AbstractTableModel implements EcoSysObserver  {
	
	private List<String> _header;
	Map<RegionData, AnimalInfo> mapa;
	List<RegionData> listaRegiones;
	
	
	public SpeciesInfoTable(Controller c){
		c.addObserver(this);
		this._header.add("row");
		this._header.add("col");
		this._header.add("specie");
		this._header.add("max speed");
		this._header.add("specie with max speed");
		
	}

	@Override
	public int getRowCount() {
		return listaRegiones.size();
	}

	@Override
	public int getColumnCount() {
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RegionData elem = listaRegiones.get(rowIndex);
		Animal a;
		
		switch(columnIndex)
		{
		case 0: {
			return elem.row();
		}
		case 1: {
			return elem.col();
		}
		case 2:{
			if(a!= null)
				return a.get_genetic_code();
			else return "";
		}
		case 3: {
			if(a!=null)
				return a.get_speed();
			else return "";
		}
		default:
			throw new IllegalArgumentException("Unexpected Value");
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for(RegionData d: map) {
			AnimalInfo a = computeMax(d.region().getAnimalsInfo());
			mapa.put(d, a);
			listaRegiones.add(d);
			
			fireTableStructureChanged();
		}
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	mapa.clear();
	listaRegiones.clear();
		for(RegionData d: map) {
			AnimalInfo a = computeMax(d.region().getAnimalsInfo());
			mapa.put(d, a);
			listaRegiones.add(d);
			
			fireTableStructureChanged();
		}
		
	}
	private AnimalInfo computeMax(List<AnimalInfo> listaAnimales) {
		Optional<AnimalInfo> a = listaAnimales.stream().max((a1, a2)->Double.compare(a1.get_speed(), a2.get_speed()));
		if(a.isPresent()) {
			return a.get();
		}
		else {
			return null;
		}
	}

}
