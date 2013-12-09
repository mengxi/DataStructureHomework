/* This contains the modular for Dijstra algorithm.
 * Usage: classes just have to extend interface DijkstraInterface, 
 * then call the function find in class Dijkstra.
 * */

package hw3_graph;

import java.lang.Object;
import java.lang.Long;
import java.lang.Integer;
import java.lang.Double;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;


class PathResult{
  double pathLength;
  long prevId;
  PathResult(long prevId, double len){
    this.pathLength = len;
    this.prevId = prevId;
  }
  long pred(){ return this.prevId; }
  double len(){ return this.pathLength; }
}


class IntegerMap<T>{
  /* One to one maps an object to an integer. */
  private Map<T, Integer> mapping = new HashMap<T, Integer>();
  List<T> list = new LinkedList<T>();
  private void add(T a){
    assert !this.mapping.containsKey(a);
    this.list.add(a);
    this.mapping.put(a, this.list.size()-1);
  }
  int getInt(T a){
    if(!this.mapping.containsKey(a)){
      this.add(a);
    }
    return this.mapping.get(a);
  }
  T getObj(int i){
    assert i >= 0 && i <= this.list.size()-1 && this.list.size() == 
      this.mapping.size();
    return this.list.get(i);
  }
  IntegerMap(){}
}


interface DijkstraInterface{
  public int numNodes();
  public Set<Long> getNeighbors(long id);
  public double getDistance(long src, long dest);
}


public class Dijkstra {

  public int fetch_cloest_neighbor(Set<Integer> nodes, double[] dist){
    /* Find the node id in nodes with the smallest dist value */
    double init_dist = Double.MAX_VALUE;
    int ret = -1;
    for(int i : nodes){
      if(dist[i] < init_dist){
        init_dist = dist[i];
        ret = i;
      }
    }
    assert !nodes.isEmpty() && ret >= 0 && nodes.contains(ret);
    return ret;
  }

  // the class to find shortest path using Dijkstra algorithm
  // I wrote this by my own.
  public Map<Long, PathResult> find(long sourceId, int n, 
                                    DijkstraInterface graph){
    // find the n shortest neighbors (id)
    IntegerMap<Long> maps = new IntegerMap<Long>();
    final double [] dist = new double [graph.numNodes()];
    final int [] pred = new int[graph.numNodes()];
    final boolean [] visited = new boolean [graph.numNodes()]; 
    for (int i = 0; i < dist.length; i++){
      dist[i] = Double.MAX_VALUE;
      visited[i] = false;
      pred[i] = -1;
    }

    int total_neighbors_found = -1;
    Set<Integer> potential_neighbors = new HashSet<Integer>();
    int source = maps.getInt(sourceId);
    dist[source] = 0.0;
    potential_neighbors.add(source);

    while(!potential_neighbors.isEmpty() && total_neighbors_found < n){
      int u = fetch_cloest_neighbor(potential_neighbors, dist);
      potential_neighbors.remove(u);
      visited[u] = true;
      total_neighbors_found ++;
      long u_id = maps.getObj(u);

      for(long v_id : graph.getNeighbors(u_id)){
        double alt = dist[u] + graph.getDistance(u_id, v_id);
        int v = maps.getInt(v_id);
        if(alt < dist[v]){
          dist[v] = alt;
          pred[v] = u;
          if(!visited[v]){
            potential_neighbors.add(v);
          }
        }
      }
    }

    Map<Long, PathResult> result = new HashMap<Long, PathResult>();
    for(int i = 0; i < visited.length; i++){
      if(visited[i] && maps.getObj(i) != sourceId){
        PathResult path = new PathResult(maps.getObj(pred[i]), dist[i]);
        result.put(maps.getObj(i), path);
      }
    }
    return result;
  }

  public Map<Long, PathResult> find(long sourceId, DijkstraInterface graph){
    return this.find(sourceId, graph.numNodes(), graph);
  }
}


