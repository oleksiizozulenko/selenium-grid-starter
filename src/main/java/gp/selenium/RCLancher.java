package gp.selenium;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class RCLancher
{

	public static final int DEFAULT_PORT = 5555;
	public static SortedSet<Integer> portpool = new TreeSet<Integer>();

	protected static int getGenericPort()
	{
		// Randomization oRand = new Randomization();
		Random oRand = new Random();
		int iGenericPort = 0;
		
		iGenericPort = oRand.nextInt(2 * DEFAULT_PORT);
		
		if (!portpool.add(iGenericPort))
		{
			return getGenericPort();
		}
		
		return iGenericPort;
	}
	
}
