package simulator.model;

import simulator.misc.Vector2D;

public interface AnimalInfo extends JSONable {

	public State get_state();

	public Vector2D get_position();

	public String get_genetic_code();

	public Diet get_diet();

	public double get_speed();

	public double get_speed(double dt);

	public double get_sight_range();

	public double get_energy();

	public double get_desire();

	public double get_age();

	public Vector2D get_destination();

	public boolean is_pregnant();

	public boolean can_pregnant();

	public boolean on_heat();

	public boolean is_alive();

	public boolean is_out();

	public boolean normal();

	public boolean mate();

	public boolean danger();

	public boolean hunger();
	
	public boolean dead();

	public boolean carnivore();

	public boolean herbivore();
}
