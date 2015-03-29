package waveapprox.instruments;

import java.io.File;

import de.sciss.jcollider.Control;
import de.sciss.jcollider.GraphElem;
import de.sciss.jcollider.SynthDef;

public abstract class Instrument {
	protected String name;

	protected Control freqControl;
	protected Control ampControl;
	
	protected SynthDef synthDef = null;
	protected GraphElem graph = null;
	protected File file = null;
	
	public Instrument(String name) {
		this.name = name;
		freqControl = Control.kr(new String[] {"freq"}, new float[] {440.0f});
		ampControl = Control.kr(new String[] {"amp"}, new float[] {1.0f});
	}
	
	public String getName() {
		return name;
	}
	
	public SynthDef getSynthDef() {
		return synthDef;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public abstract void createSynthDef();
}
