/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author User
 */
public class UpcomingStops {
    private Queue<BusStopGPS> stopsQueue;

    public UpcomingStops() {
        stopsQueue = new LinkedList<>();
    }

    public void addStop(BusStopGPS stop) {
        stopsQueue.offer(stop);
    }

    public BusStopGPS getNextStop() {
        return stopsQueue.peek();
    }

    public BusStopGPS removeCurrentStop() {
        return stopsQueue.poll();
    }

    public boolean hasStops() {
        return !stopsQueue.isEmpty();
    }

    public void displayQueue() {
        System.out.println("Upcoming Stops: " + stopsQueue);
    }
}
    

