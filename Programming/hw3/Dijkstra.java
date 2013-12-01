
import java.lang.Object;
import java.lang.Long;
import java.lang.Integer;
import java.util.Map;
import java.util.Arrays;

class PathResult{
  double pathLength;
  long prevId;
  PathResult(long prevId, double len){
    this.pathLength = len;
    this.prevId = prevId;
  }
  long prevId(){ return this.prevId; }
  double length(){ return this.pathLength; }
}

public abstract class Dijkstra {
  // the class to find shortest path using Dijkstra algorithm
  // I wrote this by my own.
  public abstract int numNodes();
  public abstract long[] getNeighbors(long id);
  public abstract double getDistance(long src, long dest);
  public Map<Long, PathResult> findPath(long sourceId, int n){
    // find the n shortest neighbors (id)
    map = 
    
    final double [] dist = new double [this.numNodes()];
    final int [] pred = new int[this.numNodes];
    final boolean [] visited = new boolean [this.numNodes];
    
    for (int i = 0; i < dist.length; i++){
      dist[i] = Integer.MAX_VALUE;
    }

    return null;
  }

  public Map<Long, PathResult> find(long sourceId){
    return this.find(sourceId, this.numNodes());
  }
}


