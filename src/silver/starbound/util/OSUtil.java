//The MIT License (MIT)
//
//Copyright (c) 2015 , SilverFishCat@GitHub
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.

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
