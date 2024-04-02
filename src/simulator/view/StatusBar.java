package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Ver para añadir más
	private double _sim_time;
	private int _total_animals;
	private int _cols;
	private int _rows;
	private int _height;
	private int _width;
	private Controller _ctrl;

	StatusBar(Controller ctrl) {
		this._ctrl = ctrl;
		initGUI();
		// TODO registrar this como observador
		
		this._ctrl.addObserver(this);
		
		
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));

		JLabel l_time = new JLabel("Time: " + this._sim_time);
		l_time.setHorizontalAlignment(JLabel.LEFT);
		this.add(l_time);

		this.separator();

		JLabel l_num_animals = new JLabel("Total Animals: " + this._total_animals);
		l_num_animals.setHorizontalAlignment(JLabel.LEFT);
		this.add(l_num_animals);

		this.separator();

		JLabel l_dimension = new JLabel(
				"Dimension: " + this._width + "x" + this._height + " " + this._cols + "x" + this._rows);
		l_dimension.setHorizontalAlignment(JLabel.LEFT);
		this.add(l_dimension);

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
	// TODO el resto de métodos van aquí…

	private void separator() {
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
	}
}
