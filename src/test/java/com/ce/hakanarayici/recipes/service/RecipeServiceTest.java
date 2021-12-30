package com.ce.hakanarayici.recipes.service;

import com.ce.hakanarayici.recipes.data.dao.RecipeDAO;
import com.ce.hakanarayici.recipes.data.model.IngredientEntity;
import com.ce.hakanarayici.recipes.data.model.RecipeEntity;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    RecipeDAO recipeDAO;

    private IRecipeService recipeService;

    @Before
    public void setUp(){
        recipeDAO = mock(RecipeDAO.class);
        recipeService = new RecipeService(recipeDAO);
    }


    @Test
    public void getRecipeByName() {

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
    public void createRecipe() {

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
    public void updateRecipe() {

        when(recipeDAO.save(any(RecipeEntity.class))).thenReturn(null);
        when(recipeDAO.findById(anyLong())).thenReturn(Optional.of(RecipeEntity.builder().build()));

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
    public void deleteRecipe() {
        when(recipeDAO.findById(anyLong())).thenReturn(Optional.of(RecipeEntity.builder().build()));
        doNothing().when(recipeDAO).delete(any(RecipeEntity.class));

        Boolean success = recipeService.deleteRecipe(1L);

        assertTrue(success);
        verify(recipeDAO,times(1)).delete(any(RecipeEntity.class));
        verify(recipeDAO,times(1)).findById(anyLong());
    }
}