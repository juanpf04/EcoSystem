package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulator.control.Controller;
import simulator.misc.Messages;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ExamDialog extends JDialog implements EcoSysObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultTableModel _dataTableModel;

	private Controller _ctrl;

	private String[] _headers = { "Step", "Deaths" };
	private int step = 0;

	ExamDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		this.initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setTitle("Death Animals per step");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// - Table ---------------------------------------------------------
		this._dataTableModel = new DefaultTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
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

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
//		for (int i = this._dataTableModel.getRowCount() - 1; i >= 0; i--)
//			this._dataTableModel.removeRow(i);

		this._dataTableModel.setRowCount(0);
		this.step = 0;
		this._dataTableModel.fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		Object[] o = { this.step++, animals.stream().filter((a) -> a.get_state() == State.DEAD).count() };
		this._dataTableModel.addRow(o);

		this._dataTableModel.fireTableDataChanged();
	}
}
