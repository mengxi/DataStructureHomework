/* The main datastructure to store cities.
 * Author: Mengxi Li
 * Date: 2013/11/29 */

package hw3_graph;

import java.lang.String;
import java.lang.Double;
import java.lang.Long;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

class City{
  /* The class for one city */
  public long id = 0;
  public String name = null;
  public String state = null;
  public String lat = null;
  public String lng = null;
  /* Stores the man-made dist information. */
  public Map<City, Double> edges = new HashMap<City, Double>();

  public City(String name, String state, String lat, String lng){
    this.name = name; this.state = state; this.lat = lat; this.lng = lng;
    this.id = City.nextId();
  }

  public long id(){
    return this.id;
  }

  public void addEdge(City dest, double cost){
    // Add directed link cost to City dest.
    this.edges.put(dest, cost);
  }

  public double getManualDis(City dest){
    if(this.edges.containsKey(dest))
      return this.edges.get(dest);
    return -1.0;
  }

  public Set<City> getManualNeighbors(){
    return this.edges.keySet();
  }

  public double gpsDis(City b){
    /* Return the gps distance from City b, instead of the man-made dist info.*/
    return City.distFrom(Double.parseDouble(this.lat),
                         Double.parseDouble(this.lng),
                         Double.parseDouble(b.lat),
                         Double.parseDouble(b.lng));
  }

  public static long city_id = 0;
  public static long nextId(){
    return ++ city_id;
  }

  public static double distFrom(double lat1, double lng1, double lat2, 
                                double lng2) {
    /* Function to calculate gps distance between two gps data, in Meter */
    double earthRadius = 3958.75;
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    double dist = earthRadius * c;
    int meterConversion = 1609;
    return new Double(dist * meterConversion).floatValue();
  }
}

public class Cities implements DijkstraInterface{
  /* Class to store a list of cities, so that convinient for search */
  Map<Long, City> id_to_city = new HashMap<Long, City>();
  Map<String, Set<City>> name_to_city = new HashMap<String, Set<City>>();
  Map<String, Set<City>> state_to_city = new HashMap<String, Set<City>>();

  void add(City a){
    this.id_to_city.put(a.id, a);

    if(!this.name_to_city.containsKey(a.name))
      this.name_to_city.put(a.name, new HashSet<City>());
    this.name_to_city.get(a.name).add(a);

    if(!this.state_to_city.containsKey(a.state))
      this.state_to_city.put(a.state, new HashSet<City>());
    this.state_to_city.get(a.state).add(a);
  }

  City hasId(long id){
    if(this.id_to_city.containsKey(id))
      return this.id_to_city.get(id);
    return null;
  }

  Set<Long> allIds(){
    return this.id_to_city.keySet();
  }

  Set<City> inState(String state){
    if((!this.state_to_city.containsKey(state)) || 
       this.state_to_city.get(state).isEmpty())
      return null;
    return this.state_to_city.get(state);
  }

  Set<City> withName(String name){
    if((!this.name_to_city.containsKey(name)) ||
       this.name_to_city.get(name).isEmpty())
      return null;
    return this.name_to_city.get(name);
  }

  public void buildRandomConnection(City src){
    // Build random connection for city src to cities here.
    // Return True if successful.
    assert src.edges.isEmpty();
    int numEdges = myRandom.nextInt(2,8);
    // find numEdges number of cities as neighbors randomly.
    Set<Long> city_ids = new HashSet<Long>();
    city_ids.addAll(this.allIds());
    city_ids.remove(src.id);
    Set<Long> neighbor_ids = myRandom.withoutReplacement(
        city_ids, numEdges);
    // Assign costs and build link.
    for(long nei_id: neighbor_ids){
      double cost = myRandom.nextDouble(100, 2000);
      assert this.hasId(nei_id) != null;
      src.addEdge(this.hasId(nei_id), cost);
    }
  }

  public String distance_in_use = "gps";

  public void setGPSDis(){
    this.distance_in_use = "gps";
  }

  public void setManualDis(){
    this.distance_in_use = "manual";
  }

  /* The function of in DijkstraInterface to be implemented. */
  public int numNodes(){
    /* Returns the total number of nodes in the graph */
    return this.id_to_city.size();
  }

  public Set<Long> getNeighbors(long sourceId){
    /* return a list of neighbor ids of the source node */
    Set<Long> set_neighbors = new HashSet<Long>();
    if(this.distance_in_use == "gps"){
      set_neighbors.addAll(this.id_to_city.keySet());
      set_neighbors.remove(sourceId);
    }
    else if(this.distance_in_use == "manual"){
      for(City city : this.hasId(sourceId).getManualNeighbors()){
        set_neighbors.add(city.id());
      }
    }
    return set_neighbors;
  }

  public double getDistance(long src, long dest){
    if(this.distance_in_use == "gps"){
      return this.hasId(src).gpsDis(this.hasId(dest));
    }
    else if(this.distance_in_use == "manual"){
      double dis = this.hasId(src).getManualDis(this.hasId(dest));
      if(dis < 0) return Double.MAX_VALUE;
      return dis;
    }
    assert false;
    return -1.0;
  }


  public Cities(){}
}
