package waveapprox;

import java.nio.file.Paths;

public class OSUtil {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String scProgram;
    private static final String synthDefDirectory;
    private static final String workingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
    
    static {
    	if(isWindows()) {
    		scProgram = "C:/Program Files (x86)/SuperCollider-3.6.6/scsynth.exe";
    		synthDefDirectory = System.getenv("LOCALAPPDATA")+"/SuperCollider/synthdefs/";
    	} else if(isMac()) {
    		scProgram = "/Applications/SuperCollider/SuperCollider.app/Contents/Resources/scsynth";
    		synthDefDirectory = System.getProperty("user.home")+"/Library/Application Support/SuperCollider/synthdefs/";
    	} else if(isUnix()) {
    		scProgram = "/usr/local/bin/scsynth";
    		synthDefDirectory = System.getProperty("user.home")+"/.local/share/SuperCollider/synthdefs/";
    	} else {
    		scProgram = null;
    		synthDefDirectory = null;
    		System.out.println("operating system not supported");
    		System.exit(0);
    	}
    }
    
    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }
    
    public static String getSCProgram() {
    	return scProgram;
    }
    
    public static String getSynthDefDirectory() {
    	return synthDefDirectory;
    }
    
    public static String getWorkingDirectory() {
    	return workingDirectory;
    }
}
