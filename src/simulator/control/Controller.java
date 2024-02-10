package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.Messages;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
	
	private Simulator _sim;
	
	public Controller(Simulator sim) {
		this._sim = sim;
	}
	
	public void load_data(JSONObject data) {
		// recorrer data para sacar los animales y las regiones
		//utilizar 
		this._sim.set_region(data, data, data);
		this._sim.add_animal(data);
		// tratar data
	}
	
	public void run(double t, double dt, boolean sv, OutputStream out) {
		JSONObject jo = new JSONObject();	
		jo.put(Messages.IN, this._sim.as_JSON());
		
		SimpleObjectViewer view = null;
		if (sv) {
			MapInfo m = _sim.get_map_info();
			view = new SimpleObjectViewer("[ECOSYSTEM]",
			m.get_width(), m.get_height(),
			m.get_cols(), m.get_rows());
			view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt);
		}

		while(this._sim.get_time() <= t) {
			this._sim.advance(dt);
			if (sv) view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt);
		}
		
		if (sv) view.close();
		
		jo.put(Messages.OUT, this._sim.as_JSON());
		PrintStream p = new PrintStream(out);
		p.println(jo);
	}
	
	private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.get_genetic_code(),
					(int) a.get_position().getX(),
					(int) a.get_position().getY(), (int)Math.round(a.get_age()) + 2));
		return ol;
	}

}
