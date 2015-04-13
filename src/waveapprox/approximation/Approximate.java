package waveapprox.approximation;

import java.io.File;
import java.util.ArrayList;

import waveapprox.instruments.Instrument;
import waveapprox.instruments.InstrumentManager;
import waveapprox.instruments.NoteUtil;
import waveapprox.instruments.Violin;
import waveapprox.sound.AudioRenderer;
import waveapprox.sound.BundleManager;

public class Approximate {
	public static void main(String args[]){
		//Some test code will go here
	}
	public int createApproximation(File f){
		
		// The renderer has an InstrumentManager and a BundleManager
		// BundleManager creates the notes using .synthNew(time, inst, note, vol)
		
		final double timestep = 0.1;
		
		
		ArrayList<Double> time = new ArrayList<Double>();
		ArrayList<Integer> note = new ArrayList<Integer>();
		
		
		//Create a renderer
		AudioRenderer renderer = new AudioRenderer(44100, 2);
		
		// Instrument
		InstrumentManager iManager = renderer.getInstrumentManager();
		Instrument inst1 = new Violin("violin");
		
		// Try notes
		// Render file
		// Evaluate
		// Pick the best one and add it to the array lists.
		
		BundleManager bManager = renderer.getBundleManager();
		bManager.bufferAllocReadAll(iManager);
		bManager.synthNew(0, inst1, (float)NoteUtil.midiToFreq(48), 0.7f);
		bManager.synthNew(0.5, inst1, (float)NoteUtil.midiToFreq(55), 0.3f);
		
		
		return 0;
	}
}
