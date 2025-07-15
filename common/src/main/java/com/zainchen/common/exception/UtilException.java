package com.zainchen.common.exception;

import java.io.Serial;

public class UtilException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UtilException(String message) {
        super(message);
    }
}
