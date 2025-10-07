package com.corporatebanking.faq.features.faq.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String message){
        super(message);
    }
}
