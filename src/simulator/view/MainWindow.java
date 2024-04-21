package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;

import simulator.control.Controller;
import simulator.misc.Messages;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private static final Dimension _default_table_size = new Dimension(500, 250);

	public MainWindow(Controller ctrl) {
		super(Messages.GUI_TITLE);
		this._ctrl = ctrl;
		this.initGUI();
	}

	private void initGUI() {
		// this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/ecosystem.png"));

		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		// - MenuBar --------------------------------------------
		EcoMenu menu = new EcoMenu(this._ctrl);
		this.setJMenuBar(menu);
		// ------------------------------------------------------

		// - ToolBar --------------------------------------------
		ControlPanel controlPanel = new ControlPanel(this._ctrl);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		// ------------------------------------------------------

		// - StatusBar ------------------------------------------
		StatusBar statusBar = new StatusBar(this._ctrl);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		// ------------------------------------------------------

		// - Tables panel ----------------------------------------
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		// ------------------------------------------------------

		// - Species table --------------------------------------
		InfoTable species = new InfoTable(Messages.SPECIES_TABLE_TITLE, new SpeciesTableModel(this._ctrl));
		species.setPreferredSize(_default_table_size);
		contentPanel.add(species);
		// ------------------------------------------------------

		// - Regions table --------------------------------------
		InfoTable regions = new InfoTable(Messages.REGIONS_TABLE_TITLE, new RegionsTableModel(this._ctrl));
		regions.setPreferredSize(_default_table_size);
		contentPanel.add(regions);
		// ------------------------------------------------------

		addWindowListener(new WindowListener() {

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
				ViewUtils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		});

		pack();

		// - Center window -----------------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;

		setLocation(x, y);
		// ---------------------------------------------------------------

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setVisible(true);
	}

}
