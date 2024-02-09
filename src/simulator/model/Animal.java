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
	 
	 protected Animal(String genetic_code, Diet diet, double sight_range,
			 double init_speed, SelectionStrategy mate_strategy, Vector2D pos) {
		 
		 // revisar excepciones
		 if(genetic_code.isEmpty() || sight_range < 0 || init_speed < 0 || mate_strategy == null)
			 throw new IllegalArgumentException(Messages.MENSAJE_PERSONALIZADO);
		 
		 this._genetic_code = genetic_code;
		 this._diet = diet;
		 this._sight_range = sight_range;
		 this._speed = Utils.get_randomized_parameter(init_speed, SPEED_TOLERANCE);
		 this._mate_strategy = mate_strategy;
		 this._pos = pos;
		 
		 this._state = State.NORMAL;
		 this._energy = MAX_ENERGY;
		 this._age = 0.0;	
		 this._desire = 0.0;
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
		 this._desire = 0.0;
		 this._genetic_code = p1.get_genetic_code();
		 this._diet = p1.get_diet();
		 this._energy = (p1.get_energy() + p2.get_energy())/2;
		 this._pos = p1.get_position().plus(Vector2D.get_random_vector(-1,1).scale(FACTOR*(Utils._rand
				 .nextGaussian()+1)));
		 this._sight_range = Utils.get_randomized_parameter((p1.get_sight_range()+p2.get_sight_range())/2, MUTATION_TOLERANCE);
		 this._speed = Utils.get_randomized_parameter((p1.get_speed()+p2.get_speed())/2, MUTATION_TOLERANCE);
	 }
	 
	 public void init(AnimalMapView reg_mngr) { 
		 this._region_mngr = reg_mngr;
		 
		 if(this.get_position() == null) 		// revisar si hay que poner  -1 o no
			 this._pos = new Vector2D(Utils._rand.nextDouble(0, this._region_mngr.get_width()), Utils._rand.nextDouble(0, this._region_mngr.get_height()));
		 else {			 
//			 terminar 
//			 Si _pos no es
//			 null hay que ajustarlo para que estÃ© dentro del mapa si es necesario (ver el apartado â€œAjustar
//					 posicionesâ€�).
		 }
			 
		 this._dest = new Vector2D(Utils._rand.nextDouble(0, this._region_mngr.get_width()), Utils._rand.nextDouble(0, this._region_mngr.get_height()));
	 }
	 
	 public Animal deliver_baby(){
		 Animal a = this._baby;	// cambiar
		 this._baby = null;		// si no esto hara que a sea null;
		 return a;
//		 devolver _baby y ponerlo a null. El simulador invocarÃ¡ a este mÃ©todo
//		 para que nazcan los animales
		 // return this._baby = null;
	 }
	 
	 protected void move(double speed) {
		 this._pos = this.get_position().plus(this.get_destination().minus(this.get_position()).direction().scale(speed));
	 }
	 
	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();
			
		jo.put(Messages.POSITION, this.get_position());
		jo.put(Messages.GENETIC_CODE, this.get_genetic_code());
		jo.put(Messages.DIET, this.get_diet());			
		jo.put(Messages.ANIMAL_STATE, this.get_state());
			
		return jo;
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

	public double distanceTo(Animal closest) {
		return this.get_position().distanceTo(closest.get_position());
	}
	
	public boolean is_alive() {
		return this.get_state() != State.DEAD;
	}
	 
}
