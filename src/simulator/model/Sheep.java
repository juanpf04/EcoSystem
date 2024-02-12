package simulator.model;

import java.util.Arrays;
import java.util.List;

import simulator.misc.Vector2D;

public class Sheep extends Animal {

	// revisar
	protected static final String GENETIC_CODE = "Sheep";
	protected static final Diet DIET = Diet.HERBIVORE;
	protected static final List<State> ALLOWED_STATES = Arrays.asList(
			State.NORMAL,
			State.MATE,
			State.DANGER,
			State.DEAD
	);
	protected static final double INIT_SIGHT_RANGE = 40.0;
	protected static final double INIT_SPEED = 35.0;
	protected static final double MAX_AGE = 8.0;

	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super(GENETIC_CODE, DIET, INIT_SIGHT_RANGE,INIT_SPEED, mate_strategy, pos);
		
		this._danger_source = null;
		this._danger_strategy = danger_strategy;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		
		this._danger_source = null;
		this._danger_strategy = p1._danger_strategy;
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
