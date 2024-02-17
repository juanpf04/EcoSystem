package simulator.model;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region {

	private double _food;
	private double _factor;

	public DynamicSupplyRegion(double init_food, double growth_factor) {
		super();
		this._food = init_food;
		this._factor = growth_factor;
	}

	@Override
	public void update(double dt) {
		if (Utils._rand.nextDouble() < 0.5)
			this._food += dt * this._factor;
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a.get_diet() == Diet.CARNIVORE)
			return 0.0;

		double f = Math.min(this._food,
				FOOD * Math.exp(-Math.max(0, this.count(Diet.HERBIVORE) - NOMBRE_RECHULON) * NOMBRE_RECHULON2) * dt);
		this._food -= f;
		return f;
	}
}
