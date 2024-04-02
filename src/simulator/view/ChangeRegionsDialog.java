package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;

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
	private String[] _headers = { "Key", "Value", "Description" };

	// TODO en caso de ser necesario, añadir los atributos aquí…
	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		_ctrl = ctrl;
		initGUI();
		// TODO registrar this como observer;
		
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		// TODO crea varios paneles para organizar los componentes visuales en el
		// dialogo, y añadelos al mainpanel. P.ej., uno para el texto de ayuda,
		// uno para la tabla, uno para los combobox, y uno para los botones.
		
		JPanel txt = new JPanel();
		JPanel table = new JPanel();
		JPanel combobox = new JPanel();
		JPanel buttons = new JPanel();
		
		
		mainPanel.add(txt);
		mainPanel.add(table);
		mainPanel.add(combobox);
		mainPanel.add(buttons);
		
		//crear el texto de ayuda que aparece en la parte superior del diálogo y
		// añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)
		
		JLabel help_text = new JLabel(Messages.CHANGE_REGIONS_HELP_DESCRIPTION);
		txt.add(help_text); //FIXME mirar si se hace con un JLabel o un TextArea
		
		// _regionsInfo se usará para establecer la información en la tabla
		_regionsInfo = Main._regions_factory.get_info();
		
		// _dataTableModel es un modelo de tabla que incluye todos los parámetros de la region
		_dataTableModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				
				return column == 1;
				
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		
		//crear un JTable que use _dataTableModel, y añadirlo al diálogo
		
		JTable dialog_table = new JTable(this._dataTableModel);
		table.add(dialog_table);
		
		// _regionsModel es un modelo de combobox que incluye los tipos de regiones
		_regionsModel = new DefaultComboBoxModel<>();
		
		// TODO añadir la descripción de todas las regiones a _regionsModel, para eso
		//usa la clave “desc” o “type” de los JSONObject en _regionsInfo,
		//ya que estos nos dan información sobre lo que puede crear la factoría.
		
		
		// TODO crear un combobox que use _regionsModel y añadirlo al diálogo.
		
		JComboBox<String> regions_combobox = new JComboBox<String>(this._regionsModel);
		combobox.add(regions_combobox);
		
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel, _fromColModel y _toColModel.
		JComboBox<String> fromRowModel_combobox = new JComboBox<String>(this._fromRowModel);
		JComboBox<String> toRowModel_combobox = new JComboBox<String>(this._toRowModel);
		JComboBox<String> fromColModel_combobox = new JComboBox<String>(this._fromColModel);
		JComboBox<String> toColModel_combobox = new JComboBox<String>(this._toColModel);
		// FUMADA DE LAS GRANDES, MIRAR
		
		// TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
		
		// ????
		
		//crear los botones OK y Cancel y añadirlos al diálogo.
		
		JButton ok = new JButton();
		JButton cancel = new JButton();
		
		buttons.add(ok);
		buttons.add(cancel);
		
		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open(Frame parent) {
		setLocation(//
				parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, //
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}
	// TODO el resto de métodos van aquí…

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub
		
	}
}
