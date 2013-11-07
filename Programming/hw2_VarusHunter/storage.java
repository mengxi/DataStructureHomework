/* Provides the hash table storage and file IO for the dictionary directory
 *
 * Time: 2013/11/2
 * Author: Mengxi Li   UNI: ML3577
 * */
import java.util.*;

package hw2_VarusHunter

/* The dictionary storing # of bytes seen in a type of programs
 * */
class BytesDistProgram
{
  private Map<String, Integer> dist;
  
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


class ReadWriteFile
{
  String file;
  int max_bytes = 300; // max one time read-write.

  public String Read(){
    // read max_bytes from file.
  }

  public void Write(String str){
    // write str to file.
  }

  public void Close(){
    // close file.
  }

  public void __init__(String file){
    this.file = file;
  }

  ReadWriteFile(String file){
    this.__init__(file);
  }
}


public class VirusFile
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
      String read_str = input.Read();
      str = this._substring(str, - this.key_len + 1) + 
            read_str;
      for(int i = 0; i <= str.length() - this.key_len; i++){
        data.add(this._substring(str, i));
      }
    }while(read_str.length() > 0);
    input.Close();
  }

  public void AddBenighFile(String file){
    this._AddFile(file, this.benigh);
  }

  public void AddVirusFile(String file){
    this._AddFile(file, this.virus);
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
