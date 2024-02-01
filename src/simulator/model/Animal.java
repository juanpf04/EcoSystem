package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {
	
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
			 double init_speed, SelectionStrategy mate_strategy, Vector2D pos) { // lanzar excepciones, pagina 5
		 
		 this._genetic_code = genetic_code;
		 this._diet = diet;
		 this._sight_range = sight_range;
		 this._speed = Utils.get_randomized_parameter(init_speed, 0.1);
		 this._mate_strategy = mate_strategy;
		 this._pos = pos;
		 
		 this._state = State.NORMAL;
		 this._energy = 100.0;
		 this._desire = 0.0; 
		 this._dest = null;
		 this._mate_target = null;
		 this._baby = null;
		 this._region_mngr = null;
	 }

}
