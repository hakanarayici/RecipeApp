package com.ce.hakanarayici.recipes.api.recipe;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/api/recipe")
public interface IRecipeApi {

    @Operation(summary = "gets recipe by given name", description = "gets recipe")
    @ApiResponse(description = "gets recipe by given name")
    @GetMapping("/get")
    ResponseEntity<Recipe> getRecipe(@RequestParam String recipeName);

    @Operation(summary = "creates recipe", description = "creates recipe")
    @ApiResponse(description = "creates recipe")
    @PostMapping("/create")
    ResponseEntity<RecipeApiResponse> createRecipe(@RequestBody Recipe recipe);

    @Operation(summary = "updates recipe", description = "updates recipe")
    @ApiResponse(description = "updates recipe")
    @PutMapping("/update")
    ResponseEntity<RecipeApiResponse> updateRecipe(@RequestBody Recipe recipe);

    @Operation(summary = "deletes recipe", description = "deletes recipe")
    @ApiResponse(description = "deletes recipe")
    @DeleteMapping("/delete")
    ResponseEntity<RecipeApiResponse> deleteRecipe(@RequestParam Long recipeID);

    @Operation(summary = "gets all recipes", description = "gets all recipes")
    @ApiResponse(description = "gets all recipes")
    @GetMapping("/getAll")
    ResponseEntity<List<Recipe>> getAll();

}
