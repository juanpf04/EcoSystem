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
import simulator.model.Region;
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

	private String[] _headers = { "Key", "Value", "Description" };

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		this._regionsInfo = Main._regions_factory.get_info();
		this.initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// - Help text -----------------------------------------------------
		JLabel helpText = new JLabel(Messages.CHANGE_REGIONS_DESCRIPTION);
		helpText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mainPanel.add(helpText);
		// -----------------------------------------------------------------

		// - Table ---------------------------------------------------------
		JPanel table = new JPanel();

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

		JTable dataTable = new JTable(this._dataTableModel);
		dataTable.getTableHeader().setEnabled(false);
		table.add(new JScrollPane(dataTable));
		mainPanel.add(table);
		// -----------------------------------------------------------------

		// - ComboBoxes ----------------------------------------------------
		JPanel comboBoxes = new JPanel();

		this._regionsModel = new DefaultComboBoxModel<>();

		comboBoxes.add(new JLabel("Region type: "));
		JComboBox<String> regions = new JComboBox<String>(this._regionsModel);
		regions.addActionListener((e) -> {
			for (int i = 0; i < this._dataTableModel.getRowCount(); i++)
				this._dataTableModel.removeRow(i);

			JSONObject info = this._regionsInfo.get(regions.getSelectedIndex());

			System.out.println(info.toString());
			JSONObject data = info.getJSONObject(Messages.DATA_KEY);
			System.out.println(data.toString());

			Iterator<String> it = data.keys();
			while (it.hasNext()) {
				String key = it.next();
				Object[] o = { key, 0, data.getString(key) };
				this._dataTableModel.addRow(o);
			}

			this._dataTableModel.fireTableDataChanged();
		});
		comboBoxes.add(regions);

		this._fromRowModel = new DefaultComboBoxModel<String>();
		this._toRowModel = new DefaultComboBoxModel<String>();
		this._fromColModel = new DefaultComboBoxModel<String>();
		this._toColModel = new DefaultComboBoxModel<String>();

		JComboBox<String> fromRow = new JComboBox<String>(this._fromRowModel);
		JComboBox<String> toRow = new JComboBox<String>(this._toRowModel);
		JComboBox<String> fromCol = new JComboBox<String>(this._fromColModel);
		JComboBox<String> toCol = new JComboBox<String>(this._toColModel);

		comboBoxes.add(new JLabel("Row from/to: "));
		comboBoxes.add(fromRow);
		comboBoxes.add(toRow);
		comboBoxes.add(new JLabel("Column from/to: "));
		comboBoxes.add(fromCol);
		comboBoxes.add(toCol);

		mainPanel.add(comboBoxes);
		// -----------------------------------------------------------------

		// - Buttons -------------------------------------------------------
		JPanel buttons = new JPanel();

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> setVisible(false));
		buttons.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener((e) -> { // FIXME caso no hay datos

			JSONObject data = new JSONObject();

			for (int i = 0; i < this._dataTableModel.getRowCount(); i++) {
				data.put((String) this._dataTableModel.getValueAt(i, 0), this._dataTableModel.getValueAt(i, 1));
			}

			JSONObject r_json = new JSONObject();

			r_json.put(Messages.TYPE_KEY, this._regionsModel.getSelectedItem());
			r_json.put(Messages.DATA_KEY, data);

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

			try {
				this._ctrl.set_regions(rs);
			} catch (Exception ex) {
				ViewUtils.showErrorMsg("FALALAL");
			}
			// TODO
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
		for (JSONObject jo : this._regionsInfo) {
			this._regionsModel.addElement(jo.getString("type"));
		}

		for (int i = 0; i < map.get_rows(); i++) {
			this._toRowModel.addElement(String.valueOf(i));
			this._fromRowModel.addElement(String.valueOf(i));
		}

		for (int i = 0; i < map.get_cols(); i++) {
			this._toColModel.addElement(String.valueOf(i));
			this._fromColModel.addElement(String.valueOf(i));
		}
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._toRowModel.removeAllElements();
		this._fromRowModel.removeAllElements();
		for (int i = 0; i < map.get_rows(); i++) {
			this._toRowModel.addElement(String.valueOf(i));
			this._fromRowModel.addElement(String.valueOf(i));
		}

		this._toColModel.removeAllElements();
		this._fromColModel.removeAllElements();
		for (int i = 0; i < map.get_cols(); i++) {
			this._toColModel.addElement(String.valueOf(i));
			this._fromColModel.addElement(String.valueOf(i));
		}

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

}
