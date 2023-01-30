package com.example.englishlearningwebsite.entities.enums;

public enum Level {
  BEGINNER(0), LOW(550), MEDIUM(650), HIGH(750), ADVANCE(850);
  private final int value;

  Level(int value) {
    this.value = value;
  }

  public int value(){
    return value;
  }
}
