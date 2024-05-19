package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.misc.Messages;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class AddAnimalsDialog extends JDialog implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel<String> _speciesModel;
	private JSpinner _amount;

	private DefaultTableModel _dataTableModel;

	private Controller _ctrl;

	private List<JSONObject> _animalsInfo;

	private String[] _headers = { Messages.KEY, Messages.VALUE, Messages.DESCRIPTION };

	AddAnimalsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		this._animalsInfo = Main._animals_factory.get_info();
		this.initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setTitle("Add Animals");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// - Help text -----------------------------------------------------
		JLabel helpText = new JLabel(Messages.CHANGE_REGIONS_DESCRIPTION);
		helpText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mainPanel.add(helpText);
		// -----------------------------------------------------------------

		// - Table ---------------------------------------------------------
		this._dataTableModel = new DefaultTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};

		this._dataTableModel.setColumnIdentifiers(this._headers);

		JTable table = new JTable(this._dataTableModel) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		table.getTableHeader().setEnabled(false);
		JScrollPane scroll = new JScrollPane(table);
		mainPanel.add(scroll);
		// -----------------------------------------------------------------

		// - ComboBoxes ----------------------------------------------------
		JPanel comboBoxes = new JPanel();

		this._speciesModel = new DefaultComboBoxModel<>();

		comboBoxes.add(new JLabel("Specie type"));
		JComboBox<String> species = new JComboBox<String>(this._speciesModel);
		species.addActionListener((e) -> updateTable());
		comboBoxes.add(species);

		this._amount = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		comboBoxes.add(this._amount);

		mainPanel.add(comboBoxes);
		// -----------------------------------------------------------------

		// - Buttons -------------------------------------------------------
		JPanel buttons = new JPanel();

		JButton cancelButton = new JButton(Messages.CANCEL_BUTTON);
		cancelButton.addActionListener((e) -> setVisible(false));
		buttons.add(cancelButton);

		JButton okButton = new JButton(Messages.OK_BUTTON);
		okButton.addActionListener((e) -> {
			try {
				this._ctrl.set_animals(tableToJSON());
				setVisible(false);
			} catch (Exception ex) {
				ViewUtils.showErrorMsg(ex.getMessage());
			}
		});
		buttons.add(okButton);

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

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for (JSONObject r_json : this._animalsInfo) {
			this._speciesModel.addElement(r_json.getString(Messages.TYPE_KEY));
		}
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}

	private void updateTable() {
		for (int i = this._dataTableModel.getRowCount() - 1; i >= 0; i--)
			this._dataTableModel.removeRow(i);

		JSONObject info = this._animalsInfo.get(this._speciesModel.getIndexOf(this._speciesModel.getSelectedItem()));
		JSONObject data = info.getJSONObject(Messages.DATA_KEY);

		Iterator<String> it = data.keys();

		while (it.hasNext()) {
			String key = it.next();
			Object[] o = { key, null, data.get(key) };
			this._dataTableModel.addRow(o);
		}
	}

	private JSONObject tableToJSON() {
//		JSONObject region_data = new JSONObject();
//
//		for (int i = 0; i < this._dataTableModel.getRowCount(); i++) {
//			String key = this._dataTableModel.getValueAt(i, 0).toString();
//			Object value = this._dataTableModel.getValueAt(i, 1);
//
//			if (value != null && !value.toString().isEmpty())
//				region_data.put(key, Double.parseDouble(value.toString()));
//		}

		JSONObject a_json = new JSONObject();

		a_json.put(Messages.TYPE_KEY, this._speciesModel.getSelectedItem());
//		a_json.put(Messages.DATA_KEY, region_data);

		JSONObject animal = new JSONObject();

		animal.put(Messages.SPEC_KEY, a_json);
		animal.put(Messages.AMOUNT_KEY, this._amount.getValue());

		JSONArray animals = new JSONArray();

		animals.put(animal);

		JSONObject as = new JSONObject();

		as.put(Messages.ANIMALS_KEY, animals);

		return as;
	}

}
