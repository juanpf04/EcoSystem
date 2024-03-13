package simulator.view;

import javax.swing.JPanel;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class InfoTable extends JPanel {
	String _title;
	TableModel _tableModel;

	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {
	 // TODO cambiar el layout del panel a BorderLayout()
	 // TODO añadir un borde con título al JPanel, con el texto _title
	 // TODO añadir un JTable (con barra de desplazamiento vertical) que use
	 //_tableModel
		
	JTable table = new JTable(this._tableModel);
	JScrollPane scroll = new
			JScrollPane (table , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	 }
}