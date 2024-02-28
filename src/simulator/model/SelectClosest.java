package simulator.model;

import java.util.List;

import simulator.view.Messages;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (as == null)
			throw new IllegalArgumentException(Messages.INVALID_LIST);

		if (as.isEmpty())
			return null;

		Animal closest = as.get(0);

		for (Animal animal : as)
			if (a.distanceTo(animal) < a.distanceTo(closest))
				closest = animal;

		return closest;
	}
}
