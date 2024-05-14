package simulator.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import simulator.control.Controller;


public class North implements EcoSysObserver {

	List<Integer> north;
	Map<AnimalInfo, Double> estado_anterior;

	public North(Controller ctrl) {
		this.north = new LinkedList<Integer>();
		this.estado_anterior = new HashMap<AnimalInfo, Double>();
		ctrl.addObserver(this);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for(AnimalInfo a: animals) {
			this.estado_anterior.put(a, a.get_position().getY());
		}
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
		int cont = 0;
		for(AnimalInfo a: animals) {
			Double new_y = a.get_position().getY();
			if(this.estado_anterior.containsKey(a)) {
				Double old_y = this.estado_anterior.get(a);
				if(old_y > new_y)
					cont++;
				this.estado_anterior.replace(a, old_y, new_y);
			}
			else
				this.estado_anterior.put(a, new_y);
		}
		this.north.add(cont);
//		Integer cont = (int) animals.stream().filter((a)->{
//			return a.get_destination().getY() < a.get_position().getY();
//		}).count();
//		this.north.add(cont);
	}

	public void imprimeInfo() {
		System.out.println("numero de animales hacia el norte en cada iteracion");
		
		for(int i = 0; i<north.size();i++) {
			System.out.println("Iteracion " + i + ": " + north.get(i));
		}
	}
}
