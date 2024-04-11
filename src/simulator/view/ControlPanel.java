package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
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

	private JButton _openButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JButton _exitButton;

	private JSpinner _steps_spinner;
	private JTextField _delta_time_textField;

	ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		initGUI();
	}

	private void initGUI() { // TODO crear mensajes
		this.setLayout(new BorderLayout());
		this._toolBar = new JToolBar();
		this.add(this._toolBar, BorderLayout.PAGE_START);

		// - Open Button --------------------------------------------------------
		this._openButton = new JButton();
		this._openButton.setToolTipText("Load an input file into the simulator");
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
		});
		this._toolBar.add(this._openButton);
		// ----------------------------------------------------------------------

		this._toolBar.addSeparator();

		// - Viewer Button ------------------------------------------------------
		this._viewerButton = new JButton();
		this._viewerButton.setToolTipText("Map Viewer");
		this._viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		this._viewerButton.addActionListener((e) -> {
			new MapWindow(ViewUtils.getWindow(this), this._ctrl); // TODO hace falta guardarlo en una variable o como la
																	// variable no se utiliza asi sirve
		});
		this._toolBar.add(this._viewerButton);
		// ----------------------------------------------------------------------

		// - Regions Button -----------------------------------------------------
		this._regionsButton = new JButton();
		this._regionsButton.setToolTipText("Change Regions");
		this._regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
		this._regionsButton.addActionListener((e) -> this._changeRegionsDialog.open(ViewUtils.getWindow(this)));
		this._toolBar.add(this._regionsButton);
		// ----------------------------------------------------------------------

		// - Run Button ---------------------------------------------------------
		this._runButton = new JButton();
		this._runButton.setToolTipText("Run the simulator");
		this._runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this._runButton.addActionListener((e) -> {
			this._stopped = false;
			this.setEnableButtons(false);
			try {
				this.run_sim((int) this._steps_spinner.getValue(),
						Double.valueOf(this._delta_time_textField.getText()));
			} catch (NumberFormatException ex) {
				ViewUtils.showErrorMsg("Delta Time must be a number");
				this.setEnableButtons(true);
				this._stopped = true;
			} catch (Exception ex) {
				ViewUtils.showErrorMsg(ex.getMessage());
				this.setEnableButtons(true);
				this._stopped = true;
			}
		});

		this._toolBar.add(this._runButton);
		// ----------------------------------------------------------------------

		this._toolBar.addSeparator();

		// - Stop Button --------------------------------------------------------
		this._stopButton = new JButton();
		this._stopButton.setToolTipText("Stop the simulator");
		this._stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this._stopButton.addActionListener((e) -> this._stopped = true);
		this._toolBar.add(this._stopButton);
		// ----------------------------------------------------------------------

		// - Steps spinner ------------------------------------------------------
		this._toolBar.add(new JLabel("Steps: "));
		this._steps_spinner = new JSpinner(new SpinnerNumberModel(10000, 1, 10000, 100));
		Dimension d = this._steps_spinner.getPreferredSize();
		this._steps_spinner.setMinimumSize(new Dimension(d.width - 20, d.height * 2));
		this._steps_spinner.setMaximumSize(new Dimension(d.width - 20, d.height * 2));
		this._steps_spinner.setPreferredSize(new Dimension(d.width - 20, d.height * 2));
		this._steps_spinner.setToolTipText("Simulation steps to run: 1-10000");
		this._toolBar.add(this._steps_spinner);
		// ----------------------------------------------------------------------

		// - Delta time text field ----------------------------------------------
		this._toolBar.add(new JLabel("Delta-Time: "));
		this._delta_time_textField = new JTextField(6);
		this._delta_time_textField.setText("0.03");
		d = this._delta_time_textField.getPreferredSize();
		this._delta_time_textField.setMinimumSize(new Dimension((d.width - 20) / 2, d.height * 2));
		this._delta_time_textField.setMaximumSize(new Dimension(d.width - 20, d.height * 2));
		this._delta_time_textField.setPreferredSize(new Dimension(d.width - 40, d.height * 2));
		this._delta_time_textField.setToolTipText("Real time (seconds) corresponding to a step");
		this._toolBar.add(this._delta_time_textField);
		// ----------------------------------------------------------------------

		this._toolBar.add(Box.createGlue()); // this aligns the button to the right
		this._toolBar.addSeparator();

		// - Quit Button --------------------------------------------------------
		this._exitButton = new JButton();
		this._exitButton.setToolTipText("Exit");
		this._exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this._exitButton.addActionListener((e) -> ViewUtils.quit(this));
		this._toolBar.add(this._exitButton);
		// ----------------------------------------------------------------------

		this._fc = new JFileChooser();
		this._fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));

		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
	}

	private void run_sim(int n, double dt) {
		if (n > 0 && !this._stopped) {
			try {
				long startTime = System.currentTimeMillis();
				this._ctrl.advance(dt);
				long stepTime = System.currentTimeMillis() - startTime;
				long delay = (long) (dt * 1000 - stepTime);
				Thread.sleep(delay > 0 ? delay : 0);
				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				ViewUtils.showErrorMsg(e.getMessage());
				this.setEnableButtons(true);
				this._stopped = true;
			}
		} else {
			this.setEnableButtons(true);
			this._stopped = true;
		}
	}

	private void setEnableButtons(boolean b) {
		this._openButton.setEnabled(b);
		this._viewerButton.setEnabled(b);
		this._regionsButton.setEnabled(b);
		this._runButton.setEnabled(b);
		this._exitButton.setEnabled(b);
		this._steps_spinner.setEnabled(b);
		this._delta_time_textField.setEnabled(b);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}
}
