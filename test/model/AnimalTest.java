package model;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

import simulator.misc.Vector2D;
import simulator.model.SelectFirst;
import simulator.model.Sheep;

public class AnimalTest {

	@Test
	public void test1() {
//		RegionManager r = new RegionManager(10, 20, 500, 600); // lo necesita el método update de animal
		Sheep s = new Sheep(new SelectFirst(), new SelectFirst(), new Vector2D());
		s.update(0.03);
		
		

		assertTrue("no puede salirse del mapa", s.is_out());
	}
}
