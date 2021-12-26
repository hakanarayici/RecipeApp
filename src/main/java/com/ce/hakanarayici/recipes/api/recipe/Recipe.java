package com.ce.hakanarayici.recipes.api.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Recipe implements Serializable {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime createDate;

    private Long recipeID;

    private String recipeName;

    private Boolean vegetarian;

    private Integer suitablePeopleCount;

    private List<String> ingredientList;

    private String instructions;
}
