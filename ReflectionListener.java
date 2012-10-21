import java.awt.event.*;
import java.lang.reflect.*;

public class ReflectionListener extends WindowAdapter implements ActionListener {
	private Method method;
	private Object obj;

	public ReflectionListener(Object obj, String methodName) {
		this.obj = obj;		
		try {
			method = obj.getClass().getMethod(methodName, null);
			method.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void actionPerformed(ActionEvent evt) {
		invokeMethod();
	}			
	public void windowClosing(WindowEvent evt) {
		invokeMethod();
	}	
	private void invokeMethod() {
		try {
			method.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}