package simulator.model;

import java.util.List;
import java.util.Optional;

import simulator.misc.Messages;

public class SelectFirst implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (as == null)
			throw new IllegalArgumentException(Messages.INVALID_LIST);

		Optional<Animal> first = as.stream().findFirst();
		return first.orElse(null);
//		Animal first = null;
//
//		if (!as.isEmpty())
//			first = as.get(0);
//
//		return first;
	}
}
