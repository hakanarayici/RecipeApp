package com.ce.hakanarayici.recipes.api.recipe;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/recipe")
public interface IRecipeApi {

    @Operation(summary = "gets recipe by given name", description = "gets recipe")
    @ApiResponse(description = "gets recipe by given name")
    @GetMapping("/get")
    Recipe getReceipe(@RequestParam String recipeName);

    @Operation(summary = "creates recipe", description = "creates recipe")
    @ApiResponse(description = "creates recipe")
    @PostMapping("/create")
    ResponseEntity<String> createReceipe(@RequestBody Recipe receipe);

    @Operation(summary = "updates recipe", description = "updates recipe")
    @ApiResponse(description = "updates recipe")
    @PutMapping("/update")
    ResponseEntity<String> updateReceipe(@RequestBody Recipe recipe);

    @Operation(summary = "deletes recipe", description = "deletes recipe")
    @ApiResponse(description = "deletes recipe")
    @DeleteMapping("/delete")
    ResponseEntity<String> deleteReceipe(@RequestParam Long recipeID);

}
