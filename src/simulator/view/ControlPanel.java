package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
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

	private boolean _stopped = true; // for run/stop buttons

	private JButton _openButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JButton _quitButton;

	// TODO a�ade m�s atributos aqu� �

	private JSpinner _steps_spinner;
	private JTextField _delta_time_textField;

	ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		this._toolaBar = new JToolBar();
		this.add(this._toolaBar, BorderLayout.PAGE_START);

		// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para a�adir la l�nea de separaci�n vertical
		// entre las componentes que lo necesiten.

		// - Open Button --------------------------------------------------------
		this._openButton = new JButton();
		this._openButton.setToolTipText("Open");
		this._openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		this._openButton.addActionListener((ev) -> {
			int option = this._fc.showOpenDialog(ViewUtils.getWindow(this));
			if (option == JFileChooser.APPROVE_OPTION) {
				File file = this._fc.getSelectedFile();
				try {
					JSONObject data = load_JSON_file(new FileInputStream(file));
					int cols = data.getInt(Messages.COLUMNS_KEY);
					int rows = data.getInt(Messages.ROWS_KEY);
					int width = data.getInt(Messages.WIDTH_KEY);
					int height = data.getInt(Messages.HEIGHT_KEY);

					this._ctrl.reset(cols, rows, width, height);
					this._ctrl.load_data(data);

				} catch (Exception e) {
					ViewUtils.showErrorMsg(e.getMessage());
				}
			}
		}); // TODO check
		this._toolaBar.add(this._openButton);
		// ----------------------------------------------------------------------

		// - Viewer Button ------------------------------------------------------
		this._toolaBar.addSeparator();
		this._viewerButton = new JButton();
		this._viewerButton.setToolTipText("Viewer");
		this._viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		this._viewerButton.addActionListener((e) -> {
			MapWindow mapWindow = new MapWindow(new MainWindow(this._ctrl), this._ctrl); // TODO VER QUÉ PONER COMO PARENT DEL MAPWINDOW
		}); // TODO mirar si se pueden crear varias ventanas o solo una
		this._toolaBar.add(this._viewerButton);
		// ----------------------------------------------------------------------

		// - Regions Button -----------------------------------------------------
		this._regionsButton = new JButton();
		this._regionsButton.setToolTipText("Regions");
		this._regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
		this._regionsButton.addActionListener((e) -> this._changeRegionsDialog.open(ViewUtils.getWindow(this))); // TODO check
		this._toolaBar.add(this._regionsButton);
		// ----------------------------------------------------------------------

		// - Run Button ---------------------------------------------------------
		this._runButton = new JButton();
		this._runButton.setToolTipText("Run");
		this._runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this._runButton.addActionListener((e) -> {
			this._stopped = false;
			this.setEnableButtons(false);
			try {				
				this.run_sim((int) (((int) this._steps_spinner.getValue())/(Double.valueOf(this._delta_time_textField.getText()))), Double.valueOf(this._delta_time_textField.getText()));
			}
			catch(Exception ex) {
				ViewUtils.showErrorMsg("si mensaje to pro");
			}
		}); // TODO check

		this._toolaBar.add(this._runButton);
		// ----------------------------------------------------------------------

		// - Stop Button --------------------------------------------------------
		this._toolaBar.addSeparator();
		this._stopButton = new JButton();
		this._stopButton.setToolTipText("Stop");
		this._stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this._stopButton.addActionListener((e) -> this._stopped = true); // TODO check
		this._toolaBar.add(this._stopButton);
		// ----------------------------------------------------------------------

		// - Steps spinner ------------------------------------------------------
		this._toolaBar.add(new JLabel("Steps: "));
		this._steps_spinner = new JSpinner(new SpinnerNumberModel(10000, 10000, 100000, 100));
		this._toolaBar.add(this._steps_spinner);
		// ----------------------------------------------------------------------

		// - Delta time text field ----------------------------------------------
		this._toolaBar.add(new JLabel("Delta-Time: "));
		this._delta_time_textField = new JTextField("0.03");
		this._delta_time_textField.setMinimumSize(new Dimension(300, 300)); // TODO Ver que poner aqui
		this._delta_time_textField.setMaximumSize(new Dimension(100, 100));
		this._toolaBar.add(this._delta_time_textField);
		// ----------------------------------------------------------------------

		// - Quit Button --------------------------------------------------------
		this._toolaBar.add(Box.createGlue()); // this aligns the button to the right
		this._toolaBar.addSeparator();
		this._quitButton = new JButton();
		this._quitButton.setToolTipText("Quit");
		this._quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this._quitButton.addActionListener((e) -> ViewUtils.quit(this)); // TODO check
		this._toolaBar.add(this._quitButton);
		// ----------------------------------------------------------------------

		// TODO Inicializar _fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:

		//
		// _fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));

		this._fc = new JFileChooser();
		this._fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));

		// TODO Inicializar _changeRegionsDialog con instancias del di�logo de cambio
		// de regiones

		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);


	}
	// TODO el resto de m�todos van aqu�

	private void run_sim(int n, double dt) {
		if (n > 0 && !this._stopped) {
			try {
				long startTime = System.currentTimeMillis(); // fumadón de samir
				this._ctrl.advance(dt);
				long stepTime = System.currentTimeMillis() - startTime;
				long delay = (long) (dt * 1000 - stepTime);
				Thread.sleep(delay > 0 ? delay : 0);
				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				// TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
				// que corresponda
				ViewUtils.showErrorMsg(e.getMessage()); // cual es

				// TODO activar todos los botones
				this.setEnableButtons(true);

				this._stopped = true;
			}
		} else {
			// TODO activar todos los botones
			this.setEnableButtons(true);
			this._stopped = true;
		}
	}

	private void setEnableButtons(boolean b) {
		this._openButton.setEnabled(b);
		this._viewerButton.setEnabled(b);
		this._regionsButton.setEnabled(b);
		this._runButton.setEnabled(b);
		this._quitButton.setEnabled(b);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}
}
