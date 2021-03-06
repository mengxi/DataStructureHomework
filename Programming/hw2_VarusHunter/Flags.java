
package VirusHunter;

import java.lang.String;
import java.util.UUID;

public class Flags {
  public static final String myname = "Mengxi Li";
  public static final String UIN = "ML3577";
  public static final int key_len_default = 7;
  public static final int min_key = 1;
  public static final int max_key = 10;
  public static final double zeroffset = 1e-10;
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
}
