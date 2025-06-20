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
  private static final long DEFAULT_MAX_RESULTS_PER_USER = -1; // -1 means unlimited
  private static final long DEFAULT_MAX_RESULTS_PER_LEVEL = -1; // -1 means unlimited

  private static final Comparator<UserResult> BY_RESULT_AND_LEVEL =
      Comparator.comparingInt(UserResult::getResult).reversed()
          .thenComparing(Comparator.comparingLong(UserResult::getLevel_id).reversed());

  private static final Comparator<UserResult> BY_RESULT_AND_USER =
      Comparator.comparingInt(UserResult::getResult).reversed()
          .thenComparing(Comparator.comparingLong(UserResult::getUser_id).reversed());

  private final Map<Long, List<UserResult>> resultsByUser = new ConcurrentHashMap<>();
  private final Map<Long, List<UserResult>> resultsByLevel = new ConcurrentHashMap<>();

  //Insert: O(log n) to find position + O(n) to insert = O(n)
  private void insertSorted(List<UserResult> list, UserResult userResult, Comparator<UserResult> comparator) {
    int idx = Collections.binarySearch(list, userResult, comparator);
    if (idx < 0) {
      idx = -idx - 1;
    }
    list.add(idx, userResult);
  }

  private void limitResultsIfNeeded(List<UserResult> list, long maxSize) {
    if (maxSize > 0 && list.size() > maxSize) {
      list.subList((int) maxSize, list.size()).clear();
    }
  }

  public synchronized void setResult(UserResult userResult) {
    resultsByLevel.computeIfAbsent(
        userResult.getLevel_id(),
        k -> Collections.synchronizedList(new ArrayList<>())
    ).add(userResult);

    resultsByUser.computeIfAbsent(
        userResult.getUser_id(),
        k -> new ArrayList<>()
    );
    List<UserResult> userResultsList = resultsByUser.get(userResult.getUser_id());
    insertSorted(userResultsList, userResult, BY_RESULT_AND_LEVEL);
    
    limitResultsIfNeeded(userResultsList, DEFAULT_MAX_RESULTS_PER_USER);
    
    List<UserResult> levelResultsList = resultsByLevel.get(userResult.getLevel_id());
    levelResultsList.sort(BY_RESULT_AND_USER);
    limitResultsIfNeeded(levelResultsList, DEFAULT_MAX_RESULTS_PER_LEVEL);
  }

  //Read: O(1) to get sorted list + O(k) to limit sorted list [20 items -> O(1)]
  public List<UserResult> getTopResultsByUser(long user_id) {
    List<UserResult> userResults = resultsByUser.getOrDefault(user_id, Collections.emptyList());
    return userResults.stream()
        .limit(DEFAULT_TOP_SIZE)
        .collect(Collectors.toList());
  }

  //Read: O(n log n)
  public List<UserResult> getTopResultsByLevel(long level_id) {
    List<UserResult> levelResults = resultsByLevel.getOrDefault(level_id, Collections.emptyList());
    return levelResults.stream()
        .sorted(BY_RESULT_AND_USER)
        .limit(DEFAULT_TOP_SIZE)
        .collect(Collectors.toList());
  }

}