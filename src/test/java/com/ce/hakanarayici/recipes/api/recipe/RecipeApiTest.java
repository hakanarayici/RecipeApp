package com.ce.hakanarayici.recipes.api.recipe;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyString;

import com.ce.hakanarayici.recipes.service.RecipeService;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;
import com.google.gson.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RecipeApiTest {

    private MockMvc mockMvc;

    @Mock
    private RecipeService recipeService;

    @Before
    public void setUp(){
        RecipeApi recipeApi = new RecipeApi(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeApi).build();
    }

    @Test
    public void shouldReturnRecipe() throws Exception {

        RecipeDTO recipe = RecipeDTO.builder()
                .recipeName("salata")
                .build();

        when(recipeService.getRecipeByName(anyString()))
                .thenReturn(recipe);

        mockMvc.perform(get("/api/recipe/get?recipeName={name}", "salata" ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("salata")));

        verify(recipeService,times(1)).getRecipeByName(anyString());
    }

    @Test
    public void shouldCreateRecipe() throws Exception {

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        }}).create();

        Recipe recipe = new Recipe();
        recipe.setRecipeID(1L);
        recipe.setRecipeName("Kofte");
        recipe.setInstructions("bla bla bla");

        when(recipeService.createRecipe(any(RecipeDTO.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/api/recipe/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(recipeService,times(1)).createRecipe(any(RecipeDTO.class));


    }


    @Test
    public void shouldUpdateRecipe() throws Exception {

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            }}).create();

        Recipe recipe = new Recipe();
        recipe.setRecipeID(1L);
        recipe.setRecipeName("Kofte");
        recipe.setInstructions("bla bla bla");

        when(recipeService.updateRecipe(any(RecipeDTO.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(put("/api/recipe/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(recipeService,times(1)).updateRecipe(any(RecipeDTO.class));


    }

    @Test
    public void shouldDeleteRecipe() throws Exception {

        when(recipeService.deleteRecipe(anyLong()))
                .thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/recipe/delete?recipeID={recipeID}", "1" ))
                .andDo(print())
                .andExpect(status().isOk());

        verify(recipeService,times(1)).deleteRecipe(anyLong());
    }




}