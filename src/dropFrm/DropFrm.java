package dropFrm;

import java.io.*;
import java.util.ArrayList;
import utils.splitIO;
/**
 * read files to drop frames.
 * @author chenw revised on Jun-13-2013
 *
 */
public class DropFrm{


public static void main(String[] arg) throws Exception {

	 String manifest_csv = "D:\\Dropbox\\codes\\Matlab\\optFrmDropping\\data\\brave-drop-2.csv";
	 String drop_csv = "D:\\exp\\OFD\\brave.csv";
	 String video = "D:\\exp\\OFD\\brave.mp4";
	 String out_video = "";
	 
     if (arg.length > 0)
     {
 		// Show licence if requested
 		for (int i = 0; i < arg.length; ++i)
 		{
 			if (arg[i].equalsIgnoreCase("-i"))
 				video = arg[i + 1];
 			else if (arg[i].equalsIgnoreCase("-m"))
 				manifest_csv = arg[i + 1];
 			else if (arg[i].equalsIgnoreCase("-d"))
 				drop_csv = arg[i + 1];
 			else if (arg[i].equalsIgnoreCase("-o"))
 				out_video = arg[i + 1];
 		}
     }
     else
     {
    	 	System.out.println("No input arguments. You need to denote the arguments as follows:");
	         System.out.println("-i $(The input mp4 video)");
	         System.out.println("-m $(The manifest file)");
	         System.out.println("-d $(The frame-dropping file)");
	         System.out.println("-o $(The output mp4 video)");
	         System.exit(0);  // is it necessary? And when it must be called? 
     }
	 
	 // Read the csv file into the string array.	 
	 utils.CSVRead manifestReader = new utils.CSVRead(manifest_csv);
	 ArrayList<Integer[]> manifestArray = manifestReader.load2DArray();
	 manifestReader.CSVReadClose();
	 
	 // Read the csv file into the string array.	 
	 utils.CSVRead droppingcsvReader = new utils.CSVRead(drop_csv);
	 ArrayList<Integer[]> dropArray = droppingcsvReader.load2DArray();
	 droppingcsvReader.CSVReadClose();
	 	  
	 // Read the video file
	 File vidFile = new File(video);
	 RandomAccessFile vidRaf = new RandomAccessFile(vidFile, "r");
	 
	 File outFile = new File(out_video);
	 RandomAccessFile vidOut = new RandomAccessFile(outFile, "rw");
	
	 int frameSize;
	 long curPos;
	 int readBytes = 0;
	 int frmTyp;
	 byte[] byteChunk;
	 ArrayList<Integer> droppedFrms = new ArrayList<Integer>();
	 
	 droppedFrms = parseDropArray(dropArray);
	 
	 int frmNo = 0;;
	 
	 curPos = vidRaf.getFilePointer();
	 for (frmNo = 0; frmNo < manifestArray.size(); frmNo ++)
	 {
		 Integer[] row = manifestArray.get(frmNo);
		 frmTyp = row[2];
		 frameSize = row[1];
		 		 
		 byteChunk = new byte[frameSize];
		 readBytes = vidRaf.read(byteChunk, 0, frameSize);
		 
		 if (!droppedFrms.contains(frmNo))
		 {
			 vidOut.seek(curPos);
			 vidOut.write(byteChunk);
			 byteChunk = null;
			 curPos = vidRaf.getFilePointer();
			 //System.out.println("The end of the frame " + frmNo + " is " + curPos);
		 }
		 else
		 {
			 System.out.println("Frame " + "\t" + frmNo + "\t dropped!");
			 
			 for (int i = 0; i < byteChunk.length; i ++)
				 byteChunk[i] = 0;
			 // vidOut.seek(curPos);
			 vidOut.write(byteChunk);
			 curPos = vidRaf.getFilePointer();
			 //System.out.println("The end of the frame " + frmNo + " is " + curPos);
			 byteChunk = null;
		 }
		 
	 }
	 
	 vidOut.close();
	 vidRaf.close();
 }

	/**
	 * Read the dropped frames from 2D array into one Integer ArrayList
	 * @param dropArray
	 * @return
	 */
	static ArrayList<Integer> parseDropArray(ArrayList<Integer[]> dropArray)
	{
		ArrayList<Integer> droppedFrms = new ArrayList<Integer>();
		
		for(Integer[] item : dropArray)
		{
			Integer dropped_frm_num = item[0];
			droppedFrms.add(dropped_frm_num);
		}
		
		return droppedFrms;
	}
} 