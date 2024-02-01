package simulator.model;

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
			 double init_speed, SelectionStrategy mate_strategy, Vector2D pos) {
		 
		 this._genetic_code = genetic_code;
		 this._diet = diet;
		 this._sight_range = sight_range;
		 this._speed = init_speed;
		 this._mate_strategy = mate_strategy;
		 this._pos = pos;
		 
		// resto de atributos 
	 }

}
