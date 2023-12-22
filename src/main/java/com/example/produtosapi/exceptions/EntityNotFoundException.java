package com.example.produtosapi.exceptions;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(String entity){
        super(entity + " not found.");
    }
}
