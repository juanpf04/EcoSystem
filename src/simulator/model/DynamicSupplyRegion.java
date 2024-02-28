package simulator.model;

import simulator.misc.Utils;
import simulator.view.Messages;

public class DynamicSupplyRegion extends DefaultRegion {

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
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		if (Utils._rand.nextDouble() < PROBABILITY_OF_GROWTH)
			this._food += dt * this._factor;
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		double food = Math.min(_food, super.get_food(a, dt));
		this._food -= food;
		return food;
	}
}
