package com.ce.hakanarayici.recipes.api.advice;

import com.ce.hakanarayici.recipes.api.recipe.RecipeApiResponse;
import com.ce.hakanarayici.recipes.exception.RecipeAlreadyExistException;
import com.ce.hakanarayici.recipes.exception.RecipeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RecipeApiAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Object> handleRecipeNotFoundException(
            RecipeNotFoundException ex, WebRequest request) {

        return new ResponseEntity<>(new RecipeApiResponse(false,ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(RecipeAlreadyExistException.class)
    public ResponseEntity<Object> handleRecipeAlreadyExistException(
            RecipeAlreadyExistException ex, WebRequest request) {
        return new ResponseEntity<>(new RecipeApiResponse(false,ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

}
