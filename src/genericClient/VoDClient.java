package genericClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import utils.splitIO;

public class VoDClient extends Thread{
	
	private static String serverURL = "";
	private static String vidName = "";
	private static String localBuffer = "";
	private ArrayList<Integer> freezingPeriod;
	static long startDelay = 2000;
	static long minBufferingPeriod = 200;
	
	Date startTS;
	ArrayList<String[]> dataArray;
	RandomAccessFile vidRaf;
	InputStream vidStream;
	int frmNo;
	SimpleDateFormat dateFormat;
	Date curTS;
    FileWriter logFile;
    PrintWriter out;
	int freezingNo;
	long tsDelta;
	
	public VoDClient(String serverURL, String vidName, String csvDir, String outDir) throws ParseException, IOException {
		super();
		this.serverURL = serverURL;
		this.vidName = vidName;
		this.localBuffer = outDir;
		
		this.dateFormat = new SimpleDateFormat("mm:ss.SSS");
		String playbackStart = "00:00.000";
		this.startTS = dateFormat.parse(playbackStart);
		this.curTS = new Date();
		 
		 // Read the csv file into the string array.	 
		 utils.CSVRead csvReader = new utils.CSVRead(csvDir + vidName + ".csv");
		 this.dataArray = csvReader.doCSVRead();
		 csvReader.CSVReadClose();
		  
		 // Read the video file
		 File vidFile = new File(localBuffer + vidName + "-" + curTS.getHours() 
				 + "-" + curTS.getMinutes() + "-" + curTS.getSeconds() + ".mp4");
		 this.vidRaf = new RandomAccessFile(vidFile, "rw");
		 
		 // Generic Video Downloading
		 URL vidURL = new URL(serverURL + vidName + ".mp4");
		 this.vidStream = vidURL.openStream();
		 this.frmNo = 0;
		 
		 logFile = new FileWriter(localBuffer + vidName + "-" + curTS.getHours() 
				 + "-" + curTS.getMinutes() + "-" + curTS.getSeconds() + ".csv");
		 out = new PrintWriter(logFile);
		 
		 this.freezingPeriod = new ArrayList<Integer>();
		 this.freezingNo = 0;
	}

	public void run() {
		 int frameSize;
		 long offSet;
		 long curPos;
		 // String frmTyp;
		 byte[] byteChunk = null;
		 Date frmTS;
		 long totalFreezing = 0;
		 String[] row;
		 long receiveTS;
		 long playTS;
		 
		 row = dataArray.get(this.frmNo);
		 //frmNo = Integer.parseInt(row[0]);
		 frameSize = Integer.parseInt(row[4]);
		 offSet = Integer.parseInt(row[2].substring(2), 16);
		 
		 for (long freezePeriod : this.freezingPeriod)
			 totalFreezing = totalFreezing + freezePeriod;
		 
		this.tsDelta = this.startDelay + totalFreezing;
		
		 try {
			curPos = vidRaf.getFilePointer();
			
			 if (curPos != offSet)
			 {
				 frameSize = (int) (offSet + frameSize - curPos);
			 }
			 
			 byteChunk = new byte[frameSize];
			 byteChunk = utils.stitchIO.sureRead(this.vidStream, frameSize);

		} catch (IOException e1) {
		}
		 
		 
		 // Capture the time stamp of receiving the frames.
		 try {
			 Date playbackTS = dateFormat.parse(row[3]);
			 frmTS = new Date();
			 
			 playTS = playbackTS.getTime() - startTS.getTime();
			 receiveTS = frmTS.getTime() - curTS.getTime();
			 
			 int tmpFreezingTime = (int)Math.max((int)receiveTS - playTS - this.tsDelta, minBufferingPeriod);
			 
			 if (receiveTS > playTS + this.tsDelta)
			 {
				 this.out.println(this.frmNo + ", " + this.freezingNo + ", " + tmpFreezingTime + ", " +
						 (new SimpleDateFormat("mm:ss.SSS").format(playTS + this.tsDelta)) );
				 System.out.println("Freeze: " + this.freezingNo + " with "
						 + tmpFreezingTime + "milliseconds");
				 this.freezingNo = this.freezingNo + 1;
				 this.freezingPeriod.add(tmpFreezingTime);
			 }
			 
			 System.out.println("Frame Number: " + this.frmNo);
			 // System.out.println("Frame: " + frmNo + " Playback Timestamp: " 
				//	 + Long.toString(playTS) + " Receiving TS: " + Long.toString(receiveTS));
			 
			 this.out.flush();
			 this.frmNo = this.frmNo + 1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		 
		 try {
			vidRaf.write(byteChunk, 0, frameSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		 
		 if (this.frmNo < dataArray.size())
			 this.run();
		 else
			 this.out.close();
			 
	}
}
