package simulator.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import simulator.control.Controller;
import simulator.model.Observable;
import simulator.model.ViewObserver;

public class EcoMenu extends JMenuBar implements ViewObserver, Observable<ViewObserver> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private ViewActions _actions;
	private ControlPanel _toolBar;
	private StatusBar _statusBar;

	private List<ViewObserver> _observers;

	public EcoMenu(Controller ctrl, ViewActions actions, ControlPanel toolBar, StatusBar statusBar) {
		this._ctrl = ctrl;
		this._actions = actions;
		this._toolBar = toolBar;
		this._statusBar = statusBar;
		this._ctrl.addMenu(this);
		this.initGUI();
		this._observers = new LinkedList<>();
		this._ctrl.addObserver(this);
		this._ctrl.addObserver(toolBar);
		this._ctrl.addObserver((ViewObserver) statusBar);
	}

	private void initGUI() {
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

		// -------------------------------------------------------------------
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		this.add(file);

		JMenuItem soup = new JMenuItem("soup");
		soup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
		soup.addActionListener((e) -> System.out.print("soup seleccionado"));
		file.add(soup);
		JMenuItem pate = new JMenuItem("pate");
		pate.addActionListener((e) -> System.out.print("pate seleccionado"));
		file.add(pate);
		JMenuItem salad = new JMenuItem("salad");
		salad.addActionListener((e) -> System.out.print("salad seleccionado"));
		file.add(salad);
		// -------------------------------------------------------------------

		// -------------------------------------------------------------------
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		this.add(edit);

		JMenuItem regions = new JMenuItem("Regions");
		regions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.SHIFT_MASK));
		regions.addActionListener((e) -> System.out.print("hacer"));
		edit.add(regions);
		JMenuItem steps = new JMenuItem("Steps");
		steps.addActionListener((e) -> System.out.print("hacer"));
		edit.add(steps);
		JMenuItem delta_time = new JMenuItem("Delta time");
		delta_time.addActionListener((e) -> System.out.print("hacer"));
		edit.add(delta_time);
		JMenuItem delay = new JMenuItem("Delay");
		delay.addActionListener((e) -> System.out.print("hacer"));
		edit.add(delay);
		// -------------------------------------------------------------------

		// -------------------------------------------------------------------
		JMenu mode = new JMenu("Mode");
		mode.setMnemonic(KeyEvent.VK_M);
		this.add(mode);

		ButtonGroup mode_group = new ButtonGroup();

		JRadioButtonMenuItem normal = new JRadioButtonMenuItem("Normal");
		// TODO añadir actionlistener
		mode.add(normal);
		mode_group.add(normal);
		normal.setSelected(true);
		JRadioButtonMenuItem thread = new JRadioButtonMenuItem("Thread");
		// TODO añadir actionlistener
		mode.add(thread);
		mode_group.add(thread);
		JRadioButtonMenuItem worker = new JRadioButtonMenuItem("Worker");
		// TODO añadir actionlistener
		mode.add(worker);
		mode_group.add(worker);

		// -------------------------------------------------------------------

		// -------------------------------------------------------------------
		JMenu window = new JMenu("Window");
		window.setMnemonic(KeyEvent.VK_W);
		this.add(window);

		JMenuItem viewer = new JMenuItem("New Map Viewer");
		viewer.addActionListener((e) -> new MapWindow(ViewUtils.getWindow(this), this._ctrl));
		window.add(viewer);
		// TODO añadir poder ocultar y mostrar el status bar y toolbar
		// poder crear una ventana nueva del viewer

		// --------- View: standard (normal) / spectacular (icons) ---------------

		JMenu view = new JMenu("Viewer");
		view.setMnemonic(KeyEvent.VK_V);
		JRadioButtonMenuItem standard_view = new JRadioButtonMenuItem("Standard View");
		standard_view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
		// TODO standard_view.addActionListener((e) -> this._ctrl.));

		JRadioButtonMenuItem spectacular_view = new JRadioButtonMenuItem("Spectacular View");
		// TODO spectacular_view.addActionListener((e) -> this._ctrl.));
		spectacular_view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.SHIFT_MASK));

		ButtonGroup view_group = new ButtonGroup();
		view_group.add(standard_view);
		view_group.add(spectacular_view);
		view.add(standard_view);
		view.add(spectacular_view);

		window.add(view);

		window.addSeparator();
		// ------------ Theme: Dark/Light --------------------

		JMenu appearance = new JMenu("Appearance");
		window.add(appearance);

		JCheckBoxMenuItem toolBar = new JCheckBoxMenuItem("ToolBar");
		toolBar.setSelected(true);
		toolBar.addActionListener((e) -> _toolBar.setVisible(toolBar.isSelected()));
		appearance.add(toolBar);
		JCheckBoxMenuItem statusBar = new JCheckBoxMenuItem("StatusBar");
		statusBar.setSelected(true);
		statusBar.addActionListener((e) -> _statusBar.setVisible(statusBar.isSelected()));
		appearance.add(statusBar);

		JMenu theme = new JMenu("Theme");
		theme.setMnemonic(KeyEvent.VK_T);

		JRadioButtonMenuItem light_theme = new JRadioButtonMenuItem("Light Theme");
		light_theme.addActionListener((e) -> notify_on_light_theme());
		light_theme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.SHIFT_MASK));

		JRadioButtonMenuItem dark_theme = new JRadioButtonMenuItem("Dark Theme");
		dark_theme.addActionListener((e) -> notify_on_dark_theme());
		dark_theme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.SHIFT_MASK));

		ButtonGroup theme_group = new ButtonGroup();
		theme_group.add(light_theme);
		theme_group.add(dark_theme);
		theme.add(light_theme);
		theme.add(dark_theme);
		light_theme.setSelected(true);

		appearance.add(theme);

		// -------------------------------------------------------------------
		JMenu run = new JMenu("Run");
		run.setForeground(Color.LIGHT_GRAY); // TODO PARA EL MODO OSCURO
		run.setMnemonic(KeyEvent.VK_R);
		this.add(run);

		JMenuItem run_ = new JMenuItem("Run");
		run_.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		run_.addActionListener((e) -> System.out.print("run seleccionado"));
		run.add(run_);
		JMenuItem stop = new JMenuItem("Stop");
		stop.addActionListener((e) -> System.out.print("stop seleccionado"));
		run.add(stop);
		// -------------------------------------------------------------------
	}

	private void notify_on_dark_theme() {
		for (ViewObserver o : this._observers)
			o.onDarkMode();
	}

	private void notify_on_light_theme() {
		for (ViewObserver o : this._observers)
			o.onLightMode();
	}

	// Observable

	@Override
	public void addObserver(ViewObserver o) {
		if (!this._observers.contains(o))
			this._observers.add(o);

		this.notify_on_register(o);
	}

	@Override
	public void removeObserver(ViewObserver o) {
		this._observers.remove(o);
	}

	// Notifications

	private void notify_on_register(ViewObserver o) {
		o.onLightMode();
		o.onStandardView();
	}

	@Override
	public void onDarkMode() {
		this.setBackground(Color.DARK_GRAY);
	}

	@Override
	public void onLightMode() {
		this.setBackground(Color.WHITE);
	}

	@Override
	public void onSpectacularView() {
	}

	@Override
	public void onStandardView() {
	}

}
