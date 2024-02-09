package simulator.control;

import java.io.OutputStream;

import org.json.JSONObject;

import simulator.model.Simulator;
import simulator.view.Messages;

public class Controller {
	
	private Simulator _sim;
	
	public Controller(Simulator sim) {
		this._sim = sim;
	}
	
	public void load_data(JSONObject data) {
		// recorrer data para sacar los animales y las regiones
		//utilizar 
		this._sim.add_animal(data);
		this._sim.set_region(data, data, data);
		// tratar data
	}
	
	public void run(double t, double dt, boolean sv, OutputStream out) {
		JSONObject jo = new JSONObject();	
		jo.put(Messages.IN, this._sim.as_JSON());
		while(this._sim.get_time() <= t) {
			this._sim.advance(dt);
		}
		jo.put(Messages.OUT, this._sim.as_JSON());
		//out.write(jo);
	}
	
}
