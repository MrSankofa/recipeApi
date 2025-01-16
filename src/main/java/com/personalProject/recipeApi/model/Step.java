package com.personalProject.recipeApi.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  private int stepNumber;

  @NotNull
  public int getStepNumber() {
    return stepNumber;
  }

  public void setStepNumber(@NotNull int stepNumber) {
    this.stepNumber = stepNumber;
  }

  public @NotNull String getDescription() {
    return description;
  }

  public void setDescription(@NotNull String description) {
    this.description = description;
  }

  @NotNull
  private String description;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
