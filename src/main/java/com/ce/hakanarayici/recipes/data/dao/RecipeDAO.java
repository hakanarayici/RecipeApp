package com.ce.hakanarayici.recipes.data.dao;

import com.ce.hakanarayici.recipes.data.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDAO extends JpaRepository<RecipeEntity, Long> {

    RecipeEntity findByRecipeName(String name);
}
