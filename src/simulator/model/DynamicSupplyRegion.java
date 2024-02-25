package simulator.model;

import simulator.misc.Utils;
import simulator.view.Messages;

public class DynamicSupplyRegion extends Region {

	private static final double PROBABILITY_OF_GROWTH = 0.5;

	private double _food;
	private double _factor;

	public DynamicSupplyRegion(double init_food, double factor) {
		super();

		if (init_food <= 0)
			throw new IllegalArgumentException(Messages.INVALID_INIT_FOOD);
		if (factor < 0)
			throw new IllegalArgumentException(Messages.INVALID_FACTOR);

		this._food = init_food;
		this._factor = factor;
	}

	@Override
	public void update(double dt) {
		if (Utils._rand.nextDouble() < PROBABILITY_OF_GROWTH)
			this._food += dt * this._factor;
	}

	@Override
	public double get_food(Animal a, double dt) {
		double f = 0.0;
		if (a.get_diet().equals(Diet.HERBIVORE))
			f = Math.min(this._food, FOOD
					* Math.exp(-Math.max(0, this.count(Diet.HERBIVORE) - NOMBRE_RECHULON) * NOMBRE_RECHULON2) * dt);

		this._food -= f;
		return f;
	}
}
