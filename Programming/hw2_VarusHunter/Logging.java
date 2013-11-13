package hw2_VarusHunter;

import java.lang.String;


public class Logging{
  public static void print(String str){
    System.out.println(str);
  }

  public static void error(String str){
    this.print("ERROR: " + str);
  }

  public static void info(String str){
    this.print("INFO: " + str);
  }

  public static void warn(String str){
    this.print("WARN: " + str);
  }

  public static void stdout(String str){
    this.print(str);
  }
}
