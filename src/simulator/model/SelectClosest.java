package simulator.model;

import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty())
			return null;

		Animal closest_animal = as.get(0);
		double closest_distance = a.distanceTo(closest_animal);

		for (Animal animal : as) {
			double distance = a.distanceTo(animal);
			if (distance < closest_distance && a != animal) { // TODO PREGUNTAR
				closest_animal = animal;
				closest_distance = distance;
			}
		}

		return closest_animal;
	}
}
