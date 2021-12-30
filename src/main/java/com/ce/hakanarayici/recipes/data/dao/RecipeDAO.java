package com.ce.hakanarayici.recipes.data.dao;

import com.ce.hakanarayici.recipes.data.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeDAO extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findByRecipeName(String name);
}
