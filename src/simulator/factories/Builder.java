package simulator.factories;

import org.json.JSONObject;

import simulator.view.Messages;

public abstract class Builder<T> {

	private String _type_tag;
	private String _desc;

	public Builder(String type_tag, String desc) {
		if (type_tag == null || type_tag.isBlank())
			throw new IllegalArgumentException(Messages.INVALID_TYPE);
		if (desc == null || desc.isBlank())
			throw new IllegalArgumentException(Messages.INVALID_DESC);
		this._type_tag = type_tag;
		this._desc = desc;
	}

	public String get_type_tag() {
		return this._type_tag;
	}

	public JSONObject get_info() {
		JSONObject info = new JSONObject();
		info.put(Messages.TYPE_KEY, this._type_tag);
		info.put(Messages.DESCRIPTION_KEY, this._desc);
		JSONObject data = new JSONObject();
		fill_in_data(data);
		info.put(Messages.DATA_KEY, data);
		return info;
	}

	protected void fill_in_data(JSONObject o) {
	}

	@Override
	public String toString() {
		return this._desc;
	}

	protected abstract T create_instance(JSONObject data);
}
