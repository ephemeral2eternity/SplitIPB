package stitchingOnTheFly;


import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.nio.*;
import java.net.URL;
import java.net.HttpURLConnection;

public class StitchingOnTheFly {

public static void main(String[] arg) throws Exception {

	 String urlString = "http://chenw.cloudapp.net/IPBStreaming/";
	 String vidName = "cloudAtlas";
	 String outDir = "D:\\tmp\\";
	 int frmInd = 0;
	 SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
	 String playbackStart = "00:00.000";
	 Date startTS = dateFormat.parse(playbackStart);
	 
	 //Date curDate;
	 //String method = "noMiss";
	 //long startMissing = 70000000;
	 //long endMissing = 100000000;
	 
	 // Read the csv file into the string array.	 
	 utils.CSVRead csvReader = new utils.CSVRead("D:\\Dropbox\\videoDataSet\\IPB\\tsData\\" + vidName + ".csv");
	 ArrayList<String[]> dataArray = csvReader.doCSVRead();
	 csvReader.CSVReadClose();
	  
	 // Read the video file
	 File vidFile = new File(outDir + "recIPB.mp4");
	 RandomAccessFile vidRaf = new RandomAccessFile(vidFile, "rw");
	
	 // All the urls for the I, P, B frames.
	 URL urlIFrame = new URL(urlString + vidName + "_IF.txt");
	 URL urlPFrame = new URL(urlString + vidName + "_PF.txt");
	 URL urlBFrame = new URL(urlString + vidName +"_BF.txt");
	 
	 Date startDate = new Date();
	 Date playbackTS;
	 
	 // Open file Directory
	 InputStream iFile = urlIFrame.openStream();
	 InputStream pFile = urlPFrame.openStream();
	 InputStream bFile = urlBFrame.openStream();
	 
	 byte[] byteChunk;
	 long iOffset, pOffset, bOffset;
	 int iFrameSize, pFrameSize, bFrameSize;
	 int iFrmNo, pFrmNo, bFrmNo;
	 
	 // Filling bytes from IFrame file.
	 iOffset = utils.stitchIO.readInt64(iFile); 
	 pOffset = utils.stitchIO.readInt64(pFile); 
	 bOffset = utils.stitchIO.readInt64(bFile);
	 iFrameSize = utils.stitchIO.readInt32(iFile); 
	 pFrameSize = utils.stitchIO.readInt32(pFile); 
	 bFrameSize = utils.stitchIO.readInt32(bFile);
	 iFrmNo = utils.stitchIO.readInt32(iFile);
	 pFrmNo = utils.stitchIO.readInt32(pFile);
	 bFrmNo = utils.stitchIO.readInt32(bFile);
	 
	 while ( ((iOffset >= 0) && (iFrameSize > 0)) ||
			 ((pOffset >= 0) && (pFrameSize > 0)) ||
			 ((bOffset >= 0) && (bFrameSize > 0)))
	 {
		 // System.out.print(offset + "\t" + frameSize);
		 // System.out.println();
		 
		 if (((iOffset < pOffset) && (iOffset < bOffset) && (iOffset >= 0) && (iFrameSize > 0)) ||
		 ((iOffset < pOffset) && (iOffset >= 0) && (iFrameSize > 0) && (bOffset < 0)) ||
		 ((iOffset >= 0) && (iFrameSize > 0) && (pOffset < 0) && (bOffset < 0)) ) 
		 {
			 byteChunk = utils.stitchIO.sureRead(iFile, iFrameSize);
			 // System.out.print(iOffset + "\t" + iFrameSize);
			 // System.out.println();
			 vidRaf.seek(iOffset);
			 vidRaf.write(byteChunk, 0, iFrameSize);
			 
			 playbackTS = dateFormat.parse(dataArray.get(frmInd)[3]);
			 
			 // Capture the receiving time stamp
			 System.out.println("Playback Timestamp: " + (playbackTS.getTime() - startTS.getTime())
					 + " Receiving Timestamp: "+ Long.toString((new Date().getTime() - startDate.getTime())));
			 
			 frmInd = frmInd + 1;
			 iOffset = utils.stitchIO.readInt64(iFile);
			 iFrameSize = utils.stitchIO.readInt32(iFile);
			 iFrmNo = utils.stitchIO.readInt32(iFile);
		 }
		 
		 if (((pOffset < iOffset) && (pOffset < bOffset) && (pOffset >= 0) && (pFrameSize > 0)) ||
				 ((pOffset < bOffset) && (pOffset >= 0) && (pFrameSize > 0) && (iOffset < 0)) ||
				 ((pOffset >= 0) && (pFrameSize > 0) && (iOffset < 0) && (bOffset < 0)) ) 
		 {
			 byteChunk = utils.stitchIO.sureRead(pFile, pFrameSize);
			 //System.out.print(pOffset + "\t" + pFrameSize);
			 //System.out.println();
			 vidRaf.seek(pOffset);
			 vidRaf.write(byteChunk, 0, pFrameSize);
			 
			 playbackTS = dateFormat.parse(dataArray.get(frmInd)[3]);
			 
			 // Capture the receiving time stamp
			 System.out.println("Playback Timestamp: " + (playbackTS.getTime() - startTS.getTime())
					 + " Receiving Timestamp: "+ Long.toString((new Date().getTime() - startDate.getTime())));
			 
			 frmInd = frmInd + 1;
			 
			 pOffset = utils.stitchIO.readInt64(pFile);
			 pFrameSize = utils.stitchIO.readInt32(pFile);
			 pFrmNo = utils.stitchIO.readInt32(pFile);
		 }
		  
		 if (((bOffset < iOffset) && (bOffset < pOffset) && (bOffset >= 0) && (bFrameSize > 0)) ||
				 ((bOffset < pOffset) && (bOffset >= 0) && (bFrameSize > 0) && (iOffset < 0)) ||
				 ((bOffset >= 0) && (bFrameSize > 0) && (iOffset < 0) && (pOffset < 0)) ) 
		 {
			 byteChunk = utils.stitchIO.sureRead(bFile, bFrameSize);
			 //System.out.print(bOffset + "\t" + bFrameSize);
			 //System.out.println();
			 vidRaf.seek(bOffset);
			 vidRaf.write(byteChunk, 0, bFrameSize);
			 
			 playbackTS = dateFormat.parse(dataArray.get(frmInd)[3]);
			 
			 // Capture the receiving time stamp
			 System.out.println("Playback Timestamp: " + (playbackTS.getTime() - startTS.getTime())
					 + " Receiving Timestamp: "+ Long.toString((new Date().getTime() - startDate.getTime())));
			 
			 frmInd = frmInd + 1;
			 
			 bOffset = utils.stitchIO.readInt64(bFile);
			 bFrameSize = utils.stitchIO.readInt32(bFile);
			 bFrmNo = utils.stitchIO.readInt32(bFile);
		 }
	 }
	 
	 vidRaf.close();
	 iFile.close();
	 pFile.close();
	 bFile.close();
 }
}
