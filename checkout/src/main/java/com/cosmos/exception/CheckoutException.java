package com.cosmos.exception;

/**
 * The type Checkout exception.
 *
 * @author ambujmehra
 */
public class CheckoutException extends RuntimeException{

    /**
     * Instantiates a new Partner source exception.
     */
    public CheckoutException() {
        super();
    }

    /**
     * Instantiates a new Partner source exception.
     *
     * @param exception the exception
     */
    public CheckoutException(String exception) {
        super(exception);
    }

    /**
     * Instantiates a new Partner source exception.
     *
     * @param errorMsg  the error msg
     * @param exception the exception
     */
    public CheckoutException(String errorMsg, Exception exception) {
        super(errorMsg, exception);
    }

}
