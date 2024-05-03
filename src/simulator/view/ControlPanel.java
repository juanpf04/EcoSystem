package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Box;
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

	private JToolBar _toolBar;
	private JFileChooser _fc;

	private boolean _stopped = true; // for run/stop buttons
	volatile Thread _thread;

	private JButton _openButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JButton _exitButton;

	private JSpinner _steps_spinner;
	private JTextField _delta_time_textField;
	private JSpinner _delay_spinner;

	ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		this.initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		this._toolBar = new JToolBar();
		this.add(this._toolBar, BorderLayout.PAGE_START);

		// - Open Button --------------------------------------------------------
		this._openButton = new JButton();
		this._openButton.setToolTipText(Messages.OPEN_BUTTON_DESCRIPTION);
		this._openButton.setIcon(ViewUtils.get_icon("open"));
		this._openButton.addActionListener((e) -> openButtonAction());
		this._toolBar.add(this._openButton);
		// ----------------------------------------------------------------------

		this._toolBar.addSeparator();

		// - Viewer Button ------------------------------------------------------
		this._viewerButton = new JButton();
		this._viewerButton.setToolTipText(Messages.VIEWER_BUTTON_DESCRIPTION);
		this._viewerButton.setIcon(ViewUtils.get_icon("viewer"));
		this._viewerButton.addActionListener((e) -> new MapWindow(ViewUtils.getWindow(this), this._ctrl));
		this._toolBar.add(this._viewerButton);
		// ----------------------------------------------------------------------

		// - Regions Button -----------------------------------------------------
		this._regionsButton = new JButton();
		this._regionsButton.setToolTipText(Messages.REGIONS_BUTTON_DESCRIPTION);
		this._regionsButton.setIcon(ViewUtils.get_icon("regions"));
		this._regionsButton.addActionListener((e) -> this._changeRegionsDialog.open(ViewUtils.getWindow(this)));
		this._toolBar.add(this._regionsButton);
		// ----------------------------------------------------------------------

		// - Run Button ---------------------------------------------------------
		this._runButton = new JButton();
		this._runButton.setToolTipText(Messages.RUN_BUTTON_DESCRIPTION);
		this._runButton.setIcon(ViewUtils.get_icon("run"));
		this._runButton.addActionListener((e) -> runButtonAction());

		this._toolBar.add(this._runButton);
		// ----------------------------------------------------------------------

		this._toolBar.addSeparator();

		// - Stop Button --------------------------------------------------------
		this._stopButton = new JButton();
		this._stopButton.setToolTipText(Messages.STOP_BUTTON_DESCRIPTION);
		this._stopButton.setIcon(ViewUtils.get_icon("stop"));
		this._stopButton.addActionListener((e) -> this._stopped = true);
		this._toolBar.add(this._stopButton);
		// ----------------------------------------------------------------------

		// - Steps spinner ------------------------------------------------------
		this._toolBar.add(new JLabel(Messages.STEPS));
		this._steps_spinner = new JSpinner(new SpinnerNumberModel(10000, 1, 10000, 100));
		Dimension size = this._steps_spinner.getPreferredSize();
		size = new Dimension(size.width - 20, size.height * 2);
		this._steps_spinner.setMinimumSize(size);
		this._steps_spinner.setMaximumSize(size);
		this._steps_spinner.setPreferredSize(size);
		this._steps_spinner.setToolTipText(Messages.STEPS_SPINNER_DESCRIPTION);
		this._toolBar.add(this._steps_spinner);
		// ----------------------------------------------------------------------

		// - Delta time text field ----------------------------------------------
		this._toolBar.add(new JLabel(Messages.DELTA_TIME));
		this._delta_time_textField = new JTextField("" + Main._delta_time);
		this._delta_time_textField.setMinimumSize(size);
		this._delta_time_textField.setMaximumSize(size);
		this._delta_time_textField.setPreferredSize(size);
		this._delta_time_textField.setToolTipText(Messages.DT_TEXT_FIELD_DESCRIPTION);
		this._toolBar.add(this._delta_time_textField);
		// ----------------------------------------------------------------------

		// - Delay spinner ------------------------------------------------------
		this._toolBar.add(new JLabel(Messages.DELAY));
		this._delay_spinner = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		this._delay_spinner.setMinimumSize(size);
		this._delay_spinner.setMaximumSize(size);
		this._delay_spinner.setPreferredSize(size);
		this._delay_spinner.setToolTipText(Messages.DELAY_SPINNER_DESCRIPTION);
		this._toolBar.add(this._delay_spinner);
		// ----------------------------------------------------------------------

		this._toolBar.add(Box.createGlue()); // this aligns the button to the right
		this._toolBar.addSeparator();

		// - Quit Button --------------------------------------------------------
		this._exitButton = new JButton();
		this._exitButton.setToolTipText(Messages.EXIT_BUTTON_DESCRIPTION);
		this._exitButton.setIcon(ViewUtils.get_icon("exit"));
		this._exitButton.addActionListener((e) -> ViewUtils.quit(this));
		this._toolBar.add(this._exitButton);
		// ----------------------------------------------------------------------

		this._fc = new JFileChooser();
		this._fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/" + Messages.EXAMPLES_DIRECTORY));

		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
	}

	private void run_sim(int n, double dt, int delay) {
		while (n > 0 && !Thread.interrupted()) {
			try {
				this._ctrl.advance(dt);
				Thread.sleep(delay);
				n--;
			} catch (InterruptedException e) {
				Thread.interrupted();
			} catch (Exception e) {
				ViewUtils.showErrorMsg(e.getMessage());
				n = 0;
			}
		}

		this.setEnabledButtons(true);
		this._stopped = true;

//		if (n > 0 && !this._stopped) {
//			try {
//				long startTime = System.currentTimeMillis();
//				this._ctrl.advance(dt);
//				long endTime = System.currentTimeMillis();
//				long delay = (long) (dt * 1000 - (endTime - startTime));
//				Thread.sleep(delay > 0 ? delay : 0);
//				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
//			} catch (Exception e) {
//				ViewUtils.showErrorMsg(e.getMessage());
//				this.setEnabledButtons(true);
//				this._stopped = true;
//			}
//		} else {
//			this.setEnabledButtons(true);
//			this._stopped = true;
//		}
	}

	private void setEnabledButtons(boolean b) {
		this._openButton.setEnabled(b);
		this._viewerButton.setEnabled(b);
		this._regionsButton.setEnabled(b);
		this._runButton.setEnabled(b);
		this._exitButton.setEnabled(b);
		this._steps_spinner.setEnabled(b);
		this._delay_spinner.setEnabled(b);
		this._delta_time_textField.setEnabled(b);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private void openButtonAction() {
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
	}

	private void runButtonAction() {
		if(this._thread == null) {
			this._thread = nuevo hilo
			this._stopped = false;
			this.setEnabledButtons(false);
			try {
				this.run_sim((int) this._steps_spinner.getValue(), Double.valueOf(this._delta_time_textField.getText()),
						(int) this._delay_spinner.getValue());
			} catch (NumberFormatException e) {
				ViewUtils.showErrorMsg(Messages.DELTA_TIME_ERROR);
				this.setEnabledButtons(true);
				this._stopped = true;
			} 
		}
	}
}
