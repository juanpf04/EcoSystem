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
	protected static final double MIN_DISTANCE_TO_DESTINATION = 8.0;
	protected static final double SPEED_MULTIPLIER = 0.007;
	protected static final double ENERGY_COST = 20.0;
	protected static final double DESIRE_COST = 40.0;
	protected static final double UMBRAL_DESIRE = 65.0;
	protected static final double FLEE_SPEED = 2.0;
	protected static final double FLEE_ENERGY_COST = 1.2;
	protected static final double OESTRUS_SPEED = 2.0;
	protected static final double OESTRUS_ENERGY_COST = 1.2;

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
		switch(this.get_state()) {
		case NORMAL: 
			if(this.get_destination().distanceTo(this.get_position()) < MIN_DISTANCE_TO_DESTINATION)
				this.new_dest();
			this.move( _speed*dt*Math.exp((_energy-MAX_ENERGY)*SPEED_MULTIPLIER));
			this._age += dt;
			this._energy -= ENERGY_COST * dt;
			this.adjust_energy();
			this._desire += DESIRE_COST * dt;
			this.adjust_desire();
			
			if(this._danger_source == null)
				this._danger_source = this._danger_strategy.select(this, this._region_mngr.get_animals_in_range(this, null));// hacer
			
			if(this._danger_source != null) 
				this._state = State.DANGER;
			else if(this._desire > UMBRAL_DESIRE)
				this._state = State.MATE;
			
			break;
			
		case DANGER:
			if(this._danger_source != null && !this._danger_source.is_alive())
				this._danger_source = null;
			
			if(this._danger_source == null)
				this.move( _speed*dt*Math.exp((_energy-MAX_ENERGY)*SPEED_MULTIPLIER));
			else {
				this._dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
				this.move(  FLEE_SPEED*_speed*dt*Math.exp((_energy-MAX_ENERGY)*SPEED_MULTIPLIER));
				this._age += dt;
				this._energy -= ENERGY_COST * FLEE_ENERGY_COST * dt;
				this.adjust_energy();
				this._desire += DESIRE_COST * dt;
				this.adjust_desire();
				
			}
			
			if(this._danger_source == null || this._danger_source.distanceTo(this) <= this.get_sight_range()){
				this._danger_source = this._danger_strategy.select(this, this._region_mngr.get_animals_in_range(this, null));// hacer

				if(this._danger_source == null) {
					if(this._desire < UMBRAL_DESIRE)
						this._state = State.NORMAL;
					else this._state = State.MATE;
				}
			}
			
			break;
			
		case MATE:
			if(this._mate_target != null && !this._mate_target.is_alive() || this._danger_source.distanceTo(this) > this.get_sight_range() )
				this._mate_target = null;
			
			if(this._mate_target == null) 
				this._mate_target = this._mate_strategy.select(this, this._region_mngr.get_animals_in_range(this, null)); // hacer
			
			if(this._mate_target == null)
				this.move( _speed*dt*Math.exp((_energy-MAX_ENERGY)*SPEED_MULTIPLIER));
			else {
				this._dest =  this._mate_target.get_position();
				this.move(OESTRUS_SPEED*_speed*dt*Math.exp((_energy-MAX_ENERGY)*SPEED_MULTIPLIER));
				this._age += dt;
				this._energy -= ENERGY_COST * OESTRUS_ENERGY_COST * dt;
				this.adjust_energy();
				this._desire += DESIRE_COST * dt;
				this.adjust_desire();
				//2.6
			}
				
			
			break;
		case HUNGER:
			
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
