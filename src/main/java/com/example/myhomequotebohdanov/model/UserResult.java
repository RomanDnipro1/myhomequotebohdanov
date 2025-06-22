package com.example.myhomequotebohdanov.model;

public class UserResult {
  private final long user_id;
  private final long level_id;
  private final int result;

  public UserResult(long user_id, long level_id, int result) {
    this.user_id = user_id;
    this.level_id = level_id;
    this.result = result;
  }

  public long getUser_id() {
    return user_id;
  }

  public long getLevel_id() {
    return level_id;
  }

  public int getResult() {
    return result;
  }

  @Override
  public String toString() {
    return "UserResult{" +
        "user_id=" + user_id +
        ", level_id=" + level_id +
        ", result=" + result +
        '}';
  }
}