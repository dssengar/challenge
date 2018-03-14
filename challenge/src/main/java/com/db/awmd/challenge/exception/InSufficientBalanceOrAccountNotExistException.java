package com.db.awmd.challenge.exception;

/**
 * Created by Devendra Singh Sengar on 14-03-2018.
 */
public class InSufficientBalanceOrAccountNotExistException extends RuntimeException {
    public InSufficientBalanceOrAccountNotExistException(String message) {
        super(message);
    }
}
