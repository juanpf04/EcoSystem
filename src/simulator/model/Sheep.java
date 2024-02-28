package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.view.Messages;

public class Sheep extends Animal {

	protected static final String GENETIC_CODE = Messages.SHEEP_GENETIC_CODE;
	protected static final Diet DIET = Diet.HERBIVORE;
	protected static final double INIT_SIGHT_RANGE = 40.0;
	protected static final double INIT_SPEED = 35.0;

	protected static final double MAX_AGE = 8.0;

	protected static final double ENERGY_COST = 20.0;
	protected static final double DESIRE_COST = 40.0;

	protected static final double FLEE_SPEED = 2.0;
	protected static final double OESTRUS_SPEED = 2.0;

	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super(GENETIC_CODE, DIET, INIT_SIGHT_RANGE, INIT_SPEED, mate_strategy, pos);

		if (danger_strategy == null)
			throw new IllegalArgumentException(Messages.INVALID_STRATEGY);

		this._danger_strategy = danger_strategy;
		this._danger_source = null;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);

		this._danger_strategy = p1._danger_strategy;
		this._danger_source = null;
	}

	@Override
	protected double max_age() {
		return MAX_AGE;
	}

	@Override
	protected void update_reference_animal() {
		this._danger_source = null;
	}

	@Override
	protected void update_normal(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		super.update_normal(dt);

		if (this._danger_source == null)
			this._danger_source = this._danger_strategy.select(this,
					this._region_mngr.get_animals_in_range(this, a -> a.get_diet() == Diet.CARNIVORE));

		if (this._danger_source != null)
			this._state = State.DANGER;
		else if (this._desire > UMBRAL_DESIRE)
			this._state = State.MATE;
	}

	@Override
	protected void update_danger(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		if (this._danger_source != null && !this._danger_source.is_alive())
			this._danger_source = null;

		if (this._danger_source == null)
			super.update_normal(dt);
		else {
			this._dest = this.get_position().plus(this.get_position().minus(_danger_source.get_position()).direction());

			this.move(
					FLEE_SPEED * this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

			this._age += dt;

			this._energy -= ENERGY_COST * this.get_state().get_energy_weighting() * dt;
			this.adjust_energy();

			this._desire += DESIRE_COST * dt;
			this.adjust_desire();
		}

		if (this._danger_source == null || !this._danger_source.in_sight_range(this)) {
			this._danger_source = this._danger_strategy.select(this,
					this._region_mngr.get_animals_in_range(this, a -> a.get_diet() == Diet.CARNIVORE));

			if (this._danger_source == null) {
				if (this._desire <= UMBRAL_DESIRE)
					this._state = State.NORMAL;
				else
					this._state = State.MATE;
			}
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
					this._baby = new Sheep(this, this._mate_target);

				this._mate_target = null;
			}

		if (this._danger_source == null)
			this._danger_source = this._danger_strategy.select(this,
					this._region_mngr.get_animals_in_range(this, a -> a.get_diet() == Diet.CARNIVORE));

		if (this._danger_source != null)
			this._state = State.DANGER;
		else if (this._desire <= UMBRAL_DESIRE)
			this._state = State.NORMAL;
	}

	@Override
	protected void update_hunger(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		throw new IllegalStateException(Messages.ILLEGAL_SHEEP_STATE);
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
	protected double sex_speed() {
		return OESTRUS_SPEED;
	}
}
