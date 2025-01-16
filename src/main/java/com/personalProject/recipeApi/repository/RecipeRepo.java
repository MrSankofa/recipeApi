package com.personalProject.recipeApi.repository;

import com.personalProject.recipeApi.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {

  List<Recipe> findByNameContainingIgnoreCase(String name);

  List<Recipe> findAllByReviews_RatingGreaterThanEqual(int rating);
}
