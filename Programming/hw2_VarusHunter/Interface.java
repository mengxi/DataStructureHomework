package VirusHunter;

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
  private Map<Integer, Option> options; 
  public VirusDatabase database;

  public void addOption(Option o){
    this.options.put(o.key, o);
  }
  
  public void printInterface(){
    for(int i : this.options.keySet()){
      String str = i + " : " + this.options.get(i).notice;
      Logging.stdout(str);
    }
    System.out.print("Please make your selection: ");
  }
  
  public int waitForIntInScale(int min, int max){
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
        if(s.equals(input)) return input;
      }
      Logging.warn("Invalid Choice:" + input);
      System.out.print(notice);
    }while(true);
  }

  public void save(){
    /* save the database */
    Logging.stdout("Please type in a file name to store database \n" +
                   "will generate a random file name if blank\n " +
                   "To return without save anything: type '-': ");
    String filename = this.waitForString();
    String fileRet = null;
    if(filename.equals("-")) return;

    fileRet = this.database.saveDatabase(filename);
    
    if(fileRet==null || fileRet.isEmpty()){
      Logging.warn("Error in save database");
    } else {
      Logging.info("database saved to " + fileRet);
    }
  }

  public void load(){
    /* load a database from file */
    String notice = "This will clear the current database, " +
                    "are you sure to go on? (y/n): ";
    System.out.print(notice);
    String[] choices = {"y", "n"};
    String str = this.waitUntilRequire(choices, notice);
    if(str.equals("n")) return;
    if(str.equals("y")){
      Logging.stdout("Please type in a database file name to load, " +
                     "To return: blank");
      String filename = waitForString();
      if(filename.isEmpty()) return;
      String ret = this.database.loadDatabase(filename);
      if(ret == null || ret.isEmpty()){
        Logging.warn("Error in load database");
      } else{
        Logging.info("database loaded successfully from: " + ret);
      }
    }
  }

  public void showDatabaseDir(){
    Logging.info("Current database dir is \n" + 
                 this.database.databaseDir);
  }

  public void changeDatabaseDir(){
    /* Read a dir input by user and updated it to this.database. */
    String dirname;
    do{
      Logging.stdout("Please type in a database dir, " +
                     "To return: blank");
      dirname = this.waitForString();
      if(dirname.isEmpty()) return;
      if(!new ReadWriteFile(dirname).is_dir()){
        Logging.warn("The directory you typed in is not correct!!!");
        continue;
      } else break;
    } while (true);
    String path = new ReadWriteFile(dirname).getAbsoluteDirectoryPath();
    this.database.changeDatabaseDir(path);
    Logging.info("Successfully updated database directory to: " + 
                   this.database.databaseDir);
  }

  public void quit(){
    String notice = "Do you want to save your database before leave? (y/n): ";
    System.out.print(notice);
    String[] choices = {"y", "n"};
    String str = this.waitUntilRequire(choices, notice);
    if(str.equals("y")) this.save();
    this.clear();
    Logging.stdout("Quit the program.....Goodbye");
  }

  public void clear(){
    /* clear database */
    this.database.clear();
    Logging.info("Database cleared.");
  }

  public void key_change(){
    /* change the default key length */
    String notice = "This will clear your current database. Do you want to " +
                    "save your database before continue? (y/n): ";
    Logging.stdout(notice);
    String[] choices = {"y", "n"};
    String str = this.waitUntilRequire(choices, notice);
    if(str.equals("y")) this.save();
    this.clear();
    Logging.stdout("Please input a new key length in ["+Flags.min_key+
                   ", "+Flags.max_key+"]: ");
    int new_key = this.waitForIntInScale(Flags.min_key, Flags.max_key);
    this.database = new VirusDatabase(new_key);
    Logging.info("The key length of the new database is " + 
                   this.database.key_len);
  }

  private void add_files(String file_database){
    /* Wildcard matching: 
     * http://plexus.codehaus.org/plexus-utils/apidocs/org/codehaus/plexus/util/DirectoryScanner.html*/
    Logging.stdout("Please type in a list of " + file_database + " files." +
                   "Separated by space.\n " + 
                   "Use wildcard expression as that in Linux bash to " + 
                   "type in multiple files at one time\n" +
                   "E.X. benigh/* means all files under directory ./benigh\n" + 
                   "To return: type blank");
    String input = this.waitForString();
    if(input.isEmpty()) return;
    String[] files = new ReadWriteFile().parseStringToFileArray(input);
    if(files==null || files.length==0){
      Logging.warn("Do not discover any file match your input.");
      return;
    }
    for(String filename : files){
      if(file_database.equals("benigh"))
        this.database.addBenighFile(filename);
      else if(file_database.equals("virus"))
        this.database.addVirusFile(filename);
    }
  }

  public void add_benigh_files(){
    this.add_files("benigh");
  }

  public void add_virus_files(){
    this.add_files("virus");
  }

  public void predict(){
    /* calculate probability of an unknown file */
    Logging.stdout("Please type in the file name to predict. " +
                   "To return: blank");
    String filename = this.waitForString();
    if(filename.isEmpty()) return;
    double virusProb = 0.0;
    try{
      virusProb = this.database.virusProb(filename);
    } catch (Exception e){
      Logging.warn("Error in predict: " + e);
      e.printStackTrace();
      return;
    }
    Logging.info("The probability of " + filename + " to be\n" +
                   "  malicious: " + virusProb + "\n" +
                   "  benigh: " + (1.0-virusProb));
  }

  public void info(){
    /* Print information about myself */
    Logging.info("Information about myself: ");
    Logging.stdout("Name: " + Flags.myname);
    Logging.stdout("UNI:  " + Flags.UIN);
    Logging.stdout("Byte length: " + this.database.key_len);
    Logging.stdout("Number of benigh files added: " +
                   this.database.num_benigh_files);
    Logging.stdout("Number of virus files added: " +
                   this.database.num_virus_files);
  }

  public void setup() throws Exception {
    int key = 0;
    this.addOption(new Option(key++, "Quit",
                              Interface.class.getMethod("quit")));
    this.addOption(new Option(key++, "Change database directory",
                              Interface.class.getMethod("changeDatabaseDir")));
    this.addOption(new Option(key++, "Show current database directory",
                              Interface.class.getMethod("showDatabaseDir")));
    this.addOption(new Option(key++, "load a database file",
                              Interface.class.getMethod("load")));
    this.addOption(new Option(key++, "save the current database",
                              Interface.class.getMethod("save")));
    this.addOption(new Option(key++, "clear the current database",
                              Interface.class.getMethod("clear")));
    this.addOption(new Option(key++, "change the default key length",
                              Interface.class.getMethod("key_change")));
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

  public void __init__(){
    try{
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
