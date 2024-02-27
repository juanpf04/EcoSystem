package simulator.model;

public class DefaultRegion extends Region {

	protected static final double FOOD = 60.0;
	protected static final double ANIMALS_MARGIN = 5.0;
	protected static final double ANIMALS_WEIGHTING = 2.0;

	@Override
	public void update(double dt) {
	}

	@Override
	public double get_food(Animal a, double dt) {
		return a.get_diet().get_herbivorous_region_weighting() * FOOD
				* Math.exp(-Math.max(0, this.count(Diet.HERBIVORE) - ANIMALS_MARGIN) * ANIMALS_WEIGHTING) * dt;
	}
}
