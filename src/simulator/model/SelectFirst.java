package simulator.model;

import java.util.List;

public class SelectFirst implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty())
			return null;
		if (a != as.get(0)) // TODO PREGUNTAR
			return as.get(0);
		else if (as.size() > 1)
			return as.get(1);
		else return null;
	}
}
