package com.ce.hakanarayici.recipes.api.recipe;

import com.ce.hakanarayici.recipes.config.AsciiDocConfiguration;
import com.ce.hakanarayici.recipes.config.JwtAuthenticationEntryPoint;
import com.ce.hakanarayici.recipes.exception.RecipeAlreadyExistException;
import com.ce.hakanarayici.recipes.exception.RecipeNotFoundException;
import com.ce.hakanarayici.recipes.service.recipe.RecipeService;
import com.ce.hakanarayici.recipes.service.recipe.dto.RecipeDTO;
import com.ce.hakanarayici.recipes.service.security.JwtUserDetailService;
import com.ce.hakanarayici.recipes.util.JWTTokenUtil;
import com.ce.hakanarayici.recipes.util.LocalDateJSONAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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

    }

    @Test
    public void given_recipe_name_shouldReturnRecipe_when_recipe_exists() throws Exception {

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
                .andDo(document("{method-name}",
                        requestParameters(parameterWithName("recipeName").description("recipe name")),
                        responseFields(
                                fieldWithPath("recipeID").description("Recipe id"),
                                fieldWithPath("recipeName").description("Recipe name"),
                                fieldWithPath("createDate").description("Create date of recipe"),
                                fieldWithPath("vegetarian").description("is vegetarian flag"),
                                fieldWithPath("suitablePeopleCount").description("people count that recipe suits"),
                                fieldWithPath("ingredientList").description("Recipe ingredients"),
                                fieldWithPath("instructions").description("Recipe instructions")
                        )
                ));

        verify(recipeService,times(1)).getRecipeByName(anyString());
    }


    @Test
    public void given_recipe_name_should_return_not_found_when_recipe__doesnt_exists() throws Exception {

        when(recipeService.getRecipeByName(anyString()))
                .thenThrow(RecipeNotFoundException.class);

        mockMvc.perform(get("/api/recipe/get?recipeName={name}", "salata" ))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("{method-name}"));

        verify(recipeService,times(1)).getRecipeByName(anyString());
    }


    @Test
    public void should_return_all_recipes_if_exists() throws Exception {

        when(recipeService.getAllRecipes())
                .thenReturn(Arrays.asList(
                       RecipeDTO.builder()
                                .recipeName("salata")
                                .recipeID(1L)
                                .instructions("bla bla bla")
                                .ingredientList(Arrays.asList("domates" ,"sogan", "zeytinyagi"))
                                .suitablePeopleCount(5)
                                .vegetarian(Boolean.TRUE)
                                .createDate(LocalDateTime.now())
                                .build(),
                        RecipeDTO.builder()
                                .recipeName("kofte")
                                .recipeID(1L)
                                .instructions("bla bla bla")
                                .ingredientList(Arrays.asList("kiyma" ,"sogan", "ekmek ici"))
                                .suitablePeopleCount(5)
                                .vegetarian(Boolean.TRUE)
                                .createDate(LocalDateTime.now())
                                .build()

                ));

        mockMvc.perform(get("/api/recipe/getAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("salata")))
                .andExpect(content().string(containsString("kofte")))
                .andDo(document("{method-name}",
                        requestParameters(),
                        responseFields(
                                fieldWithPath("[].recipeID").description("Recipe id"),
                                fieldWithPath("[].recipeName").description("Recipe name"),
                                fieldWithPath("[].createDate").description("Create date of recipe"),
                                fieldWithPath("[].vegetarian").description("is vegetarian flag"),
                                fieldWithPath("[].suitablePeopleCount").description("people count that recipe suits"),
                                fieldWithPath("[].ingredientList").description("Recipe ingredients"),
                                fieldWithPath("[].instructions").description("Recipe instructions")

                        )


                ));


        verify(recipeService,times(1)).getAllRecipes();
    }


    @Test
    public void given_recipe_should_Create_Recipe_when_recipe_is_not_already_exists() throws Exception {

        Recipe recipe = Recipe.builder()
                .recipeName("Kofte")
                .instructions("bla bla bla")
                .ingredientList(Arrays.asList("Kiyma","sogan","ekmek ici"))
                .createDate(LocalDateTime.now())
                .suitablePeopleCount(50)
                .vegetarian(Boolean.FALSE)
                .build();

        when(recipeService.createRecipe(any(RecipeDTO.class))).thenReturn(Boolean.TRUE);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,new LocalDateJSONAdapter())
                .create();

        mockMvc.perform(post("/api/recipe/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        requestFields(
                                fieldWithPath("recipeName").description("Recipe name"),
                                fieldWithPath("createDate").description("Create date of recipe"),
                                fieldWithPath("vegetarian").description("is vegetarian flag"),
                                fieldWithPath("suitablePeopleCount").description("people count that recipe suits"),
                                fieldWithPath("ingredientList").description("Recipe ingredients"),
                                fieldWithPath("instructions").description("Recipe instructions")

                        ),
                        responseFields(
                                fieldWithPath("success").description("is successfull"),
                                fieldWithPath("errorMessage").description("error message")

                                )
                ))
                .andReturn();

        verify(recipeService,times(1)).createRecipe(any(RecipeDTO.class));

    }

    @Test
    public void given_recipe_should_not_Create_Recipe_when_recipe_is_already_exists() throws Exception {

        Recipe recipe = Recipe.builder()
                .recipeName("Kofte")
                .instructions("bla bla bla")
                .ingredientList(Arrays.asList("Kiyma","sogan","ekmek ici"))
                .createDate(LocalDateTime.now())
                .suitablePeopleCount(50)
                .vegetarian(Boolean.FALSE)
                .build();

        when(recipeService.createRecipe(any(RecipeDTO.class))).thenThrow(RecipeAlreadyExistException.class);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,new LocalDateJSONAdapter())
                .create();

        mockMvc.perform(post("/api/recipe/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andDo(document("{method-name}"))
                .andReturn();

        verify(recipeService,times(1)).createRecipe(any(RecipeDTO.class));

    }

    @Test
    public void given_recipe_should_Update_Recipe_if_exists() throws Exception {

        Recipe recipe = Recipe.builder()
                .recipeID(1L)
                .recipeName("Kofte")
                .instructions("bla bla bla")
                .ingredientList(Arrays.asList("Kiyma","sogan","ekmek ici"))
                .suitablePeopleCount(50)
                .vegetarian(Boolean.FALSE)
                .build();

        when(recipeService.updateRecipe(any(RecipeDTO.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(put("/api/recipe/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        requestFields(
                                fieldWithPath("recipeID").description("Recipe ID"),
                                fieldWithPath("recipeName").description("Recipe name"),
                                fieldWithPath("vegetarian").description("is vegetarian flag"),
                                fieldWithPath("suitablePeopleCount").description("people count that recipe suits"),
                                fieldWithPath("ingredientList").description("Recipe ingredients"),
                                fieldWithPath("instructions").description("Recipe instructions")

                        ),
                        responseFields(
                                fieldWithPath("success").description("is successfull"),
                                fieldWithPath("errorMessage").description("error message")

                        )
                ))
                .andReturn();

        verify(recipeService,times(1)).updateRecipe(any(RecipeDTO.class));

    }


    @Test
    public void given_recipe_should_not_Update_Recipe_if_recipe_does_not_exists() throws Exception {

        Recipe recipe = Recipe.builder()
                .recipeID(1L)
                .recipeName("Kofte")
                .instructions("bla bla bla")
                .ingredientList(Arrays.asList("Kiyma","sogan","ekmek ici"))
                .suitablePeopleCount(50)
                .vegetarian(Boolean.FALSE)
                .build();

        when(recipeService.updateRecipe(any(RecipeDTO.class))).thenThrow(RecipeNotFoundException.class);

        mockMvc.perform(put("/api/recipe/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("{method-name}"))
                .andReturn();

        verify(recipeService,times(1)).updateRecipe(any(RecipeDTO.class));

    }


    @Test
    public void given_recipe_should_not_Update_Recipe_if_recipe_name_belongs_to_another_recipe() throws Exception {

        Recipe recipe = Recipe.builder()
                .recipeID(1L)
                .recipeName("Kofte")
                .instructions("bla bla bla")
                .ingredientList(Arrays.asList("Kiyma","sogan","ekmek ici"))
                .suitablePeopleCount(50)
                .vegetarian(Boolean.FALSE)
                .build();

        when(recipeService.updateRecipe(any(RecipeDTO.class))).thenThrow(RecipeAlreadyExistException.class);

        mockMvc.perform(put("/api/recipe/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(recipe))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andDo(document("{method-name}"))
                .andReturn();

        verify(recipeService,times(1)).updateRecipe(any(RecipeDTO.class));

    }



    @Test
    public void given_Recipe_id_should_Delete_if_Recipe_exists() throws Exception {

        when(recipeService.deleteRecipe(anyLong()))
                .thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/recipe/delete?recipeID={recipeID}", "1" ))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        requestParameters(parameterWithName("recipeID").description("recipe id")),
                        responseFields(
                                fieldWithPath("success").description("is successfull"),
                                fieldWithPath("errorMessage").description("error message")
                        )
                ));


        verify(recipeService,times(1)).deleteRecipe(anyLong());
    }

    @Test
    public void given_Recipe_id_should_not_Delete_if_Recipe_does_not_exists() throws Exception {

        when(recipeService.deleteRecipe(anyLong()))
                .thenThrow(RecipeNotFoundException.class);

        mockMvc.perform(delete("/api/recipe/delete?recipeID={recipeID}", "1" ))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("{method-name}"));

        verify(recipeService,times(1)).deleteRecipe(anyLong());
    }

}