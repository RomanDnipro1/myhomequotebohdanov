package com.example.myhomequotebohdanov.service;

import com.example.myhomequotebohdanov.model.UserResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserResultService {

  private static final long DEFAULT_TOP_SIZE = 20;
  private static final long DEFAULT_MAX_RESULTS_PER_USER = 20;
  private static final long DEFAULT_MAX_RESULTS_PER_LEVEL = 20;

  private static final Comparator<UserResult> BY_RESULT_AND_LEVEL =
      Comparator.comparingInt(UserResult::getResult).reversed()
          .thenComparing(UserResult::getLevel_id);

  private static final Comparator<UserResult> BY_RESULT_AND_USER =
      Comparator.comparingInt(UserResult::getResult).reversed()
          .thenComparing(UserResult::getUser_id);

  private final Map<Long, List<UserResult>> resultsByUser = new ConcurrentHashMap<>();
  private final Map<Long, List<UserResult>> resultsByLevel = new ConcurrentHashMap<>();

  private void limitResultsIfNeeded(List<UserResult> list, long maxSize) {
    if (maxSize > 0 && list.size() > maxSize) {
      list.subList((int) maxSize, list.size()).clear();
    }
  }

  public void setResult(UserResult userResult) {
    List<UserResult> userResultsList = resultsByUser.computeIfAbsent(
        userResult.getUser_id(),
        k -> new ArrayList<>()
    );
    synchronized (userResultsList) {
      userResultsList.add(userResult);
      userResultsList.sort(BY_RESULT_AND_LEVEL);
      limitResultsIfNeeded(userResultsList, DEFAULT_MAX_RESULTS_PER_USER);
    }

    List<UserResult> levelResultsList = resultsByLevel.computeIfAbsent(
        userResult.getLevel_id(),
        k -> new ArrayList<>()
    );
    synchronized (levelResultsList) {
      levelResultsList.add(userResult);
      levelResultsList.sort(BY_RESULT_AND_USER);
      limitResultsIfNeeded(levelResultsList, DEFAULT_MAX_RESULTS_PER_LEVEL);
    }
  }

  public List<UserResult> getTopResultsByUser(long user_id) {
    List<UserResult> userResults = resultsByUser.get(user_id);
    if (userResults == null) {
      return Collections.emptyList();
    }
    synchronized (userResults) {
      return userResults.stream()
          .limit(DEFAULT_TOP_SIZE)
          .collect(Collectors.toList());
    }
  }

  public List<UserResult> getTopResultsByLevel(long level_id) {
    List<UserResult> levelResults = resultsByLevel.get(level_id);
    if (levelResults == null) {
      return Collections.emptyList();
    }
    synchronized (levelResults) {
      return levelResults.stream()
          .limit(DEFAULT_TOP_SIZE)
          .collect(Collectors.toList());
    }
  }

}