package hw3_graph;

import java.lang.String;
import java.util.InputMismatchException;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.lang.reflect.Method;


class Option{
  public int key;
  public String notice;
  public Method func_noparam;
  Option(int key, String str, Method func_noparam){
    this.key = key;
    this.notice = str;
    this.func_noparam = func_noparam;
  }
  public void invoke(Object obj) throws Exception {
    this.func_noparam.invoke(obj);    
  }
}

abstract class Options{
  public Map<Integer, Option> options = new HashMap<Integer, Option>();

  public void addOption(Option o){
    this.options.put(o.key, o);
  }
  public String getOptionNotice(){
    /* Return the notice of by the list of options. */
    String ret = "";
    for(int i : this.options.keySet()){
      String str = i + " : " + this.options.get(i).notice;
      ret += str + "\n";
    }
    assert ret != "";
    return ret;
  }

  public Set<Integer> getKeys(){
    return this.options.keySet();
  }
}


abstract class CmdStart extends Options{
  
  private boolean is_running = false;

  public void setOptions() throws NoSuchMethodException{
    int key = 0;
    this.addOption(new Option(key++, "Quit",
                              this.getClass().getMethod("quit")));
    this.addOption(new Option(key++, "load a city file",
                              this.getClass().getMethod("loadCityFile")));
    this.addOption(new Option(key++, "Search for state and list all cities",
                              this.getClass().getMethod("searchState")));
    this.addOption(new Option(key++, "Search for a city",
                              this.getClass().getMethod("searchCity")));
    this.addOption(new Option(key++, "Set current city as the starting point",
                              this.getClass().getMethod("setCurrentCity")));
    this.addOption(new Option(key++, "Show current city",
                              this.getClass().getMethod("showCurrentCity")));
    this.addOption(new Option(key++, "Find n cloest cities using gps distance",
                              this.getClass().getMethod("gpsNeighbor")));
    this.addOption(new Option(key++, "Find n cloest cities using edge costs",
                              this.getClass().getMethod("costNeighbor")));
    this.addOption(new Option(key++, "Find shortest path between two cities",
                              this.getClass().getMethod("findPath")));
  }

  public void quit(){
    this.is_running = false;
  }

  public void loadCityFile(){}

  public void searchState(){}

  public void searchCity(){}

  public void setCurrentCity(){}

  public void showCurrentCity(){}

  public void gpsNeighbor(){}

  public void costNeighbor(){}

  public void findPath(){}

  public void invoke(int key){
    if(!this.options.containsKey(key)){
      Logging.warn("Invalid Selection");
      return;
    }
    try{
      this.options.get(key).invoke(this);
    } catch (Exception e){
      Logging.warn("Exception in: " + this.options.get(key).notice);
      e.printStackTrace();
    }
  }

  public void main_loop(){
    this.is_running = true;
    while(this.is_running){
      String notice = this.getOptionNotice() + 
                      "Please make your selection: ";
      Logging.stdout(notice);
      int key = Interface_Util.waitForInt(this.getKeys(), notice);
      this.invoke(key);
    }
  }

  public void start(){
    try{
      this.setOptions();
    }catch(NoSuchMethodException e){
      Logging.error(e.toString());
      e.printStackTrace();
      System.exit(1);
    }
    this.main_loop();
  }
}


class Interface_Util{
  
  public static int waitForIntInScale(int min, int max){
    assert min <= max;
    int key = 0;
    Scanner scan = new Scanner(System.in);
    do{
      key = scan.nextInt();
      if(key < min || key > max){
        Logging.warn("Please input an integer in [" + min + ", " + max + "]");
      }
    }while(key < min || key > max);
    return key;
  }

  public static int waitForInt(){
    return new Scanner(System.in).nextInt();
  }

  public static int waitForInt(Set<Integer> ints, String notice){
    Scanner scan = new Scanner(System.in);
    int input = -1;
    do{
      try{
        input = scan.nextInt();
        if(ints.contains(input)) return input;
        Logging.warn("Invalid Choice:" + input);
      }catch(InputMismatchException e){
        Logging.warn("Please type in an integer." + input + e.toString());
        System.exit(1);
      } 
      if(notice != null)
        Logging.stdout(notice);
    }while(true); 
  }

  public static String waitForString(){
    return new Scanner(System.in).nextLine();
  }

  public static String waitUntilString(Set<String> strs, String notice){
    Scanner scan = new Scanner(System.in);
    do{
      String input = scan.nextLine();
      if(strs.contains(input))
        return input;
      Logging.warn("Invalid Choice:" + input);
      Logging.stdout(notice);
    }while(true);
  }
}


public class CmdInterface extends CmdStart{
  public static void main(String [] args){
    new CmdInterface().start();
  }
}


