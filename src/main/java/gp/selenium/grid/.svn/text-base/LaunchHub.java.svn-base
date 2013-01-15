/*
 * --------------------------------------------------------------- File: LaunchHub.java Project: GP-Selenium-RC2Grid-Launcher Created: Dec 17, 2010 11:51:28 AM Author: oleksii.zozulenko ---------------------------------------------------------------
 */
package gp.selenium.grid;

/**
 * @author oleksii.zozulenko
 */
public class LaunchHub
{
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception
	{
		Thread th = new Thread()
		{
			public void run()
			{
				try
				{
					// com.thoughtworks.selenium.grid.hub.HubServer.main(args);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			};
		};
		
		th.setDaemon(true);
		th.start();
		
		if (th.isDaemon())
		{
			Thread.currentThread().join();
		}
		
	}
}
