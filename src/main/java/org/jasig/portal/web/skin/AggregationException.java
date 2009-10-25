/**
 * 
 */
package org.jasig.portal.web.skin;

/**
 * Base Exception type for Aggregation problems.
 * 
 * @author Nicholas Blair, npblair@wisc.edu
 */
public class AggregationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AggregationException() {
	}

	/**
	 * @param message
	 */
	public AggregationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AggregationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AggregationException(String message, Throwable cause) {
		super(message, cause);
	}

}
