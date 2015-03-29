package waveapprox.instruments;

import waveapprox.sound.BundleManager;


public class Tuba extends BufferInstrument {

	public Tuba(String name, BundleManager bManager) {
		super(name, bManager);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/tuba";
	}
}
