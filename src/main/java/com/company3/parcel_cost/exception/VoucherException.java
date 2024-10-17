package com.company3.parcel_cost.exception;

public class VoucherException extends RuntimeException {

    public VoucherException(String message) {
        super(message);
    }

    public VoucherException(String message, Throwable cause) {
        super(message, cause);
    }
}
