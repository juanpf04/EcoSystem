package simulator.view;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import simulator.control.Controller;
import simulator.launcher.Main;

public class ControlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _openButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JButton _quitButton;

	// TODO añade más atributos aquí …

	private JSpinner _spinner;
	private JTextField _textField;
	private double _delta_time;

	ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		this._delta_time = Main._delta_time;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		this._toolaBar = new JToolBar();
		this.add(_toolaBar, BorderLayout.PAGE_START);

		// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten.

		// Open Button
		this._openButton = new JButton();
		this._openButton.setToolTipText("Open");
		this._openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		this._openButton.addActionListener((e) -> this._fc.showOpenDialog(ViewUtils.getWindow(this))); // TODO check
		this._toolaBar.add(this._openButton);

		// Viewer Button
		this._toolaBar.addSeparator();
		this._viewerButton = new JButton();
		this._viewerButton.setToolTipText("Viewer");
		this._viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		this._viewerButton.addActionListener((e) -> ); // TODO check
		this._toolaBar.add(this._viewerButton);

		// Regions Button
		this._regionsButton = new JButton();
		this._regionsButton.setToolTipText("Regions");
		this._regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this._regionsButton.addActionListener((e) -> ); // TODO check
		this._toolaBar.add(this._regionsButton);

		// Run Button
		this._runButton = new JButton();
		this._runButton.setToolTipText("Run");
		this._runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this._runButton.addActionListener((e) -> ); // TODO check
		this._toolaBar.add(this._runButton);

		// Stop Button
		this._toolaBar.addSeparator();
		this._stopButton = new JButton();
		this._stopButton.setToolTipText("Stop");
		this._stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this._stopButton.addActionListener((e) -> this._fc.showOpenDialog(ViewUtils.getWindow(this))); // TODO check
		this._toolaBar.add(this._stopButton);

		//spinner y lo otro
		
		// Quit Button
		this._toolaBar.add(Box.createGlue()); // this aligns the button to the right
		this._toolaBar.addSeparator();
		this._quitButton = new JButton();
		this._quitButton.setToolTipText("Quit");
		this._quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this._quitButton.addActionListener((e) -> ViewUtils.quit(this)); // TODO check
		this._toolaBar.add(this._quitButton);

		// TODO Inicializar _fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:
		//
		// _fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));
		// TODO Inicializar _changeRegionsDialog con instancias del diálogo de cambio
		// de regiones
	}
	// TODO el resto de métodos van aquí…

}
