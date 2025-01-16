package com.personalProject.recipeApi.repository;

import com.personalProject.recipeApi.model.Recipe;
import com.personalProject.recipeApi.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

  List<Review> findByUsername(String username);

  List<Recipe> findAllByRatingGreaterThanEqual(int rating);
}
