package simulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		System.out.println("holaaa    " +this.contador);
	}

}
