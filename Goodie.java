import java.awt.*;

public class Goodie extends GameObject {
	private String label;
	private int x = -1;
	private int y = -1;
	
	public Goodie() {
	}
	public Goodie(String label) {
		setLabel(label);
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public int getNumericValue() {
		try {
			return Integer.parseInt(label);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void randomInit() {
		getParent().clear(x, y);
		int newX, newY;
		do {
			newX = (int) (Math.random() * (double) getParent().getFieldWidth());
			newY = (int) (Math.random() * (double) getParent().getFieldHeight());
		} while (getParent().getObjectAt(newX, newY) != null);
		x = newX;
		y = newY;
		getParent().obstruct(x, y, this);
	}
	public void incrementNumber() {
		setLabel(String.valueOf(getNumericValue() + 1));
	}
	public void levelAdvanced(int level) {
		setLabel("1");
		randomInit();
	}
	public void paintSquare(int x, int y, int width, int height, Graphics2D g2d) {
		Font originalFont = g2d.getFont();
		Paint originalPaint = g2d.getPaint();
		g2d.setFont(new Font(originalFont.getName(), Font.BOLD, originalFont.getSize()));
		g2d.setPaint(getPaint());		
		g2d.drawString(label, x, y + height);
		g2d.setFont(originalFont);
		g2d.setPaint(originalPaint);
	}	
}
