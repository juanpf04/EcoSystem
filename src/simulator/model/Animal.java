package simulator.model;

import java.lang.IllegalArgumentException;

import org.json.JSONObject;

import simulator.misc.Messages;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {

	private static final double SPEED_TOLERANCE = 0.1;
	private static final double MUTATION_TOLERANCE = 0.2;
	private static final double FACTOR = 60.0;
	private static final double ACTION_RANGE = 8.0;
	private static final double SPEED_MULTIPLIER = 0.007;
	private static final double HEAT_DESIRE = 65.0;
	private static final double PREGNANT_PROBABILITY = 0.9;

	protected static final double MIN_ENERGY = 0.0;
	protected static final double MAX_ENERGY = 100.0;
	protected static final double MIN_DESIRE = 0.0;
	protected static final double MAX_DESIRE = 100.0;

	protected String _genetic_code;

	public static enum Diet {
		HERBIVORE(1.0, 0.0), CARNIVORE(0.0, 1.0);

		private double _herbivorous_region_weighting;
		private double _carnivorous_region_weighting;

		Diet(double herbivorous_region_weighting, double carnivorous_region_weighting) {
			this._herbivorous_region_weighting = herbivorous_region_weighting;
			this._carnivorous_region_weighting = carnivorous_region_weighting;
		}

		public double get_herbivorous_region_weighting() {
			return this._herbivorous_region_weighting;
		}

		public double get_carnivorous_region_weighting() {
			return this._carnivorous_region_weighting;
		}
	}

	protected Diet _diet;

	public static enum State {
		NORMAL(1.0), MATE(1.2), HUNGER(1.2), DANGER(1.2), DEAD(0.0);

		private double _energy_weighting;

		State(double energy_weighting) {
			this._energy_weighting = energy_weighting;
		}

		public double get_energy_weighting() {
			return _energy_weighting;
		}
	}

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
		if (diet == null)
			throw new IllegalArgumentException(Messages.INVALID_DIET);

		this._genetic_code = genetic_code;
		this._diet = diet;
		this._sight_range = sight_range;
		this._speed = Utils.get_randomized_parameter(init_speed, SPEED_TOLERANCE);
		this._mate_strategy = mate_strategy;
		this._pos = pos;
		this.set_normal();
		this._energy = MAX_ENERGY;
		this._age = 0.0;
		this.reset_desire();
		this._dest = null;
		this._mate_target = null;
		this._baby = null;
		this._region_mngr = null;
	}

	protected Animal(Animal p1, Animal p2) {
		if (p1 == null || p2 == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		this._age = 0.0;
		this._dest = null;
		this._baby = null;
		this._mate_target = null;
		this._region_mngr = null;
		this.set_normal();
		this.reset_desire();
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
		if (reg_mngr == null)
			throw new IllegalArgumentException(Messages.INVALID_REGION_MANAGER);

		this._region_mngr = reg_mngr;

		if (this.get_position() == null)
			this._pos = this.random_position();
		else if (this.is_out())
			this.adjust_position();

		this._dest = this.random_position();
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

	// Entity

	@Override
	public void update(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		if (this.is_alive()) {

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

			if (this.is_out()) {
				this.adjust_position();
				this.set_normal();
			}

			if (this.get_energy() == MIN_ENERGY || this.get_age() > this.max_age())
				this.set_dead();

			if (this.is_alive()) {
				this.update_energy(this._region_mngr.get_food(this, dt));
			}
		}
	}

	// AnimalInfo

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
	public double get_speed(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		return this.get_speed() * dt * Math.exp((this.get_energy() - MAX_ENERGY) * SPEED_MULTIPLIER);
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
	public double get_desire() {
		return this._desire;
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

	@Override
	public boolean can_pregnant() {
		return Utils._rand.nextDouble() < PREGNANT_PROBABILITY;
	}

	@Override
	public boolean on_heat() {
		return this.get_desire() > HEAT_DESIRE;
	}

	@Override
	public boolean is_alive() {
		return !this.dead();
	}

	@Override
	public boolean is_out() {
		double x = this.get_position().getX();
		double y = this.get_position().getY();

		return x < 0 || x >= this._region_mngr.get_width() || y < 0 || y >= this._region_mngr.get_height();
	}

	@Override
	public boolean normal() {
		return this.get_state() == State.NORMAL;
	}

	@Override
	public boolean mate() {
		return this.get_state() == State.MATE;
	}

	@Override
	public boolean danger() {
		return this.get_state() == State.DANGER;
	}

	@Override
	public boolean hunger() {
		return this.get_state() == State.HUNGER;
	}

	@Override
	public boolean dead() {
		return this.get_state() == State.DEAD;
	}

	@Override
	public boolean carnivore() {
		return this.get_diet() == Diet.CARNIVORE;
	}

	@Override
	public boolean herbivore() {
		return this.get_diet() == Diet.HERBIVORE;
	}

	// JSONable

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();

		jo.put(Messages.POSITION_KEY, this.get_position().asJSONArray());
		jo.put(Messages.GENETIC_CODE_KEY, this.get_genetic_code());
		jo.put(Messages.DIET_KEY, this.get_diet().toString());
		jo.put(Messages.ANIMAL_STATE_KEY, this.get_state().toString());

		return jo;
	}

	// Auxiliary

	protected void advance(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		if (this.in_action_range(this.get_destination()))
			this._dest = this.random_position();

		this.move(this.get_speed(dt));

		this.grow(dt);

		this.update_energy(this.energy_cost() * dt);

		this.update_desire(this.desire_cost() * dt);
	}

	protected void update_status(double dt, double speed) {
		this.move(speed * this.get_speed(dt));

		this.grow(dt);

		this.update_energy(this.get_state().get_energy_weighting() * this.energy_cost() * dt);

		this.update_desire(this.desire_cost() * dt);
	}

	protected void update_normal(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		this.advance(dt);
	}

	protected void update_mate(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		if (this._mate_target != null)
			if (this._mate_target.dead() || !this._mate_target.in_sight_range(this))
				this._mate_target = null;

		if (this._mate_target == null)
			this._mate_target = this._mate_strategy.select(this,
					this._region_mngr.get_animals_in_range(this, a -> this.get_genetic_code() == a.get_genetic_code()));

		if (this._mate_target == null)
			this.advance(dt);
		else {
			this._dest = this._mate_target.get_position();

			this.update_status(dt, this.mate_speed());
		}
	}

	protected void update_danger(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);
	}

	protected void update_hunger(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);
	}

	protected void update_dead(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);
	}

	protected abstract void update_reference_animal();

	protected void update_energy(double energy) {
		this._energy = Utils.constrain_value_in_range(this.get_energy() + energy, MIN_ENERGY, MAX_ENERGY);
	}

	protected void update_desire(double desire) {
		this._desire = Utils.constrain_value_in_range(this.get_desire() + desire, MIN_DESIRE, MAX_DESIRE);
	}

	protected void reset_desire() {
		this._desire = MIN_DESIRE;
	}

	protected void grow(double dt) {
		this._age += dt;
	}

	protected abstract double max_age();

	protected abstract double energy_cost();

	protected abstract double desire_cost();

	protected abstract double mate_speed();

	public double distanceTo(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		return this.get_position().distanceTo(a.get_position());
	}

	public boolean in_sight_range(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		return this.distanceTo(a) <= a.get_sight_range();
	}

	protected boolean in_action_range(Vector2D pos) {
		return this.get_position().distanceTo(pos) < ACTION_RANGE;
	}

	protected boolean in_action_range(Animal a) {
		return this.in_action_range(a.get_position());
	}

	protected Vector2D random_position() {
		return new Vector2D(Utils._rand.nextDouble(this._region_mngr.get_width()),
				Utils._rand.nextDouble(this._region_mngr.get_height()));
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

	protected void set_dead() {
		this._state = State.DEAD;
	}

	protected void set_normal() {
		this._state = State.NORMAL;
		this._mate_target = null;
		this.update_reference_animal();
	}

	protected void set_mate() {
		this._state = State.MATE;
		this.update_reference_animal();
	}

	protected void set_danger() {
		this._state = State.DANGER;
		this._mate_target = null;
	}

	protected void set_hunger() {
		this._state = State.HUNGER;
		this._mate_target = null;
	}

	@Override
	public String toString() {
		return this.get_genetic_code();
	}

}
