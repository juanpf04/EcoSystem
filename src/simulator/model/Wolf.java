package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.view.Messages;

public class Wolf extends Animal {

	protected static final String GENETIC_CODE = Messages.WOLF_GENETIC_CODE;
	protected static final Diet DIET = Diet.CARNIVORE;
	protected static final double INIT_SIGHT_RANGE = 50.0;
	protected static final double INIT_SPEED = 60.0;
	protected static final double MAX_AGE = 14.0;

	protected static final double ENERGY_COST = -18.0;
	protected static final double DESIRE_COST = 30.0;

	protected static final double HUNT_ENERGY = 50.0;
	protected static final double HUNT_SPEED = 3.0;
	protected static final double MATE_SPEED = 3.0;

	protected static final double MATING_COST = -10.0;

	private Animal _hunt_target;
	private SelectionStrategy _hunting_strategy;

	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) {
		super(GENETIC_CODE, DIET, INIT_SIGHT_RANGE, INIT_SPEED, mate_strategy, pos);

		if (hunting_strategy == null)
			throw new IllegalArgumentException(Messages.INVALID_STRATEGY);

		this._hunting_strategy = hunting_strategy;
		this._hunt_target = null;
	}

	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);

		this._hunting_strategy = p1._hunting_strategy;
		this._hunt_target = null;
	}

	@Override
	protected double max_age() {
		return MAX_AGE;
	}

	@Override
	protected void update_reference_animal() {
		this._hunt_target = null;
	}

	@Override
	protected void update_normal(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		super.update_normal(dt);

		if (this._energy < HUNT_ENERGY)
			this._state = State.HUNGER;
		else if (this._desire > HEAT_DESIRE)
			this._state = State.MATE;
	}

	@Override
	protected void update_hunger(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		super.update_hunger(dt);

		if (this._hunt_target == null || !this._hunt_target.is_alive() || !this._hunt_target.in_sight_range(this))
			this._hunt_target = this._hunting_strategy.select(this,
					this._region_mngr.get_animals_in_range(this, a -> a.get_diet() == Diet.HERBIVORE));

		if (this._hunt_target == null)
			super.advance(dt);
		else {
			this._dest = this._hunt_target.get_position();

			this.move(
					HUNT_SPEED * this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

			this.grow(dt);

			this.update_energy(this.energy_cost() * this.get_state().get_energy_weighting() * dt);

			this.update_desire(this.desire_cost() * dt);

			if (this._hunt_target.distanceTo(this) < ACTION_RANGE) {
				this._hunt_target.set_dead();
				this._hunt_target = null;
				this.update_energy(HUNT_ENERGY);
			}
		}

		if (this.get_energy() > HUNT_ENERGY) {
			if (this._desire < HEAT_DESIRE)
				this.set_normal();
			else
				this.set_mate();
		}
	}

	@Override
	protected void update_mate(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		super.update_mate(dt);

		if (this._mate_target != null)
			if (this.distanceTo(this._mate_target) < ACTION_RANGE) {
				this.reset_desire();
				this._mate_target.reset_desire();

				if (!this.is_pregnant() && Utils._rand.nextDouble() < PREGNANT_PROBABILITY)
					this._baby = new Wolf(this, this._mate_target);

				this.update_energy(MATING_COST);
				this._mate_target = null;
			}

		if (this._energy < HUNT_ENERGY)
			this._state = State.HUNGER;
		else if (this._desire < HEAT_DESIRE)
			this._state = State.NORMAL;
	}

	@Override
	protected void update_danger(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		throw new IllegalStateException(Messages.illegal_state(this.get_genetic_code(), this.get_state()));
	}

	@Override
	protected double energy_cost() {
		return ENERGY_COST;
	}

	@Override
	protected double desire_cost() {
		return DESIRE_COST;
	}

	@Override
	protected double mate_speed() {
		return MATE_SPEED;
	}

}
