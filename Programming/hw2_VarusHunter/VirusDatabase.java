/* Provides the hash table storage and file IO for the dictionary directory
 *
 * Time: 2013/11/2
 * Author: Mengxi Li   UNI: ML3577
 * */
package hw2_VarusHunter;

import java.lang.Math;
import java.lang.String;
import java.lang.Integer;
import java.util.*;
import java.io.IOException;
import org.json.simple.JSONObject;

// The dictionary storing # of bytes seen in a type of programs
class BytesDistProgram
{
  private Map<String, Integer> dist;
 
  public Map<String, Integer> getData(){
    // fetch the data 
    return this.dist;
  }

  /* add counts of observation of key*/
  public void add(String key){
    int pre_count = this.count(key);
    this.dist.put(key, pre_count+1);
  }
  
  public void put(String key, int count){
    /* set (key, count) to dist*/
    this.dist.put(key, count);
  }

  /* retrive the counts of observation of key*/
  public int count(String key){
    return this.dist.containsKey(key) ? this.dist.get(key) : 0;
  }

  public int sumValues(){
    /* Returns the sum of all values */
    int sum = 0;
    for(int v : dist.values()){
      sum += v;
    }
    return sum;
  }

  /* clear the map*/
  public void clear(){
    this.dist.clear();
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
  int num_benigh_files;
  int num_virus_files;
  String databaseDir;

  public void addBenighStr(String str){
    /*Add a string to benigh*/
    assert str.length() == this.key_len;
    Logging.info("Add file " + str + " to benigh database");
    this.benigh.add(str);
  }

  public void addVirusStr(String str){
    /*Add a string to virus*/
    assert str.length() == this.key_len;
    Logging.info("Add file " + str + " to virus database");
    this.virus.add(str);
  }

  public void _addFile(String file, BytesDistProgram data){
    /*Parse all key_len bytes in file to benigh*/
    String str = new ReadWriteFile(file).toString();
    assert str != null;
    for(int i = 0; i <= str.length() - this.key_len; i++){
      data.add(str.substring(i, i+this.key_len));
    }
  }

  public void addBenighFile(String file){
    this._addFile(file, this.benigh);
    ++ this.num_benigh_files;
  }

  public void addVirusFile(String file){
    this._addFile(file, this.virus);
    ++ this.num_virus_files;
  }
  
  public String saveDatabase(){
    return this.saveDatabase(null);
  }

  public String saveDatabase(String file){
    // save database to file
    // Args:
    //   file: write file name, select a random one if null
    // Return:
    //   Saved file name
    //   http://www.mkyong.com/java/json-simple-example-read-and-write-json/

    JSONObject obj = new JSONObject();
    obj.put("name", "MengXi");
    obj.put("num_benigh_files", this.num_benigh_files);
    obj.put("num_virus_files", this.num_virus_files);
    obj.put("key_len", this.key_len);
    JSONObject benigh_obj = new JSONObject();
    for(Map.Entry<String, Integer> entry : this.benigh.getData().entrySet()){
      benigh_obj.put(entry.getKey(), entry.getValue());
    }
    obj.put("benigh", benigh_obj);
    JSONObject virus_obj = new JSONObject();
    for(Map.Entry<String, Integer> entry : this.virus.getData().entrySet()){
      virus_obj.put(entry.getKey(), entry.getValue());
    }
    obj.put("virus", virus_obj);
    if(file==null || file.length()==0) file = this.databaseDir + this._randomFileName();
    else file = this.databaseDir + file;
    boolean write_success = 
            new ReadWriteFile(file).write(obj.toJSONString());
    assert write_success;
    if(write_success) return file;
    else return null;
  }

  private String _randomFileName(){
    // return a random file name for database storage.
    return Flags.UIN + '_' + this.num_benigh_files + '_'
           + this.num_virus_files + '_' + Flags.uuid();
  }

  private boolean _resetFile(Map<String, Integer> obj, BytesDistProgram data){ 
    /* Reset data as shown in obj 
     * Args: 
     *   obj: map<String, Integer> from a Json file
     *   data: the destination to write to 
     * Returns:
     *   true if success load. false if exception*/
    assert data != null;
    data.clear();
    for(String key : obj.keySet()){}
    // Iterator<String> keys = obj.keys();
    /*while(keys.hasNext()){
      String key = keys.next();
      try{
        data.put(key, obj.getInt(key));
      } catch (JSONException e) {
        e.printStackTrace();
        return false;
      }     
    }*/
    return true;
  }

  public String loadDatabase(String file){
    // Read database from file.
    // Args:
    //   file: name of file storing database
    // Return:
    //   Name of the file loaded if success, null otherwise
    this.clear();
    boolean load_success = true;
    file = this.databaseDir + file;
    JSONObject json_obj = new ReadWriteFile(file).readJSON();
    assert json_obj != null;
    String name = (String) json_obj.get("name");
    this.num_benigh_files = Integer.parseInt((String)json_obj.get("num_benigh_files"));
    this.num_virus_files = Integer.parseInt((String)json_obj.get("num_virus_files"));
    this.key_len = Integer.parseInt((String)json_obj.get("key_len"));
    load_success &= 
      this._resetFile((JSONObject)json_obj.get("benigh"),
                      this.benigh);
    load_success &= 
      this._resetFile((JSONObject)json_obj.get("virus"),
                      this.virus);
    if(load_success) return file;
    return null;
  }

  public void changeDatabaseDir(String dirName){
    this.databaseDir = dirName;
  }

  public double virusProb(String filename) throws Exception {
    // decide whether a prog file is virus.
    // Args:
    //   prog: name of the program file
    // Raises:
    //   Exception if file cannot load, and return -1.
    // Return:
    //   The probability of the filename being virus.
    assert this.num_benigh_files > 0;
    assert this.num_virus_files > 0;
    double log_is_benigh = Math.log(this.num_benigh_files);
    double log_is_virus = Math.log(this.num_virus_files);
    String str = new ReadWriteFile(filename).toString();
    if(str == null || str.isEmpty()) {
      throw new Exception("cannot open file " + filename);
    }
    for(int i = 0; i <= str.length() - this.key_len; i++){
      String bytes = str.substring(i, i + this.key_len);
      log_is_benigh += Math.log((1e-7 + this.benigh.count(bytes)) /
                                this.num_benigh_files);
      log_is_virus += Math.log((1e-7 + this.virus.count(bytes)) /
                               this.num_virus_files);
    }
    double ratio = Math.exp(log_is_virus - log_is_benigh);
    return ratio / (1 + ratio); 

  }

  public void __init__(int key_len){
    this.benigh = new BytesDistProgram();
    this.virus = new BytesDistProgram();
    this.key_len = key_len;
    this.num_benigh_files = 0;
    this.num_virus_files = 0;
    this.databaseDir = "./";
  }

  public void clear(){
    this.benigh.clear();
    this.virus.clear();
    this.key_len = 0;
    this.num_benigh_files = 0;
    this.num_virus_files = 0;
  }

  VirusDatabase(int key_len){
    this.__init__(key_len);
  }

  VirusDatabase(){
    this.__init__(Flags.key_len_default);
  }
}
