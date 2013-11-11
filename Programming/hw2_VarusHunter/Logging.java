package hw2_VarusHunter;

import java.lang.String;


public class Logging{
  public static void print(String str){
    System.out.println(str);
  }

  public static void error(String str){
    print("ERROR: " + str);
  }

  public static void info(String str){
    print("INFO: " + str);
  }

  public static void warn(String str){
    print("WARN: " + str);
  }
}
