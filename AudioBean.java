import java.applet.*;
import java.io.*;

public class AudioBean implements AudioClip {
	private AudioClip clip;
	private String file;
	
	public AudioBean() {
	}
	public AudioBean(String file) {
		setFile(file);		
	}
	public void setFile(String file) {
		this.file = file;
		try {
			clip = Applet.newAudioClip(new File(file).toURL());
			if (clip == null) {
				System.out.println("Couldn't make clip.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public String getFile() {
		return file;
	}
	public void loop() {
		if (clip != null) clip.loop();
	}	
	public void play() {
		if (clip != null) clip.play();
	}	
	public void stop() {
		if (clip != null) clip.stop();
	}	
}
