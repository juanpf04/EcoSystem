package simulator.factories;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONObject;

import simulator.view.Messages;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _builders_info;

	public BuilderBasedFactory() {
		// Create a HashMap for _builders, and a LinkedList _builders_info
		// …
		this._builders = new HashMap<String, Builder<T>>();
		this._builders_info = new LinkedList<JSONObject>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {
		this();
		// call add_builder(b) for each builder b in builder
		// …
		for (Builder<T> b : builders)
			this.add_builder(b);
	}

	public void add_builder(Builder<T> b) {
		// add an entry “b.getTag() |−> b” to _builders.
		// ...
		this._builders.put(b.get_type_tag(), b);
		// add b.get_info() to _buildersInfo
		// ...
		this._builders_info.add(b.get_info());
	}

	@Override
	public T create_instance(JSONObject info) {
		if (info == null) {
			throw new IllegalArgumentException(Messages.INVALID_INFO);
		}
		// Look for a builder with a tag equals to info.getString("type"), in the
		// map _builder, and call its create_instance method and return the result
		// if it is not null. The value you pass to create_instance is the following
		// because ‘data’ is optional:
		//
		// info.has("data") ? info.getJSONObject("data") : new getJSONObject()
		// …
		// If no builder is found or the result is null ...
		Builder<T> b = this._builders.get(info.getString(Messages.TYPE_KEY));
		T t = null;

		if (b != null)
			t = b.create_instance(
					info.has(Messages.DATA_KEY) ? info.getJSONObject(Messages.DATA_KEY) : new JSONObject());
		if (t != null)
			return t;

		throw new IllegalArgumentException(Messages.unrecognized_info(info.toString()));
	}

	@Override
	public List<JSONObject> get_info() {
		return Collections.unmodifiableList(this._builders_info);
	}

}