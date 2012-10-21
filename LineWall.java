import java.util.*;
import java.awt.geom.*;

public class LineWall extends GameObject {
	private ArrayList linesForLevels;
	private boolean includeExternalWalls;

	public LineWall() {
		linesForLevels = new ArrayList();
	}
	public void setIncludeExternalWalls(boolean includeExternalWalls) {
		this.includeExternalWalls = includeExternalWalls;
	}
	public boolean getIncludeExternalWalls() {
		return includeExternalWalls;
	}
	public void addLine(int level, IntLine line) {		
		makeLevelLineArrays(level);
		ArrayList linesForThisLevel = getLinesForLevel(level);
				
		linesForThisLevel.add(line);		
	}
	public int getLinesForLevelCount() {
		return linesForLevels.size();
	}
	public ArrayList getLinesForLevels() {
		return linesForLevels;
	}
	public void setLinesForLevels(ArrayList linesForLevels) {
		this.linesForLevels = linesForLevels;
	}
	public ArrayList getLinesForLevel(int level) {
		return (ArrayList) linesForLevels.get(level - 1);
	}
	public void setLinesForLevel(int level, Collection lines) {
		makeLevelLineArrays(level);		
		linesForLevels.set(level - 1, lines);		
	}	
	private void makeLevelLineArrays(int maxLevel) {
		for (int i = 0; i < maxLevel - linesForLevels.size(); i++) {
			linesForLevels.add(new ArrayList());
		}		
	}
	public void levelAdvanced(int level) {
		if (includeExternalWalls) {		
			for (int i = 0; i < getParent().getFieldWidth(); i++) {
				getParent().obstruct(i, 0, this);
				getParent().obstruct(i, getParent().getFieldHeight() - 1, this);		
			}
			for (int i = 0; i < getParent().getFieldHeight(); i++) {	
				getParent().obstruct(0, i, this);
				getParent().obstruct(getParent().getFieldWidth() - 1, i, this);
			}
		}
		
		// Draw other walls		
		if (level <= linesForLevels.size()) {
			ArrayList linesCollection = getLinesForLevel(level);
			if (linesCollection.size() > 0) {
				Iterator i = linesCollection.iterator();

				while (i.hasNext()) {
					IntLine line = (IntLine) i.next();
					obstructLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
				}
			}
		}
	}

	// This is Bresenham's line drawing algorithm.
	// Code taken from http://java.sun.com/docs/books/performance/1st_edition/html/JPAlgorithms.fm.html.
	private void obstructLine(int x0, int y0, int x1, int y1) {
		int dx,dy;
		int temp;
		// reduce to half the octant cases by always 
		// drawing + in y
		if (y0>y1) {
			temp = y0;
			y0 = y1;
			y1 = temp;
			temp = x0;
			x0 = x1;
			x1 = temp;
		}
		dx = x1-x0;
		dy = y1-y0;
		if (dx > 0) {
			if (dx > dy) {
				octant0(x0, y0, dx, dy, 1);
			} else {
				octant1(x0, y0, dx, dy, 1);
			}
		} else {
			dx = -dx;
			if (dx > dy) {
				octant0(x0, y0, dx, dy, -1);
			} else {
				octant1(x0, y0, dx, dy, -1);
			}
		}
	}
	private void octant0(int x0, int y0, int dx, int dy, int xdirection){
		int DeltaYx2;
		int DeltaYx2MinusDeltaXx2;
		int ErrorTerm;		
		// set up initial error term and drawing values
		DeltaYx2 = dy*2;
		DeltaYx2MinusDeltaXx2 = DeltaYx2 - (dx*2);
		ErrorTerm = DeltaYx2 - dx;
		// draw loop
		(getParent()).obstruct(x0, y0, this);
		while ( dx-- > 0) {
			// check if we need to advance y
			if (ErrorTerm >= 0) {
				// advance Y and reset ErrorTerm
				y0++;
				ErrorTerm += DeltaYx2MinusDeltaXx2;
			} else {
				// add error to ErrorTerm
				ErrorTerm += DeltaYx2;
			}
			x0 += xdirection;
			(getParent()).obstruct(x0, y0, this);
		}
	}
	private void octant1(int x0, int y0, int dx, int dy, int xdirection) {
		int DeltaXx2;
		int DeltaXx2MinusDeltaYx2;
		int ErrorTerm;		
		// set up initial error term and drawing values
		DeltaXx2 = dx * 2;
		DeltaXx2MinusDeltaYx2 = DeltaXx2 - (dy*2);
		ErrorTerm = DeltaXx2- dy;
		// draw loop
		(getParent()).obstruct(x0, y0, this);
		while ( dy-- > 0) {
			// check if we need to advance x
			if (ErrorTerm >= 0) {
				// advance X and reset ErrorTerm
				x0 += xdirection;
				ErrorTerm += DeltaXx2MinusDeltaYx2;
			} else {
				// add to ErrorTerm
				ErrorTerm += DeltaXx2;
			}
			y0++;
			(getParent()).obstruct(x0, y0, this);
		}
	}
}
