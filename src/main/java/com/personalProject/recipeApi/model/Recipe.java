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


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * @version 1.0
 * @since 2025-01-16
 *
 * Includes one-to-many relationship definitions between Recipe and Step, Review, and Ingredient.
 * Regulates the difficulty rating of recipes.
 * Defines a validate() method to check the recipe object has both at least one ingredient and step.
 */
public class Recipe {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

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

  public List<Review> getReviews() {
    return new ArrayList<>(reviews); // Convert to a List
  }

  public double averageRating() {
    int totalRatings = reviews.size();
    double sumOfRatings = reviews.stream().mapToDouble(Review::getRating).sum();

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
}
