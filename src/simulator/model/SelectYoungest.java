package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if(as.isEmpty())
				return null;
		//animal mas joven
		return null;
	}

}
