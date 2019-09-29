package com.kzh.sys.core.exception;

public class WorldDaoException extends RuntimeException{
    public WorldDaoException(){
        super();
    }

    public WorldDaoException(String message){
        super(message);
    }

    public WorldDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
