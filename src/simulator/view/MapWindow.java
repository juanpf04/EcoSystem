package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;


class MapWindow extends JFrame implements EcoSysObserver {
	private Controller _ctrl;
	private AbstractMapViewer _viewer;
	private Frame _parent;

	MapWindow(Frame parent, Controller ctrl) { // FIXME en el enunciado está mal la constructora??? 
	 super("[MAP VIEWER]");
	 _ctrl = ctrl;
	 _parent = parent;
	 intiGUI();
	 // TODO registrar this como observador
	 
	 this._ctrl.addObserver(this);
	 }

	private void intiGUI() {
	 JPanel mainPanel = new JPanel(new BorderLayout());
	 // TODO poner contentPane como mainPanel
	 
	 this.setContentPane(mainPanel); // TODO Check
	 
	 // TODO crear el viewer y añadirlo a mainPanel (en el centro)
	 
	 MapViewer map = new MapViewer();
	 
	 mainPanel.add(map, BorderLayout.CENTER);
	 
	 // TODO en el método windowClosing, eliminar ‘MapWindow.this’ de los observadores
	 //addWindowListener(new WindowListener() { … });
	 pack();
	 if (_parent != null)
	 setLocation(
	 _parent.getLocation().x + _parent.getWidth()/2- getWidth()/2,
	 _parent.getLocation().y + _parent.getHeight()/2- getHeight()/2);
	 setResizable(false);
	 setVisible(true);
	 }

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
	// TODO otros métodos van aquí….
}
