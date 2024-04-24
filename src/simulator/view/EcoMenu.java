package simulator.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import simulator.control.Controller;

public class EcoMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	public EcoMenu(Controller ctrl) {
		this._ctrl = ctrl;
		this.initGUI();
	}

	private void initGUI() {
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

		JMenu starter = new JMenu("starter");
		starter.setMnemonic(KeyEvent.VK_S);
		JMenu maincourse = new JMenu("mainCourse");
		JMenu desserts = new JMenu("desserts");
		this.add(starter);
		this.add(maincourse);
		this.add(desserts);

		JMenuItem soup = new JMenuItem("soup");
		soup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
		soup.addActionListener((e) -> System.out.print("soup seleccionado"));
		JMenuItem pate = new JMenuItem("pate");
		pate.addActionListener((e) -> System.out.print("pate seleccionado"));
		JMenuItem salad = new JMenuItem("salad");
		salad.addActionListener((e) -> System.out.print("salad seleccionado"));
		starter.add(soup);
		starter.add(pate);
		starter.add(salad);

		JMenuItem fish = new JMenuItem("Haddock");
		maincourse.add(fish);
		JMenu steak = new JMenu("Steak");
		JMenuItem rare = new JMenuItem("Rare");
		JMenuItem welldone = new JMenuItem("WellDone");
		steak.add(rare);
		steak.add(welldone);
		maincourse.add(steak);
		JMenuItem pie = new JMenuItem("Pie");
		maincourse.add(pie);
		maincourse.addSeparator();
		JRadioButtonMenuItem chips = new JRadioButtonMenuItem("Chips");
		JRadioButtonMenuItem bp = new JRadioButtonMenuItem("BakedPotato");
		JRadioButtonMenuItem vege = new JRadioButtonMenuItem("Vegetables");
		ButtonGroup sides = new ButtonGroup();
		sides.add(chips);
		sides.add(bp);
		sides.add(vege);
		maincourse.add(chips);
		maincourse.add(bp);
		maincourse.add(vege);

		JCheckBoxMenuItem cake = new JCheckBoxMenuItem("Cake");
		desserts.add(cake);
		JCheckBoxMenuItem sorbet = new JCheckBoxMenuItem("Sorbet");
		desserts.add(sorbet);
		JMenu icecream = new JMenu("IceCream");
		JCheckBoxMenuItem choc = new JCheckBoxMenuItem("Chocolate");
		JCheckBoxMenuItem vani = new JCheckBoxMenuItem("Vanilla");
		icecream.add(choc);
		icecream.add(vani);
		desserts.add(icecream);
		
		
		//--------- View: standard (normal) / spectacular (icons)---------------
		
		JMenu view = new JMenu("View");
		view.setMnemonic(KeyEvent.VK_V);
		JRadioButtonMenuItem standard_view = new JRadioButtonMenuItem("Standard View");
		standard_view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
		//TODO standard_view.addActionListener((e) -> this._ctrl.));
		
		JRadioButtonMenuItem spectacular_view = new JRadioButtonMenuItem("Spectacular View");
		//TODO spectacular_view.addActionListener((e) -> this._ctrl.));
		spectacular_view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.SHIFT_MASK));
		
		ButtonGroup view_group = new ButtonGroup();
		view_group.add(standard_view);
		view_group.add(spectacular_view);
		view.add(standard_view);
		view.add(spectacular_view);
		
		this.add(view);
		
		//------------ Theme: Dark/Light --------------------
		
		JMenu theme = new JMenu("Theme");
		theme.setMnemonic(KeyEvent.VK_T);
		
		JRadioButtonMenuItem light_theme = new JRadioButtonMenuItem("Light Theme");
		light_theme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.SHIFT_MASK));
		
		JRadioButtonMenuItem dark_theme = new JRadioButtonMenuItem("Dark Theme");
		dark_theme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.SHIFT_MASK));
		
		ButtonGroup theme_group = new ButtonGroup();
		theme_group.add(light_theme);
		theme_group.add(dark_theme);
		theme.add(light_theme);
		theme.add(dark_theme);
		
		this.add(theme);

	}
}
