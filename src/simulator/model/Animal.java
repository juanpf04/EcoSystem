package simulator.model;

import java.lang.IllegalArgumentException;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.view.Messages;

public abstract class Animal implements Entity, AnimalInfo {
	
	// revisar nombres
	protected static final double ENERGY = 100.0;
	protected static final double SPEED_TOLERANCE = 0.1;
	protected static final double FACTOR = 60.0;
	protected static final double MUTATION_TOLERANCE = 0.2;
	
	
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
		 this._energy = ENERGY;
		 
		 // revisar inicialización de age
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
		 this._genetic_code = p1._genetic_code;
		 this._diet = p1._diet;
		 this._energy = (p1._energy + p2._energy)/2;
		 this._pos = p1.get_position().plus(Vector2D.get_random_vector(-1,1).scale(FACTOR*(Utils._rand
				 .nextGaussian()+1)));
		 this._sight_range = Utils.get_randomized_parameter((p1.get_sight_range()+p2.get_sight_range())/2, MUTATION_TOLERANCE);
		 this._speed = Utils.get_randomized_parameter((p1.get_speed()+p2.get_speed())/2, MUTATION_TOLERANCE);
	 }
	 
	 public void init(AnimalMapView reg_mngr) { 
		 this._region_mngr = reg_mngr;
		 
		 if(this._pos == null) 		
			 this._pos = new Vector2D((0,region_mngr.get_width()-1), (0, _region_mngr.get_height()-1));
		 else 
			 this._pos = null;
//			 terminar 
//			 Si _pos no es
//			 null hay que ajustarlo para que esté dentro del mapa si es necesario (ver el apartado “Ajustar
//					 posiciones”).
		 
			 
		  // terminar Elegir una posición aleatoria para _dest (dentro del rango del mapa).
		 this._dest = Vector2D.get_random_vector(0, maximo del mapa);
	 }
	 
	 public Animal deliver_baby(){
		 Animal a = this._baby;	// cambiar
		 this._baby = null;		// si no esto hara que a sea null;
		 return a;
//		 devolver _baby y ponerlo a null. El simulador invocará a este método
//		 para que nazcan los animales
		 // return this._baby = null;
	 }
	 
	 protected void move(double speed) {
		 this._pos = _pos.plus(_dest.minus(_pos).direction().scale(speed));
	 }
	 
	 @Override
	 public JSONObject as_JSON() { // revisar
		 JSONObject jo = new JSONObject();
			
			jo.put("pos", this._pos);
			jo.put("gcode", this._genetic_code);
			jo.put("diet", this._diet);
			jo.put("state", this._state);
			
		 return jo;
	 }
	 
}
