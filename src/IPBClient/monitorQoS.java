package IPBClient;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class monitorQoS {
	private ArrayList<Integer> freezingPeriod;
	private int freezingNo;
	private long tsDelta;
	private long startDelay;
	private Date startDownloadTS;
	private ArrayList<String[]> dataArray;
	
    FileWriter logFile;
    PrintWriter out;
	
	public static long minBufferingPeriod = 200;
	
	// Construct the class
	@SuppressWarnings("deprecation")
	public monitorQoS(long startDelay, Date startDownloadTS, String csvdir, String vidName) throws IOException {
		super();
		this.startDelay = startDelay;
		this.startDownloadTS = startDownloadTS;
		
		 // Read the csv file into the string array.	 
		utils.CSVRead csvReader = new utils.CSVRead(csvdir + vidName + ".csv");
		this.dataArray = csvReader.doCSVRead();
		csvReader.CSVReadClose();
		
		// Other variable initialization
		this.tsDelta = 0;
		this.freezingPeriod = new ArrayList<Integer>();
		this.freezingNo = 0;
		
		this.logFile = new FileWriter("D:\\tmp\\" + vidName + "-" + startDownloadTS.getHours() 
				 + "-" + startDownloadTS.getMinutes() + "-" + startDownloadTS.getSeconds() + ".csv");
		this.out = new PrintWriter(logFile);
	}

	public boolean updateQoS(int frmNo) throws IOException, ParseException
	{		
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
		String playbackStart = "00:00.000";
		Date startTS = dateFormat.parse(playbackStart);
		Date frmTS;
		Date playbackTS;
		long receiveTS;
		long playTS;
		String[] row;
		boolean drop = false;
		 
		 // The difference between startTS and playbackTS
		this.tsDelta = startDelay + this.getTotalFreezing(this.freezingPeriod);
		
		// Detect Freezing Event
		row = dataArray.get(frmNo);
		
		// Capture the time stamp of receiving the frames.
		playbackTS = dateFormat.parse(row[3]);
		frmTS = new Date();
		
		playTS = playbackTS.getTime() - startTS.getTime();
		receiveTS = frmTS.getTime() - startDownloadTS.getTime();
		
		// System.out.println("Frame Number: " + frmNo + ", " + playTS + ", " + receiveTS);
	
		// int tmpFreezingTime = (int)(receiveTS - playTS - this.tsDelta);
		int tmpFreezingTime = (int)Math.max((int)receiveTS - playTS - this.tsDelta, minBufferingPeriod);
		
		 if ((receiveTS > playTS + this.tsDelta) && (row[1].equals("I") || row[1].equals("P")))
		 {
			 this.out.println(frmNo + ", " + this.freezingNo + ", " + tmpFreezingTime + ", " +
					 (new SimpleDateFormat("mm:ss.SSS").format(playTS + this.tsDelta)) );
			 System.out.println(frmNo + ", " + this.freezingNo + ", " + tmpFreezingTime + ", " +
					 (new SimpleDateFormat("mm:ss.SSS").format(playTS + this.tsDelta)) );
			 this.freezingNo = this.freezingNo + 1;
			 this.freezingPeriod.add(tmpFreezingTime);
		 }
		 else if ((receiveTS > playTS + this.tsDelta) && (row[1].equals("B")))
		 {
			 drop = true;
			 //drop = false;
		 }
		 
		 //this.out.flush();
		 this.updateTSDelta(startDelay);
		 
		 return drop;
	}
	
	public long getTotalFreezing(ArrayList<Integer> freezingPeriod)
	{
		long totalFreezing = 0;
		for (long freezePeriod : this.freezingPeriod)
			 totalFreezing = totalFreezing + freezePeriod;
		return totalFreezing;
	}

	public void updateTSDelta(long startDelay)
	{
		this.tsDelta = startDelay + this.getTotalFreezing(freezingPeriod);
	}
	
	public void closeLog()
	{
		this.out.close();
	}
	
}
