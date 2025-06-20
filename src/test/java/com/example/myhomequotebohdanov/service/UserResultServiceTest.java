package com.example.myhomequotebohdanov.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.myhomequotebohdanov.model.UserResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserResultServiceTest {

  private UserResultService userResultService;

  @BeforeEach
  void setUp() {
    userResultService = new UserResultService();
  }

  @Test
  @DisplayName("Test sorting user results by result (descending) and level_id (descending)")
  void testGetTopResultsByUser_Sorting() {
    userResultService.setResult(new UserResult(1, 2, 8));
    userResultService.setResult(new UserResult(1, 1, 55));
    userResultService.setResult(new UserResult(1, 3, 15));
    userResultService.setResult(new UserResult(1, 4, 55));

    List<UserResult> results = userResultService.getTopResultsByUser(1);

    System.out.println("=== Results for user 1 ===");
    for (int i = 0; i < results.size(); i++) {
      UserResult result = results.get(i);
      System.out.printf("[%d] user_id=%d, level_id=%d, result=%d%n",
          i, result.getUser_id(), result.getLevel_id(), result.getResult());
    }

    assertEquals(4, results.size(), "Should be 4 results");
    assertEquals(4, results.get(0).getLevel_id());
    assertEquals(55, results.get(0).getResult());
    assertEquals(1, results.get(1).getLevel_id());
    assertEquals(55, results.get(1).getResult());
    assertEquals(3, results.get(2).getLevel_id());
    assertEquals(15, results.get(2).getResult());
    assertEquals(2, results.get(3).getLevel_id());
    assertEquals(8, results.get(3).getResult());
  }

  @Test
  @DisplayName("Test sorting level results by result (descending) and user_id (descending)")
  void testGetTopResultsByLevel_Sorting() {
    userResultService.setResult(new UserResult(2, 1, 75));
    userResultService.setResult(new UserResult(1, 1, 100));
    userResultService.setResult(new UserResult(3, 1, 75));
    userResultService.setResult(new UserResult(4, 1, 50));

    List<UserResult> results = userResultService.getTopResultsByLevel(1);

    System.out.println("=== Results for level 1 ===");
    for (int i = 0; i < results.size(); i++) {
      UserResult result = results.get(i);
      System.out.printf("[%d] user_id=%d, level_id=%d, result=%d%n",
          i, result.getUser_id(), result.getLevel_id(), result.getResult());
    }

    assertEquals(4, results.size(), "Should be 4 results");
    assertEquals(1, results.get(0).getUser_id());
    assertEquals(100, results.get(0).getResult());
    assertEquals(3, results.get(1).getUser_id());
    assertEquals(75, results.get(1).getResult());
    assertEquals(2, results.get(2).getUser_id());
    assertEquals(75, results.get(2).getResult());
    assertEquals(4, results.get(3).getUser_id());
    assertEquals(50, results.get(3).getResult());
  }

  @Test
  @DisplayName("Test limiting number of results")
  void testGetTopResultsByUser_Limit() {
    for (int i = 1; i <= 25; i++) {
      userResultService.setResult(new UserResult(1, i, 100 - i));
    }

    List<UserResult> results = userResultService.getTopResultsByUser(1);

    System.out.println("=== Results with default limit ===");
    for (int i = 0; i < results.size(); i++) {
      UserResult result = results.get(i);
      System.out.printf("[%d] user_id=%d, level_id=%d, result=%d%n",
          i, result.getUser_id(), result.getLevel_id(), result.getResult());
    }

    assertEquals(20, results.size(), "Should return only 20 results (default limit)");
    assertEquals(99, results.get(0).getResult(), "First result should be highest");
    assertEquals(80, results.get(19).getResult(), "Last result should be 80");
  }

  @Test
  @DisplayName("Test returning empty list for non-existent user")
  void testGetTopResultsByUser_NonExistentUser() {
    List<UserResult> results = userResultService.getTopResultsByUser(999);
    assertTrue(results.isEmpty(), "Should return empty list");
  }

  @Test
  @DisplayName("Test returning empty list for non-existent level")
  void testGetTopResultsByLevel_NonExistentLevel() {
    List<UserResult> results = userResultService.getTopResultsByLevel(999);
    assertTrue(results.isEmpty(), "Should return empty list");
  }

  @Test
  @DisplayName("Test saving and retrieving single result")
  void testSetAndGetSingleResult() {
    UserResult userResult = new UserResult(1, 1, 100);
    userResultService.setResult(userResult);
    List<UserResult> userResults = userResultService.getTopResultsByUser(1);
    assertEquals(1, userResults.size());
    assertEquals(100, userResults.get(0).getResult());
    List<UserResult> levelResults = userResultService.getTopResultsByLevel(1);
    assertEquals(1, levelResults.size());
    assertEquals(100, levelResults.get(0).getResult());
  }
} 