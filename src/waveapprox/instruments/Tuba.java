package waveapprox.instruments;



public class Tuba extends BufferInstrument {

	public Tuba(String name) {
		super(name);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/tuba";
	}
}
