/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds_project;

import java.util.*;

/**
 * RouteGraph.java
 * Represents the entire campus/city bus network as a weighted directed graph.
 *
 * Data Structures used:
 *   - HashMap<String, BusStop>         : fast stop lookup by stopId  (Hash Table)
 *   - HashMap<String, List<Edge>>      : adjacency list of connections (Graph)
 *
 * Each Edge stores the destination stop and the distance (in metres) between stops.
 */
public class RouteGraph {


    //  Inner class: Edge (directed connection between two stops)             
    public static class Edge {
        private BusStop destination;
        private int distanceMetres;

        public Edge(BusStop destination, int distanceMetres) {
            this.destination    = destination;
            this.distanceMetres = distanceMetres;
        }

        public BusStop getDestination()   { return destination; }
        public int     getDistance()      { return distanceMetres; }

        @Override
        public String toString() {
            return destination.getName() + " (" + distanceMetres + "m)";
        }
    }

    //  Fields                                                             
    private HashMap<String, BusStop>       stops;       // stopId  -> BusStop
    private HashMap<String, List<Edge>>    adjList;     // stopId  -> neighbours

    //  Constructor                                                        
    public RouteGraph() {
        stops   = new HashMap<>();
        adjList = new HashMap<>();
    }

    //  Graph-building methods                                             

    /**
     * Register a stop in the graph (adds a node).
     */
    public void addStop(BusStop stop) {
        stops.put(stop.getStopId(), stop);
        adjList.putIfAbsent(stop.getStopId(), new ArrayList<>());
        System.out.println("  + Stop added  : " + stop);
    }

    /**
     * Add a directed edge from one stop to another with a distance.
     * For a two-way road, call this method twice (swap from/to).
     */
    public void addConnection(String fromId, String toId, int distanceMetres) {
        BusStop from = stops.get(fromId);
        BusStop to   = stops.get(toId);

        if (from == null || to == null) {
            System.out.println("  ! Connection failed: stop not found (" + fromId + " -> " + toId + ")");
            return;
        }

        adjList.get(fromId).add(new Edge(to, distanceMetres));
        System.out.println("  + Edge added  : " + from.getName()
                           + " --> " + to.getName()
                           + " [" + distanceMetres + "m]");
    }

    /**
     * Convenience: add a bidirectional connection (two directed edges).
     */
    public void addBidirectionalConnection(String id1, String id2, int distanceMetres) {
        addConnection(id1, id2, distanceMetres);
        addConnection(id2, id1, distanceMetres);
    }

    //  Lookup / retrieval methods                                        
    public BusStop getStop(String stopId) {
        return stops.get(stopId);
    }

    public List<Edge> getNeighbours(String stopId) {
        return adjList.getOrDefault(stopId, Collections.emptyList());
    }

    public HashMap<String, BusStop> getAllStops() {
        return stops;
    }

 
    //  Print helpers                                             
    /**
     * Print the full adjacency list of the graph.
     */
    public void printGraph() {
        System.out.println("\n========================================");
        System.out.println("        CAMPUS BUS NETWORK MAP          ");
        System.out.println("========================================");

        for (String stopId : stops.keySet()) {
            BusStop     stop      = stops.get(stopId);
            List<Edge>  neighbours = adjList.get(stopId);

            System.out.printf("%-30s  -->  ", stop.getName());
            if (neighbours.isEmpty()) {
                System.out.print("(no outgoing connections)");
            } else {
                StringJoiner sj = new StringJoiner("  |  ");
                for (Edge e : neighbours) {
                    sj.add(e.toString());
                }
                System.out.print(sj);
            }
            System.out.println();
        }
        System.out.println("========================================\n");
    }
}
