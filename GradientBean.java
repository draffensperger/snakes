import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

public class GradientBean implements Paint {
	private GradientPaint grad;
	public GradientBean(Point2D p1, Color c1, Point2D p2, Color c2, boolean cyclic) {
		grad = new GradientPaint(p1, c1, p2, c2, cyclic);
	}
	public GradientBean(float x1, float y1, Color c1, float x2, float y2, Color c2, boolean cyclic) {
		grad = new GradientPaint(x1, y1, c1, x2, y2, c2, cyclic);
	}
	public GradientBean() {
		this (0.0f, 0.0f, Color.white, 50.002341f, 50.4340001f, Color.black, true);
	}
	public void setPoint1(Point2D p1) {
		grad = new GradientPaint(p1, grad.getColor2(), grad.getPoint2(), grad.getColor2(), grad.isCyclic());
	}
	public void setPoint2(Point2D p2) {
		grad = new GradientPaint(grad.getPoint1(), grad.getColor2(), p2, grad.getColor2(), grad.isCyclic());
	}
	public void setColor1(Color c1) {
		grad = new GradientPaint(grad.getPoint1(), c1, grad.getPoint2(), grad.getColor2(), grad.isCyclic());
	}
	public void setColor2(Color c2) {
		grad = new GradientPaint(grad.getPoint1(), grad.getColor1(), grad.getPoint2(), c2, grad.isCyclic());
	}
	public void setIsCyclic(boolean isCyclic) {
		grad = new GradientPaint(grad.getPoint1(), grad.getColor1(), grad.getPoint2(), grad.getColor1(), isCyclic);
	}
	public Color getColor1() {return grad.getColor1();}
	public Color getColor2() {return grad.getColor2();}
	public Point2D getPoint1() {return grad.getPoint1();}
	public Point2D getPoint2() {return grad.getPoint2();}
	public boolean getIsCyclic() {return grad.isCyclic();}
	public boolean isCyclic() {return grad.isCyclic();}
	
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
		return grad.createContext(cm, deviceBounds, userBounds, xform, hints);
	}
	public int getTransparency() {
		return grad.getTransparency();
	}
}
