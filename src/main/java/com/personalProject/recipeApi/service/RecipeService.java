package com.personalProject.recipeApi.service;

import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.repository.RecipeRepo;
import com.personalProject.recipeApi.exceptions.*;
import com.personalProject.recipeApi.repository.ReviewRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

  @Autowired
  RecipeRepo recipeRepo;

  @Autowired
  ReviewRepo reviewRepo;

  @Transactional
  public Recipe createNewRecipe(Recipe recipe)
      throws IllegalStateException {
    recipe.validate();
    recipe = recipeRepo.save(recipe);
    recipe.generateLocationURI();
    return recipe;
  }

  public Recipe getRecipeById(Long id) throws NoSuchRecipeException {
    Optional<Recipe> recipeOptional = recipeRepo.findById(id);

    if (recipeOptional.isEmpty()) {
      throw new NoSuchRecipeException(
          "No recipe with ID " + id + " could be found.");
    }

    Recipe recipe = recipeOptional.get();
    recipe.generateLocationURI();
    return recipe;
  }

  public List<Recipe> getRecipesByName(String name)
      throws NoSuchRecipeException {
    List<Recipe> matchingRecipes =
        recipeRepo.findByNameContainingIgnoreCase(name);

    if (matchingRecipes.isEmpty()) {
      throw new NoSuchRecipeException(
          "No recipes could be found with that name.");
    }

    return matchingRecipes;
  }

  public List<Recipe> getAllRecipes() throws NoSuchRecipeException {
    List<Recipe> recipes = recipeRepo.findAll();

    if (recipes.isEmpty()) {
      throw new NoSuchRecipeException(
          "There are no recipes yet :( feel free to add one.");
    }
    return recipes;
  }

  public List<Recipe> getAllRecipesByRating(int rating) throws NoSuchRecipeException {
    List<Recipe> recipes = recipeRepo.findDistinctByReviews_RatingGreaterThanEqual(rating);

    if (recipes.isEmpty()) {
      throw new NoSuchRecipeException("Could not find any recipes with rating " + rating);
    }

    return recipes;
  }

  public List<Recipe> getAllRecipesByMaxDifficulty(String name, int difficulty) throws NoSuchRecipeException {
    List<Recipe> recipes = recipeRepo.findByNameContainingIgnoreCaseAndDifficultyRatingLessThanEqual(name, difficulty);

    if (recipes.isEmpty()) {
      throw new NoSuchRecipeException("Could not find any recipes with the name: " + name + " or difficult: " + difficulty);
    }

    return recipes;
  }

  @Transactional
  public Recipe deleteRecipeById(Long id) throws NoSuchRecipeException {
    try {
      Recipe recipe = getRecipeById(id);
      recipeRepo.deleteById(id);
      return recipe;
    } catch (NoSuchRecipeException e) {
      throw new NoSuchRecipeException(
          e.getMessage() + " Could not delete.");
    }
  }

  @Transactional
  public Recipe updateRecipe(Recipe recipe, boolean forceIdCheck)
      throws NoSuchRecipeException {
    try {
      if (forceIdCheck) {
        getRecipeById(recipe.getId());
      }
      recipe.validate();
      Recipe savedRecipe = recipeRepo.save(recipe);
      savedRecipe.generateLocationURI();
      return savedRecipe;
    } catch (NoSuchRecipeException e) {
      throw new NoSuchRecipeException(
          "The recipe you passed in did not have an ID found " +
              "in the database. Double check that it is correct. " +
              "Or maybe you meant to POST a recipe not PATCH one.");
    }
  }
}
