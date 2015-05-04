package silver.starbound.util;

public class OSUtil {
	public enum OperatingSystem{
		WINDOWS,
		LINUX,
		OSX,
		UNKNOWN
	}

	public enum Architecture{
		BIT32,
		BIT64,
		UNKNOWN
	}
	
	public static OperatingSystem getOS(){
		String osName = System.getProperty("os.name");
		if(osName.startsWith("Windows")){
			return OperatingSystem.WINDOWS;
		}
		else if(osName.equals("Linux")){
			return OperatingSystem.LINUX;
		}
		else if(osName.equals("MAX OS X")){
			return OperatingSystem.OSX;
		}
		else{
			return OperatingSystem.UNKNOWN;
		}
	}
	public static Architecture getArchitecture(){
		String archName = System.getProperty("os.arch");
		if(archName.equals("x86"))
			return Architecture.BIT64;
		else
			return Architecture.BIT32;
	}
}
