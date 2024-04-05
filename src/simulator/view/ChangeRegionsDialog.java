package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;

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

	// TODO en caso de ser necesario, aÃ±adir los atributos aquÃ­â€¦
	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
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
		// dialogo, y aÃ±adelos al mainpanel. P.ej., uno para el texto de ayuda,
		// uno para la tabla, uno para los combobox, y uno para los botones.
		
		JPanel table = new JPanel();
		JPanel combobox = new JPanel();
		JPanel buttons = new JPanel();
		
		
		

		
		//crear el texto de ayuda que aparece en la parte superior del diÃ¡logo y
		// aÃ±adirlo al panel correspondiente diÃ¡logo (Ver el apartado Figuras)
		
		JLabel help_text = new JLabel(Messages.CHANGE_REGIONS_DESCRIPTION);
		help_text.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		mainPanel.add(help_text);

		
		// _regionsInfo se usarÃ¡ para establecer la informaciÃ³n en la tabla
		this._regionsInfo = Main._regions_factory.get_info();
		
		// _dataTableModel es un modelo de tabla que incluye todos los parÃ¡metros de la region
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
		
		//crear un JTable que use _dataTableModel, y aÃ±adirlo al diÃ¡logo
		
		for(JSONObject jo: this._regionsInfo) {
			//TODO 
		}
		JTable dialog_table = new JTable(this._dataTableModel);
		JScrollPane tablapro = new JScrollPane(dialog_table);
		tablapro.setPreferredSize(new Dimension(600,150));
		table.add(tablapro);
		mainPanel.add(table);
		
		// _regionsModel es un modelo de combobox que incluye los tipos de regiones
		this._regionsModel = new DefaultComboBoxModel<>();
		
		// TODO aÃ±adir la descripciÃ³n de todas las regiones a _regionsModel, para eso
		//usa la clave â€œdescâ€� o â€œtypeâ€� de los JSONObject en _regionsInfo,
		//ya que estos nos dan informaciÃ³n sobre lo que puede crear la factorÃ­a.
		
		//FIXME AQUI NO HAY QUE HACER NADA NO? YA ESTA EN EL OBSERVER
		
		
		// TODO crear un combobox que use _regionsModel y aÃ±adirlo al diÃ¡logo.
		
		JLabel region_type = new JLabel("Region type: ");
		JComboBox<String> regions_combobox = new JComboBox<String>(this._regionsModel);
		combobox.add(region_type);
		combobox.add(regions_combobox);
		
		
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel, _fromColModel y _toColModel.
		
		this._fromRowModel = new DefaultComboBoxModel<String>();
		this._toRowModel = new DefaultComboBoxModel<String>();
		this._fromColModel = new DefaultComboBoxModel<String>();
		this._toColModel = new DefaultComboBoxModel<String>();
		// FUMADA DE LAS GRANDES, MIRAR
		
		// TODO crear 4 combobox que usen estos modelos y aÃ±adirlos al diÃ¡logo.
		JComboBox<String> fromRowModel_combobox = new JComboBox<String>(this._fromRowModel);
		JComboBox<String> toRowModel_combobox = new JComboBox<String>(this._toRowModel);
		JComboBox<String> fromColModel_combobox = new JComboBox<String>(this._fromColModel);
		JComboBox<String> toColModel_combobox = new JComboBox<String>(this._toColModel);
		
		JLabel row_from_to = new JLabel("Row from/to: ");
		JLabel col_from_to = new JLabel("Col from/to: ");
		
		combobox.add(row_from_to);
		combobox.add(fromRowModel_combobox);
		combobox.add(toRowModel_combobox);
		combobox.add(col_from_to);
		combobox.add(fromColModel_combobox);
		combobox.add(toColModel_combobox);
		
		mainPanel.add(combobox);
		
		//crear los botones OK y Cancel y aÃ±adirlos al diÃ¡logo.
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((e) -> setVisible(false));
		JButton ok = new JButton("OK");
		ok.addActionListener((e)->{
			System.out.println("HACER");
		});
		
		buttons.add(cancel);
		buttons.add(ok);
		
		mainPanel.add(buttons);
		
		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaÃ±o
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
	// TODO el resto de mÃ©todos van aquÃ­â€¦

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

		_regionsModel.removeAllElements();
		for (JSONObject jo: this._regionsInfo) {
			_regionsModel.addElement(jo.getString("type"));
		}
		
		_toRowModel.removeAllElements();
		_fromRowModel.removeAllElements();
		for(int i= 0; i < map.get_rows(); i++) {
			_toRowModel.addElement(String.valueOf(i));
			_fromRowModel.addElement(String.valueOf(i));			
		}
		
		_toColModel.removeAllElements();
		_fromColModel.removeAllElements();
		for(int i= 0; i < map.get_cols(); i++) {
			_toColModel.addElement(String.valueOf(i));
			_fromColModel.addElement(String.valueOf(i));			
		}	
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
