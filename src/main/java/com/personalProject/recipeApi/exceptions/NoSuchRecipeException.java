package com.personalProject.recipeApi.exceptions;

public class NoSuchRecipeException extends Exception {

  public NoSuchRecipeException(String message) {
    super(message);
  }

  public NoSuchRecipeException() {
  }
}
