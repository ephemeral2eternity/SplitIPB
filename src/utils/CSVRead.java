package utils;
// CSVRead.java
//Reads a Comma Separated Value file and prints its contents.

import java.io.*;
import java.util.ArrayList;

/**
 * 
 * @author chenw
 *
 */
public class CSVRead{
	private BufferedReader CSVFile;
	private String CSVFileName;
	private ArrayList<String[]> dataArray;
	private ArrayList<Integer> datArry;
	private ArrayList<Integer[]> data2D;
	
	
	/**
	 * @param fileName: the csv file name
	 * @throws FileNotFoundException
	 */
	public CSVRead(String fileName) throws FileNotFoundException
	{
		CSVFileName = fileName;
		CSVFile = new BufferedReader(new FileReader(CSVFileName));
		dataArray = new ArrayList<String[]>();
		datArry = new ArrayList<Integer>();
		data2D = new ArrayList<Integer[]>();
	}
	
	/**
	 * @return dataArray that stores the data in csv file in String format
	 * @throws IOException
	 */
	public ArrayList<String[]> doCSVRead() throws IOException
	{
		  String dataRow = CSVFile.readLine(); // Read first line.
		  // The while checks to see if the data is null. If 
		  // it is, we've hit the end of the file. If not, 
		  // process the data.

		  while (dataRow != null){
		   String[] rowArray = dataRow.split(",");
/*		   for (String item:rowArray) { 
		      System.out.print(item + "\t");
		      
		   }*/
		   dataArray.add(rowArray);
//		   System.out.println(); // Print the data line.
		   dataRow = CSVFile.readLine(); // Read next line of data.
		  }


		  // End the printout with a blank line.
//		  System.out.println();

		 return dataArray;
	}
	
	/**
	 * Load the 1 dimension array of Integer into the ArrayList 
	 * @return ArrayList<Integer> 
	 * @throws IOException
	 */
	public ArrayList<Integer> loadDat() throws IOException
	{
		  String dataRow = CSVFile.readLine(); // Read first line.
		  // The while checks to see if the data is null. If 
		  // it is, we've hit the end of the file. If not, 
		  // process the data.

		  while (dataRow != null){
			  int rowDat = Integer.parseInt(dataRow);
/*		   for (String item:rowArray) { 
		      System.out.print(item + "\t");
		      
		   }*/
		   datArry.add(rowDat);
//		   System.out.println(); // Print the data line.
		   dataRow = CSVFile.readLine(); // Read next line of data.
		  }


		  // End the printout with a blank line.
//		  System.out.println();

		 return datArry;
	}
	
	/**
	 * Load the 2 dimension array of Integer into the ArrayList 
	 * @return ArrayList<Integer[]> 
	 * @throws IOException
	 */
	public ArrayList<Integer[]> load2DArray() throws IOException
	{
		  String dataRow = CSVFile.readLine(); // Read first line.
		  // The while checks to see if the data is null. If 
		  // it is, we've hit the end of the file. If not, 
		  // process the data.

		  while (dataRow != null)
		  {
			  String[] rowArray = dataRow.split(",");
			  Integer[] datRow = new Integer[rowArray.length];

			   for (int i = 0; i < rowArray.length; i ++) 
			   {
			      datRow[i] = Integer.parseInt(rowArray[i].replaceAll("\\s", ""));
			   }
			   data2D.add(datRow);
			   dataRow = CSVFile.readLine(); // Read next line of data.
		  }


		  // End the printout with a blank line.
//		  System.out.println();

		 return data2D;
	}
	/**
	 * Close the BufferReader
	 * @throws IOException
	 */
	public void CSVReadClose() throws IOException
	{
		  // Close the file once all data has been read.
		  CSVFile.close();
	}
}