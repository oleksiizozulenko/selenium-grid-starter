/*
 * --------------------------------------------------------------- 
 * File: LaunchRemoteControl.java 
 * Project: GP-Selenium-RC2Grid-Launcher 
 * Created: Dec 2, 2010 1:20:18 PM A
 * uthor: oleksii.zozulenko 
 * ---------------------------------------------------------------
 */
package gp.selenium.grid2;

import gp.selenium.RCLancher;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.openqa.grid.selenium.GridLauncher;
import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

/**
 * @author oleksii.zozulenko
 */
public class LaunchRC extends RCLancher {

	/**
	 * @param args
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public static void main(final String[] args) throws UnknownHostException,
			InterruptedException, Exception {
		String defaultEnv = "firefox";
		String envsLine = "firefox";
		String defaultHubUrl = "http://kievtomcat.kievlan.local:44444/";
		String hubUrl = "";
		String maxInstances = "6";
		String brVers = null;
		List<String> tmpArgs = Arrays.asList(args);

		while (tmpArgs.contains(null)) {
			int s = tmpArgs.indexOf(null);
			try {
				tmpArgs.set(s, "");
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
		String[] argsNotNull = tmpArgs.toArray(new String[tmpArgs.size()]);

		Options opts = new Options();
		opts.addOption("environment", true, "Environment (Browser to launch)");
		opts.addOption("instances", true, "How many browsers to start there");
		Option version = new Option("version", true, "Browser version to start");
		version.setOptionalArg(true);

		opts.addOption(version);
		opts.addOption("hubUrl", true, "Url Of selenium Grid hub");

		Parser parser = new PosixParser();
		try {
			CommandLine cl = parser.parse(opts, argsNotNull);

			if (cl.hasOption("environment")) {
				String val = cl.getOptionValue("environment");
				envsLine = (val.equalsIgnoreCase("")) ? defaultEnv : val;
			}

			if (cl.hasOption("hubUrl")) {
				String val = cl.getOptionValue("hubUrl");
				hubUrl = (val.equalsIgnoreCase("")) ? defaultHubUrl : val;
			}

			if (cl.hasOption("instances")) {
				maxInstances = cl.getOptionValue("instances");
			}

			if (cl.hasOption("version")) {
				brVers = cl.getOptionValue("version");

				if (brVers != null) {
					if (brVers.equalsIgnoreCase("")
							|| brVers.equalsIgnoreCase("${rc.version}")
							|| brVers.equalsIgnoreCase("any")) {
						brVers = null;
					}
				}
			}
		} catch (ParseException e1) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp("OptionsTip", opts);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		final String thHubUrl = hubUrl + "grid/register";

		String[] roles = { /* "rc", */"webdriver" };

		java.net.InetAddress rchost = java.net.InetAddress.getLocalHost();
		String my_ip = rchost.getHostAddress();
		String my_host_name = rchost.getCanonicalHostName();

		String pre_local_ip = "localhost";

		if (!my_host_name.equalsIgnoreCase("")) {
			pre_local_ip = my_host_name;
		} else if (!my_ip.equalsIgnoreCase("")) {
			pre_local_ip = my_ip;
		}

		final String local_ip = pre_local_ip;
		final String env = envsLine;
		final Integer insts = Integer.parseInt(maxInstances);
		final String brVersion = brVers;

		for (final String role : roles) {
			new Thread() {
				@Override
				public void run() {
					try {

						int port = getGenericPort();
						Integer maxinsts = ((role.equals("rc")) ? (2 * insts)
								: insts);
						String[] selargs = {
								"-role",
								"node",
								"-hub",
								thHubUrl,
								"-host",
								local_ip,
								"-port",
								"" + port,
								"-browser",
								"browserName="
										+ env
										+ ((brVersion == null) ? ""
												: (",version=" + brVersion))
										+ ",maxInstances=" + maxinsts,
								"-maxConcurrent", "" + maxinsts,
								"-nodeTimeout", "3000000", "-registerCycle",
								"1374389" };

						GridLauncher.main(selargs);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}.start();
			Thread.sleep(3000);
		}
	}

	public static String reverseDns(String hostIp) throws IOException {
		Record opt = null;
		Resolver res = new ExtendedResolver();

		Name name = ReverseMap.fromAddress(hostIp);
		int type = Type.PTR;
		int dclass = DClass.IN;
		Record rec = Record.newRecord(name, type, dclass);
		Message query = Message.newQuery(rec);
		Message response = res.send(query);

		Record[] answers = response.getSectionArray(Section.ANSWER);
		if (answers.length == 0)
			return hostIp;
		else
			return answers[0].rdataToString();
	}
}
