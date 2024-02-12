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
	protected void update_according_to_state(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected double max_age() {
		return MAX_AGE;
	}
	
}
