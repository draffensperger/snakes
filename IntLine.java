public class IntLine {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	public IntLine() {
	}
	public IntLine(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public void setX1(int val) {x1 = val;}
	public void setY1(int val) {y1 = val;}
	public void setX2(int val) {x2 = val;}
	public void setY2(int val) {y2 = val;}
	public int getX1() {return x1;}
	public int getY1() {return y1;}
	public int getX2() {return x2;}
	public int getY2() {return y2;}
}
