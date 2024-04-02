package simulator.model;

import java.util.List;

import simulator.misc.Messages;

public class SelectFirst implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (as == null)
			throw new IllegalArgumentException(Messages.INVALID_LIST);

		if (as.isEmpty())
			return null;

		return as.get(0);
	}
}
