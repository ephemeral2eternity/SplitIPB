package IPBClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;


public class downloadThread extends Thread{
	
	private URL url;
	private InputStream inputStream;
	private RandomAccessFile vidRaf;
	private Date requestTime;
	private monitorQoS monQoS;
	
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public downloadThread(String urlString, File vidFile, String vidName, monitorQoS mQoS) throws IOException {
		super();
		try {
			this.url = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Input URL Stream
		try {
			this.inputStream = this.url.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.vidRaf = new RandomAccessFile(vidFile, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.requestTime = new Date();
		this.monQoS = mQoS;
	}

	// Downloading Thread for each type of Frame
	public synchronized void run() 
	{
		long offset;
		int frameSize;
		int frmNo;
		byte[] byteChunk;
		@SuppressWarnings("unused")
		long frmTimeStamp;
		boolean drop = false;
		 // Filling bytes from IFrame file.
		 offset = utils.stitchIO.readInt64(inputStream);
		 frameSize = utils.stitchIO.readInt32(inputStream);
		 frmNo = utils.stitchIO.readInt32(inputStream);
		 
		 if (frmNo < 0)
			 return;

		 // System.out.print(offset + "\t" + frameSize);
		 // System.out.println();
		 byteChunk = utils.stitchIO.sureRead(inputStream, frameSize); 
		 
		 frmTimeStamp = captureTS(requestTime);
		 // System.out.println("Frame" + frmNo + " Receiving TS: "+ frmTimeStamp);
		 
		 try {
			drop = this.monQoS.updateQoS(frmNo);
		 } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 } catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }
		 
		 try {
			if (!drop)
			{
				vidRaf.seek(offset);
				vidRaf.write(byteChunk, 0, frameSize);
			}
			else
			{
				// vidRaf.seek(offset);
				// vidRaf.write(byteChunk, 0, frameSize);
				System.out.println("!!!!!!!!!!!!!!!Frame" + frmNo + " Dropped !!!!!!!!!!!!!!!!!!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		
		if ((offset >= 0) && (frameSize > 0))
		{
			this.run();
			// this.monQoS.closeLog();
		}
	}
	
	// Print the relative receiving timestamp 
	// according to the baseline REQUEST timestamp.
	
	public long captureTS(Date startDate)
	{
		Date curTS = new Date();
		long frmTS;
		
		frmTS = curTS.getTime() - startDate.getTime();
		return frmTS;
	}
	
}
