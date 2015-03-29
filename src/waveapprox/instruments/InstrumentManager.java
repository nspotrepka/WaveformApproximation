package waveapprox.instruments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	public boolean add(Instrument inst) {
		instruments.add(inst);
		inst.createSynthDef();
		try {
			File file = instrumentToFile(inst);
			inst.getSynthDef().writeDefFile(file);
			inst.setFile(file);
		} catch (IOException e) {
			System.out.println("error writing scsyndef file for " + inst.getName());
		}
		return true;
	}
	
	public void clear() {
		Iterator<Instrument> iterator = instruments.iterator();
		while (iterator.hasNext()) {
			if(iterator.hasNext()) {
				Instrument inst = iterator.next();
				File file = inst.getFile();
				if(file.exists())
					file.delete();
		        iterator.remove();
		    }
		}
	}
	
	public Instrument get(int i) {
		return instruments.get(i);
	}
	
	public int size() {
		return instruments.size();
	}
	
	public boolean remove(Instrument inst) {
		File file = inst.getFile();
		if(file.exists() && instruments.contains(inst)) {
			file.delete();
			instruments.remove(inst);
			return true;
		}
		return false;
	}
}
