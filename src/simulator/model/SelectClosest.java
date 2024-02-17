package simulator.model;

import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty())
			return null;

		Animal closest_animal = as.get(0);
		double closest_distance = a.distanceTo(closest_animal);

		for (int i = 1; i < as.size(); i++) {
			Animal animal = as.get(i);
			double distance = a.distanceTo(animal);
			if (distance < closest_distance) {
				closest_animal = animal;
				closest_distance = distance;
			}
		}

		return closest_animal;
	}
}
