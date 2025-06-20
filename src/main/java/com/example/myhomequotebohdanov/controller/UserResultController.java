package com.example.myhomequotebohdanov.controller;

import com.example.myhomequotebohdanov.model.UserResult;
import com.example.myhomequotebohdanov.service.UserResultService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserResultController {
  private static final Logger logger = LoggerFactory.getLogger(UserResultController.class);

  private final UserResultService userResultService;

  @Autowired
  public UserResultController(UserResultService userResultService) {
    this.userResultService = userResultService;
  }

  @GetMapping("/userinfo/{user_id}")
  public ResponseEntity<List<UserResult>> getUserInfo(@PathVariable long user_id) {
    logger.info("GET request: /api/userinfo/{}", user_id);

    List<UserResult> results = userResultService.getTopResultsByUser(user_id);

    logger.info("Response: found {} results for user {}", results.size(), user_id);
    return ResponseEntity.ok(results);
  }

  @GetMapping("/levelinfo/{level_id}")
  public ResponseEntity<List<UserResult>> getLevelInfo(@PathVariable long level_id) {
    logger.info("GET request: /api/levelinfo/{}", level_id);

    List<UserResult> results = userResultService.getTopResultsByLevel(level_id);

    logger.info("Response: found {} results for level {}", results.size(), level_id);
    return ResponseEntity.ok(results);
  }

  @PutMapping("/setinfo")
  public ResponseEntity<UserResult> setInfo(@RequestBody UserResult userResult) {
    logger.info("PUT request: /api/setinfo");
    logger.info("Request body: user_id={}, level_id={}, result={}",
        userResult.getUser_id(), userResult.getLevel_id(), userResult.getResult());

    userResultService.setResult(userResult);

    logger.info("Result successfully saved");
    return ResponseEntity.ok(userResult);
  }
}