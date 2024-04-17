package simulator.view;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.misc.Messages;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel<String> _regionsModel;

	private DefaultComboBoxModel<String> _fromRowModel;
	private DefaultComboBoxModel<String> _toRowModel;
	private DefaultComboBoxModel<String> _fromColModel;
	private DefaultComboBoxModel<String> _toColModel;

	private DefaultTableModel _dataTableModel;

	private Controller _ctrl;

	private List<JSONObject> _regionsInfo;

	private String[] _headers = { Messages.KEY, Messages.VALUE, Messages.DESCRIPTION };

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		this._regionsInfo = Main._regions_factory.get_info();
		this.initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setTitle(Messages.CHANGE_REGIONS_TITLE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// - Help text -----------------------------------------------------
		JLabel helpText = new JLabel(Messages.CHANGE_REGIONS_DESCRIPTION);
		helpText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mainPanel.add(helpText);
		// -----------------------------------------------------------------

		// - Table ---------------------------------------------------------
		JPanel tablePanel = new JPanel();

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

		JTable table = new JTable(this._dataTableModel);
		table.getTableHeader().setEnabled(false);
//		table.setPreferredSize(new Dimension(700, 400));		
		JScrollPane scroll = new JScrollPane(table);
//		scroll.setPreferredSize(new Dimension(700, 400));
		tablePanel.add(scroll);
		mainPanel.add(tablePanel);
		// -----------------------------------------------------------------

		// - ComboBoxes ----------------------------------------------------
		JPanel comboBoxes = new JPanel();

		this._regionsModel = new DefaultComboBoxModel<>();

		comboBoxes.add(new JLabel(Messages.REGION_TYPE));
		JComboBox<String> regions = new JComboBox<String>(this._regionsModel);
		regions.addActionListener((e) -> updateTable());
		comboBoxes.add(regions);

		this._fromRowModel = new DefaultComboBoxModel<String>();
		this._toRowModel = new DefaultComboBoxModel<String>();
		this._fromColModel = new DefaultComboBoxModel<String>();
		this._toColModel = new DefaultComboBoxModel<String>();

		JComboBox<String> fromRow = new JComboBox<String>(this._fromRowModel);
		fromRow.addActionListener((e) -> fromRowAction());

		JComboBox<String> toRow = new JComboBox<String>(this._toRowModel);

		JComboBox<String> fromCol = new JComboBox<String>(this._fromColModel);
		fromCol.addActionListener((e) -> fromColAction());

		JComboBox<String> toCol = new JComboBox<String>(this._toColModel);

		comboBoxes.add(new JLabel(Messages.ROW_FROM_TO));
		comboBoxes.add(fromRow);
		comboBoxes.add(toRow);
		comboBoxes.add(new JLabel(Messages.COL_FROM_TO));
		comboBoxes.add(fromCol);
		comboBoxes.add(toCol);

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
				this._ctrl.set_regions(tableToJSON());
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
		for (JSONObject r_json : this._regionsInfo) {
			this._regionsModel.addElement(r_json.getString(Messages.TYPE_KEY));
		}

		this.setComboBoxes(map.get_rows(), map.get_cols());
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.setComboBoxes(map.get_rows(), map.get_cols());
		this._toColModel.setSelectedItem("0");
		this._toRowModel.setSelectedItem("0");
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

		JSONObject info = this._regionsInfo.get(this._regionsModel.getIndexOf(this._regionsModel.getSelectedItem()));
		JSONObject data = info.getJSONObject(Messages.DATA_KEY);

		Iterator<String> it = data.keys();
		while (it.hasNext()) {
			String key = it.next();
			Object[] o = { key, null, data.getString(key) };
			this._dataTableModel.addRow(o);
		}

		this._dataTableModel.fireTableDataChanged();
	}

	private JSONObject tableToJSON() {
		JSONObject region_data = new JSONObject();

		for (int i = 0; i < this._dataTableModel.getRowCount(); i++) {
			region_data.put((String) this._dataTableModel.getValueAt(i, 0), this._dataTableModel.getValueAt(i, 1));
		}

		JSONObject r_json = new JSONObject();

		r_json.put(Messages.TYPE_KEY, this._regionsModel.getSelectedItem());
		r_json.put(Messages.DATA_KEY, region_data);

		JSONObject reg = new JSONObject();

		reg.put(Messages.SPEC_KEY, r_json);

		JSONArray col = new JSONArray();
		col.put(this._fromColModel.getSelectedItem());
		col.put(this._toColModel.getSelectedItem());
		JSONArray row = new JSONArray();
		row.put(this._fromRowModel.getSelectedItem());
		row.put(this._toRowModel.getSelectedItem());

		reg.put(Messages.COLUMN_KEY, col);
		reg.put(Messages.ROW_KEY, row);

		JSONArray regs = new JSONArray();

		regs.put(reg);

		JSONObject rs = new JSONObject();

		rs.put(Messages.REGIONS_KEY, regs);

		return rs;
	}

	private void setComboBoxes(int rows, int cols) {
		this._fromColModel.removeAllElements();

		for (int i = 0; i < cols; i++)
			this._fromColModel.addElement(String.valueOf(i));

		this._fromRowModel.removeAllElements();

		for (int i = 0; i < rows; i++)
			this._fromRowModel.addElement(String.valueOf(i));

		this.fromColAction();
		this.fromRowAction();
	}

	private void fromRowAction() {
		if (this._fromRowModel.getSize() != 0) {
			String actual_row = (String) this._toRowModel.getSelectedItem();
			this._toRowModel.removeAllElements();

			String selected_row = (String) this._fromRowModel.getSelectedItem();

			int last_row = Integer.valueOf(this._fromRowModel.getElementAt(this._fromRowModel.getSize() - 1));

			for (int i = Integer.valueOf(selected_row); i <= last_row; i++)
				this._toRowModel.addElement(String.valueOf(i));

			if (actual_row != null && Integer.valueOf(actual_row) > Integer.valueOf(selected_row))
				this._toRowModel.setSelectedItem(actual_row);
		}
	}

	private void fromColAction() {
		if (this._fromColModel.getSize() != 0) {
			String actual_col = (String) this._toColModel.getSelectedItem();
			this._toColModel.removeAllElements();

			String selected_col = (String) this._fromColModel.getSelectedItem();

			int last_col = Integer.valueOf(this._fromColModel.getElementAt(this._fromColModel.getSize() - 1));

			for (int i = Integer.valueOf(selected_col); i <= last_col; i++)
				this._toColModel.addElement(String.valueOf(i));

			if (actual_col != null && Integer.valueOf(actual_col) > Integer.valueOf(selected_col))
				this._toColModel.setSelectedItem(actual_col);
		}
	}
}
