/* The main graph class for data storage and algorithms 
 * Author: Mengxi Li
 * Date: 2013/11/29 */

import java.lang.String
import java.lang.Double
import java.lang.Long
import java.lang.Math


class City{
  /* The class for one city */
  public long id = 0;
  public String name = null;
  public String state = null;
  public String lat = null;
  public String lng = null;
  public Map<City, Double> edges = new HashMap<City, Double>();

  public addEdge(City dest, double cost){
    // Add directed link cost to City dest.
    this.edges.add(dest, cost);
  }

  public double gpsDis(City b){
    /* Return the gps distance from City b */
    return City.distFrom(Double.parseDouble(this.lat),
                         Double.parseDouble(this.lng),
                         Double.parseDouble(b.lat),
                         Double.parseDouble(b.lng));
  }

  public City(String name, String state, String lat, String lon){
    this.name = name; this.state = state; this.lat = lat; this.lon = lon;
    this.id = City.nextId();
  }

  public static city_id = 0;
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

class Cities{
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

  public Cities(){}
}

public class MyGraphMap13{

  

  private void __init__(){}

  public MyGraphMap13(){
    this.__init__()
  }
}
