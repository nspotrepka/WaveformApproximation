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
		createApproximation();
	}
	public static int createApproximation(){
		
		// The renderer has an InstrumentManager and a BundleManager
		// BundleManager creates the notes using .synthNew(time, inst, note, vol)
		
		
		final double timestep = 0.05;
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
		
		// Code to create empty candidate.wav
//		try {
//			bManager.synthNew(0, inst1, (float)NoteUtil.midiToFreq(54), 0);
//			renderer.render(OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav");
//		} catch (IOException e1) {
//			System.out.println("Couldn't create empty file");
//			e1.printStackTrace();
//		}
		
		// Try notes
		for(double t = 0; t < targetDuration; t += timestep){
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
			
			int minDifferenceNote = 0;
			
			for(int i = firstMidiNote; i < lastMidiNote; i++){
				bManager.synthNew(t, inst1, (float)NoteUtil.midiToFreq(i), amplitude);
				try{
					File file = new File(OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav");
					file.delete();
					System.out.println("File path: "+OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav");
					renderer.render(OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav");
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
			renderer.render(OSUtil.getWorkingDirectory() + "/wavFiles/candidate.wav");
		} catch (IOException e) {
			System.out.println("Could not do the final render");
			System.exit(1);
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
