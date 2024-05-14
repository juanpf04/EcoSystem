package simulator.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import simulator.control.Controller;
import simulator.model.Animal.Diet;
import simulator.model.MapInfo.RegionData;

public class Contador implements EcoSysObserver {

	private Map<RegionData, Integer> _contador;

	public Contador(Controller ctrl) {
		this._contador = new TreeMap<>(new Comparator<RegionData>() {

			@Override
			public int compare(RegionData r1, RegionData r2) {
				if (r1.row() == r2.row() && r1.col() == r2.col())
					return 0;
				if (r1.row() < r2.row() || (r1.row() == r2.row() && r1.col() < r2.col()))
					return -1;

				return 1;
			}
		});
		ctrl.addObserver(this);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for (RegionData r : map) {
			this._contador.put(r, 0);
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
		// recorrer las regiones
		// para cada region comprobar si hay 3 carnivoros en ella
		// si los hay aumento su contador, si no no

		for (RegionData r : map) {
			Integer contador = this._contador.get(r);

			long numCarnivoros = r.region().getAnimalsInfo().stream().filter((a) -> a.get_diet() == Diet.CARNIVORE)
					.count();

			if (numCarnivoros >= 3) {
				contador++;
				this._contador.replace(r, contador);
			}
		}
	}

	public void imprimeInfo() {
		// region y su contador
		System.out.println("numero de veces que hay 3 carnivoros en cada region");
		for (Entry<RegionData, Integer> pair : this._contador.entrySet()) {
			RegionData r = pair.getKey();
			System.out.println(r.region() + " region (" + r.row() + "," + r.col() + "): " + pair.getValue());
		}
	}
}
