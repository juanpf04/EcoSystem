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
	protected static final double DESTINATION_RANGE = 8.0;
	protected static final double PROCREATION_RANGE = 8.0;
	protected static final double SPEED_MULTIPLIER = 0.007;
	protected static final double ENERGY_COST = 18.0;
	protected static final double DESIRE_COST = 30.0;
	protected static final double UMBRAL_DESIRE = 65.0;
	protected static final double UMBRAL_ENERGY = 50.0;
	protected static final double HUNT_SPEED = 3.0;
	protected static final double HUNT_ENERGY_COST = 1.2;
	protected static final double OESTRUS_SPEED = 3.0;
	protected static final double OESTRUS_ENERGY_COST = 1.2;
	protected static final double PREGNANT_PROBABILITY = 0.9;
	protected static final double ENERGY_TO_SUBTRACT = 10.0;

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
	protected void update_according_to_state(double dt) {
		switch (this.get_state()) {
		case NORMAL:

			if (this.get_destination().distanceTo(this.get_position()) <= DESTINATION_RANGE)
				this.new_random_dest();

			this.move(this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

			this._age += dt;

			this._energy -= ENERGY_COST * dt;
			this.adjust_energy();

			this._desire += DESIRE_COST * dt;
			this.adjust_desire();

			if (this._energy < UMBRAL_ENERGY)
				this._state = State.HUNGER;

			else if (this._desire > UMBRAL_DESIRE)
				this._state = State.MATE;

			break;
		case HUNGER:

			if (this._hunt_target == null || (this._hunt_target != null && !this._hunt_target.is_alive())
					|| this._hunt_target.distanceTo(this) > this.get_sight_range())
				this._hunt_target = this._hunting_strategy.select(this,
						this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() != a.get_genetic_code()));

			if (this._hunt_target == null)
				this.move(this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));
			else {
				this._dest = _hunt_target.get_position();

				this.move(HUNT_SPEED * this.get_speed() * dt
						* Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

				this._age += dt;

				this._energy -= ENERGY_COST * HUNT_ENERGY_COST * dt;
				this.adjust_energy();

				this._desire += DESIRE_COST * dt;
				this.adjust_desire();

			if (this._hunt_target.get_destination().distanceTo(this.get_position()) < DESTINATION_RANGE) {
				this._hunt_target._state = State.DEAD;
				this._hunt_target = null;
				this._energy += UMBRAL_ENERGY;
				this.adjust_energy();
			}
			}

			if (this._energy >= UMBRAL_ENERGY) {
				if (this._desire <= UMBRAL_DESIRE)
					this._state = State.NORMAL;
				else
					this._state = State.MATE;
			}

			break;
		case MATE:

			if (this._mate_target != null)
				if(!this._mate_target.is_alive()
					|| this._mate_target.distanceTo(this) > this.get_sight_range())
					this._mate_target = null;

			if (this._mate_target == null)
				this._mate_target = this._mate_strategy.select(this,
						this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() == a.get_genetic_code())); 

			if (this._mate_target == null)
				this.move(this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));
			else {
				this._dest = _mate_target.get_position();

				this.move(OESTRUS_SPEED * _speed * dt * Math.exp((_energy - MAX_ENERGY) * SPEED_MULTIPLIER));

				this._age += dt;

				this._energy -= ENERGY_COST * OESTRUS_ENERGY_COST * dt;
				this.adjust_energy();

				this._desire += DESIRE_COST * dt;
				this.adjust_desire();

				if (this.distanceTo(this._mate_target) <= PROCREATION_RANGE) {
					this.reset_desire();
					this._mate_target.reset_desire();

					if (!this.is_pregnant() && Utils._rand.nextDouble() <= PREGNANT_PROBABILITY)
						this._baby = new Wolf(this, this._mate_target);

					this._energy -= ENERGY_TO_SUBTRACT;
					this._mate_target = null;
				}

			}
			if (this._energy < UMBRAL_ENERGY)
				this._state = State.HUNGER;
			else if (this._desire < UMBRAL_DESIRE)
				this._state = State.NORMAL;

			break;
		default:
			break;
		}
	}

	@Override
	protected double max_age() {
		return MAX_AGE;
	}

	@Override
	protected void update() {
		this._hunt_target = null;
	}

}
