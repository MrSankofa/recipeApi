package com.personalProject.recipeApi.serviceTests;


import com.personalProject.recipeApi.exceptions.NoSuchRecipeException;
import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.model.Review;
import com.personalProject.recipeApi.repository.RecipeRepo;
import com.personalProject.recipeApi.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecipeServiceIntegrationTest {

  @Autowired
  private RecipeService recipeService;

  @Autowired
  private RecipeRepo recipeRepo;

  @Test
  void testGetAllRecipesByRating_Success() throws NoSuchRecipeException {
    // Arrange
    Recipe recipe = new Recipe();
    recipe.setName("Test Recipe");
    recipe.setMinutesToMake(30);
    recipe.setDifficultyRating(5);

    Review review = new Review();
    review.setRating(5);
    review.setUsername("User1");
    review.setDescription("I love it");

    recipe.setReviews(List.of(review));
    recipeRepo.save(recipe);

    // Act
    List<Recipe> recipes = recipeService.getAllRecipesByRating(4);

    // Assert
    assertNotNull(recipes);
    assertEquals(1, recipes.size());
    assertEquals("Test Recipe", recipes.get(0).getName());
  }

  @Test
  void testGetAllRecipesByRating_NoRecipesFound() {
    // Act & Assert
    NoSuchRecipeException exception = assertThrows(
        NoSuchRecipeException.class,
        () -> recipeService.getAllRecipesByRating(5)
    );

    assertEquals("Could not find any recipes with rating 5", exception.getMessage());
  }
}
