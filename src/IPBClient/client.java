package IPBClient;

import java.io.File;
import java.util.Date;

public class client {
	
	
	public static void main(String[] args) throws Exception {

		 String outDir = ".";
		 String I_IP = "10.7.7.68";
		 String P_IP = "10.7.7.68";
		 String B_IP = "10.7.7.68";
		 String vidName = "brave";
		 String csvDir = ".";
	     long startDelay = 100;
		 
         if (args.length > 0)
         {
     		// Show licence if requested
     		for (int i = 0; i < args.length; ++i)
     			if (args[i].equalsIgnoreCase("-I"))
     				I_IP = args[i + 1];
     			else if (args[i].equalsIgnoreCase("-P"))
     				P_IP = args[i + 1];
     			else if (args[i].equalsIgnoreCase("-B"))
     				B_IP = args[i + 1];
     			else if (args[i].equalsIgnoreCase("-V"))
     				vidName = args[i + 1];
     			else if (args[i].equalsIgnoreCase("-D"))
     				csvDir = args[i + 1];
     			else if (args[i].equalsIgnoreCase("-O")) {
     				outDir = args[i + 1];
     				break;
     			}
         }
         else
         {
        	 System.out.println("Wrong input arguments. You need to denote the arguments as follows:");
	         System.out.println("-V $(videoName.mp4)");
	         System.out.println("-I $(I Server IP address)");
	         System.out.println("-P $(I Server IP address)");
	         System.out.println("-B $(I Server IP address)");
	         System.out.println("-O $(Local Directory)");
	         System.out.println("-D $(CSV File Directory)");
	         System.exit(0);  // is it necessary? And when it must be called? 
         }
         
		 
		 String I_URL = "http://" + I_IP + '/' + vidName + "_IF";
		 String P_URL = "http://" + P_IP + '/' + vidName + "_PF";
		 String B_URL = "http://" + B_IP + '/' + vidName + "_BF";
		 
		 // The start downloading timestamp
		 Date startTS = new Date();
		 
		 // Create a monitorQoS instance.
		 monitorQoS monQoS = new monitorQoS(startDelay, startTS, csvDir, vidName);
		 
		 // Read the video file
		 File vidFile = new File(outDir + vidName + "-recIPB.mp4");
		 
		 downloadThread iDownload = new downloadThread(I_URL, vidFile, vidName, monQoS);
		 downloadThread pDownload = new downloadThread(P_URL, vidFile, vidName, monQoS);
		 downloadThread bDownload = new downloadThread(B_URL, vidFile, vidName, monQoS);
		  
		 iDownload.setRequestTime(startTS);
		 pDownload.setRequestTime(startTS);
		 bDownload.setRequestTime(startTS);
		 
		 iDownload.start();
		 pDownload.start();
		 bDownload.start();	 
		 
		 monQoS.closeLog();
	 }
}
