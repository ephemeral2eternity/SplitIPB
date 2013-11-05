package stitchDASH;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import utils.CSVRead;

public class StitchDASH {
	public static void main(String[] arg) throws Exception 
	{
	 String dir = "D:\\Dropbox\\videoDataSet\\adaptiveStreaming\\";
	 String outDir = "D:\\exp\\";
	 String vidName = "brave";
	 ArrayList<String[]> dataArray;
     String[] levels = {"230", "470", "950", "1900", "2800", "5600"};
     int chunkNo = 0;
	 
	  
	 // Read the video file
	 File vidFile = new File(outDir + vidName + "-DASH-rec.mp4");
	 FileOutputStream os = new FileOutputStream(vidFile);
	 
	 // Open manifest file 
	 utils.CSVRead csvReader = new utils.CSVRead(dir + vidName + ".csv");
	 dataArray = csvReader.doCSVRead();
	 csvReader.CSVReadClose();
	 
	 @SuppressWarnings("unused")
	 byte[] byteChunk = null;
	 long startOffset;
	 long endOffset;
	 String[] curRow, nextRow;
	 int chunkLevel = 0;
	 //FileInputStream inFile;
	 int chunkSize;
	 int osOffset = 0;
	 RandomAccessFile input;
	 
	 ArrayList<RandomAccessFile> fpArray = new ArrayList<RandomAccessFile>();
	 
	 for (int i = 0; i < dataArray.get(0).length; i ++)
	 {
		 RandomAccessFile fip = new RandomAccessFile(dir + vidName + "_" + levels[i] + ".mp4", "r");
		 fpArray.add(fip);
	 }
	 
	 // Filling bytes from IFrame file.
	 while ( chunkNo < dataArray.size() - 1 )
	 {
		 //inFile = null;
		 //byteChunk = null;
		 curRow = dataArray.get(chunkNo);
		 nextRow = dataArray.get(chunkNo + 1);
		 input = fpArray.get(chunkLevel);
		 
		 startOffset = Integer.parseInt(curRow[chunkLevel].substring(2), 16);
		 endOffset = Integer.parseInt(nextRow[chunkLevel].substring(2), 16) - 1;
		 chunkSize = (int) (endOffset - startOffset + 1);
			 
		 input.seek(startOffset);
		 byteChunk = utils.stitchIO.sureRead(input, chunkSize);
		 os.write(byteChunk);
		 
		 System.out.println("Write chunk " + Integer.toString(chunkNo) + " in level " + levels[chunkLevel]);
		 chunkNo = chunkNo + 1;
		 // inFile.close();
		 if (chunkNo%20 == 0)
		 {
			 chunkLevel = chunkLevel + 1;
		 }
	 
	 }
	 os.close();
	 
	 for (int i = 0; i < dataArray.get(0).length; i ++)
	 {
		 fpArray.get(i).close();
	 }
	}

}
