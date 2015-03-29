package waveapprox.instruments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import waveapprox.OSUtil;
import waveapprox.sound.BundleManager;
import de.sciss.jcollider.SynthDef;
import de.sciss.jcollider.UGen;

public abstract class BufferInstrument extends Instrument {
	private int bufferFirst = 0;
	private int midiFirst = 0;
	
	protected List<File> bufferFiles;
	protected Comparator<File> fileComparator;
	
	public BufferInstrument(String name, BundleManager bManager) {
		super(name);
		bufferFiles = new ArrayList<File>();
		fileComparator = new BufferFileComparator();
		listBufferFiles();
		bManager.bufferAllocRead(this);
	}
	
	public int getBufferFirst(int index) {
		return bufferFirst;
	}
	public void setBufferFirst(int index) {
		bufferFirst = index;
	}
	
	public int getMidiFirst() {
		return midiFirst;
	}
	
	public int getRange() {
		return bufferFiles.size();
	}
	
	public List<File> getBufferFiles() {
		return bufferFiles;
	}
	
	public void listBufferFiles() {
		bufferFiles.clear();
		File[] files = new File(OSUtil.getWorkingDirectory() + "/" + getBufferFilePath()).listFiles();
		for(File f : files) {
			if(f.isFile()) {
				String fname = f.getName();
				if((fname.substring(fname.lastIndexOf('.') + 1).toLowerCase()).equals("wav"))
					bufferFiles.add(f);
			}
		}
		Collections.sort(bufferFiles, fileComparator);
		if(bufferFiles.size() > 0) {
			String[] a = bufferFiles.get(0).getName().split("\\.");
			midiFirst = NoteUtil.stringToMidi(a[a.length-3]);
		}
	}
	
	public abstract String getBufferFilePath();
	
	@Override
	public void createSynthDef() {
		graph = UGen.ar("Out", UGen.ir(0),
					UGen.ar("*", ampControl,
						UGen.ar("PlayBuf", 2,
							UGen.kr("+", UGen.ir(bufferFirst-midiFirst),
								UGen.kr("max", UGen.ir(midiFirst), UGen.kr("min", UGen.ir(midiFirst+bufferFiles.size()-1),
									UGen.kr("round", UGen.kr("cpsmidi", freqControl), UGen.ir(1))
								))
							)
						)
					)
				);
		synthDef = new SynthDef(name, graph);
	}
	
	public static class BufferFileComparator implements Comparator<File> {
	    @Override
	    public int compare(File f1, File f2) {
	    	String[] a1 = f1.getName().split("\\.");
	    	String[] a2 = f2.getName().split("\\.");
	    	int midi1 = NoteUtil.stringToMidi(a1[a1.length-3]);
	    	int midi2 = NoteUtil.stringToMidi(a2[a2.length-3]);
	        return midi1 - midi2;
	    }
	}
}
