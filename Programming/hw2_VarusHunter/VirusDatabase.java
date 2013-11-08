/* Provides the hash table storage and file IO for the dictionary directory
 *
 * Time: 2013/11/2
 * Author: Mengxi Li   UNI: ML3577
 * */
package hw2_VarusHunter

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// The dictionary storing # of bytes seen in a type of programs
class BytesDistProgram
{
  private Map<String, Integer> dist;
 
  public Map<String, Integer> getData(){
    // fetch the data 
    return dist;
  }

  /* add counts of observation of key*/
  public void add(String key){
    int pre_count = this.count(key);
    dist.put(key, pre_count+1);
  }

  /* retrive the counts of observation of key*/
  public int count(String key){
    return dist.containsKey(key) ? dist.get(key) : 0;
  }

  /* clear the map*/
  public void clear(){
    dist.clear();
  }

  private void __init__(){
    this.dist = new HashMap<String, Integer>();
  }

  BytesDistProgram(){
    this.__init__();
  }
}


public class VirusDatabase
{
  BytesDistProgram benigh;
  BytesDistProgram virus;
  int key_len; // length of keys

  public void AddBenighStr(String str){
    /*Add a string to benigh*/
    assert str.length() == this.key_len;
    this.benigh.add(str);
  }

  public void AddVirusStr(String str){
    /*Add a string to virus*/
    assert str.length() == this.key_len;
    this.virus.add(str);
  }

  public void _AddFile(String file, BytesDistProgram data){
    /*Parse all key_len bytes in file to benigh*/
    ReadWriteFile input = new ReadWriteFile(file);
    String str = '';
    do{
      String read_str = input.read();
      str = this._substring(str, - this.key_len + 1) + 
            read_str;
      for(int i = 0; i <= str.length() - this.key_len; i++){
        data.add(str.substring(i, i+this.key_len));
      }
    }while(read_str.length() > 0);
    input.close();
  }

  public void addBenighFile(String file){
    this._AddFile(file, this.benigh);
  }

  public void addVirusFile(String file){
    this._AddFile(file, this.virus);
  }
  
  public String saveDatabase(String file=null){
    // save database to file
    // Args:
    //   file: write file name, select a random one if null
    // Return:
    //   Saved file name
    //   http://www.mkyong.com/java/json-simple-example-read-and-write-json/

    JSONObject obj = new JSONObject();
	obj.put("name", "MengXi");
 
	JSONArray benigh_obj = new JSONObject();
    for(Map.Entry<String, Integer> entry : this.benigh.getData()){
      benigh_obj.put(entry.getKey(), entry.getValue());
    }
	obj.put("benigh", benigh_obj);

    JSONArray virus_obj = new JSONObject();
    for(Map.Entry<String, Integer> entry : this.virus.getData()){
      virus_obj.put(entry.getKey(), entry.getValue());
    }
	obj.put("virus", virus_obj);
    
    if(!file) file = this._randomFileName();

	try {
	  FileWriter fw = new FileWriter(file);
	  fw.write(obj.toJSONString());
	  fw.flush();
	  fw.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	//  System.out.print(obj);
    return file;
  }

  private _randomFileName(){
    // return a random file name for database storage.
  }

  public boolean loadDatabase(String file){
    // Read database from file.
    // Args:
    //   file: name of file storing database
    // Return:
    //   True if success, false otherwise
  }

  public boolean isVirus(String prog){
    // decide whether a prog file is virus.
    // Args:
    //   prog: name of the program file
    // Raises:
    //   Error if file cannot load
    // Return:
    //   True if it is virus, False otherwise
    
  }

  private String _substring(String str, int len){
    String ret = '';
    int new_len = len >= 0 ? len : str.length() + len;
    if(new_len < 0) new_len = 0;
    if(new_len > str.length()) new_len = str.length();
    return str.substring(new_len);
  }

  private void __init__(int key_len){
    this.benigh = new BytesDistProgram();
    this.virus = new BytesDistProgram();
    this.key_len = key_len;
  }

  VirusFile(int key_len){
    this.__init__(key_len);
  }
}
