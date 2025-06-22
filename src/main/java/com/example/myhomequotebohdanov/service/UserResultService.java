package com.example.myhomequotebohdanov.service;

import com.example.myhomequotebohdanov.model.UserResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class UserResultService {

  private static final int MAX_RESULTS_SIZE = 20;

  private static final Comparator<UserResult> BY_RESULT_AND_LEVEL =
      Comparator.comparingInt(UserResult::getResult).reversed()
          .thenComparing(Comparator.comparingLong(UserResult::getLevel_id).reversed());

  private static final Comparator<UserResult> BY_RESULT_AND_USER =
      Comparator.comparingInt(UserResult::getResult).reversed()
          .thenComparing(Comparator.comparingLong(UserResult::getUser_id).reversed());

  private final Map<Long, List<UserResult>> resultsByUser = new ConcurrentHashMap<>();
  private final Map<Long, List<UserResult>> resultsByLevel = new ConcurrentHashMap<>();

  public void setResult(UserResult userResult) {

    if (userResult == null) {
      throw new IllegalArgumentException("UserResult cannot be null");
    }

    long userId = userResult.getUser_id();
    long levelId = userResult.getLevel_id();

    if (userId <= 0) {
      throw new IllegalArgumentException("User ID must be positive, got: " + userId);
    }
    if (levelId <= 0) {
      throw new IllegalArgumentException("Level ID must be positive, got: " + levelId);
    }

    //виконуємо операції паралельно з окремими блокуваннями
    updateUserResultsWithLock(userId, userResult);
    updateLevelResultsWithLock(levelId, userResult);
  }

  private void updateUserResultsWithLock(long userId, UserResult userResult) {
    //computeIfAbsent - потокобезпечний атомарний метод у ConcurrentHashMap
    List<UserResult> userResults = resultsByUser.computeIfAbsent(userId,
        k -> new ArrayList<>());

    //Compound опреація: додавання нового елемента, сортування, обрізки до 20
    //виконується неподільно, без можливості вклинитися іншим потокам
    synchronized (userResults) {  //лочимо тільки список який оновлюємо, а не всю мапу
      userResults.add(userResult);
      userResults.sort(BY_RESULT_AND_LEVEL);
      if (userResults.size() > MAX_RESULTS_SIZE) {
        //відсікаємо значення після 20 саме в існуючому лісті, а не створюємо новий
        userResults.subList(MAX_RESULTS_SIZE, userResults.size()).clear();
      }
    }
  }

  private void updateLevelResultsWithLock(long levelId, UserResult userResult) {
    //computeIfAbsent - потокобезпечний атомарний метод у ConcurrentHashMap
    List<UserResult> levelResults = resultsByLevel.computeIfAbsent(levelId,
        k -> new ArrayList<>());

    //Compound опреація: додавання нового елемента, сортування, обрізки до 20
    //виконується неподільно, без можливості вклинитися іншим потокам
    synchronized (levelResults) {  //лочимо тільки список який оновлюємо, а не всю мапу
      levelResults.add(userResult);
      levelResults.sort(BY_RESULT_AND_USER);
      if (levelResults.size() > MAX_RESULTS_SIZE) {
        //відсікаємо значення після 20 саме в існуючому лісті, а не створюємо новий
        levelResults.subList(MAX_RESULTS_SIZE, levelResults.size()).clear();
      }
    }
  }

  public List<UserResult> getTopResultsByUser(long user_id) {

    if (user_id < 0) {
      throw new IllegalArgumentException("User ID must be positive, got: " + user_id);
    }

    List<UserResult> userResults = resultsByUser.get(user_id);
    if (userResults == null) {
      return Collections.emptyList();
    }
    //синхронізуємося по лісту топ-результатів юзера задля консистентності данних
    synchronized (userResults) {  //операції паралельного читання блокуються, але вважаю це ефективніше і прозоріше за використання CopyOnWriteArrayList
      return new ArrayList<>(userResults); //повертаємо копію замість посилання, щоб інший поток міг паралельно змінювати оригінальну колекцію
    }
  }

  public List<UserResult> getTopResultsByLevel(long level_id) {

    if (level_id < 0) {
      throw new IllegalArgumentException("Level ID must be positive, got: " + level_id);
    }

    List<UserResult> levelResults = resultsByLevel.get(level_id);
    if (levelResults == null) {
      return Collections.emptyList();
    }
    //синхронізуємося по лісту топ-результатів левела задля консистентності данних
    synchronized (levelResults) { //операції паралельного читання блокуються, але вважаю це ефективніше і прозоріше за використання CopyOnWriteArrayList
      return new ArrayList<>(levelResults); //повертаємо копію замість посилання, щоб інший поток міг паралельно змінювати оригінальну колекцію
    }
  }

}