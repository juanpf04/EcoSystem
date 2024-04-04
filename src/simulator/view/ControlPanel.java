package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.misc.Messages;

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

		// Open Button*/
		this._openButton = new JButton();
		this._openButton.setToolTipText("Open");
		this._openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		this._openButton.addActionListener((e) -> {
		int selection = this._fc.showOpenDialog(ViewUtils.getWindow(this));
		if(selection == JFileChooser.APPROVE_OPTION) {
			File file = this._fc.getSelectedFile();
			try {
				JSONObject data = load_JSON_file(new FileInputStream(file));
				int cols = data.getInt(Messages.COLUMNS_KEY);
				int rows = data.getInt(Messages.ROWS_KEY);
				int width = data.getInt(Messages.WIDTH_KEY);
				int height = data.getInt(Messages.HEIGHT_KEY);
				
				this._ctrl.reset(cols, rows, width, height);
				this._ctrl.load_data(data);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		;}); // TODO check
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
		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
		this._regionsButton.addActionListener((e) -> this._changeRegionsDialog.open(ViewUtils.getWindow(this))); // TODO check
		this._toolaBar.add(this._regionsButton);

		// Run Button
		this._runButton = new JButton();
		this._runButton.setToolTipText("Run");
		this._runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this._runButton.addActionListener((e) -> {
			this._stopped = false;
			this._openButton.setEnabled(false);
			this._viewerButton.setEnabled(false);
			this._regionsButton.setEnabled(false);
			this._runButton.setEnabled(false);
			
			String delta_time = this._textField.getText();
			this._delta_time =  Double.valueOf(delta_time);
			this.run_sim((int) this._spinner.getValue(), this._delta_time); //TODO REVISAR
		}); // TODO check
		
		this._toolaBar.add(this._runButton);

		// Stop Button
		this._toolaBar.addSeparator();
		this._stopButton = new JButton();
		this._stopButton.setToolTipText("Stop");
		this._stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this._stopButton.addActionListener((e) ->{
			this._openButton.setEnabled(true);
			this._viewerButton.setEnabled(true);
			this._regionsButton.setEnabled(true);
			this._quitButton.setEnabled(true);
			this._runButton.setEnabled(true);
			this._stopped = true;
			
		}); // TODO check
		this._toolaBar.add(this._stopButton);

		//spinner y lo otro
		//JLabel para donde pone: Steps: 
		JLabel l_steps = new JLabel("Steps: ");
		this._toolaBar.add(l_steps);
		
		//Spinner
		this._spinner = new JSpinner(new SpinnerNumberModel(10000, 10000, 100000, 100)); // Inicial y final?
		this._toolaBar.add(this._spinner);
		
		//JTextField
		JLabel l_deltaTime = new JLabel("Delta-Time: ");
		this._toolaBar.add(l_deltaTime);
		this._textField= new JTextField("0.03");
		this._textField.setMinimumSize(new Dimension(300, 300)); // Ver que poner aqui 
		this._textField.setMaximumSize(new Dimension(100,100));
		this._toolaBar.add(this._textField);
		
		// Quit Button
		this._toolaBar.add(Box.createGlue()); // this aligns the button to the right
		this._toolaBar.addSeparator();
		this._quitButton = new JButton();
		this._quitButton.setToolTipText("Quit");
		this._quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this._quitButton.addActionListener((e) -> ViewUtils.quit(this)); // TODO check
		this._toolaBar.add(this._quitButton);
		
		//fc
		this._fc = new JFileChooser();
		this._fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		//
		// _fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));
		// TODO Inicializar _changeRegionsDialog con instancias del di�logo de cambio
		// de regiones
		
		
		
	}
	// TODO el resto de m�todos van aqu�
	
	private void run_sim(int n, double dt) {
		if (n > 0 && !this._stopped) {
			try {
			this._ctrl.advance(dt);
			SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				// TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
				// que corresponda
				
				ViewUtils.showErrorMsg(e.getMessage()); // cual es

				// TODO activar todos los botones
				
				this.enableButtons();

				this._stopped = true;
			}
		} else {
			// TODO activar todos los botones
			this.enableButtons();
			this._stopped = true;
		}
	}
	private void enableButtons() {
		this._openButton.setEnabled(true);
		this._viewerButton.setEnabled(true);
		this._regionsButton.setEnabled(true);
		this._runButton.setEnabled(true);
		this._quitButton.setEnabled(true);
	}
	
	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}
}
