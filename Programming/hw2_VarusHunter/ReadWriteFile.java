package hw2_VarusHunter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.lang.String;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReadWriteFile
{
  String filename;

  public String read(){
    // read max_bytes from file.
    return null;
  }

  public JSONObject readJSON(){
    JSONParser parser = new JSONParser();
    JSONObject json_obj = null;
    try{
      json_obj = (JSONObject) parser.parse(new FileReader(this.filename));
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

  public boolean write(String content){
    // write str to file.
    // Args: 
    //   content: the content to export
    // Returns:
    //   True if write to file, False if exception.
    try {
      FileWriter fw = new FileWriter(this.filename);
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
      content = (new Scanner(new File(this.filename))
                 .useDelimiter("\\Z").next());
    } catch (IOException e){
      e.printStackTrace();
      return null;
    }
    return content;
  }

  public void close(){
    // close file.
  }

  public void __init__(String filename){
    this.filename = filename;
  }

  ReadWriteFile(String filename){
    this.__init__(filename);
  }
}

