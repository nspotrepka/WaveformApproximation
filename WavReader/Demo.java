
public class Demo {
	public static void main(String[] args){
		//a should be 0
		int a = WaveFileReader.WavDifference("wav/wav_20_8_1_pcm.wav","wav/wav_20_8_1_pcm.wav");
		int b = WaveFileReader.WavDifference("wav/wav_40_16_1_pcm.wav","wav/wav_20_8_2_pcm.wav");
		System.out.println(a + "\n" + b);
	}
}
