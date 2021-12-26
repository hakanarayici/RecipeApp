package com.ce.hakanarayici.recipes.service;

import com.ce.hakanarayici.recipes.api.recipe.Recipe;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;

public interface IRecipeService {
    RecipeDTO getRecipeByName(String recipeName);

    Boolean createRecipe(RecipeDTO receipeDTO);

    Boolean updateRecipe(RecipeDTO receiptDTO);

    Boolean deleteRecipe(Long recipeID);
}
