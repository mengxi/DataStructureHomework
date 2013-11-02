/* Provides the hash table storage and file IO for the dictionary directory
 *
 * Time: 2013/11/2
 * Author: Mengxi Li   UNI: ML3577
 * */
import java.util.*;

package hw2_VarusHunter

/* The dictionary storing # of bytes seen in a type of programs
 * */
public class BytesDistProgram
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

  private void __init__(){
    this.dist = new HashMap<String, Integer>();
  }

  BytesDistProgram(){
    this.__init__();
  }
}

