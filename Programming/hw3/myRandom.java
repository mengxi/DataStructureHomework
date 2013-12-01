package hw3_graph;

import java.util.Random;
import java.util.Array;
import java.util.Set;

public class myRandom{
  public static Random rand = new Random();

  public static nextInt(int min, int max){
    // return integer in [min, max]
    assert min <= max;
    return rand.nextInt(max - min + 1) + min;
  }

  public static nextDouble(double min, double max){
    // return double in [min, max)
    assert min <= max;
    return rand.nextDouble() * (max - min) + min;
  }

  public static Set<Integer> intWithoutReplacement(int max, int d){
    // Returns a set of size d from [0, max) without replacement.
    assert max > 0 && d > 0 && d <= max;
    Set<Integer> ret = new TreeSet<Integer>();
    if(d == max) for(int i=0;i<max;i++) ret.add(i);
    else{
      int newv;
      do{
        newv = rand.nextInt(max);
        if(ret.contains(newv)) continue;
        else ret.add(newv);
      }while(ret.size() < d);
    }
    return ret;
  }

  public static<T> Set<T> withoutReplacement(T[] list, int n){
    // Returns a random set with size n from list.
    assert list.size() >= n;
    Set<Integer> selects = intWithoutReplacement(list.length, n);
    Set<T> retSet = new TreeSet<T>();
    for(int i : selects){
      assert !retSet.contains(list[i]);
      retSet.add(list[i]);
    }
    return retSet;
  }
}
