package simulator.model;

import java.util.Arrays;
import java.util.List;

import simulator.misc.Vector2D;

public class Wolf extends Animal {

	protected static final String GENETIC_CODE = "Wolf";
	protected static final Diet DIET = Diet.CARNIVORE;
	protected static final List<State> ALLOWED_STATES = Arrays.asList(
			State.NORMAL,
			State.MATE,
			State.HUNGER,
			State.DANGER,
			State.DEAD
	);
	protected static final double INIT_SIGHT_RANGE = 50.0;
	protected static final double INIT_SPEED = 60.0;
	protected static final double MAX_AGE = 14.0;
	
	private Animal _hunt_target;
	private SelectionStrategy _hunting_strategy;
	
	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy,
			Vector2D pos) {
		super(GENETIC_CODE, DIET, INIT_SIGHT_RANGE,INIT_SPEED, mate_strategy, pos);
		
		this._hunt_target = null;
		this._hunting_strategy = hunting_strategy;
	}
	
	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		
		this._hunt_target = null;
		this._hunting_strategy = p1._hunting_strategy;
	}

	@Override
	public void update(double dt) {
		
	}

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
	
}
