package waveapprox.instruments;



public class Clarinet extends BufferInstrument {

	public Clarinet(String name) {
		super(name);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/clarinet";
	}
}
