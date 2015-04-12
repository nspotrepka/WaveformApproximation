package waveapprox.evaluation;

import java.io.File;
import java.io.IOException;

import waveapprox.lib.FFT;
import waveapprox.lib.WavFile;
import waveapprox.lib.WavFileException;

public class FFTEvaluation {
	public static double evaluate(String fileName1, String fileName2) {
		
		// Number of frames (power of 2)
		int n = 1024;
		
		// Open the wav files specified
		WavFile file1;
		WavFile file2;
		try {
			file1 = WavFile.openWavFile(new File(fileName1));
			file2 = WavFile.openWavFile(new File(fileName2));
		} catch (IOException e) {
			file1 = null;
			file2 = null;
			e.printStackTrace();
		} catch (WavFileException e) {
			file1 = null;
			file2 = null;
			e.printStackTrace();
		}
		

		// Get the number of audio channels
		int channels1 = file1.getNumChannels();
		int channels2 = file2.getNumChannels();

		// Create a buffer of n frames
		double[] buffer1 = new double[n * channels1];
		double[] buffer2 = new double[n * channels2];

		// Evaluation metric
		double evaluationMetric = 0;
		
		int framesRead1;
		int framesRead2;
		do {
			// Read frames into buffer
			try {
				framesRead1 = file1.readFrames(buffer1, n);
				framesRead2 = file2.readFrames(buffer2, n);
			} catch (IOException | WavFileException e) {
				framesRead1 = 0;
				framesRead2 = 0;
				e.printStackTrace();
			}
			
			
			double[] fftReal1 = new double[n];
			double[] fftImag1 = new double[n];
			
			double[] fftReal2 = new double[n];
			double[] fftImag2 = new double[n];
			
			// Loop through frames of file 1
			for (int s = 0; s < framesRead1; s++) {
				fftReal1[s] = 0;
				for(int i = 0; i < channels1; i++) {
					fftReal1[s] += buffer1[s*channels1+i]/n;
				}
				fftImag1[s] = 0;
			}
			for(int s = framesRead1; s < n; s++) {
				fftReal1[s] = 0;
				fftImag1[s] = 0;
			}
			
			// Loop through frames of file 2
			for (int s = 0; s < framesRead2; s++) {
				fftReal2[s] = 0;
				for(int i = 0; i < channels2; i++) {
					fftReal2[s] += buffer2[s*channels2+i]/n;
				}
				fftImag2[s] = 0;
			}
			for(int s = framesRead2; s < n; s++) {
				fftReal2[s] = 0;
				fftImag2[s] = 0;
			}
			
			// Fast Fourier Transform
			FFT.transformRadix2(fftReal1, fftImag1);
			FFT.transformRadix2(fftReal2, fftImag2);
			
			// This is where the magic happens
			for(int s = 0; s < n; s++) {
				evaluationMetric += Math.abs((fftReal1[s] - fftReal2[s]));
			}
		}
		while (framesRead1 != 0 || framesRead2 != 0);

		// Close the wavFile
		try {
			file1.close();
			file2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return evaluationMetric;
	}
}
