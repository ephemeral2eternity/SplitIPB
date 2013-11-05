package utils;

public class splitIO {
	
	/**
	 * Change int to 4 bytes
	 * @param value : input 32bit integer
	 * @return
	 */
	public static byte[] Int32toBytes(int value)
	{
		byte[] result = new byte[4];
		
		result[0] = (byte) (value >> 24);
		result[1] = (byte) (value >> 16);
		result[2] = (byte) (value >> 8);
		result[3] = (byte) (value >> 0);
		return result;
	}
	
	/**
	 * Change a long int to n bytes.
	 * @param value : input long value
	 * @param n : the number of bytes the long value change to
	 * @return
	 */
	public static byte[] Long2Bytes(long value, int n)
	{
		byte[] result = new byte[n];
		
	    for (int k = 1; k <= n; k ++)
	    {
	    	result[n - k] = (byte) (value >> ((k-1)*8 & 0xFF));
	    }
		
		return result;
	}

}
