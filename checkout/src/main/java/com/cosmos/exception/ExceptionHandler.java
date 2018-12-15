package com.cosmos.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * The type Exception handler.
 *
 * @author ambujmehra
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    /**
     * Handle scheduler exception response entity.
     *
     * @param coreException the core exception
     * @return the response entity
     */
    @org.springframework.web.bind.annotation.ExceptionHandler({CheckoutException.class})
    public ResponseEntity<CheckoutException> handleCheckoutException(
            CheckoutException coreException) {
        log.error("Error in the service : {}", coreException);
        return new ResponseEntity<>(coreException, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ResponseEntity<CheckoutException> handleException(
            CheckoutException coreException) {
        log.error("Error in the service : {}", coreException);
        return new ResponseEntity<>(coreException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
