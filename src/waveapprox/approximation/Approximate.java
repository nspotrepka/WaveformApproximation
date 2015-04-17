package waveapprox.approximation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import waveapprox.OSUtil;
import waveapprox.evaluation.WaveFileReader;
import waveapprox.instruments.BufferInstrument;
import waveapprox.instruments.Clarinet;
import waveapprox.instruments.Instrument;
import waveapprox.instruments.InstrumentManager;
import waveapprox.instruments.NoteUtil;
import waveapprox.sound.AudioRenderer;
import waveapprox.sound.BundleManager;

public class Approximate {
	public static void main(String args[]){
		stupidTest();
	}
	
	public static void stupidTest(){
		AudioRenderer renderer = new AudioRenderer(44100, 2);
		InstrumentManager iManager = renderer.getInstrumentManager();
		Instrument inst1 = new Clarinet("clarinet");
		BundleManager bManager = renderer.getBundleManager();
		bManager.bufferAllocReadAll(iManager);
		final float amplitude = 1f;
		for (double t = 0; t < 3; t+=0.1){
			bManager.synthNew(t, inst1, (float)NoteUtil.midiToFreq((int)(56+t)), amplitude);
		}
		try{renderer.render("lol");}
		catch(Exception e){System.out.println("Well shit");}

	}
	
	public static int createApproximation(){
		
		// The renderer has an InstrumentManager and a BundleManager
		// BundleManager creates the notes using .synthNew(time, inst, note, vol)
		// All time is in seconds.
		
		
		final double timestep = 0.1;
		final float amplitude = 0.5f;
		final double targetDuration = getDurationOfWavInSeconds(OSUtil.getWorkingDirectory() + "/wavFiles/target.wav");
		System.out.println("targetDuration: "+targetDuration);
//		File target = new File(OSUtil.getWorkingDirectory() + "/target.wav");
//		File currentCandidate;
		
		ArrayList<Double> time = new ArrayList<Double>();
		ArrayList<Integer> note = new ArrayList<Integer>();
		
		
		//Create a renderer
		AudioRenderer renderer = new AudioRenderer(44100, 2);
		
		// Setup JCollider
		InstrumentManager iManager = renderer.getInstrumentManager();
		Instrument inst1 = new Clarinet("clarinet");
		BundleManager bManager = renderer.getBundleManager();
		bManager.bufferAllocReadAll(iManager);
		
		// Get range of midi notes
		BufferInstrument temp = (BufferInstrument) inst1;
		int firstMidiNote = temp.getMidiFirst();
		int lastMidiNote = firstMidiNote + temp.getRange();
		temp = null;
		

		// Try notes
		for(double t = 0; t < targetDuration; t += timestep){
			System.out.println(t);
			bManager.reset();
			
			// *Load previous notes here
			for(int x=0; x<note.size(); x++){
				bManager.synthNew(time.get(x), inst1,
						note.get(x),
						amplitude);
			}
			
			// Difference between current and target
			int minDifference = WaveFileReader.WavDifference(
					OSUtil.getWorkingDirectory() + "/wavFiles/target.wav",
					OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav"); 
			
			int minDifferenceNote = firstMidiNote+1;
			
			// The loop that tries and evaluates the notes
			for(int i = firstMidiNote; i < lastMidiNote; i++){
				bManager.synthNew(t, inst1, (float)NoteUtil.midiToFreq(i), amplitude);
				try{
					renderer.render("wavFiles/candidate.wav");
				}
				catch(IOException e){
					System.out.println("Could not render file. \nt == " +t+ "\ni == "+i);
					e.printStackTrace();
					System.exit(2);
				}

				int candidateDifference = WaveFileReader.WavDifference(
						OSUtil.getWorkingDirectory() + "/wavFiles/target.wav",
						OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav"); 
				
				if (candidateDifference < minDifference){
					minDifference = candidateDifference;
					minDifferenceNote = i;
				}
			}
			if(minDifferenceNote != 0){
				note.add(minDifferenceNote);
				time.add(t);
			}
		}
		
		// Finally create the track
		bManager.reset();
		for(int x=0; x<note.size(); x++){
			bManager.synthNew(time.get(x), inst1,
					note.get(x),
					amplitude);
		}
		try {
			bManager.controlSetEnd(targetDuration);
			renderer.render("wavFiles/candidate.wav");
		} catch (IOException e) {
			System.out.println("Could not do the final render");
			System.exit(1);
		}
		for(int i=0; i<note.size(); i++){
			System.out.println(note.get(i));
		}
		return 0;
	}
	
	// This function is a modified version of the one found at http://stackoverflow.com/a/6540528
	// http://stackoverflow.com/questions/2709508/how-to-learn-wav-duration-in-java-media-frame-work
	public static double getDurationOfWavInSeconds(String filename)
	{   
		File file = new File(filename);
	    AudioInputStream stream = null;

	    try 
	    {
	        stream = AudioSystem.getAudioInputStream(file);

	        AudioFormat format = stream.getFormat();

	        return file.length() / format.getSampleRate() / (format.getSampleSizeInBits() / 8.0) / format.getChannels();
	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	        return -1;
	    }
	    finally
	    {
	        try { stream.close(); } catch (Exception ex) { }
	    }
	}
}
