package simulator.model;

import java.util.function.Predicate;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.view.Messages;

public class Sheep extends Animal {

	protected static final String GENETIC_CODE = Messages.SHEEP_GENETIC_CODE;
	protected static final Diet DIET = Diet.HERBIVORE;
	protected static final double INIT_SIGHT_RANGE = 40.0;
	protected static final double INIT_SPEED = 35.0;
	protected static final double MAX_AGE = 8.0;
	protected static final double DESTINATION_RANGE = 8.0;
	protected static final double PROCREATION_RANGE = 8.0;
	protected static final double SPEED_MULTIPLIER = 0.007;
	protected static final double ENERGY_COST = 20.0;
	protected static final double DESIRE_COST = 40.0;
	protected static final double UMBRAL_DESIRE = 65.0;
	protected static final double FLEE_SPEED = 2.0;
	protected static final double FLEE_ENERGY_COST = 1.2;
	protected static final double OESTRUS_SPEED = 2.0;
	protected static final double OESTRUS_ENERGY_COST = 1.2;
	protected static final double PREGNANT_PROBABILITY = 0.9;

	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super(GENETIC_CODE, DIET, INIT_SIGHT_RANGE, INIT_SPEED, mate_strategy, pos);

		if (danger_strategy == null)
			throw new IllegalArgumentException(Messages.INVALID_DANGER_STRATEGY);

		this._danger_strategy = danger_strategy;
		this._danger_source = null;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);

		this._danger_strategy = p1._danger_strategy;
		this._danger_source = null;
	}
	
	/*
	 * EJEMPLOS LAMDA FUNCION PREDICATE
	 * 1.
	 * (Animal a) -> {return this.get_genetic_code() == a.get_genetic_code();}
	 * 
	 * 2.
	 * (Animal a) -> this.get_genetic_code() == a.get_genetic_code()
	 * 
	 * 3.
	 * new Predicate<Animal>() {

				@Override
				public boolean test(Animal t) {
					return get_genetic_code() == t.get_genetic_code();
				}
			
			});
	 *
	 * 4.
	 * a -> this.get_genetic_code() == a.get_genetic_code()
	 */
	

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

			if (this._danger_source == null)
				this._danger_source = this._danger_strategy.select(this,
						this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() != a.get_genetic_code()));

			if (this._danger_source != null)
				this._state = State.DANGER;
			else if (this._desire > UMBRAL_DESIRE)
				this._state = State.MATE;

			break;
		case DANGER:

			if (this._danger_source != null && !this._danger_source.is_alive())
				this._danger_source = null;

			if (this._danger_source == null)
				this.move(this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));
			else {
				this._dest = this.get_position()
						.plus(this.get_position().minus(_danger_source.get_position()).direction());

				this.move(FLEE_SPEED * this.get_speed() * dt
						* Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

				this._age += dt;

				this._energy -= ENERGY_COST * FLEE_ENERGY_COST * dt;
				this.adjust_energy();

				this._desire += DESIRE_COST * dt;
				this.adjust_desire();
			}

			if (this._danger_source == null || this._danger_source.distanceTo(this) <= this.get_sight_range()) {
				this._danger_source = this._danger_strategy.select(this,
						this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() != a.get_genetic_code()));

				if (this._danger_source == null) {
					if (this._desire <= UMBRAL_DESIRE)
						this._state = State.NORMAL;
					else
						this._state = State.MATE;
				}
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
				this._dest = this._mate_target.get_position();

				this.move(OESTRUS_SPEED * _speed * dt * Math.exp((_energy - MAX_ENERGY) * SPEED_MULTIPLIER));

				this._age += dt;

				this._energy -= ENERGY_COST * OESTRUS_ENERGY_COST * dt;
				this.adjust_energy();

				this._desire += DESIRE_COST * dt;
				this.adjust_desire();

				if (this.distanceTo(this._mate_target) <= PROCREATION_RANGE) {
					this.reset_desire();

					if (!this.is_pregnant() && Utils._rand.nextDouble() <= PREGNANT_PROBABILITY)
						this._baby = new Sheep(this, this._mate_target);

					this._mate_target = null;
				}

			}
			if (this._danger_source == null)
				this._danger_source = this._danger_strategy.select(this,
						this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() != a.get_genetic_code()));

			if (this._danger_source != null)
				this._state = State.DANGER;
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
		this._danger_source = null;
	}

}
