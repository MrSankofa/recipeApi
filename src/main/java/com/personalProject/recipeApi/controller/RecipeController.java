package com.personalProject.recipeApi.controller;

import com.personalProject.recipeApi.exceptions.NoSuchRecipeException;
import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@Validated
public class RecipeController {

  @Autowired
  RecipeService recipeService;

  @PostMapping
  public ResponseEntity<?> createNewRecipe(@Valid @RequestBody Recipe recipe) {
    try {
      Recipe insertedRecipe = recipeService.createNewRecipe(recipe);
      return ResponseEntity.created(
          insertedRecipe.getLocationURI()).body(insertedRecipe);
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch ( DataIntegrityViolationException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllRecipes() {
    try {
      return ResponseEntity.ok(recipeService.getAllRecipes());
    } catch (NoSuchRecipeException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    }
  }

  @GetMapping("/rating/{min}")
  public ResponseEntity<?> getAllRecipesByRating(@PathVariable int min) {
    try {
      return ResponseEntity.ok(recipeService.getAllRecipesByRating(min));
    } catch (NoSuchRecipeException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getRecipeById(@PathVariable("id") Long id) {
    try {
      Recipe recipe = recipeService.getRecipeById(id);
      return ResponseEntity.ok(recipe);
    } catch (NoSuchRecipeException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    }
  }

  @GetMapping("/search/{name}")
  public ResponseEntity<?> getRecipesByName(
      @PathVariable("name") String name) {
    try {
      List<Recipe> matchingRecipes =
          recipeService.getRecipesByName(name);
      return ResponseEntity.ok(matchingRecipes);
    } catch (NoSuchRecipeException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    }
  }

  @GetMapping("/search/{name}/difficulty")
  public ResponseEntity<?> getRecipesByDifficulty(@PathVariable("name") String name, @RequestParam Integer maxDifficulty) {
    try {
      List<Recipe> matchingRecipes = recipeService.getAllRecipesByMaxDifficulty(name, maxDifficulty);
      return ResponseEntity.ok(matchingRecipes);
    } catch (NoSuchRecipeException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    }
  }

  @GetMapping("/search/username/{username}")
  public ResponseEntity<?> getRecipesByUsername(@PathVariable("username") String username) {
    try {
      List<Recipe> matchingRecipes = recipeService.getAllRecipesByUsername(username);
      return ResponseEntity.ok(matchingRecipes);

    } catch (NoSuchRecipeException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(e.getMessage());
    }
  }


  @PatchMapping
  public ResponseEntity<?> updateRecipe(
      @RequestBody Recipe updatedRecipe) {
    try {
      Recipe returnedUpdatedRecipe =
          recipeService.updateRecipe(updatedRecipe, true);
      return ResponseEntity.ok(returnedUpdatedRecipe);
    } catch (NoSuchRecipeException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteRecipeById(
      @PathVariable("id") Long id) {
    try {
      Recipe deletedRecipe = recipeService.deleteRecipeById(id);
      return ResponseEntity
          .ok("The recipe with ID " + deletedRecipe.getId() +
              " and name " + deletedRecipe.getName() +
              " was deleted.");
    } catch (NoSuchRecipeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
