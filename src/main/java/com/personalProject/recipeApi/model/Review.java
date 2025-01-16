package com.personalProject.recipeApi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// TODO: Question I had a payload that was incorrect were the username key was Username and I received a 500 instead of a 400. How do I fix that?

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String username;

  public void setId(Long id) {
    this.id = id;
  }

  public @NotNull String getUsername() {
    return username;
  }

  public void setUsername(@NotNull String username) {
    this.username = username;
  }

  public @NotNull String getDescription() {
    return description;
  }

  public void setDescription(@NotNull String description) {
    this.description = description;
  }

  private int rating;

  @NotNull
  private String description;

  public void setRating(int rating) {
    if (rating <= 0 || rating > 10) {
      throw new IllegalStateException("Rating must be between 0 and 10.");
    }
    this.rating = rating;
  }

  public int getRating() {
    return rating;
  }

  public Long getId(){
    return id;
  }
}
