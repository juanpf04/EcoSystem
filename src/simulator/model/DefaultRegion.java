package simulator.model;

public class DefaultRegion extends Region {

	protected static final double FOOD = 60.0;
	protected static final double NOMBRE_RECHULON = 5.0;
	protected static final double NOMBRE_RECHULON2 = 2.0;

	@Override
	public void update(double dt) {
	}

	@Override
	public double get_food(Animal a, double dt) {
		return a.get_diet().get_herbivorous_region_weighting() * FOOD
				* Math.exp(-Math.max(0, this.count(Diet.HERBIVORE) - NOMBRE_RECHULON) * NOMBRE_RECHULON2) * dt;
	}
}
