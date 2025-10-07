package com.corporatebanking.faq.features.faq.exceptions;

public class FaqNotFoundAfterInsertException extends RuntimeException{

    public FaqNotFoundAfterInsertException(String message){
        super(message);
    }
}
