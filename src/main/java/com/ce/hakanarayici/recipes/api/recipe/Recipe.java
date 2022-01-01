package com.ce.hakanarayici.recipes.api.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe implements Serializable {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime createDate;

    private Long recipeID;

    @NotBlank
    @NotNull
    private String recipeName;

    @NotNull
    private Boolean vegetarian;

    @NotNull
    private Integer suitablePeopleCount;

    @NotEmpty
    @NotNull
    private List<String> ingredientList;

    @NotBlank
    @NotNull
    private String instructions;
}
