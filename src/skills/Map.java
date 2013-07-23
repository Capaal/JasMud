package skills;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import java.util.ArrayList;
import processes.DrawingPanel;
import processes.Command;
import processes.Location;
import processes.PlayerPrompt;
import processes.WorldServer;

public class Map extends Skill implements Command {
	
	public final int CHANGE = 20;
	
	private ArrayList<Location> doneLocations;
	private DrawingPanel panel;
	private Graphics g;

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
	//	doneLocations = new ArrayList<Location>();
	//	panel = new DrawingPanel(300, 300);
	//	g = panel.getGraphics();
	//	g.setColor(Color.BLACK);
	//	drawLocation(WorldServer.locationCollection.get(1), 150, 250, 150, 250, 25);
	}
	
	private void drawLocation(Location loc,int x1,int y1,int x2,int y2, int times) {		
		if (!doneLocations.contains(loc)) {			
			doneLocations.add(loc);
			if (times >= 0) {
				drawPoint(x1, y1, x2, y2);
				loc.point = new Point(x1, y1);
				x2 = x1;
				y2 = y1;
				for (int i = 0; i < 8; i ++) {
					if (loc.getLocation(i) != null) {						
						int[] list = calcP(x2, y2, i);
						x1 = list[0];
						y1 = list[1];
						drawLocation(loc.getLocation(i), x1, y1, x2, y2, times - 1);
					}
				}
			}
		} else {
			drawPoint(x2, y2, (int)loc.point.getX(), (int)loc.point.getY());
		}
	}
	
	private void drawPoint(int x1,int y1,int x2,int y2) {
		g.drawOval(x1, y1, 3, 3);
		g.drawLine(x2, y2, x1, y1);
	}
	
	private int[] calcP(int x1, int y1, int i) {
		switch (i) {		
			case 0:
				x1 += 0;
				y1 += -CHANGE;
			break;
			
			case 1:
				x1 += CHANGE;
				y1 += -CHANGE;
			break;
			
			case 2:
				x1 += CHANGE;
				y1 += -0;
			break;
			
			case 3:
				x1 += CHANGE;
				y1 += CHANGE;
			break;
			
			case 4:
				x1 += 0;
				y1 += CHANGE;
			break;
			
			case 5:
				x1 += -CHANGE;
				y1 += CHANGE;
			break;
			
			case 6:
				x1 += -CHANGE;
				y1 += 0;
			break;
			
			case 7:
				x1 += -CHANGE;
				y1 += -CHANGE;
			break;		
		}		
		return new int[] {x1, y1};
	}
}
