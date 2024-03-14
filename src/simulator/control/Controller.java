package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.Messages;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {

	private Simulator _sim;

	public Controller(Simulator sim) {

		if (sim == null)
			throw new IllegalArgumentException(Messages.INVALID_SIMULATOR);

		this._sim = sim;
	}

	public void load_data(JSONObject data) {
		if (data == null || data.isEmpty())
			throw new IllegalArgumentException(Messages.INVALID_JSON);

		if (data.has(Messages.REGIONS_KEY)) {
			this.set_regions(data);
		}

		JSONArray ja = data.getJSONArray(Messages.ANIMALS_KEY);

		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			int amount = jo.getInt(Messages.AMOUNT_KEY);

			if (amount <= 0)
				throw new IllegalArgumentException(Messages.INVALID_AMOUNT);

			for (int j = 0; j < amount; j++)
				this._sim.add_animal(jo.getJSONObject(Messages.SPEC_KEY));
		}
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		if (t <= 0)
			throw new IllegalArgumentException(Messages.TIME_ERROR);
		if (dt <= 0)
			throw new IllegalArgumentException(Messages.DELTA_TIME_ERROR);

		JSONObject jo = new JSONObject();
		jo.put(Messages.IN_KEY, this._sim.as_JSON());

		SimpleObjectViewer view = null;
		if (sv) {
			MapInfo m = this._sim.get_map_info();
			view = new SimpleObjectViewer(Messages.TITLE, m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
			view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt);
		}

		while (this._sim.get_time() <= t) {
			this.advance(dt);
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

	public void reset(int cols, int rows, int width, int height) {
		this._sim.reset(cols, rows, width, height);
	}

	public void set_regions(JSONObject rs) {
		JSONArray ja = rs.getJSONArray(Messages.REGIONS_KEY);

		for (int i = 0; i < ja.length(); i++) {

			JSONObject jo = ja.getJSONObject(i);

			JSONArray jac = jo.getJSONArray(Messages.COLUMN_KEY);
			JSONArray jar = jo.getJSONArray(Messages.ROW_KEY);

			for (int col = jac.getInt(0); col <= jac.getInt(1); col++)
				for (int row = jar.getInt(0); row <= jar.getInt(1); row++)
					this._sim.set_region(row, col, jo.getJSONObject(Messages.SPEC_KEY));
		}
	}

	public void advance(double dt) {
		this._sim.advance(dt);
	}

	public void addObserver(EcoSysObserver o) {
		this._sim.addObserver(o);
	}

	public void removeObserver(EcoSysObserver o) {
		this._sim.removeObserver(o);
	}

}
