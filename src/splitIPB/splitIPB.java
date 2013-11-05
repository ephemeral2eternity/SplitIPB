package splitIPB;

import java.io.*;
import java.util.ArrayList;
import utils.splitIO;
/**
 * split one 264 video file into three files contains only I, P, and B frames.
 * @author chenw revised on Feb-1-2013
 *
 */
public class splitIPB{


public static void main(String[] arg) throws Exception {

	 String dir = "D:\\Dropbox\\videoDataSet\\IPB\\";
	 String vidName = "ted";
	 
	 // Read the csv file into the string array.	 
	 utils.CSVRead csvReader = new utils.CSVRead(dir + "tsData\\" + vidName + ".csv");
	 ArrayList<String[]> dataArray = csvReader.doCSVRead();
	 csvReader.CSVReadClose();
	  
	 // Read the video file
	 File vidFile = new File(dir + vidName + ".mp4");
	 RandomAccessFile vidRaf = new RandomAccessFile(vidFile, "r");
	 
	 /*// Create Directories for I, P, B files
	 File dirIFrames = new File(dir + vidName + "-IPBFrames"); dirIFrames.mkdir();
	 
	 int frameSize;
	 int frmNo;
	 long offSet;
	 long curPos;
	 int readBytes = 0;
	 String frmTyp;
	 byte[] byteChunk;
	 int splitBytes = 0;
	 FileOutputStream outputFile = null;
	 
	 for (String[] row : dataArray)
	 {
		 frmNo = Integer.parseInt(row[0]);
		 frmTyp = row[1];
		 frameSize = Integer.parseInt(row[4]);
		 offSet = Integer.parseInt(row[2].substring(2), 16);
		 
		 curPos = vidRaf.getFilePointer();
		 
		 if (curPos != offSet)
		 {
			 frameSize = (int) (offSet + frameSize - curPos);
		 }
		 
		 byteChunk = new byte[frameSize];
		 readBytes = vidRaf.read(byteChunk, 0, frameSize);
		 
		 if (frmTyp.equals("I"))
		 {
			 outputFile = new FileOutputStream(dir + vidName + "-IPBFrames\\"
			 + Long.toString(curPos) + "_" + Integer.toString(frameSize) + "_I" ); 
		 }
		 else if (frmTyp.equals("P"))
		 {	
			 outputFile = new FileOutputStream(dir + vidName + "-IPBFrames\\" 
			 + Long.toString(curPos) + "_" + Integer.toString(frameSize) + "_P" ); 
		 }
		 else if (frmTyp.equals("B"))
		 {
			 outputFile = new FileOutputStream(dir + vidName + "-IPBFrames\\" 
			 + Long.toString(curPos) + "_" + Integer.toString(frameSize) + "_B"); 
		 }
		 
		 outputFile.write(byteChunk);
		 
		 byteChunk = null;
		 
		 outputFile.close();
		 splitBytes = splitBytes + readBytes;
		 
		 System.out.print(frmTyp + "\t" + curPos + "\t" + frameSize);
		 System.out.println();
	 }*/
	 
	 
	 FileOutputStream iFile, pFile, bFile;
	 
	 // Open file handler
	 iFile = new FileOutputStream(dir + vidName + "_IF.txt");
	 pFile = new FileOutputStream(dir + vidName + "_PF.txt");
	 bFile = new FileOutputStream(dir + vidName + "_BF.txt");
	 
	 
	 int frameSize;
	 long offSet;
	 long curPos;
	 int readBytes = 0;
	 String frmTyp;
	 byte[] byteChunk;
	 int splitBytes = 0;
	 int frmNo;
	 
	 for (String[] row : dataArray)
	 {
		 frmNo = Integer.parseInt(row[0]);
		 frmTyp = row[1];
		 frameSize = Integer.parseInt(row[4]);
		 offSet = Integer.parseInt(row[2].substring(2), 16);
		 curPos = vidRaf.getFilePointer();
		 
		 if (curPos != offSet)
		 {
			 frameSize = (int) (offSet + frameSize - curPos);
		 }
		 
		 if (frmTyp.equals("I"))
		 {
			 byteChunk = new byte[frameSize];
			 readBytes = vidRaf.read(byteChunk, 0, frameSize);
			 
			 iFile.write(splitIO.Long2Bytes(curPos, 8));
			 iFile.write(splitIO.Int32toBytes(frameSize));
			 iFile.write(splitIO.Int32toBytes(frmNo));
			 iFile.write(byteChunk);
			 byteChunk = null;
			 
		 }
		 else if (frmTyp.equals("P"))
		 {
			 byteChunk = new byte[frameSize];
			 // vidRaf.seek(offSet);
			 readBytes = vidRaf.read(byteChunk, 0, frameSize);
			 
			 pFile.write(splitIO.Long2Bytes(curPos, 8));
			 pFile.write(splitIO.Int32toBytes(frameSize));
			 pFile.write(splitIO.Int32toBytes(frmNo));
			 pFile.write(byteChunk);
			 byteChunk = null;
			 
		 }
		 else if (frmTyp.equals("B"))
		 {
			 byteChunk = new byte[frameSize];
			 // vidRaf.seek(offSet);
			 readBytes = vidRaf.read(byteChunk, 0, frameSize);
			 
			 bFile.write(splitIO.Long2Bytes(curPos, 8));
			 bFile.write(splitIO.Int32toBytes(frameSize));
			 bFile.write(splitIO.Int32toBytes(frmNo));
			 bFile.write(byteChunk);
			 byteChunk = null;
		 }
		 splitBytes = splitBytes + readBytes;
		 
		 System.out.print(frmTyp + "\t" + curPos + "\t" + frameSize);
		 System.out.println();
	 }
	 
	 vidRaf.close();
	 iFile.close();
	 pFile.close();
	 bFile.close();
	 
 }
} 