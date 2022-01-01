package com.ce.hakanarayici.recipes.service;

import com.ce.hakanarayici.recipes.data.dao.RecipeDAO;
import com.ce.hakanarayici.recipes.data.model.IngredientEntity;
import com.ce.hakanarayici.recipes.data.model.RecipeEntity;
import com.ce.hakanarayici.recipes.service.recipe.RecipeService;
import com.ce.hakanarayici.recipes.service.recipe.dto.RecipeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class RecipeServiceTest {

    @MockBean
    RecipeDAO recipeDAO;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    public void setUp(){
        recipeDAO = mock(RecipeDAO.class);
        recipeService = new RecipeService(recipeDAO);
    }

    @Test
    public void testGetRecipeByName() {

        when(recipeDAO.findByRecipeName(anyString())).thenReturn( Optional.of(RecipeEntity.builder()
                .recipeName("some food")
                .ingredientList(List.of(IngredientEntity.builder().ingredientName("some ingredient").build()))
                .build()));

        RecipeDTO recipeDTO = recipeService.getRecipeByName("some food");

        assertNotNull(recipeDTO);
        assertEquals("some food",recipeDTO.getRecipeName());
        verify(recipeDAO,times(1)).findByRecipeName(anyString());

    }

    @Test
    public void testCreateRecipe() {

        when(recipeDAO.save(any(RecipeEntity.class))).thenReturn(null);

        Boolean success = recipeService.createRecipe(RecipeDTO.builder()
                .recipeName("some food")
                .ingredientList(List.of("some ingredients"))
                .instructions("blah blah blah")
                .build());

        assertTrue(success);
        verify(recipeDAO,times(1)).save(any(RecipeEntity.class));

    }

    @Test
    public void testUpdateRecipe() {

        when(recipeDAO.save(any(RecipeEntity.class))).thenReturn(null);
        when(recipeDAO.findById(anyLong())).thenReturn(Optional.of(RecipeEntity.builder()
                .ingredientList(new ArrayList<>())
                .build()));

        Boolean success = recipeService.updateRecipe(RecipeDTO.builder()
                .recipeID(1L)
                .recipeName("some food")
                .ingredientList(List.of("some ingredients"))
                .instructions("blah blah blah")
                .build());

        assertTrue(success);
        verify(recipeDAO,times(1)).save(any(RecipeEntity.class));
        verify(recipeDAO,times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteRecipe() {
        when(recipeDAO.findById(anyLong())).thenReturn(Optional.of(RecipeEntity.builder().build()));
        doNothing().when(recipeDAO).delete(any(RecipeEntity.class));

        Boolean success = recipeService.deleteRecipe(1L);

        assertTrue(success);
        verify(recipeDAO,times(1)).delete(any(RecipeEntity.class));
        verify(recipeDAO,times(1)).findById(anyLong());
    }


    @Test
    public void testGetAllRecipes() {
        when(recipeDAO.findAll())
                .thenReturn(List.of(
                        RecipeEntity.builder().recipeName("Kofte")
                                .ingredientList(List.of(IngredientEntity.builder().ingredientName("some ingredient").build()))
                                .build(),
                        RecipeEntity.builder().recipeName("Patates")
                                .ingredientList(List.of(IngredientEntity.builder().ingredientName("some more ingredient").build()))
                                .build()
                            )
                );

        List<RecipeDTO> allRecipes = recipeService.getAllRecipes();

        assertNotNull(allRecipes);
        assertEquals(2,allRecipes.size());
        verify(recipeDAO,times(1)).findAll();
    }
}