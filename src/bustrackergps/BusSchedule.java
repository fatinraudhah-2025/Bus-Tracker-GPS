/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

/**
 *
 * @author fatin raudhah
 */
public class BusSchedule {
    
    public String busID;
    public BusRoute route;
    public String departureTime;
    public int frequency;
    
    
    public BusSchedule(String busID, BusRoute route, String departureTime, int frequency){
        this.busID = busID;
        this.route = route;
        this.departureTime = departureTime;
        this.frequency = frequency;
    }
    
    public void DisplayInfo(){
        System.out.println("Bus ID          : " + busID);
        System.out.println("Route           : " + route.getRouteName());
        System.out.println("Departure Time  : " + departureTime);
        System.out.println("Frequency       : " + frequency + " minutes");
    }
    
}
