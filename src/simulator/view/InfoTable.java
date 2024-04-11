package simulator.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;

public class InfoTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String _title;
	TableModel _tableModel;

	InfoTable(String title, TableModel tableModel) {
		this._title = title;
		this._tableModel = tableModel;
		this.initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		// - border ------------------------------------------------------
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		this.setBorder(BorderFactory.createTitledBorder(b, this._title, TitledBorder.LEFT, TitledBorder.TOP));
		// ---------------------------------------------------------------

		// - table with scroll -------------------------------------------
		JTable table = new JTable(this._tableModel);
		table.getTableHeader().setEnabled(false);
		this.add(new JScrollPane(table));
		// ---------------------------------------------------------------
	}
}