package com.kzh.sys.core.exception;

public class WorldValidateException extends RuntimeException {
    private static final long serialVersionUID = -939208231165751812L;

    private String errorCode;

    public WorldValidateException() {
        super();
    }

    public WorldValidateException(String message) {
        super(message);
    }

    public WorldValidateException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
