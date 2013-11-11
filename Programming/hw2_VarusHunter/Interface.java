package hw2_VarusHunter;

import java.lang.String;
import java.util.concurrent.Callable;
import java.util.Map;
import java.util.HashMap;
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


public class Interface{
  public Map<Integer, Option> options; 
  public String databaseDir;
  public VirusDatabasei database;

  public void addOption(Option o){
    this.options.put(o.key, o);
  }
  
  public void printInterface(){
    for(int i : this.options.keySet()){
      String str = i + " : " + this.options.get(i).notice;
      System.out.println(str);
    }
    System.out.print("Please make your selection: ");
  }

  public int waitForInt(){
    return new Scanner(System.in).nextInt();
  }

  public String waitForString(){
    return new Scanner(System.in).nextLine();
  }

  public String waitUntilRequire(String[] require, String notice){
    Scanner scan = new Scanner(System.in);
    do{
      String input = scan.nextLine();
      for(String s : require){
        if(s == input) return input;
      }
      System.out.println("Invalid Choice");
      System.out.print(notice);
    }while(true);
  }

  public void save(){
    /* save the database */
    System.out.println("Please type in a file name to store database " +
                       "(will generate a random file name if blank) " +
                       "To return without save: type '-': ");
    String filename = this.waitForString();
    String fileRet = null;
    if(filename == '-') return;

    fileRet = this.database.saveDatabase(this.databaseDir, filename);
    
    if(!fileRet){
      System.out.println("Error in save database");
    } else {
      System.out.println("database saved to " + fileRet);
    }
  }

  public void load(){
    /* load a database from file */
    String notice = "This will clear the current database, " +
                    "are you sure to go on? (y/n): ";
    System.out.print(notice);
    String str = this.waitUntilRequire(["y","n"], notice);
    if(str == "n") return;
    if(str == "y"){
      System.out.println("Please type in a database file name to load, " +
                         "To return: blank");
      String filename = waitForString();
      if(filename.isEmpty()) return;
      String ret = this.database.loadDatabase(this.databaseDir, filename);
      if(!ret){
        System.out.println("Error in load database");
      } else{
        System.out.println("database loaded successfully from: " + ret);
      }
    }
  }

  public void changeDatabaseDir(){
    do{
      System.out.println("Please type in a database dir, " +
                         "To return: blank");
      String dirname = waitForString();
      if(dirname.isEmpty()) return;
      if(!new ReadWriteFile(dirname).is_dir()){
        System.out.println("The directory you typed in is not correct!!!");
        continue;
      } else break;
    } while (true);
    this.databaseDir = dirname;
    System.out.println("Successfully updated database directory to: " + 
                       this.databaseDir);
  }

  public void quit(){
    String notice = "Do you want to save your database before leave? (y/n): ";
    System.out.print(notice);
    String str = this.waitUntilRequire(["y","n"], notice);
    if(str == "y") this.save();
    this.clear();
    System.out.println("Quit the program.....Goodbye");
  }

  public void clear(){
    /* clear database */
    this.database.clear();
    System.out.println("Database cleared.");
  }

  public void add_benigh_files(){}

  public void add_virus_files(){}

  public void predict(){
    /* calculate probability of an unknown file */
    System.out.println("Please type in the file name to predict. " + 
                       "To return: blank");
    String filename = this.waitForString();
    if(filename.isEmpty()) return;
    double[] prob_virus = new double[1];
    boolean isVirus = false;
    try{
      isVirus = this.database.isVirus(filename, prob_virus);
    } catch (Exception e){
       
    }
    
  }

  public void info(){
    /* Print information about myself */
    System.out.println("Name: " + Flags.myname);
    System.out.println("UNI:  " + Flags.UIN);
    System.out.println("Byte length: " + this.database.key_len);
    System.out.println("Number of benigh files added: " + 
                       this.database.num_benigh_files);
    System.out.println("Number of virus files added: " +
                       this.database.num_virus_files);
  }

  public void setup() throws Exception {
    int key = 0;
    this.addOption(new Option(key++, "Quit",
                              Interface.class.getMethod("quit")));
    this.addOption(new Option(key++, "Change database directory",
                              Interface.class.getMethod("changeDatabaseDir")));
    this.addOption(new Option(key++, "load a database file",
                              Interface.class.getMethod("load")));
    this.addOption(new Option(key++, "save the current database",
                              Interface.class.getMethod("save")));
    this.addOption(new Option(key++, "clear the current database",
                              Interface.class.getMethod("clear")));
    this.addOption(new Option(key++, "add benigh programs",
                              Interface.class.getMethod("add_benigh_files")));
    this.addOption(new Option(key++, "add virus programs",
                              Interface.class.getMethod("add_virus_files")));
    this.addOption(new Option(key++, "print information",
                              Interface.class.getMethod("info")));
    this.addOption(new Option(key++, "predict an unknown program",
                              Interface.class.getMethod("predict")));
  }

  public void processKey(int key){
    if(!this.options.containsKey(key)){
      System.out.println("Invalid Selection");
      return;
    }
    try{
      this.options.get(key).invoke(this);
    } catch (Exception e){
      System.out.println("Exception in: " + this.options.get(key).notice);
      e.printStackTrace();
    }
  }

  public void __init__(){
    try{
      this.databaseDir = "./";
      this.options = new HashMap<Integer, Option>();
      this.database = new VirusDatabase();

      this.setup();
      int key = 0;
      do{
        this.printInterface();
        key = this.waitForInt();
        this.processKey(key);
      } while(key != 0);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  Interface(){
    this.__init__();
  }
  
  public static void main(String [] args){
    new Interface();
	}
}
