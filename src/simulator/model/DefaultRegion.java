package simulator.model;

import simulator.misc.Messages;

public class DefaultRegion extends Region {

	private static final double FOOD = 60.0;
	private static final double ANIMALS_MARGIN = 5.0;
	private static final double ANIMALS_WEIGHTING = 2.0;

	@Override
	public void update(double dt) {
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

//		return a.get_diet().get_herbivorous_region_weighting() * FOOD * dt
//				* Math.exp(-Math.max(0, this.get_animals(animal -> animal.herbivore()).size() - ANIMALS_MARGIN)
//						* ANIMALS_WEIGHTING);
		return a.get_diet().get_herbivorous_region_weighting() * FOOD * dt
				* Math.exp(-Math.max(0, this.getAnimals().stream().filter((a1)->a1.herbivore()).count() - ANIMALS_MARGIN)
						* ANIMALS_WEIGHTING);
	}

	@Override
	public String toString() {
		return Messages.DEFAULT_REGION_NAME;
	}
}
