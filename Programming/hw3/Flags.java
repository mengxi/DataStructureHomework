
package hw3_graph;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.UUID;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;

public class Flags {
  public static final String myname = "Mengxi Li";
  public static final String UIN = "ML3577";
  public static String source_of_distance = "gps"; // or can be "manual"
}


class utilString{

  public static String uuid(){
    return(UUID.randomUUID().toString());
  }

  public static String substring(String str, int len){
    String ret = "";
    int new_len = len >= 0 ? len : str.length() + len;
    if(new_len < 0) new_len = 0;
    if(new_len > str.length()) new_len = str.length();
    return str.substring(new_len);
  }

  public static List<String> append(String head, Collection<?> s){
    List<String> builder = new LinkedList<String>();
    Iterator<?> iter = s.iterator();
    while (iter.hasNext()) {
      builder.add(head + String.valueOf(iter.next()));
    }
    return builder; 
  }

  public static String join(Collection<?> s, String delimiter) {
     StringBuilder builder = new StringBuilder();
     Iterator<?> iter = s.iterator();
     while (iter.hasNext()) {
         builder.append(iter.next());
         if (!iter.hasNext()) {
           break;
         }
         builder.append(delimiter);
     }
     return builder.toString();
 }

}
