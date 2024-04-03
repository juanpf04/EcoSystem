package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;

import simulator.control.Controller;
import simulator.misc.Messages;

public class MainWindow extends JFrame {

	/**
	 * 
	 */ 


	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super(Messages.GUI_TITLE);
		this._ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		// TODO crear ControlPanel y a�adirlo en PAGE_START de mainPanel
		ControlPanel controlPanel = new ControlPanel(this._ctrl);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		
		// TODO crear StatusBar y a�adirlo en PAGE_END de mainPanel
		StatusBar statusBar = new StatusBar(this._ctrl);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		
		// Definici�n del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/exit.png"));
		
		// TODO crear la tabla de especies y a�adirla a contentPanel.
		
		/*InfoTable species = new InfoTable("Species", new SpeciesTableModel(_ctrl));
		contentPanel.add(species); */
		
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tama�o
		
		//species.setPreferredSize(new Dimension(500, 250));
		
		// TODO crear la tabla de regiones.
		
		//InfoTable regions = new InfoTable("Regions", new RegionsTableModel(_ctrl));
		
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tama�o
		
		//regions.setPreferredSize(new Dimension(500, 250));
		
		// TODO llama a ViewUtils.quit(MainWindow.this) en el m�todo windowClosing
		
		//	this.windowClosing();
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				ViewUtils.quit(MainWindow.this);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		}); // ESTO NO VA, PABLO NOS LA HA LIADO GORDA
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	

}
