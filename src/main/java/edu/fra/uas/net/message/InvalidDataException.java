package edu.fra.uas.net.message;

/**
 * This exception is thrown when we encounter bad Data.
 */
public class InvalidDataException extends Exception {

	private static final long serialVersionUID = 372232895075030921L;

	/**
     * Constructs an <code>InvalidDataException</code> with
     * <code>null</code> for its error detail message.
     */
    public InvalidDataException() {
        super();
    }

    /**
     * Constructs an <code>InvalidDataException</code> with the
     * specified detail message.
     *
     * @param message the string to display as an error detail message
     */
    public InvalidDataException(String message) {
        super(message);
    }
    
}
