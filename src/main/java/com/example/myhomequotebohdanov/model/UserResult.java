package com.example.myhomequotebohdanov.model;

public class UserResult {
  private long user_id;
  private long level_id;
  private int result;

  public UserResult() {
  }

  public UserResult(long user_id, long level_id, int result) {
    this.user_id = user_id;
    this.level_id = level_id;
    this.result = result;
  }

  public long getUser_id() {
    return user_id;
  }

  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }

  public long getLevel_id() {
    return level_id;
  }

  public void setLevel_id(long level_id) {
    this.level_id = level_id;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
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