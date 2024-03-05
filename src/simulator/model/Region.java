package simulator.model;

import java.util.List;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.view.Messages;

import java.util.Collections;
import java.util.LinkedList;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {

	protected List<Animal> _animals;

	public Region() {
		this._animals = new LinkedList<Animal>();
	}

	public final void add_animal(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		this._animals.add(a);
	}

	public final void remove_animal(Animal a) {
		if (a == null)
			throw new IllegalArgumentException(Messages.INVALID_ANIMAL);

		this._animals.remove(a);
	}

	public final List<Animal> getAnimals() {
		return Collections.unmodifiableList(this._animals);
	}

	public List<Animal> get_animals(Predicate<Animal> filter) {
		List<Animal> animals = new LinkedList<Animal>();

		for (Animal animal : this.getAnimals())
			if (filter.test(animal))
				animals.add(animal);

		return animals;
	}

	// JSONable

	@Override
	public JSONObject as_JSON() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (Animal a : this.getAnimals())
			ja.put(a.as_JSON());

		jo.put(Messages.ANIMALS_KEY, ja);

		return jo;
	}

}
