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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

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

	// TODO a�ade m�s atributos aqu� �

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

		/*TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para a�adir la l�nea de separaci�n vertical
		// entre las componentes que lo necesiten.*/

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
		this._viewerButton.addActionListener((e) -> {MapWindow mapWindow = new MapWindow();}); // TODO check
		this._toolaBar.add(this._viewerButton);

		// Regions Button
		this._regionsButton = new JButton();
		this._regionsButton.setToolTipText("Regions");
		this._regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this._regionsButton.addActionListener((e) -> _changeRegionsDialog.open(ViewUtils.getWindow(this))); // TODO check
		this._toolaBar.add(this._regionsButton);

		// Run Button
		this._runButton = new JButton();
		this._runButton.setToolTipText("Run");
		this._runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this._runButton.addActionListener((e) -> {
			this._openButton.setEnabled(false);
			this._viewerButton.setEnabled(false);
			this._regionsButton.setEnabled(false);
			this._quitButton.setEnabled(false);
			this._stopped = false;
			
			this._delta_time =  this._textField.getText();
			
			this.run_sim((Integer) this._spinner.getValue(), this._delta_time); //TODO REVISAR
		}); // TODO check
		this._toolaBar.add(this._runButton);

		// Stop Button
		this._toolaBar.addSeparator();
		this._stopButton = new JButton();
		this._stopButton.setToolTipText("Stop");
		this._stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this._stopButton.addActionListener((e) -> this._stopped = true); // TODO check
		this._toolaBar.add(this._stopButton);

		//spinner y lo otro
		//Spinner
		
		_spinner = new JSpinner(new SpinnerNumberModel(5000, 1, 10000, 100));
		this._toolaBar.add(this._spinner);
		
		//JTextField
		this._textField= new JTextField(10);
		this._textField.setBounds(100,50,120,30); // ¿Donde colocar el txtField?
		this._toolaBar.add(this._textField);
		
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
		// TODO Inicializar _changeRegionsDialog con instancias del di�logo de cambio
		// de regiones
	}
	// TODO el resto de m�todos van aqu�
	
	private void run_sim(int n, double dt) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.advance(dt);
				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				// TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
				// que corresponda
				
				ViewUtils.showErrorMsg("Mensaje de error"); // cual es

				// TODO activar todos los botones
				
				this.enableButtons();

				_stopped = true;
			}
		} else {
			// TODO activar todos los botones
			this.enableButtons();
			_stopped = true;
		}
	}
	private void enableButtons() {
		this._openButton.setEnabled(true);
		this._viewerButton.setEnabled(true);
		this._regionsButton.setEnabled(true);
		this._runButton.setEnabled(true);
		this._stopButton.setEnabled(true);
		this._quitButton.setEnabled(true);
	}
}
