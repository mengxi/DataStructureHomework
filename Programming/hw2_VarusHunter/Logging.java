package hw2_VarusHunter;

import java.lang.String;


public class Logging{
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  public static void print(String str){
    System.out.println(str);
  }

  public static void error(String str){
    print(ANSI_RED + "ERROR: " + str + ANSI_WHITE);
  }

  public static void info(String str){
    print(ANSI_BLUE + "INFO: " + str + ANSI_WHITE);
  }

  public static void warn(String str){
    print(ANSI_YELLOW + "WARN: " + str + ANSI_WHITE);
  }

  public static void stdout(String str){
    print(str);
  }
}
