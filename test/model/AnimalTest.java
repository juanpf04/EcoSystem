package model;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import simulator.misc.Vector2D;
import simulator.model.RegionManager;
import simulator.model.SelectFirst;
import simulator.model.Sheep;

public class AnimalTest {

	@Test
	public void test1() {
		RegionManager r = new RegionManager(10, 20, 500, 600); // lo necesita el método update de animal
		Sheep s = new Sheep(new SelectFirst(), new SelectFirst(), new Vector2D());
		assertEquals(s.get_position(), new Vector2D(), "mensaje informativo en caso de que no sean igual");
		
		s.update(0.03);
		
		assertTrue("no puede salirse del mapa", s.is_out());
	}
	
	@Test
	public void test2() {
		Sheep s = new Sheep(null, new SelectFirst(), new Vector2D());

		assertThrows(Exception.class, ()->{});
	}
	
}
