package simulator.model;

import simulator.view.Messages;

public class DefaultRegion extends Region {

	protected static final double FOOD = 60.0;
	protected static final double ANIMALS_MARGIN = 5.0;
	protected static final double ANIMALS_WEIGHTING = 2.0;

	@Override
	public void update(double dt) {
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);
		
		return a.get_diet().get_herbivorous_region_weighting() * FOOD
				* Math.exp(-Math.max(0, this.count(Diet.HERBIVORE) - ANIMALS_MARGIN) * ANIMALS_WEIGHTING) * dt;
	}
}
