package com.ce.hakanarayici.recipes.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "INGREDIENT")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String ingredientName;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="recipe_id", nullable=false)
    private RecipeEntity recipe;

}
