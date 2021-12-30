package com.ce.hakanarayici.recipes.api.recipe;

import com.ce.hakanarayici.recipes.config.AsciiDocConfiguration;
import com.ce.hakanarayici.recipes.config.JWTTokenUtil;
import com.ce.hakanarayici.recipes.config.JwtAuthenticationEntryPoint;
import com.ce.hakanarayici.recipes.config.JwtUserDetailService;
import com.ce.hakanarayici.recipes.service.RecipeService;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;
import com.google.gson.*;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RecipeApi.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(outputDir = "target/generated-sources/snippets")
@Import(AsciiDocConfiguration.class)
public class RecipeApiTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private JwtUserDetailService jwtUserDetailService;

    @MockBean
    private JWTTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @BeforeEach
    public void setUp(){
        when(jwtTokenUtil.validateToken(nullable(String.class), nullable(UserDetails.class))).thenReturn(true);
    }



    @Test
    public void shouldReturnRecipe() throws Exception {


        RecipeDTO recipe = RecipeDTO.builder()
                .recipeName("salata")
                .recipeID(1L)
                .instructions("bla bla bla")
                .ingredientList(Arrays.asList("domates" ,"sogan", "zeytinyagi"))
                .suitablePeopleCount(5)
                .vegetarian(Boolean.TRUE)
                .createDate(LocalDateTime.now())
                .build();

        when(recipeService.getRecipeByName(anyString()))
                .thenReturn(recipe);

        mockMvc.perform(get("/api/recipe/get?recipeName={name}", "salata" ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("salata")))
                .andDo(document("{method-name}"));

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