package stitchIPB;

import java.io.*;
import java.util.ArrayList;

public class StitchIPB {

public static void main(String[] arg) throws Exception {

	 String dir = "D:\\Dropbox\\videoDataSet\\IPB\\";
	 String outDir = "D:\\Dropbox\\videoDataSet\\IPB\\";
	 String vidName = "cloudAtlas";
	 String method = "missAll";
	 long startMissing = 70000000;
	 long endMissing = 100000000;
	 
	  
	 // Read the video file
	 File vidFile = new File(outDir + vidName + "-" + method + "-rec.mp4");
	 RandomAccessFile vidRaf = new RandomAccessFile(vidFile, "rw");

	 FileInputStream iFile, pFile, bFile;
	 
	 // Open file handler
	 iFile = new FileInputStream(dir + vidName + ".IFrame");
	 pFile = new FileInputStream(dir + vidName + ".PFrame");
	 bFile = new FileInputStream(dir + vidName + ".BFrame");
	 
	 @SuppressWarnings("unused")
	 byte[] byteChunk;
	 long offset;
	 int frameSize;
	 int frmNo;
	 
	 // Filling bytes from IFrame file.
	 offset = utils.stitchIO.readInt64(iFile);
	 frameSize = utils.stitchIO.readInt32(iFile);
	 frmNo = utils.stitchIO.readInt32(iFile);
	 while ( (offset != -1) && (frameSize != -1) )
	 {
		 // System.out.print(offset + "\t" + frameSize);
		 // System.out.println();
		 byteChunk = utils.stitchIO.sureRead(iFile, frameSize);
		 
		 vidRaf.seek(offset);
		 
		 if ((method.equals("missI")) || (method.equals("missAll")))
		 {
			 if ( (offset < startMissing) || (offset > endMissing) )
			 {
				 vidRaf.write(byteChunk, 0, frameSize);
			 }
		 }
		 else
		 {
			 vidRaf.write(byteChunk, 0, frameSize);
		 }
			 
		 // vidRaf.write(byteChunk, 0, frameSize);
		 
		 offset = utils.stitchIO.readInt64(iFile);
		 frameSize = utils.stitchIO.readInt32(iFile);
	 }
	 
	 // Filling bytes from PFrame file.
	 offset = utils.stitchIO.readInt64(pFile);
	 frameSize = utils.stitchIO.readInt32(pFile);
	 frmNo = utils.stitchIO.readInt32(pFile);
	 while ( (offset != -1) && (frameSize != -1) )
	 {
		 // System.out.print(offset + "\t" + frameSize);
		 // System.out.println();
		 byteChunk = utils.stitchIO.sureRead(pFile, frameSize);
		 
		 vidRaf.seek(offset);
		 if ((method.equals("missP")) || (method.equals("missAll")))
		 {
			 if ( (offset < startMissing) || (offset > endMissing) )
			 {
				 vidRaf.write(byteChunk, 0, frameSize);
			 }
		 }
		 else
		 {
			 vidRaf.write(byteChunk, 0, frameSize);
		 }
		 
		 offset = utils.stitchIO.readInt64(pFile);
		 frameSize = utils.stitchIO.readInt32(pFile);
	 }
	 
	 // Filling bytes from BFrame file.
	 offset = utils.stitchIO.readInt64(bFile);
	 frameSize = utils.stitchIO.readInt32(bFile);
	 frmNo = utils.stitchIO.readInt32(bFile);
	 while ( (offset != -1) && (frameSize != -1) )
	 {
		 // System.out.print(offset + "\t" + frameSize);
		 // System.out.println();
		 byteChunk = utils.stitchIO.sureRead(bFile, frameSize);
		 
		 vidRaf.seek(offset);
		 if ((method.equals("missB")) || (method.equals("missAll")))
		 {
			 if ( (offset < startMissing) || (offset > endMissing) )
			 {
				 vidRaf.write(byteChunk, 0, frameSize);
			 }
		 }
		 else
		 {
			 vidRaf.write(byteChunk, 0, frameSize);
		 }
		 
		 offset = utils.stitchIO.readInt64(bFile);
		 frameSize = utils.stitchIO.readInt32(bFile);
	 }
	 
	 vidRaf.close();
	 iFile.close();
	 pFile.close();
	 bFile.close();
 }

}
