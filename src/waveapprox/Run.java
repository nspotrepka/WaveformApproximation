package waveapprox;

import java.io.IOException;

import waveapprox.instruments.Instrument;
import waveapprox.instruments.InstrumentManager;
import waveapprox.instruments.NoteUtil;
import waveapprox.instruments.Violin;
import waveapprox.sound.AudioRenderer;
import waveapprox.sound.BundleManager;
import de.sciss.jcollider.UGenInfo;

public class Run {

	public static void main(String[] args) {
		
		
		// Read the UGen information
		try {
			UGenInfo.readDefinitions();
		} catch(IOException e) {
			System.out.println("error reading ugen info");
			System.exit(0);
		}
		
		
		
		
		
		
		
		// EXAMPLE
		// Testing code
		
		AudioRenderer renderer = new AudioRenderer(44100, 2);
		
		// ADD INSTRUMENTS
		InstrumentManager iManager = renderer.getInstrumentManager();
		Instrument inst1 = new Violin("violin");
		iManager.getInstruments().add(inst1);
		
		// FIRST TIME THROUGH
		BundleManager bManager = renderer.getBundleManager();
		bManager.bufferAllocReadAll(iManager);
		bManager.synthNew(0, inst1, (float)NoteUtil.midiToFreq(40), 1f);
		bManager.controlSetEnd(3);
		
		try {
			renderer.render("wavFiles/candidate.wav");
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
}
