package com.kzh.sys.core.exception;

public class WorldException extends RuntimeException{
    public WorldException(){
        super();
    }

    public WorldException(String message){
        super(message);
    }

    public WorldException(String message, Throwable cause) {
        super(message, cause);
    }
}
