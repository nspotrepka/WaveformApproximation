package waveapprox;

import java.io.IOException;

import waveapprox.instruments.Clarinet;
import waveapprox.instruments.Instrument;
import waveapprox.instruments.InstrumentManager;
import waveapprox.instruments.NoteUtil;
import waveapprox.instruments.Tuba;
import waveapprox.sound.AudioRenderer;
import waveapprox.sound.BundleManager;
import de.sciss.jcollider.UGenInfo;

public class Run {
	
	//private static ServerWrapper server;
	
	public static void main(String[] args) {
		/*
		// Quit the SuperCollider server when program quits
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					if(server != null)
						server.quitLoop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Start the SuperCollider server
		try {
			server = new ServerWrapper("JColliderServer");
		} catch (IOException e) {
			server = null;
			e.printStackTrace();
			System.exit(0);
		}
		*/
		
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
		Instrument inst1 = new Tuba("tuba");
		Instrument inst2 = new Clarinet("clarinet");
		iManager.getInstruments().add(inst1);
		iManager.getInstruments().add(inst2);
		
		// FIRST TIME THROUGH
		BundleManager bManager = renderer.getBundleManager();
		bManager.bufferAllocReadAll(iManager);
		bManager.synthNew(0, inst1, (float)NoteUtil.midiToFreq(48), 0.7f);
		bManager.synthNew(0.5, inst1, (float)NoteUtil.midiToFreq(55), 0.3f);
		bManager.controlSetEnd(3);
		
		try {
			renderer.render();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// NEXT TIME THROUGH
		bManager.reset();
		
		bManager.bufferAllocReadAll(iManager);
		bManager.synthNew(0, inst2, (float)NoteUtil.midiToFreq(60), 0.7f);
		bManager.synthNew(0.5, inst2, (float)NoteUtil.midiToFreq(67), 0.3f);
		bManager.controlSetEnd(3);
		
		try {
			renderer.render();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
