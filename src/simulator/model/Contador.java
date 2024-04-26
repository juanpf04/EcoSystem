package simulator.model;

import java.util.List;
import java.util.Map;

import simulator.control.Controller;
import simulator.model.MapInfo.RegionData;

public class Contador implements EcoSysObserver {

	private Map<RegionData, Integer> contador;
	
	public Contador(Controller ctrl) {
		ctrl.addObserver(this);
	}
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	public void imprimeInfo() {
		// TODO Auto-generated method stub
		
	}

}
