package simulator.view;

import simulator.misc.Messages;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.MapInfo;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

@SuppressWarnings("serial")
public class MapViewer extends AbstractMapViewer {

	private int _width;
	private int _height;

	private int _rows;
	private int _cols;

	int _rwidth;
	int _rheight;

	Animal.State _currState;
	Queue<Animal.State> _states;
//	int _state_count;

	volatile private Collection<AnimalInfo> _objs;
	volatile private Double _time;

	private static class SpeciesInfo {
		private Integer _count;
		private Color _color;

		SpeciesInfo(Color color) {
			_count = 0;
			_color = color;
		}
	}

	Map<String, SpeciesInfo> _kindsInfo = new HashMap<>();

	private Font _font = new Font("Arial", Font.BOLD, 12);

	private boolean _showHelp;

	public MapViewer() {
		this.initGUI();
	}

	private void initGUI() {

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'h':
					_showHelp = !_showHelp;
					break;
				case 's':
					// Using the modulo
//					Animal.State[] states = Animal.State.values();
//					
//					_state_count++;
//					
//					if(states.length == _state_count) { 
//						_state_count = -1;
//						_currState = null;
//					}
//					else
//					_currState = states[_state_count];

					// Using a queue
					_states.add(_currState);
					_currState = _states.remove();

				default:
				}
				repaint();
			}

		});

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}
		});

		this._currState = null;

		this._states = new LinkedList<>();

		for (Animal.State s : Animal.State.values())
			this._states.add(s);

//		this._state_count = -1;

		this._showHelp = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Change the font
		g.setFont(this._font);

		// Draw white background
		gr.setBackground(Color.WHITE);
		gr.clearRect(0, 0, this._width, this._height);

		// Draw all (animals, time, etc.)
		if (this._objs != null)
			drawObjects(gr, this._objs, this._time);

		// Draw the help messages
		if (this._showHelp) {
			gr.setColor(Color.RED);
			gr.drawString(Messages.HELP_HELP_VIEWER, 10, 15);
			gr.drawString(Messages.STATE_HELP_VIEWER, 10, 30);
		}

	}

	private boolean visible(AnimalInfo a) {
		return this._currState == null || a.get_state().equals(this._currState);
	}

	private void drawObjects(Graphics2D g, Collection<AnimalInfo> animals, Double time) {

		// Draw regions grid
		g.setColor(Color.LIGHT_GRAY);

		for (int i = 0; i <= this._rows; i++)
			g.drawLine(0, this._rheight * i, this._width, this._rheight * i);

		for (int i = 0; i <= this._cols; i++)
			g.drawLine(this._rwidth * i, 0, this._rwidth * i, this._height);

		// Draw animals
		for (AnimalInfo a : animals) {

			// if the animal is not visible, skip
			if (!visible(a))
				continue;

			SpeciesInfo esp_info = this._kindsInfo.get(a.get_genetic_code());

			if (esp_info == null) {
				esp_info = new SpeciesInfo(ViewUtils.get_color(a.get_genetic_code()));
				this._kindsInfo.put(a.get_genetic_code(), esp_info);
			}

			esp_info._count++;

			g.setColor(esp_info._color);
			g.setBackground(esp_info._color);
			int size = (int) Math.round(a.get_age()) + 2;
			int half_size = size / 2;
			int x = (int) a.get_position().getX() - half_size;
			int y = (int) a.get_position().getY() - half_size;
			g.fillRoundRect(x, y, size, size, half_size, half_size);
		}

		int x = 20;
		int y = this._height - 30;

		// Draw State
		if (this._currState != null) {
			g.setColor(Color.BLUE); // ViewUtils.get_color(this._currState)); // RGB mode
			this.drawStringWithRect(g, x, y, "State: " + this._currState + " ");
			y -= 20;
		}

		// Draw time
		g.setColor(Color.MAGENTA); // ViewUtils.get_color(time)); // RGB mode
		this.drawStringWithRect(g, x, y, "Time: " + String.format("%.3f ", time));
		y -= 20;

		// Draw animals count
		for (Entry<String, SpeciesInfo> e : _kindsInfo.entrySet()) {
			g.setColor(e.getValue()._color);
			this.drawStringWithRect(g, x, y, e.getKey() + ": " + e.getValue()._count + " ");
			e.getValue()._count = 0;
			y -= 20;
		}
	}

	// Draw text inside of a rectangle
	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {
		this._objs = objs;
		this._time = time;
		this.repaint();
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._width = map.get_width();
		this._height = map.get_height();
		this._cols = map.get_cols();
		this._rows = map.get_rows();
		this._rwidth = map.get_region_width();
		this._rheight = map.get_region_height();

		// Esto cambia el tamaño del componente, y así cambia el tamaño de la ventana
		// porque en MapWindow llamamos a pack() después de llamar a reset
		this.setPreferredSize(new Dimension(map.get_width(), map.get_height()));

		// Draw the actual
		this.update(animals, time);
	}

}
