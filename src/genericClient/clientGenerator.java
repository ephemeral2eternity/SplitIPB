package genericClient;

import java.util.*;

public class clientGenerator {
	
	public static void main(String[] args) throws Exception {
		
		String serverURL = "http://aussie.cylab.cmu.edu:8080/";
		int N = 1;
		ArrayList<String> vidNames = new ArrayList<String>();
		String curVidName;
		String csvDir = "";
		String outDir = "";
		Random generator = new Random();
		
        if (args.length > 0)
        {
    		// Show licence if requested
    		for (int i = 0; i < args.length; ++i)
    			if (args[i].equalsIgnoreCase("-V"))
    				vidNames.add(args[i + 1]);
    			else if (args[i].equalsIgnoreCase("-S"))
    				serverURL = "http://" + args[i + 1] + "/";
    			else if (args[i].equalsIgnoreCase("-O"))
    				outDir = args[i + 1];
    			else if (args[i].equalsIgnoreCase("-D"))
    				csvDir = args[i + 1];
    			else if (args[i].equalsIgnoreCase("-N")) {
    				N = Integer.parseInt(args[i + 1]);;
    				break;
    			}
        }
        else
        {
       	 System.out.println("Wrong input arguments. You need to denote the arguments as follows:");
	         System.out.println("-V $(videoName.mp4)");
	         System.out.println("-S Server_URL");
	         System.out.println("-O $(Out Dir)");
	         System.out.println("-D $(CSV Dir)");
	         System.out.println("-N $(The number of connections to set up.)");
	         System.exit(0);  // is it necessary? And when it must be called? 
        }
        
		
		double r;
		ArrayList<VoDClient> vodClientList = new ArrayList<VoDClient>();
		
		for (int i = 0; i < N; i ++)
		{
			curVidName = vidNames.get(i % vidNames.size());
			vodClientList.add(new VoDClient(serverURL, curVidName, csvDir, outDir));
		}
		
		for (int i = 0; i < N; i ++)
		{
			vodClientList.get(i).start();
	    	
	    	r = generator.nextDouble() * 20;
	    	wait(r);
		}    	   
	}
	
	public static void wait (double n){
        long t0,t1;
        t0 = System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        }
        while (t1-t0 < n*1000);
	}

}
