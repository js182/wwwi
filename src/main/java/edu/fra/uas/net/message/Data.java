package edu.fra.uas.net.message;

/**
 * <code>Data</code> is the base class for data.
 * <p>
 * The base <code>Data</code> class provides access to two types of
 * information about a data:
 * <ul>
 * <li>The total length of the data in bytes
 * <li>A byte array containing the complete data
 * </ul>
 *
 * <code>Data</code> includes methods to get, but not set, these values.
 * Setting them is a subclass responsibility.
 */
public class Data {

    /**
     * The data. The bytes up to the length
     * of the data are data bytes for this data.
     * @see #getLength
     */
    protected byte[] dataBytes;


    /**
     * The number of bytes in the data, including
     * any data bytes.
     * @see #getLength
     */
    protected int length = 0;


    /**
     * Constructs a new <code>Data</code>. This protected
     * constructor is called by concrete subclasses, which should
     * ensure that the data array specifies a data.
     *
     * @param data an array of bytes containing the complete data.
     * The data may be changed using the <code>setData</code>
     * method.
     *
     * @see #setData
     */
    protected Data(byte[] data) {
        this.dataBytes = data;
        if (data != null) {
            this.length = data.length;
        }
    }


    /**
     * Sets the data. This protected
     * method is called by concrete subclasses, which should
     * ensure that the data array specifies a data.
     *
     * @param data the data bytes
     * @param length the number of bytes in the data byte array
     * @throws InvalidDataException if the parameter values do not specify a valid data
     */
    protected void setData(byte[] data, int length) throws InvalidDataException {
        if (length < 0 || (length > 0 && length > data.length)) {
            throw new IndexOutOfBoundsException("length out of bounds: "+length);
        }
        this.length = length;

        if (this.dataBytes == null || this.dataBytes.length < this.length) {
            this.dataBytes = new byte[this.length];
        }
        System.arraycopy(data, 0, this.dataBytes, 0, length);
    }


    /**
     * Obtains the data. The bytes up to the length of the data are data bytes.
     * The byte array may have a length which is greater than that of the actual data; 
     * the total length of the data in bytes is reported by the <code>{@link #getLength}</code>
     * method.
     *
     * @return the byte array containing the complete <code>Data</code>
     */
    public byte[] getData() {
        byte[] returnedArray = new byte[length];
        System.arraycopy(dataBytes, 0, returnedArray, 0, length);
        return returnedArray;
    }


    /**
     * Obtains the total length of the data in bytes.
     * Data consists of data bytes. 
     *
     * @return the length of the data in bytes
     */
    public int getLength() {
        return length;
    }


    /**
     * Creates a new object of the same class and with the same contents
     * as this object.
     * @return a clone of this instance.
     */
    public Object copy() {
		byte[] tmpData = new byte[length];
		System.arraycopy(dataBytes, 0, tmpData, 0, length);
		return new Data(tmpData);
    }
	
}
