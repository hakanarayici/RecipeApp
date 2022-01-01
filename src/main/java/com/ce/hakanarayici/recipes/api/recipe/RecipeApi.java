package com.ce.hakanarayici.recipes.api.recipe;

import com.ce.hakanarayici.recipes.service.recipe.IRecipeService;
import com.ce.hakanarayici.recipes.service.recipe.dto.RecipeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class RecipeApi implements IRecipeApi {

    private final IRecipeService recipeService;

    @Override
    public ResponseEntity<Recipe> getReceipe(@RequestParam(required = true) String recipeName){

        RecipeDTO recipeDTO = recipeService.getRecipeByName(recipeName);

        Recipe recipe = null;
        if(recipeDTO != null){
            recipe = new Recipe();
            recipe.setRecipeID(recipeDTO.getRecipeID());
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setInstructions(recipeDTO.getInstructions());
            recipe.setCreateDate(recipeDTO.getCreateDate());
            recipe.setIngredientList(recipeDTO.getIngredientList());
            recipe.setVegetarian(recipeDTO.getVegetarian());
            recipe.setSuitablePeopleCount(recipeDTO.getSuitablePeopleCount());
        }

        return new ResponseEntity<>(recipe,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<RecipeApiResponse> createReceipe(@Valid @RequestBody Recipe receipe){

        RecipeDTO receipeDTO = RecipeDTO.builder()
                .recipeName(receipe.getRecipeName())
                .suitablePeopleCount(receipe.getSuitablePeopleCount())
                .vegetarian(receipe.getVegetarian())
                .instructions(receipe.getInstructions())
                .ingredientList(receipe.getIngredientList())
                .createDate(Optional.ofNullable(receipe.getCreateDate()).orElse(LocalDateTime.now()))
                .build();

        recipeService.createRecipe(receipeDTO);
        return new ResponseEntity<>(new RecipeApiResponse(true,""),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecipeApiResponse> updateReceipe(@Valid @RequestBody Recipe recipe){

        recipeService.updateRecipe(RecipeDTO.builder()
                .createDate(recipe.getCreateDate())
                .ingredientList(recipe.getIngredientList())
                .instructions(recipe.getInstructions())
                .recipeID(Optional.ofNullable(recipe.getRecipeID()).orElseThrow(() -> new IllegalArgumentException("Recipe id is null")))
                .recipeName(recipe.getRecipeName())
                .vegetarian(recipe.getVegetarian())
                .suitablePeopleCount(recipe.getSuitablePeopleCount())
                .build());

        return new ResponseEntity<>(new RecipeApiResponse(true,""),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecipeApiResponse> deleteReceipe(@RequestParam(required = true) Long recipeID){
        recipeService.deleteRecipe(recipeID);
        return new ResponseEntity<>(new RecipeApiResponse(true,""),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Recipe>> getAll() {

        List<Recipe> recipeList = recipeService.getAllRecipes().stream()
                .map(recipeDTO -> Recipe.builder()
                        .recipeID(recipeDTO.getRecipeID())
                        .recipeName(recipeDTO.getRecipeName())
                        .instructions(recipeDTO.getInstructions())
                        .createDate(recipeDTO.getCreateDate())
                        .ingredientList(recipeDTO.getIngredientList())
                        .vegetarian(recipeDTO.getVegetarian())
                        .suitablePeopleCount(recipeDTO.getSuitablePeopleCount())
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(recipeList,HttpStatus.OK);
    }

}
