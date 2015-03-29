package waveapprox.instruments;



public class Violin extends BufferInstrument {

	public Violin(String name) {
		super(name);
	}

	@Override
	public String getBufferFilePath() {
		return "audio/violin";
	}
}
