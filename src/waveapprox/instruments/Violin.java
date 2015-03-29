package waveapprox.instruments;

import waveapprox.sound.BundleManager;


public class Violin extends BufferInstrument {

	public Violin(String name, BundleManager bManager) {
		super(name, bManager);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/violin";
	}
}
