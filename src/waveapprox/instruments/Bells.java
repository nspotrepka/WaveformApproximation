package waveapprox.instruments;



public class Bells extends BufferInstrument {

	public Bells(String name) {
		super(name);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/bells";
	}
}
