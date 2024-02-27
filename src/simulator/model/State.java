package simulator.model;

public enum State {
	NORMAL(1.0), MATE(1.2), HUNGER(1.2), DANGER(1.2), DEAD(0.0);
	
	private double _energy_weighting;
	
	State(double energy_weighting) {
		this._energy_weighting = energy_weighting;
	}

	public double get_energy_weighting() {
		return _energy_weighting;
	}
}
