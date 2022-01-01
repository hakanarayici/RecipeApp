package com.ce.hakanarayici.recipes.service.recipe;

import com.ce.hakanarayici.recipes.service.recipe.dto.RecipeDTO;

import java.util.List;

public interface IRecipeService {
    RecipeDTO getRecipeByName(String recipeName);

    Boolean createRecipe(RecipeDTO receipeDTO);

    Boolean updateRecipe(RecipeDTO receiptDTO);

    Boolean deleteRecipe(Long recipeID);

    List<RecipeDTO> getAllRecipes();
}
