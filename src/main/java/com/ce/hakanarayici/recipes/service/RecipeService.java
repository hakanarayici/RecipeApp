package com.ce.hakanarayici.recipes.service;

import com.ce.hakanarayici.recipes.data.dao.RecipeDAO;
import com.ce.hakanarayici.recipes.data.model.IngredientEntity;
import com.ce.hakanarayici.recipes.data.model.RecipeEntity;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;
import com.ce.hakanarayici.recipes.util.SQLUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialClob;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeService implements IRecipeService {

    private final RecipeDAO recipeDAO;

    @Override
    public RecipeDTO getRecipeByName(String recipeName) {

        RecipeEntity recipeEntity = recipeDAO.findByRecipeName(recipeName);

        return RecipeDTO.builder()
                .recipeID(recipeEntity.getId())
                .createDate(recipeEntity.getCreateDate())
                .recipeName(recipeEntity.getRecipeName())
                .ingredientList(recipeEntity.getIngredientList().stream().map(a -> a.getIngredientName()).collect(Collectors.toList()))
                .instructions(SQLUtil.clobToString(recipeEntity.getInstructions()))
                .vegetarian(recipeEntity.getVegetarian())
                .suitablePeopleCount(recipeEntity.getSuitablePeopleCount())
                .build();
    }

    @SneakyThrows
    @Override
    public Boolean createRecipe(RecipeDTO receipeDTO) {

        RecipeEntity entity = RecipeEntity.builder()
                .createDate(receipeDTO.getCreateDate())
                .recipeName(receipeDTO.getRecipeName())
                .instructions(new SerialClob(receipeDTO.getInstructions().toCharArray()))
                .suitablePeopleCount(receipeDTO.getSuitablePeopleCount())
                .vegetarian(receipeDTO.getVegetarian())
                .build();

                entity.setIngredientList(receipeDTO.getIngredientList()
                        .stream()
                        .map(a -> IngredientEntity.builder()
                                .ingredientName(a)
                                .recipe(entity)
                                .build())
                        .collect(Collectors.toList()));

        recipeDAO.save(entity);

        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public Boolean updateRecipe(RecipeDTO recipeDTO) {

        Optional<RecipeEntity> optionalRecipeEntity = recipeDAO.findById(recipeDTO.getRecipeID());

        if(optionalRecipeEntity.isPresent()){

            RecipeEntity recipeEntity = optionalRecipeEntity.get();
            recipeEntity.setCreateDate(recipeDTO.getCreateDate());
            recipeEntity.setRecipeName(recipeDTO.getRecipeName());
            recipeEntity.setInstructions(new SerialClob(recipeDTO.getInstructions().toCharArray()));
            recipeEntity.setSuitablePeopleCount(recipeDTO.getSuitablePeopleCount());
            recipeEntity.setVegetarian(recipeDTO.getVegetarian());

            recipeEntity.setIngredientList(recipeDTO.getIngredientList()
                    .stream()
                    .map(a -> IngredientEntity.builder()
                            .ingredientName(a)
                            .recipe(recipeEntity)
                            .build())
                    .collect(Collectors.toList()));

            recipeDAO.save(recipeEntity);

            return Boolean.TRUE;

        }else{
            throw new IllegalArgumentException("couldnt find recipe to update");
        }

    }


    @Override
    public Boolean deleteRecipe(Long recipeID) {

        Optional<RecipeEntity> optionalRecipeEntity = recipeDAO.findById(recipeID);

        if(optionalRecipeEntity.isPresent()){
            recipeDAO.delete(optionalRecipeEntity.get());
            return Boolean.TRUE;
        }else{
            throw new IllegalArgumentException("couldnt find recipe to update");
        }

    }

}
