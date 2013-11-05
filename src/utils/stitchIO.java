package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class stitchIO {
	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
    public static int readInt32(InputStream input) {
       
        try{
        	 int b1 = input.read();
             int b2 = input.read();
             int b3 = input.read();
             int b4 = input.read();
             return (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
        }
        catch (Exception e){
        	return -1;
        }

       // if (b1 == -1 || b2 == -1 || b3 == -1 || b4 == -1)
       //     return -1;
    }

    /**
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public static long readInt64(InputStream input){
    	try{
    		long b1 = input.read();
    		long b2 = input.read();
    		long b3 = input.read();
    		long b4 = input.read();
    		long b5 = input.read();
    		long b6 = input.read();
    		long b7 = input.read();
    		long b8 = input.read();
    		
    		return (b1 << 56) + (b2 << 48) + (b3 << 40) + (b4 << 32) + (b5 << 24) + (b6 << 16) + (b7 << 8) + b8;
    	}
    	catch(Exception e)
    	{
    		return -1;
    	}       
    }
    
    /**
     * 
     * @param input: The input file stream
     * @param len: The number of bytes to be read
     * @return The read bytes
     * @throws IOException
     */
    public static byte[] sureRead(InputStream input, int len) {
    	try{
    		byte[] res = new byte[len];
    		int read = 0;
    		while (read < len) {
    			int tmp = input.read(res, read, len - read);	
    			read += tmp;
    		}
    		return res;
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }
    
    public static byte[] sureRead(RandomAccessFile input, int len) {
    	try{
    		byte[] res = new byte[len];
    		int read = 0;
    		while (read < len) {
    			int tmp = input.read(res, read, len - read);	
    			read += tmp;
    		}
    		return res;
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }
    
    /**
     * 
     * @param input: The input file stream
     * @param len: The number of bytes to be read
     * @return The read bytes
     * @throws IOException
     */
    /*public static byte[] sureRead(InputStream input, long off, int len) {
    	try{
    		byte[] res = new byte[len];
    		int read = (int) off;
    		int readEnd = (int) (off + len);
    		while (read < readEnd) {
    			int tmp = input.read(res, read, readEnd - read);	
    			read += tmp;
    		}
    		return res;
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }*/

}
