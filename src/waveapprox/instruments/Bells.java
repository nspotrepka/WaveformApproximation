package waveapprox.instruments;

import waveapprox.sound.BundleManager;


public class Bells extends BufferInstrument {

	public Bells(String name, BundleManager bManager) {
		super(name, bManager);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/bells";
	}
}
