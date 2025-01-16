package com.personalProject.recipeApi.exceptions;

public class SelfReviewNotAllowedException extends RuntimeException {

  public SelfReviewNotAllowedException(String message) { super(message); }

  public SelfReviewNotAllowedException() {}

}
