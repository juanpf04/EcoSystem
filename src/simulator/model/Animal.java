package simulator.model;

import java.lang.IllegalArgumentException;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.view.Messages;

public abstract class Animal implements Entity, AnimalInfo {

	protected static final double SPEED_TOLERANCE = 0.1;
	protected static final double FACTOR = 60.0;
	protected static final double MUTATION_TOLERANCE = 0.2;
	protected static final double MIN_ENERGY = 0.0;
	protected static final double MAX_ENERGY = 100.0;
	protected static final double MIN_DESIRE = 0.0;
	protected static final double MAX_DESIRE = 100.0;

	protected static final double DESTINATION_RANGE = 8.0;
	protected static final double PROCREATION_RANGE = 8.0;
	protected static final double SPEED_MULTIPLIER = 0.007;
	protected static final double UMBRAL_DESIRE = 65.0;
	protected static final double PREGNANT_PROBABILITY = 0.9;

	protected String _genetic_code;
	protected Diet _diet;
	protected State _state;
	protected Vector2D _pos;
	protected Vector2D _dest;
	protected double _energy;
	protected double _speed;
	protected double _age;
	protected double _desire;
	protected double _sight_range;
	protected Animal _mate_target;
	protected Animal _baby;
	protected AnimalMapView _region_mngr;
	protected SelectionStrategy _mate_strategy;

	protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed,
			SelectionStrategy mate_strategy, Vector2D pos) {

		if (genetic_code == null || genetic_code.isBlank())
			throw new IllegalArgumentException(Messages.INVALID_GENETIC_CODE);
		if (sight_range <= 0)
			throw new IllegalArgumentException(Messages.INVALID_SIGHT_RANGE);
		if (init_speed <= 0)
			throw new IllegalArgumentException(Messages.INVALID_INIT_SPEED);
		if (mate_strategy == null)
			throw new IllegalArgumentException(Messages.INVALID_STRATEGY);

		this._genetic_code = genetic_code;
		this._diet = diet;
		this._sight_range = sight_range;
		this._speed = Utils.get_randomized_parameter(init_speed, SPEED_TOLERANCE);
		this._mate_strategy = mate_strategy;
		this._pos = pos;
		this._state = State.NORMAL;
		this._energy = MAX_ENERGY;
		this._age = 0.0;
		this._desire = MIN_DESIRE;
		this._dest = null;
		this._mate_target = null;
		this._baby = null;
		this._region_mngr = null;
	}

	protected Animal(Animal p1, Animal p2) {
		this._dest = null;
		this._baby = null;
		this._mate_target = null;
		this._region_mngr = null;
		this._state = State.NORMAL;
		this._desire = MIN_DESIRE;
		this._genetic_code = p1.get_genetic_code();
		this._diet = p1.get_diet();
		this._energy = (p1.get_energy() + p2.get_energy()) / 2;
		this._pos = p1.get_position()
				.plus(Vector2D.get_random_vector(-1, 1).scale(FACTOR * (Utils._rand.nextGaussian() + 1)));
		this._sight_range = Utils.get_randomized_parameter((p1.get_sight_range() + p2.get_sight_range()) / 2,
				MUTATION_TOLERANCE);
		this._speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed()) / 2, MUTATION_TOLERANCE);

		this._mate_strategy = p2._mate_strategy;
	}

	public void init(AnimalMapView reg_mngr) {
		this._region_mngr = reg_mngr;

		if (this.get_position() == null)
			this._pos = new Vector2D(Utils._rand.nextDouble(this._region_mngr.get_width()),
					Utils._rand.nextDouble(this._region_mngr.get_height()));
		else if (this.is_out())
			this.adjust_position();

		this.new_random_dest();
	}

	public Animal deliver_baby() {
		Animal baby = this._baby;
		this._baby = null;
		return baby;
	}

	protected void move(double speed) {
		this._pos = this.get_position()
				.plus(this.get_destination().minus(this.get_position()).direction().scale(speed));
	}

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();

		jo.put(Messages.POSITION_KEY, this.get_position().asJSONArray());
		jo.put(Messages.GENETIC_CODE_KEY, this.get_genetic_code());
		jo.put(Messages.DIET_KEY, this.get_diet().toString());
		jo.put(Messages.ANIMAL_STATE_KEY, this.get_state().toString());

		return jo;
	}

	@Override
	public void update(double dt) {
		if (this.is_alive()) {

			this.update_according_to_state(dt);

			if (this.is_out()) {
				this.adjust_position();
				this._state = State.NORMAL;
				this._mate_target = null;
				this.update_reference_animal();
			}

			if (this.get_state() == State.NORMAL) {
				this._mate_target = null;
				this.update_reference_animal();
			} else if (this.get_state() == State.MATE)
				this.update_reference_animal();
			else if (this.get_state() == State.HUNGER || this.get_state() == State.DANGER)
				this._mate_target = null;

			if (this.get_energy() == MIN_ENERGY || this.get_age() > this.max_age())
				this._state = State.DEAD;

			if (this.is_alive()) {
				this._energy += this._region_mngr.get_food(this, dt);
				this.adjust_energy();
			}
		}
	}

	protected void update_normal(double dt) {
		if (this.get_destination().distanceTo(this.get_position()) < DESTINATION_RANGE)
			this.new_random_dest();

		this.move(this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

		this._age += dt;

		this._energy -= this.energy_cost() * dt;
		this.adjust_energy();

		this._desire += this.desire_cost() * dt;
		this.adjust_desire();
	}

	protected void update_danger(double dt) {
	}

	protected void update_dead(double dt) {
	}

	protected void update_hunger(double dt) {
	}

	protected void update_mate(double dt) {
		if (this._mate_target != null)
			if (!this._mate_target.is_alive() || this._mate_target.distanceTo(this) > this.get_sight_range())
				this._mate_target = null;

		if (this._mate_target == null)
			this._mate_target = this._mate_strategy.select(this,
					this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() == a.get_genetic_code()));

		if (this._mate_target == null)
			this.move(this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));
		else {
			this._dest = this._mate_target.get_position();

			this.move(this.sex_speed() * this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER));

			this._age += dt;

			this._energy -= this.energy_cost() * this.get_state().get_energy_weighting() * dt;
			this.adjust_energy();

			this._desire += this.desire_cost() * dt;
			this.adjust_desire();
		}
	}

	/*s
	 * EJEMPLOS LAMBDA FUNCION PREDICATE 1. (Animal a) -> {return
	 * this.get_genetic_code() == a.get_genetic_code();}
	 * 
	 * 2. (Animal a) -> this.get_genetic_code() == a.get_genetic_code()
	 * 
	 * 3. new Predicate<Animal>() {
	 * 
	 * @Override public boolean test(Animal t) { return get_genetic_code() ==
	 * t.get_genetic_code(); }
	 * 
	 * });
	 *
	 * 4. a -> this.get_genetic_code() == a.get_genetic_code()
	 */

	private void update_according_to_state(double dt) {
		switch (this.get_state()) {
		case NORMAL:
			this.update_normal(dt);
			break;
		case DANGER:
			this.update_danger(dt);
			break;
		case DEAD:
			this.update_dead(dt);
			break;
		case HUNGER:
			this.update_hunger(dt);
			break;
		case MATE:
			this.update_mate(dt);
			break;
		default:
			break;
		}
	}
	
	protected abstract void update_reference_animal();

	protected abstract double max_age();

	protected abstract double energy_cost();

	protected abstract double desire_cost();
	
	protected abstract double sex_speed();

	@Override
	public State get_state() {
		return this._state;
	}

	@Override
	public Vector2D get_position() {
		return this._pos;
	}

	@Override
	public String get_genetic_code() {
		return this._genetic_code;
	}

	@Override
	public Diet get_diet() {
		return this._diet;
	}

	@Override
	public double get_speed() {
		return this._speed;
	}

	@Override
	public double get_sight_range() {
		return this._sight_range;
	}

	@Override
	public double get_energy() {
		return this._energy;
	}

	@Override
	public double get_age() {
		return this._age;
	}

	@Override
	public Vector2D get_destination() {
		return this._dest;
	}

	@Override
	public boolean is_pregnant() {
		return this._baby != null;
	}

	public double distanceTo(Animal a) {
		return this.get_position().distanceTo(a.get_position());
	}

	@Override
	public boolean is_alive() {
		return this.get_state() != State.DEAD;
	}

	protected void reset_desire() {
		this._desire = MIN_DESIRE;
	}

	protected void new_random_dest() {
		this._dest = new Vector2D(Utils._rand.nextDouble(this._region_mngr.get_width()),
				Utils._rand.nextDouble(this._region_mngr.get_height()));
	}

	@Override
	public boolean is_out() {
		return this.get_position().getX() < 0 || this.get_position().getX() >= this._region_mngr.get_width()
				|| this.get_position().getY() < 0 || this.get_position().getY() >= this._region_mngr.get_height();
	}

	protected void adjust_position() {
		while (this.get_position().getX() >= this._region_mngr.get_width())
			this._pos = this.get_position().minus(new Vector2D(this._region_mngr.get_width(), 0));
		while (this.get_position().getX() < 0)
			this._pos = this.get_position().plus(new Vector2D(this._region_mngr.get_width(), 0));
		while (this.get_position().getY() >= this._region_mngr.get_height())
			this._pos = this.get_position().minus(new Vector2D(0, this._region_mngr.get_height()));
		while (this.get_position().getY() < 0)
			this._pos = this.get_position().plus(new Vector2D(0, this._region_mngr.get_height()));
	}
	
	protected void adjust_energy() {
		if (this.get_energy() < MIN_ENERGY)
			this._energy = MIN_ENERGY;
		if (this.get_energy() > MAX_ENERGY)
			this._energy = MAX_ENERGY;
	}

	protected void adjust_desire() {
		if (this._desire < MIN_DESIRE)
			this._desire = MIN_DESIRE;
		if (this._desire > MAX_DESIRE)
			this._desire = MAX_DESIRE;
	}

	public boolean in_sight_range(Animal a) {
		return this.distanceTo(a) <= a.get_sight_range();
	}

	public void die() {
		this._state = State.DEAD;
	}
}
