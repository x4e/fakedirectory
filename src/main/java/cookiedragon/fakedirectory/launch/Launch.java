package cookiedragon.fakedirectory.launch;

import cookiedragon.fakedirectory.FakeDirectory;

/**
 * @author cookiedragon234 16/Nov/2019
 */
public class Launch
{
	/**
	 * Application entry point, called when the jar is ran
	 * @param args CLI Args given
	 */
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("java -jar fakedirectory [inputfile]");
			System.exit(0);
		}
		
		new FakeDirectory(args[0]);
	}
}
