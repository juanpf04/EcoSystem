package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class MapWindow extends JFrame implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private AbstractMapViewer _viewer;
	private Frame _parent;

	MapWindow(Frame parent, Controller ctrl) {
		super(Messages.MAP_WINDOW_TITLE);
		this._ctrl = ctrl;
		this._parent = parent;
		initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		this._viewer = new MapViewer();
		mainPanel.add(this._viewer, BorderLayout.CENTER);

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				_ctrl.removeObserver(MapWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		this.pack();
		if (this._parent != null)
			this.setLocation(this._parent.getLocation().x + this._parent.getWidth() / 2 - this.getWidth() / 2,
					this._parent.getLocation().y + this._parent.getHeight() / 2 - this.getHeight() / 2);
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> {
			this._viewer.reset(time, map, animals);
			this.pack();
		});
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> {
			this._viewer.reset(time, map, animals);
			this.pack();
		});
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		SwingUtilities.invokeLater(() -> {
			this._viewer.reset(time, map, animals);
			this.pack();
		});
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		SwingUtilities.invokeLater(() -> {
			this._viewer.update(animals, time);
		});
	}
}
