package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JLabel;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.Animal.State;

class Exam2Dialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel<State> _statesModel;

	private Controller _ctrl;

	Exam2Dialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		this.initGUI();
	}

	private void initGUI() {
		this.setTitle(Messages.CHANGE_REGIONS_TITLE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// - Born table -----------------------------------------
		StatesTableModel statesModel = new StatesTableModel(this._ctrl);
		InfoTable state = new InfoTable("States", statesModel);
		mainPanel.add(state);
		// ------------------------------------------------------

		// - ComboBoxes ----------------------------------------------------
		JPanel comboBoxes = new JPanel();

		this._statesModel = new DefaultComboBoxModel<>(State.values());

		comboBoxes.add(new JLabel("Animals in State: "));
		JComboBox<State> states = new JComboBox<State>(this._statesModel);
		states.addActionListener((e) -> statesModel.setState((State) _statesModel.getSelectedItem()));
		comboBoxes.add(states);

		mainPanel.add(comboBoxes);
		// -----------------------------------------------------------------

		// - Buttons -------------------------------------------------------
		JPanel buttons = new JPanel();

		JButton cancelButton = new JButton(Messages.CANCEL_BUTTON);
		cancelButton.addActionListener((e) -> setVisible(false));
		buttons.add(cancelButton);

		mainPanel.add(buttons);
		// -----------------------------------------------------------------

		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open(Frame parent) {
		setLocation(parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}
}
