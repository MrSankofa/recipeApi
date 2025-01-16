package com.personalProject.recipeApi.controllerTests;

import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.repository.RecipeRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private RecipeRepo recipeRepo;

  @Test
  void testGetRecipesByDifficulty_Success() {
    // Arrange
    Recipe recipe1 = new Recipe();
    recipe1.setName("Easy Pancakes");
    recipe1.setDifficultyRating(2);
    recipe1.setMinutesToMake(15);

    Recipe recipe2 = new Recipe();
    recipe2.setName("Intermediate Curry");
    recipe2.setDifficultyRating(5);
    recipe2.setMinutesToMake(45);

    Recipe recipe3 = new Recipe();
    recipe3.setName("Hard Souffle");
    recipe3.setDifficultyRating(9);
    recipe3.setMinutesToMake(60);

    recipeRepo.saveAll(List.of(recipe1, recipe2, recipe3));

    // Act: Query recipes with difficulty <= 4
    List<Recipe> recipes = recipeRepo.findByNameContainingIgnoreCaseAndDifficultyRatingLessThanEqual("Pancakes", 4);

    // Assert: Verify the results
    assertNotNull(recipes);
    assertEquals(1, recipes.size());
    assertEquals("Easy Pancakes", recipes.get(0).getName());
  }

  @Test
  void testGetRecipesByAllPies_Success() {
    // Arrange
    Recipe recipe1 = new Recipe();
    recipe1.setName("pie 1");
    recipe1.setDifficultyRating(4);
    recipe1.setMinutesToMake(15);

    Recipe recipe2 = new Recipe();
    recipe2.setName("pie 2");
    recipe2.setDifficultyRating(6);
    recipe2.setMinutesToMake(45);

    Recipe recipe3 = new Recipe();
    recipe3.setName("pie 3");
    recipe3.setDifficultyRating(10);
    recipe3.setMinutesToMake(60);

    recipeRepo.saveAll(List.of(recipe1, recipe2, recipe3));

    // Act: Query recipes with difficulty <= 4
    List<Recipe> recipes = recipeRepo.findByNameContainingIgnoreCaseAndDifficultyRatingLessThanEqual("pie", 6);

    // Assert: Verify the results
    assertNotNull(recipes);
    assertEquals(2, recipes.size());
    assertEquals("pie 1", recipes.get(0).getName());
  }

  @Test
  void testGetRecipesByDifficulty_NoMatches() {
    // Arrange
    Recipe recipe = new Recipe();
    recipe.setName("Hard Souffle");
    recipe.setDifficultyRating(9);
    recipe.setMinutesToMake(60);

    recipeRepo.save(recipe);

    // Act
    String url = "http://localhost:" + port + "/search/Souffle?maxDifficult=4";
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Assert
    assertEquals(404, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().contains("Could not find any recipes"));
  }
}
