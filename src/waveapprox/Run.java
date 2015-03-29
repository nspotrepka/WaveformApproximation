package waveapprox;

import java.io.IOException;

import waveapprox.instruments.Instrument;
import waveapprox.instruments.InstrumentManager;
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
		
		
		// Testing code
		AudioRenderer renderer = new AudioRenderer(44100, 2);
		BundleManager bManager = renderer.getBundleManager();
		InstrumentManager iManager = new InstrumentManager();
		
		Instrument inst = new Tuba("tuba", bManager);
		iManager.add(inst);
		
		bManager.synthNew(0.1, inst, 0.1f, 0.7f);
		bManager.synthNew(0.2, inst, 100000, 0.3f);
		bManager.controlSetEnd(1.4);
		
		try {
			renderer.render();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
