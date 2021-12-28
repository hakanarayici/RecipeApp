package com.ce.hakanarayici.recipes.api.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class RecipeApiResponse implements Serializable {
    private Boolean success;
    private String errorMessage;
}
