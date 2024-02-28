package simulator.model;

import java.util.List;

import simulator.view.Messages;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (as == null)
			throw new IllegalArgumentException(Messages.INVALID_LIST);
		
		if (as.isEmpty())
			return null;

		Animal youngest = as.get(0);

		for (Animal animal : as)
			if (animal.get_age() < youngest.get_age())
				youngest = animal;

		return youngest;
	}
}
