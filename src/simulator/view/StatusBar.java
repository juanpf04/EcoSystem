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
	private JLabel _time;
	private JLabel _total_animals;
	private JLabel _dimension;

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

		this.add(new JLabel("Time: "));
		this._time = new JLabel();
		this.add(_time);

		this.separator();

		this.add(new JLabel("Total Animals: "));
		this._total_animals = new JLabel();
		this.add(_total_animals);
		
		this.separator();

		this.add(new JLabel("Dimension: "));
		this._dimension = new JLabel();

	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		this.setTime(time);
		this._total_animals.setText(""+ animals.size());
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		this.setTime(time);
		this._total_animals.setText(""+ animals.size());

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub
		this.setTime(time);
		this._total_animals.setText(""+animals.size());

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub
		this.setTime(time);
		this._total_animals.setText(""+animals.size());

	}
	

	// TODO el resto de métodos van aquí…

	private void separator() {
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
	}
	
	private void setTime(double time) {
		this._time.setText(String.format("%.3f", time));
	}
}
