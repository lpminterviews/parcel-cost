package com.company3.parcel_cost.exception;

public class ParcelDimensionException extends RuntimeException {

    public ParcelDimensionException(String message) {
        super(message);
    }

    public ParcelDimensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
