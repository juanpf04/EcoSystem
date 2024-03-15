package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.Region;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {
	// TODO definir atributos necesarios
	private String[] _header; // ={"nombre", dni, etc.}
	private List<Region> _regions;

	RegionsTableModel(Controller ctrl) {
		// TODO inicializar estructuras de datos correspondientes
		// TODO registrar this como observador
		this._regions = la lista de regiones;
	}
	// TODO el resto de métodos van aquí…

	@Override
	public String getColumnName(int index) {
		return _header[index];

	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return _regions.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return _header.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Region r = _regions.get(rowIndex);

		Object result = null;

		switch (columnIndex) { // ejemplo lo mismo hacer con un for
		case 0:
			result = r.getAnimals();
			break;
		case 1:
			result = r.getClass();
			break;

		default:
			break;
		}

		return result;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub

	}
}
