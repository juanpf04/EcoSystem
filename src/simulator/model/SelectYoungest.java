package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty())
			return null;

		Animal youngest_animal = as.get(0);

		for (int i = 1; i < as.size(); i++) {
			Animal animal = as.get(i);
			if (animal.get_age() < youngest_animal.get_age())
				youngest_animal = animal;
		}

		return youngest_animal;
	}
}
