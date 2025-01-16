package com.personalProject.recipeApi.serviceTests;


import com.personalProject.recipeApi.exceptions.NoSuchRecipeException;
import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.repository.RecipeRepo;
import com.personalProject.recipeApi.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

  @Mock
  private RecipeRepo recipeRepo;

  @InjectMocks
  private RecipeService recipeService;

  public RecipeServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllRecipesByRating_Success() throws NoSuchRecipeException {
    // Arrange
    Recipe recipe1 = new Recipe();
    recipe1.setName("Recipe 1");

    Recipe recipe2 = new Recipe();
    recipe2.setName("Recipe 2");

    when(recipeRepo.findDistinctByReviews_RatingGreaterThanEqual(4))
        .thenReturn(Arrays.asList(recipe1, recipe2));

    // Act
    List<Recipe> result = recipeService.getAllRecipesByRating(4);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Recipe 1", result.get(0).getName());
    verify(recipeRepo, times(1)).findDistinctByReviews_RatingGreaterThanEqual(4);
  }

  @Test
  void testGetAllRecipesByRating_NoRecipesFound() {
    // Arrange
    when(recipeRepo.findDistinctByReviews_RatingGreaterThanEqual(5))
        .thenReturn(Collections.emptyList());

    // Act & Assert
    NoSuchRecipeException exception = assertThrows(
        NoSuchRecipeException.class,
        () -> recipeService.getAllRecipesByRating(5)
    );

    assertEquals("Could not find any recipes with rating 5", exception.getMessage());
    verify(recipeRepo, times(1)).findDistinctByReviews_RatingGreaterThanEqual(5);
  }
}
