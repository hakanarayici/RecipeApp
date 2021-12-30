package com.ce.hakanarayici.recipes.exception;

public class RecipeAlreadyExistException  extends RuntimeException{

    public RecipeAlreadyExistException(String recipeName){
        super(String.format("Recipe %s is already exist",recipeName));
    }

}
