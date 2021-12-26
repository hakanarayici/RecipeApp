package com.ce.hakanarayici.recipes.api.recipe;

import com.ce.hakanarayici.recipes.service.IRecipeService;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RecipeApi implements IRecipeApi {

    private final IRecipeService recipeService;

    @Override
    public Recipe getReceipe(@RequestParam String recipeName){

        RecipeDTO recipeDTO = recipeService.getRecipeByName(recipeName);

        Recipe recipe = new Recipe();
        recipe.setRecipeID(recipeDTO.getRecipeID());
        recipe.setRecipeName(recipeDTO.getRecipeName());
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setCreateDate(recipeDTO.getCreateDate());
        recipe.setIngredientList(recipeDTO.getIngredientList());
        recipe.setVegetarian(recipeDTO.getVegetarian());
        recipe.setSuitablePeopleCount(recipeDTO.getSuitablePeopleCount());

        return recipe;
    }


    @Override
    public ResponseEntity<String> createReceipe(@RequestBody Recipe receipe){

        RecipeDTO receipeDTO = RecipeDTO.builder()
                .recipeName(receipe.getRecipeName())
                .suitablePeopleCount(receipe.getSuitablePeopleCount())
                .vegetarian(receipe.getVegetarian())
                .instructions(receipe.getInstructions())
                .ingredientList(receipe.getIngredientList())
                .createDate(receipe.getCreateDate())
                .build();

        recipeService.createRecipe(receipeDTO);
        return new ResponseEntity<>(receipeDTO.getRecipeName() + " is created",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateReceipe(@RequestBody Recipe recipe){

        recipeService.updateRecipe(RecipeDTO.builder()
                .createDate(recipe.getCreateDate())
                .ingredientList(recipe.getIngredientList())
                .instructions(recipe.getInstructions())
                .recipeID(recipe.getRecipeID())
                .recipeName(recipe.getRecipeName())
                .vegetarian(recipe.getVegetarian())
                .suitablePeopleCount(recipe.getSuitablePeopleCount())
                .build());

        return new ResponseEntity<>(recipe.getRecipeName() + " is updated",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteReceipe(@RequestParam Long recipeID){
        recipeService.deleteRecipe(recipeID);
        return new ResponseEntity<>(recipeID + " is deleted",HttpStatus.OK);
    }
}
