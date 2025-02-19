package com.personalProject.recipeApi.service;

import com.personalProject.recipeApi.exceptions.NoSuchRecipeException;
import com.personalProject.recipeApi.exceptions.NoSuchReviewException;
import com.personalProject.recipeApi.exceptions.SelfReviewNotAllowedException;
import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.model.Review;
import com.personalProject.recipeApi.repository.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

  @Autowired
  ReviewRepo reviewRepo;

  @Autowired
  RecipeService recipeService;

  public Review getReviewById(Long id) throws NoSuchReviewException {
    Optional<Review> review = reviewRepo.findById(id);

    if (review.isEmpty()) {
      throw new NoSuchReviewException(
          "The review with ID " + id + " could not be found.");
    }
    return review.get();
  }

  public List<Review> getReviewByRecipeId(Long recipeId)
      throws NoSuchRecipeException, NoSuchReviewException {
    Recipe recipe = recipeService.getRecipeById(recipeId);

    List<Review> reviews = recipe.getReviews();

    if (reviews.isEmpty()) {
      throw new NoSuchReviewException(
          "There are no reviews for this recipe.");
    }
    return reviews;
  }

  public List<Review> getReviewByUsername(String username)
      throws NoSuchReviewException {
    List<Review> reviews = reviewRepo.findByUsername(username);

    if (reviews.isEmpty()) {
      throw new NoSuchReviewException(
          "No reviews could be found for username " + username);
    }
    return reviews;
  }

  public Recipe postNewReview(Review review, Long recipeId) throws NoSuchRecipeException, SelfReviewNotAllowedException {
    // check if the review username is the same as the username on the recipe
    Recipe foundRecipe = recipeService.getRecipeById(recipeId);

    if (foundRecipe.getUsername().equals(review.getUsername())) {
      throw new SelfReviewNotAllowedException("You cannot submit a review for a recipe you created.");
    }

    Recipe recipe = recipeService.getRecipeById(recipeId);
    recipe.getReviews().add(review);
    recipeService.updateRecipe(recipe, false);
    return recipe;
  }

  public Review deleteReviewById(Long id) throws NoSuchReviewException {
    Review review = getReviewById(id);

    if (null == review) {
      throw new NoSuchReviewException(
          "The review you are trying to delete does not exist.");
    }
    reviewRepo.deleteById(id);
    return review;
  }

  public Review updateReviewById(Review reviewToUpdate)
      throws NoSuchReviewException {
    try {
      Review review = getReviewById(reviewToUpdate.getId());
    } catch (NoSuchReviewException e) {
      throw new NoSuchReviewException(
          "The review you are trying to update. " +
              "Maybe you meant to create one? If not," +
              "please double-check the ID you passed in.");
    }
    reviewRepo.save(reviewToUpdate);
    return reviewToUpdate;
  }
}
