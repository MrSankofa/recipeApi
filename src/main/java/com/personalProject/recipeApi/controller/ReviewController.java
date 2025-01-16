package com.personalProject.recipeApi.controller;

import com.personalProject.recipeApi.exceptions.NoSuchRecipeException;
import com.personalProject.recipeApi.exceptions.NoSuchReviewException;
import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.model.Review;
import com.personalProject.recipeApi.service.RecipeService;
import com.personalProject.recipeApi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

  @Autowired
  ReviewService reviewService;

  @Autowired
  RecipeService recipeService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getReviewById(@PathVariable("id") Long id) {
    try {
      Review retrievedReview = reviewService.getReviewById(id);
      return ResponseEntity.ok(retrievedReview);
    } catch (IllegalStateException | NoSuchReviewException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/recipe/{recipeId}")
  public ResponseEntity<?> getReviewByRecipeId(
      @PathVariable("recipeId") Long recipeId) {
    try {
      List<Review> reviews =
          reviewService.getReviewByRecipeId(recipeId);
      return ResponseEntity.ok(reviews);
    } catch (NoSuchRecipeException | NoSuchReviewException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/user/{username}")
  public ResponseEntity<?> getReviewByUsername(
      @PathVariable("username") String username) {
    try {
      List<Review> reviews =
          reviewService.getReviewByUsername(username);
      return ResponseEntity.ok(reviews);
    } catch (NoSuchReviewException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/{recipeId}")
  public ResponseEntity<?> postNewReview(
      @RequestBody Review review,
      @PathVariable("recipeId") Long recipeId) {
    try {
      // check if the review username is the same as the username on the recipe
      Recipe foundRecipe = recipeService.getRecipeById(recipeId);

      if(foundRecipe.getUsername().equals(review.getUsername())){
        return ResponseEntity.badRequest().body("You cannot submit a review for a recipe you created buster!!");
      }

      Recipe insertedRecipe =
          reviewService.postNewReview(review, recipeId);
      return ResponseEntity.created(
          insertedRecipe.getLocationURI()).body(insertedRecipe);
    } catch (NoSuchRecipeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteReviewById(
      @PathVariable("id") Long id) {
    try {
      Review review = reviewService.deleteReviewById(id);
      return ResponseEntity.ok(review);
    } catch (NoSuchReviewException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PatchMapping
  public ResponseEntity<?> updateReviewById(
      @RequestBody Review reviewToUpdate) {
    try {
      Review review =
          reviewService.updateReviewById(reviewToUpdate);
      return ResponseEntity.ok(review);
    } catch (NoSuchReviewException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
