package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.ViewObserver;

class StatusBar extends JPanel implements EcoSysObserver, ViewObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel _time;
	private JLabel _total_animals;
	private JLabel _dimension;

	StatusBar(Controller ctrl) {
		this.initGUI();
		ctrl.addObserver((EcoSysObserver) this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));

		// - Time ------------------------------
		this._time = new JLabel();
		this.add(this._time);
		// -------------------------------------

		this.addVerticalSeparator();

		// - Total Animals ---------------------
		this._total_animals = new JLabel();
		this.add(this._total_animals);
		// -------------------------------------

		this.addVerticalSeparator();

		// - Dimension -------------------------
		this._dimension = new JLabel();
		this.add(this._dimension);
		// -------------------------------------
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setTime(time);
		this.setTotalAnimals(animals.size());
		this.setDimension(map.get_width(), map.get_height(), map.get_cols(), map.get_rows());
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setTime(time);
		this.setTotalAnimals(animals.size());
		this.setDimension(map.get_width(), map.get_height(), map.get_cols(), map.get_rows());

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		SwingUtilities.invokeLater(() -> {
			this.setTotalAnimals(animals.size());
		});
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		SwingUtilities.invokeLater(() -> {
			this.setTime(time);
			this.setTotalAnimals(animals.size());
		});
	}

	private void addVerticalSeparator() {
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
	}

	private void setTime(double time) {
		this._time.setText(Messages.TIME + String.format("%.3f", time));
	}

	private void setTotalAnimals(int total_animals) {
		this._total_animals.setText(Messages.TOTAL_ANIMALS + total_animals);
	}

	private void setDimension(int width, int height, int cols, int rows) {
		this._dimension.setText(Messages.DIMENSION + width + "x" + height + " " + cols + "x" + rows);
	}

	@Override
	public void onDarkMode() {
		this.setBackground(Color.DARK_GRAY);
		this._time.setForeground(Color.LIGHT_GRAY);
		this._total_animals.setForeground(Color.LIGHT_GRAY);
		this._dimension.setForeground(Color.LIGHT_GRAY);
	}

	@Override
	public void onLightMode() {
		this.setBackground(Color.white);
		this._time.setForeground(Color.black);
		this._total_animals.setForeground(Color.black);
		this._dimension.setForeground(Color.black);
	}

	@Override
	public void onSpectacularView() {
	}

	@Override
	public void onStandardView() {
	}

}
