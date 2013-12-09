package hw3_graph;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.lang.String;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.codehaus.plexus.util.DirectoryScanner;

public class File_util
{
  String location = null;
  private BufferedReader reader = null;

  void openToRead(){
    try{
      this.reader = new BufferedReader(new FileReader(this.location));
    }catch(IOException e){
      e.printStackTrace();
      System.exit(1);
    }
  }

  void close(){
    try{
      this.reader.close();
    }catch (IOException e){
      e.printStackTrace();
    }
  }

  String readLine(){
    /* Return a line from reader */
    try{
      String line = this.reader.readLine();
      return line;
    } catch (IOException e){
      Logging.error("File " + this.location + " read error! " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  public String read(){
    // read max_bytes from file.
    return null;
  }

  public JSONObject readJSON(){
    JSONParser parser = new JSONParser();
    JSONObject json_obj = null;
    try{
      json_obj = (JSONObject) parser.parse(new FileReader(this.location));
    } catch (FileNotFoundException e){
      e.printStackTrace();
      json_obj = null;
    } catch (IOException e){
      e.printStackTrace();
      json_obj = null;
    } catch (ParseException e){
      e.printStackTrace();
      json_obj = null;
    }
    return json_obj;
 }

 public String getAbsoluteDirectoryPath(){
   /* return the absolute path of a dirctory */
  String path = new File(this.location).getAbsolutePath();
  if(path.charAt(path.length()-1) != '/') path = path + "/";
  return path;
 }

 public boolean is_dir(){
   /* return true if dir is a valid directory */
   File f = new File(this.location);
   if(!f.exists()) return false;
   else if(!f.isDirectory()) return false;
   return true;
 }

 public boolean fileCanRead(File file){
   if (!file.exists()) 
     return false;
   if (!file.canRead())
     return false;
   try {
     FileReader fileReader = new FileReader(file.getAbsolutePath());
     fileReader.read();
     fileReader.close();
   } catch (Exception e) {
     Logging.warn("Exception when checked file can read with message:"+
                  e.getMessage());
     return false;
   }
   return true;
 }

 public boolean fileCanRead(){
   File file = new File(this.location);
   return this.fileCanRead(file);
 }

 public String getAbsolutePath(){
   return new File(this.location).getAbsolutePath();
 }

 public boolean writeJsonToFile(JSONObject obj){
   // Returns:
   //   True if write is successful, False otherwise
   try {
     FileWriter fw = new FileWriter(this.location);
     obj.writeJSONString(fw);
     fw.flush();
     fw.close();
   } catch (IOException e) {
     e.printStackTrace();
     return false;
   }
   return true;
 }

 public boolean write(String content){
    // write str to file.
    // Args: 
    //   content: the content to export
    // Returns:
    //   True if write to file, False if exception.
    try {
      FileWriter fw = new FileWriter(this.location);
      fw.write(content);
      fw.flush();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public String toString(){
    // Read all file to string
    // Returns:
    //   All contents in file as a string
    //   null if exception is raised.
    String content;
    try{
      content = (new Scanner(new File(this.location))
                 .useDelimiter("\\Z").next());
    } catch (IOException e){
      Logging.error("Read file: " + this.location);
      e.printStackTrace();
      return null;
    }
    return content;
  }

  public String[] parseStringToFileArray(String fileFilters){
    /* Parse a file filter list separated by space to a list of valid file names,
     * and remove repeated files input.
     * Wildcard matching is used: 
     * http://plexus.codehaus.org/plexus-utils/apidocs/org/codehaus/plexus/util/DirectoryScanner.html*/
    String[] includes = fileFilters.split(" ");
    if(includes==null || includes.length == 0) return null;
    DirectoryScanner scanner = new DirectoryScanner();
    try{
      scanner.setBasedir("./");
      scanner.setCaseSensitive(true);
      scanner.setIncludes(includes);
      scanner.scan();
      return scanner.getIncludedFiles();
    } catch (Exception e){
      Logging.error("parseStringFilterError");
      e.printStackTrace();
      return null;
    }
  }
  
  public void __init__(String location){
    this.location = location;
  }

  File_util(String location){
    this.__init__(location);
  }

  File_util(){
    this.__init__(null);
  }
}

