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
  private CityBrain cities = new CityBrain();

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

  public void loadCityFile(){
    String notice = "Please input a file name containing the list of cities. " +
                    "To return: type blank.";
    Logging.stdout(notice);
    String filename = Interface_Util.waitForValidFileName();
    if(filename != null)
      this.cities.loadCityFile(filename);
  }

  public void searchState(){
    String notice = "Please input a valid state name. To return: type blank.";
    Logging.stdout(notice);
    String statename = null;
    do{
      statename = Interface_Util.waitForString();
      if(statename == "") return; 
      if(this.cities.stateExists(statename))
        break;
      Logging.warn("State not found. Please input a valid state name." + 
                   "To return: type blank.");
    }while(true);

    assert statename != null;
    this.cities.showStateInfo(statename);
  }

  public void searchCity(){
    String notice = "Please input a valid city name. To return: type blank.";
    Logging.stdout(notice);
    String cityname = null;
    do{
      cityname = Interface_Util.waitForString();
      if(cityname == "") return; 
      if(this.cities.cityExists(cityname))
        break;
      Logging.warn("City not found. Please input a valid city name." + 
                   "To return: type blank.");
    }while(true);

    assert cityname != null;
    this.cities.showCityInfo(statename);
  }

  public void setCurrentCity(){
    String notice = "Please input a valid city id. To return: type blank.";
    Logging.stdout(notice);
    Long cityid = null;
    do{
      cityid = Interface_Util.waitForLong();
      if(cityid == null) return;
      if(this.cities.cityIdExists(cityid))
        break;
      Logging.warn("City not found. Please input a valid city id. " +
                   "To return: type blank.");
    }while(true);
    assert cityid != null;
    this.cities.setCurrentCity(cityid);
  }

  public void showCurrentCity(){
    this.cities.showCurrentCity();
  }

  public void gpsNeighbor(){
    String notice = "Please type in the number of cloest neighbors to see: ";
    Logging.stdout(notice);
    Integer input = Interface_Util.waitForInt();
    if(input != null && input > 0){
      this.cities.gpsNeighbor(input);
      return;
    }
    Logging.warn("Please type in a valid postive integer");
  }

  public void costNeighbor(){
    String notice = "Please type in the number of cloest neighbors to see: ";
    Logging.stdout(notice);
    Integer input = Interface_Util.waitForInt();
    if(input != null && input > 0){
      this.cities.costNeighbor(input);
      return;
    }
    Logging.warn("Please type in a valid postive integer");
  }

  public void findPath(){
    String notice = "Please type in the id of the destination city.";
    Logging.stdout(notice);
    Long cityid = null;
    do{
      cityid = Interface_Util.waitForLong();
      if(cityid == null) return;
      if(this.cities.cityIdExists(cityid))
        break;
      Logging.warn("City not found. Please input a valid city id. " +
                   "To return: type blank.");
    }while(true);
    this.cities.findPath(cityid);
  }

  public void invoke(Integer key){
    if(!this.options.containsKey(key)){
      Logging.warn("Invalid choice!!");
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
      Integer key = Interface_Util.waitForInt(this.getKeys());
      if(key != null)
        this.invoke(key);
      else{
        Logging.warn("Invalid choice!!");
      }
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

  public static String waitForValidFileName(){
    /*Wait for user to input a valid file name */
    Scanner scan = new Scanner(System.in);
    do{
      String name = Interface_Util.waitForString();
      if(name == "") return null;
      if(new File_util(name).fileCanRead()){
        return new File_util(name).getAbsolutePath();
      }
      Logging.warn("Please type in a file name that is readable. " + 
                   "To return: blank");
    }while(true);
  }
  
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

  public static Integer waitForInt(){
    try{
      int ret = new Scanner(System.in).nextInt();
      return ret;
    } catch (InputMismatchException e){
      Logging.warn("Please type in an integer." + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public static Long waitForLong(){
    try{
      long ret = new Scanner(System.in).nextLong();
      return ret;
    } catch (InputMismatchException e){
      Logging.warn("Please type in an integer." + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public static Integer waitForInt(Set<Integer> ints){
    /* Wait until user input an value in ints, null otherwise*/
    Integer input = this.waitForInt();
    if(input != null && ints.contains(input))
      return input;
    return null;
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


public class CmdInterface extends CmdStart{}


