package simulator.model;

import java.util.List;
import java.util.Optional;

import simulator.misc.Messages;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (as == null)
			throw new IllegalArgumentException(Messages.INVALID_LIST);

		Optional<Animal> closest = as.stream().min((a1, a2) -> {
			if (a1.distanceTo(a) > a2.distanceTo(a))
				return 1;
			else if (a1.distanceTo(a) == a2.distanceTo(a))
				return 0;
			else
				return -1;
		});
		return closest.orElse(null);
//		Animal closest = null;
//
//		if (!as.isEmpty()) {
//
//			closest = as.get(0);
//
//			for (Animal animal : as)
//				if (a.distanceTo(animal) < a.distanceTo(closest))
//					closest = animal;
//		}
//
//		return closest;
	}
}
