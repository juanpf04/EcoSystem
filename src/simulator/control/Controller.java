package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
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

	public void load_data(JSONObject data) { // revisar y terminar
		JSONArray la = data.getJSONArray(Messages.ANIMALS_KEY);
		JSONArray lr = data.getJSONArray(Messages.REGIONS_KEY);

		if (lr != null)
			for (int i = 0; i < lr.length(); i++) {
				JSONObject jo = lr.getJSONObject(i);
				JSONArray lc = jo.getJSONArray("col");
				JSONArray lrr = jo.getJSONArray("row");
				for (int col = lc.getInt(0); col <= lc.getInt(1); col++)
					for (int row = lrr.getInt(0); row <= lrr.getInt(1); row++)
						this._sim.set_region(row, col, jo.getJSONObject(Messages.SPEC_KEY));
			}

		for (int i = 0; i < la.length(); i++) {
			JSONObject jo = la.getJSONObject(i);
			int n = jo.getInt(Messages.AMOUNT_KEY);
			for (int j = 0; j < n; j++)
				this._sim.add_animal(jo.getJSONObject(Messages.SPEC_KEY));
		}
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		JSONObject jo = new JSONObject();
		jo.put(Messages.IN_KEY, this._sim.as_JSON());

		SimpleObjectViewer view = null;
		if (sv) {
			MapInfo m = _sim.get_map_info();
			view = new SimpleObjectViewer("[ECOSYSTEM]", m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
			view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt);
		}

		while (this._sim.get_time() <= t) {
			this._sim.advance(dt);
			if (sv)
				view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt);
		}

		if (sv)
			view.close();

		jo.put(Messages.OUT_KEY, this._sim.as_JSON());
		PrintStream p = new PrintStream(out);
		p.println(jo);
	}

	private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.get_genetic_code(), (int) a.get_position().getX(), (int) a.get_position().getY(),
					(int) Math.round(a.get_age()) + 2));
		return ol;
	}

}
