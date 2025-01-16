package com.personalProject.recipeApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO: Question I had a payload that was incorrect where the ingredients were a list of strings instead of a list of ingredient objects and I recieved a 500 instead of a 400. How do I fix that?

/**
 * @version 1.0
 * @since 2025-01-16
 *
 * Includes one-to-many relationship definitions between Recipe and Step, Review, and Ingredient.
 * Regulates the difficulty rating of recipes.
 * Defines a validate() method to check the recipe object has both at least one ingredient and step.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

  @Id
  @GeneratedValue
  private Long id;

  public Long getId() {
    return id;
  }

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String username;



  @Column(nullable = false)
  private Integer minutesToMake;

  @Column(nullable = false)
  private Integer difficultyRating;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Collection<Ingredient> ingredients = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Collection<Step> steps = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Collection<Review> reviews;

  @Transient
  @JsonIgnore
  private URI locationURI;

  // Default constructor
  public Recipe() {}

  public List<Review> getReviews() {
    return new ArrayList<>(reviews); // Convert to a List
  }

  public double averageRating() {
    int totalRatings = reviews.size();
    int sumOfRatings = reviews.stream().mapToInt(review -> review.getRating()).sum();

    return sumOfRatings / totalRatings;
  }

  public void setDifficultyRating(int difficultyRating) {
    if (difficultyRating < 0 || difficultyRating > 10) {
      throw new IllegalStateException(
          "Difficulty rating must be between 0 and 10.");
    }
    this.difficultyRating = difficultyRating;
  }

  /**
   * Checks the recipe object has both at least one ingredient and step.
   * Throws IllegalStateExceptions if the ingredients or steps size is 0
   */
  public void validate() throws IllegalStateException {
    if (ingredients.size() == 0) {
      throw new IllegalStateException(
          "You need at least one ingredient for your recipe!");
    } else if (steps.size() == 0) {
      throw new IllegalStateException(
          "You need at least one step for your recipe!");
    }
  }

  public void generateLocationURI() {
    try {
      locationURI = new URI(
          ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/recipes/")
              .path(String.valueOf(id))
              .toUriString());
    } catch (URISyntaxException e) {
      // exception should stop here.
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getMinutesToMake() {
    return minutesToMake;
  }

  public void setMinutesToMake(Integer minutesToMake) {
    this.minutesToMake = minutesToMake;
  }

  public Integer getDifficultyRating() {
    return difficultyRating;
  }

  public void setDifficultyRating(Integer difficultyRating) {
    this.difficultyRating = difficultyRating;
  }

  public Collection<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Collection<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public Collection<Step> getSteps() {
    return steps;
  }

  public void setSteps(Collection<Step> steps) {
    this.steps = steps;
  }

  public void setReviews(Collection<Review> reviews) {
    this.reviews = reviews;
  }

  public URI getLocationURI() {
    return locationURI;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
