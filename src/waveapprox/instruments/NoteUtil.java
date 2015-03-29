package waveapprox.instruments;

public class NoteUtil {
	
	public static final int FLAT = 0;
	public static final int SHARP = 1;
	
	public static double freqToMidi(double freq) {
		return 12.0 * Math.log(freq/440.0) / Math.log(2) + 69.0;
	}
	
	public static double midiToFreq(double midi) {
		return 440.0 * Math.pow(2, (midi - 69) / 12.0);
	}
	
	public static String midiToString(int midi, int accidental) {
		int octave = midi/12 - 1;
		int pitch = midi % 12;
		
		switch(pitch) {
			case 0:
				return "C"+octave;
			case 1:
				if(accidental == FLAT)
					return "Db"+octave;
				if(accidental == SHARP)
					return "C#"+octave;
				break;
			case 2:
				return "D"+octave;
			case 3:
				if(accidental == FLAT)
					return "Eb"+octave;
				if(accidental == SHARP)
					return "D#"+octave;
				break;
			case 4:
				return "E"+octave;
			case 5:
				return "F"+octave;
			case 6:
				if(accidental == FLAT)
					return "Gb"+octave;
				if(accidental == SHARP)
					return "F#"+octave;
				break;
			case 7:
				return "G"+octave;
			case 8:
				if(accidental == FLAT)
					return "Ab"+octave;
				if(accidental == SHARP)
					return "G#"+octave;
				break;
			case 9:
				return "A"+octave;
			case 10:
				if(accidental == FLAT)
					return "Bb"+octave;
				if(accidental == SHARP)
					return "A#"+octave;
				break;
			case 11:
				return "B"+octave;
		}
		
		return "";
	}
	
	public static int stringToMidi(String string) {
		String octaveString;
		String pitchString;
		if(string.indexOf('-') == -1) {
			octaveString = string.substring(string.length()-1);
			pitchString = string.substring(0, string.length()-1);
		} else {
			octaveString = string.substring(string.length()-2);
			pitchString = string.substring(0, string.length()-2);
		}
		
		int octave;
		try {
			octave = Integer.parseInt(octaveString);
		} catch(NumberFormatException e) {
			octave = -1;
			return -1;
		}
		
		int pitch;
		
		if(pitchString.equals("C")) {
			pitch = 0;
		} else if(pitchString.equals("Db") || pitchString.equals("C#")) {
			pitch = 1;
		} else if(pitchString.equals("D")) {
			pitch = 2;
		} else if(pitchString.equals("Eb") || pitchString.equals("D#")) {
			pitch = 3;
		} else if(pitchString.equals("E")) {
			pitch = 4;
		} else if(pitchString.equals("F")) {
			pitch = 5;
		} else if(pitchString.equals("Gb") || pitchString.equals("F#")) {
			pitch = 6;
		} else if(pitchString.equals("G")) {
			pitch = 7;
		} else if(pitchString.equals("Ab") || pitchString.equals("G#")) {
			pitch = 8;
		} else if(pitchString.equals("A")) {
			pitch = 9;
		} else if(pitchString.equals("Bb") || pitchString.equals("A#")) {
			pitch = 10;
		} else if(pitchString.equals("B")) {
			pitch = 11;
		} else {
			return -1;
		}
		
		return 12*(octave+1) + pitch;
	}
}
