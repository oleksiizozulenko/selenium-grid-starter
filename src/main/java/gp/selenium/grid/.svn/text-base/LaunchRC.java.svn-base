/*
 * --------------------------------------------------------------- File: LaunchRemoteControl.java Project: GP-Selenium-RC2Grid-Launcher Created: Dec 2, 2010 1:20:18 PM Author: oleksii.zozulenko ---------------------------------------------------------------
 */
package gp.selenium.grid;

import gp.selenium.RCLancher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

/**
 * @author oleksii.zozulenko
 */
public class LaunchRC extends RCLancher
{
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(final String[] args)
	{
		List<String> cliEnvs = new ArrayList<String>();
		
		String defaultEnv = "*firefox";
		String envsLine = "";
		String defaultHubUrl = "http://kievtomcat.kievlan.local:4444";
		String hubUrl = "";
		
		Options opts = new Options();
		opts.addOption("env", true, "Environment");
		opts.addOption("hubUrl", true, "Url Of selenium Grid hub");
		
		Parser parser = new PosixParser();
		try
		{
			CommandLine cl = parser.parse(opts, args);
			if (cl.hasOption("env"))
			{
				String val = cl.getOptionValue("env");
				envsLine = (val.equalsIgnoreCase("")) ? defaultEnv : val;
			}
			
			if (cl.hasOption("hubUrl"))
			{
				String val = cl.getOptionValue("hubUrl");
				hubUrl = (val.equalsIgnoreCase("")) ? defaultHubUrl : val;
			}
		}
		catch (ParseException e1)
		{
			HelpFormatter f = new HelpFormatter();
			f.printHelp("OptionsTip", opts);
		}
		
		if (envsLine.contains("-"))
		{
			String[] spl = envsLine.split("-");
			
			for (String e : spl)
			{
				cliEnvs.add((e.startsWith("*") ? e : ("*" + e)));
			}
		}
		else
		{
			cliEnvs.add(envsLine);
		}
		System.out.println(Arrays.asList(args));
		final String thHubUrl = hubUrl;
		for (final String env : cliEnvs)
		{
			new Thread()
			{
				@Override
				public void run()
				{
					
					try
					{
						String local_ip = java.net.InetAddress.getLocalHost().getHostAddress();
						
						if (local_ip.equalsIgnoreCase(""))
						{
							local_ip = "localhost";
						}
						int port = getGenericPort();
						String[] selargs =
						{ "-port", "" + port, "-host", local_ip, "-hubURL", thHubUrl, "-env", env,
								"-hubPollerIntervalInSeconds", "30" };
						
						// com.thoughtworks.selenium.grid.remotecontrol.SelfRegisteringRemoteControlLauncher.main(selargs);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
			}.start();
		}
	}
}
