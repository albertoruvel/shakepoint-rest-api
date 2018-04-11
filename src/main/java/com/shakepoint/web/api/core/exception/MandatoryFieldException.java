package com.shakepoint.web.api.core.exception;

public class MandatoryFieldException extends Exception {
    public MandatoryFieldException (String propertyName){
        super(propertyName + " is mandatory");
    }
}
