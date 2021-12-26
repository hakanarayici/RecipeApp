package com.ce.hakanarayici.recipes.data.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "NAME")
    private String recipeName;

    @Column(name = "IS_VEGETARIAN")
    private Boolean vegetarian;

    @Column(name = "SUITABLE_PEOPLE_COUNT")
    private Integer suitablePeopleCount;

    @OneToMany(mappedBy="recipe",cascade=CascadeType.ALL)
    private List<IngredientEntity> ingredientList;

    private Clob instructions;

}
