package com.example.batch.mapper;

public class UnknownCellTypeException extends RuntimeException {

    /**
     * Constructs an <code>UnknownCellTypeException</code> with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public UnknownCellTypeException(String message) {
        super(message);
    }

}