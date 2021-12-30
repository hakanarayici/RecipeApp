package com.ce.hakanarayici.recipes.exception;

public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(Long id) {
        super(String.format("Recipe with Id %d not found", id));
    }

    public RecipeNotFoundException(String name) {
        super(String.format("Recipe with Id %s not found", name));
    }
}
