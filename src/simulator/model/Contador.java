package simulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import simulator.control.Controller;
import simulator.model.Animal.Diet;
import simulator.model.MapInfo.RegionData;

public class Contador implements EcoSysObserver {

	private Map<RegionData, Integer> contador;

	public Contador(Controller ctrl) {
		this.contador = new HashMap<MapInfo.RegionData, Integer>();
		ctrl.addObserver(this);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {

		for (RegionData r : map) {
			Integer cont = this.contador.get(r);
			if(cont == null) {
				if(r.region().getAnimalsInfo().stream().filter((a)->a.get_diet() == Diet.CARNIVORE).count() >= 3)
					cont = 1;
				else 
					cont = 0;
			}
			else {
				if(r.region().getAnimalsInfo().stream().filter((a)->a.get_diet() == Diet.CARNIVORE).count() >= 3)
					cont++;
			}
			this.contador.put(r, cont);
		}
	}

	public void imprimeInfo() {
		System.out.println("numero de veces que hay 3 carnivoros en cada region");
		for(Entry<RegionData, Integer> pair :contador.entrySet()) {
			RegionData r = pair.getKey();
			System.out.println("Region ("+r.row() + "," + r.col() + "): " + pair.getValue());
		}
	}

}
