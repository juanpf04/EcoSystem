package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty())
			return null;

		Animal youngest_animal = as.get(0);

		for (Animal animal : as)
			if (animal.get_age() < youngest_animal.get_age() && a != animal) // TODO PREGUNTAR
				youngest_animal = animal;

		return youngest_animal;
	}
}
