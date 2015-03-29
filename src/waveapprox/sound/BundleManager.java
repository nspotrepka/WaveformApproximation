package waveapprox.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import waveapprox.instruments.BufferInstrument;
import waveapprox.instruments.Instrument;
import waveapprox.instruments.InstrumentManager;
import de.sciss.net.OSCBundle;
import de.sciss.net.OSCMessage;

public class BundleManager {
	private List<OSCBundle> bundles;
	private int bufferCounter;
	private int nodeCounter;
	
	public BundleManager() {
		bundles = new ArrayList<OSCBundle>();
		bufferCounter = 0;
		nodeCounter = 1000;
	}
	
	public List<OSCBundle> getBundles() {
		return bundles;
	}
	
	public int getBufferCounter() {
		return bufferCounter;
	}
	public void incrementBufferCounter() {
		bufferCounter++;
	}
	public void resetBufferCounter() {
		bufferCounter = 0;
	}
	
	public int getNodeCounter() {
		return nodeCounter;
	}
	public void incrementNodeCounter() {
		nodeCounter++;
	}
	public void resetNodeCounter() {
		nodeCounter = 1000;
	}
	
	public void reset() {
		bundles.clear();
		resetBufferCounter();
		resetNodeCounter();
	}
	
	public void bufferAllocRead(BufferInstrument inst) {
		OSCBundle b = new OSCBundle(0.0);
		inst.setBufferFirst(bufferCounter);
		for(File f : inst.getBufferFiles()) {
			OSCMessage m = new OSCMessage("/b_allocRead", new Object[] {bufferCounter, f.getAbsolutePath()});
			b.addPacket(m);
			bufferCounter++;
		}
		bundles.add(b);
	}
	
	public void bufferAllocReadAll(InstrumentManager iManager) {
		for(Instrument inst : iManager.getInstruments()) {
			if(BufferInstrument.class.isAssignableFrom(inst.getClass()))
				bufferAllocRead((BufferInstrument)inst);
		}
	}
	
	public void synthNew(double time, Instrument inst, float freq, float amp) {
		OSCBundle b = new OSCBundle(time);
		OSCMessage m = new OSCMessage("/s_new", new Object[] {inst.getName(), nodeCounter, 0, 0, "freq", freq, "amp", amp});
		b.addPacket(m);
		nodeCounter++;
		bundles.add(b);
	}
	
	public void controlSetEnd(double time) {
		OSCBundle b = new OSCBundle(time);
		OSCMessage m = new OSCMessage("/c_set", new Object[] {0, 0});
		b.addPacket(m);
		bundles.add(b);
	}
	
	public static class OSCBundleComparator implements Comparator<OSCBundle> {
	    @Override
	    public int compare(OSCBundle b1, OSCBundle b2) {
	        return new Long(b1.getTimeTag()).compareTo(b2.getTimeTag());
	    }
	}
}
