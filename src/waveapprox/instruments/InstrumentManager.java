package waveapprox.instruments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import waveapprox.OSUtil;

public class InstrumentManager {
	private List<Instrument> instruments;
	
	public static File instrumentToFile(Instrument inst) {
		return new File(OSUtil.getSynthDefDirectory() + inst.getName() + ".scsyndef");
	}
	
	public InstrumentManager() {
		instruments = new ArrayList<Instrument>();
	}
	
	public List<Instrument> getInstruments() {
		return instruments;
	}
	
	public void saveAll() {
		for(Instrument inst : instruments) {
			try {
				File file = instrumentToFile(inst);
				inst.getSynthDef().writeDefFile(file);
				inst.setFile(file);
			} catch (IOException e) {
				System.out.println("error writing scsyndef file for " + inst.getName());
			}
		}
	}
	
	public void deleteAll() {
		for(Instrument inst : instruments) {
			File file = inst.getFile();
			if(file.exists())
				file.delete();
			inst.setFile(null);
		}
	}
}
