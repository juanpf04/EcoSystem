package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private List<String> _header;
	// private Map<Region> _regions;

	RegionsTableModel(Controller ctrl) {
		this._ctrl = ctrl;

		this._ctrl.addObserver(this);
	}
	// TODO el resto de métodos van aquí…

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0; // _regions.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0; // _header.length;
	}

	@Override
	public String getColumnName(int index) {
		return "";// _header[index];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return 0;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setRegions(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setRegions(map);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		this.setRegions(map);
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}

	private void setRegions(MapInfo map) {

	}
}
