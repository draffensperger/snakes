import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;

public class TextureBean implements Paint {
	private String imageFile;
	private TexturePaint texture;
	private BufferedImage img;
	private Rectangle2D anchor;
	
	private static final Paint backupPaint = Color.white;
	private static final Rectangle2D defaultAnchor = new Rectangle(0, 0, 5, 5);
	private static final BufferedImage defaultImg = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	
	public TextureBean() {
		texture = null;
	}
	public TextureBean(String file, Rectangle2D anchor) {
		this();
		setAnchorRect(anchor);
		setImageFile(file);		
	}		
	public void setImageFile(String fileName) {
		this.imageFile = fileName;		
		try {			
			img = ImageIO.read(new URL(SnakeApplet.getStaticAppletCodebase() + "/" + fileName));		
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateTexture();
	}	
	public String getImageFile() {
		return imageFile;
	}	
	public void setAnchorRect(Rectangle2D anchor) {
		this.anchor = anchor;
		updateTexture();
	}
	public Rectangle2D getAnchorRect() {
		return anchor;
	}
	public BufferedImage getImage() {
		if (texture != null) {
			return texture.getImage();
		} else {
			return defaultImg;
		}
	}
	
	public void updateTexture() {
		if (texture != null) {
			makeTexture();
		}
	}
	public void makeTexture() {
		if (texture == null) {
			texture = new TexturePaint(img, anchor);
		}
	}
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
		makeTexture();
		return texture.createContext(cm, deviceBounds, userBounds, xform, hints);		
	}	
	public int getTransparency() {
		makeTexture();
		return texture.getTransparency();		
	}	
}
