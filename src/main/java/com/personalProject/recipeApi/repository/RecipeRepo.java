package com.personalProject.recipeApi.repository;

import com.personalProject.recipeApi.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {

  List<Recipe> findByNameContainingIgnoreCase(String name);

  // findDistinctBy: Ensures that recipes are not duplicated if they have multiple matching reviews.
  //"SELECT DISTINCT r FROM Recipe r JOIN r.reviews rev WHERE rev.rating >= :rating")
  List<Recipe> findDistinctByReviews_RatingGreaterThanEqual(int rating);


  List<Recipe> findByNameContainingIgnoreCaseAndDifficultyRatingLessThanEqual(String name, int difficultyRating);

  List<Recipe> findRecipeByUsername(String username);

}
