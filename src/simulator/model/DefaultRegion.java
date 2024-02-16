package simulator.model;

public class DefaultRegion extends Region {

	@Override
	public void update(double dt) {}

	@Override
	public double get_food(Animal a, double dt) {
		if(a.get_diet() == Diet.CARNIVORE)
			return 0.0;
		
		return FOOD*Math.exp(-Math.max(0,this.count(Diet.HERBIVORE)-NOMBRE_RECHULON)*NOMBRE_RECHULON2)*dt; 
	}

}
