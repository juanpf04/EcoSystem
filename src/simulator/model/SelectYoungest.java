package simulator.model;

import java.util.List;
import java.util.Optional;

import simulator.misc.Messages;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (as == null)
			throw new IllegalArgumentException(Messages.INVALID_LIST);

		Optional<Animal> youngest = as.stream().min((a1, a2) -> {
			if (a1.get_age() > a2.get_age())
				return 1;
			else if (a1.get_age() == a2.get_age())
				return 0;
			else
				return -1;
		});
		return youngest.orElse(null);
//		Animal youngest = null;
//
//		if (!as.isEmpty()) {
//			youngest = as.get(0);
//
//			for (Animal animal : as)
//				if (animal.get_age() < youngest.get_age())
//					youngest = animal;
//		}
//
//		return youngest;
	}
}
