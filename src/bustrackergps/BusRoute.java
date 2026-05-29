/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

import java.util.*;

/**
 * BusRoute.java
 * Represents a single named bus route as an ordered sequence of stops.
 *
 * Data Structure used:
 *   - LinkedList<BusStop>  : preserves stop order; O(1) add-to-end,
 *                            O(1) peek next/previous stop  (Linked List)
 *
 * A BusRoute uses the RouteGraph to validate that every stop it
 * references actually exists, and that each consecutive pair of stops
 * shares a direct edge.
 */
import java.util.*;

public class BusRoute {

    private static class StopNode {
        BusStop  stop;
        StopNode next;

        StopNode(BusStop stop) {
            this.stop = stop;
            this.next = null;
        }
    }

    private String      routeId;
    private String      routeName;
    private RouteGraph  graph;

    private StopNode    head;
    private StopNode    tail;
    private int         stopCount;

    public BusRoute(String routeId, String routeName, RouteGraph graph) {
        this.routeId   = routeId;
        this.routeName = routeName;
        this.graph     = graph;
        this.head      = null;
        this.tail      = null;
        this.stopCount = 0;
    }

    public boolean addStop(String stopId) {
        BusStop stop = graph.getStop(stopId);

        if (stop == null) {
            System.out.println("  Cannot add stop '" + stopId + "' - not found in graph.");
            return false;
        }

        if (tail != null) {
            boolean connected = false;
            for (RouteGraph.Edge e : graph.getNeighbours(tail.stop.getStopId())) {
                if (e.getDestination().getStopId().equals(stopId)) {
                    connected = true;
                    break;
                }
            }
            if (!connected) {
                System.out.println("  Cannot add stop '" + stop.getName()
                                   + "' - no direct edge from '"
                                   + tail.stop.getName() + "'.");
                return false;
            }
        }

        StopNode node = new StopNode(stop);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        stopCount++;
        return true;
    }

    public List<BusStop> getOrderedStops() {
        List<BusStop> result = new ArrayList<>();
        StopNode current = head;
        while (current != null) {
            result.add(current.stop);
            current = current.next;
        }
        return result;
    }

    public BusStop getStopAt(int index) {
        StopNode current = head;
        int i = 0;
        while (current != null) {
            if (i == index) return current.stop;
            current = current.next;
            i++;
        }
        return null;
    }

    public int indexOf(String stopId) {
        StopNode current = head;
        int index = 0;
        while (current != null) {
            if (current.stop.getStopId().equals(stopId)) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    public void printRoute() {
        System.out.println("\nRoute " + routeId + " : " + routeName);
        System.out.println("Total stops: " + stopCount);
        System.out.println();

        StopNode current = head;
        int seq = 1;
        while (current != null) {
            BusStop stop = current.stop;
            System.out.println("  " + seq + ". " + stop.getName() + " [" + stop.getStopId() + "]");
            if (current.next != null) {
                System.out.println("       |");
            }
            current = current.next;
            seq++;
        }
        System.out.println("--- End of Route " + routeId + " ---");
    }

    public String getRouteId()   { return routeId; }
    public String getRouteName() { return routeName; }
    public int    getStopCount() { return stopCount; }
}
