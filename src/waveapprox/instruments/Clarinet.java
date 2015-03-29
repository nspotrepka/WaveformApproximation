package waveapprox.instruments;

import waveapprox.sound.BundleManager;


public class Clarinet extends BufferInstrument {

	public Clarinet(String name, BundleManager bManager) {
		super(name, bManager);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/clarinet";
	}
}
