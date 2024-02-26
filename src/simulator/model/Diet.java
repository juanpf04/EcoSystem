package simulator.model;

public enum Diet {
	HERBIVORE(1.0), CARNIVORE(0.0);

	private double _herbivorous_region_weighting;

	Diet(double herbivorous_region_weighting) {
		this._herbivorous_region_weighting = herbivorous_region_weighting;
	}

	public double get_herbivorous_region_weighting() {
		return this._herbivorous_region_weighting;
	}
}
