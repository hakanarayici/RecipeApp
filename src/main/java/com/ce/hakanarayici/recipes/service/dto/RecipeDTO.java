package com.ce.hakanarayici.recipes.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class RecipeDTO {
    private LocalDateTime createDate;
    private Long recipeID;
    private String recipeName;
    private Boolean vegetarian;
    private Integer suitablePeopleCount;
    private List<String> ingredientList;
    private String instructions;
}