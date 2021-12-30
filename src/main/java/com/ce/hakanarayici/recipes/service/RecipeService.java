package com.ce.hakanarayici.recipes.service;

import com.ce.hakanarayici.recipes.data.dao.RecipeDAO;
import com.ce.hakanarayici.recipes.data.model.IngredientEntity;
import com.ce.hakanarayici.recipes.data.model.RecipeEntity;
import com.ce.hakanarayici.recipes.exception.RecipeAlreadyExistException;
import com.ce.hakanarayici.recipes.exception.RecipeNotFoundException;
import com.ce.hakanarayici.recipes.service.dto.RecipeDTO;
import com.ce.hakanarayici.recipes.util.SQLUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialClob;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeService implements IRecipeService {

    private final RecipeDAO recipeDAO;

    @Override
    public RecipeDTO getRecipeByName(String recipeName) {

        RecipeEntity recipeEntity = recipeDAO.findByRecipeName(recipeName)
                .orElseThrow(() -> new RecipeNotFoundException(recipeName));

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


        recipeDAO.findByRecipeName(receipeDTO.getRecipeName())
                .ifPresent(s -> {
                    throw new RecipeAlreadyExistException(receipeDTO.getRecipeName());
                });

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

        RecipeEntity recipeEntity  = recipeDAO.findById(recipeDTO.getRecipeID())
                .orElseThrow(() -> new RecipeNotFoundException(recipeDTO.getRecipeID()));

        List<IngredientEntity> ingredientEntities = recipeEntity.getIngredientList().stream().collect(Collectors.toList());
        ingredientEntities.stream().forEach(recipeEntity::removeChild);

        recipeEntity.setCreateDate(recipeDTO.getCreateDate());
        recipeEntity.setRecipeName(recipeDTO.getRecipeName());
        recipeEntity.setInstructions(new SerialClob(recipeDTO.getInstructions().toCharArray()));
        recipeEntity.setSuitablePeopleCount(recipeDTO.getSuitablePeopleCount());
        recipeEntity.setVegetarian(recipeDTO.getVegetarian());

        recipeDTO.getIngredientList()
                .stream()
                .map(a -> IngredientEntity.builder()
                        .ingredientName(a)
                        .recipe(recipeEntity)
                        .build())
                .collect(Collectors.toList())
                .stream().forEach(recipeEntity::addChild);

        recipeDAO.save(recipeEntity);

        return Boolean.TRUE;

    }


    @Override
    public Boolean deleteRecipe(Long recipeID) {

        RecipeEntity recipeEntity = recipeDAO.findById(recipeID)
                .orElseThrow(() -> new RecipeNotFoundException(recipeID));

        recipeDAO.delete(recipeEntity);

        return Boolean.TRUE;

    }

    @Override
    public List<RecipeDTO> getAllRecipes() {
        List<RecipeEntity> recipeEntityList = recipeDAO.findAll();

        return recipeEntityList.stream().map(recipeEntity -> RecipeDTO.builder()
                .recipeID(recipeEntity.getId())
                .createDate(recipeEntity.getCreateDate())
                .recipeName(recipeEntity.getRecipeName())
                .ingredientList(recipeEntity.getIngredientList().stream().map(a -> a.getIngredientName()).collect(Collectors.toList()))
                .instructions(SQLUtil.clobToString(recipeEntity.getInstructions()))
                .vegetarian(recipeEntity.getVegetarian())
                .suitablePeopleCount(recipeEntity.getSuitablePeopleCount())
                .build())
                .collect(Collectors.toList());
    }

}
