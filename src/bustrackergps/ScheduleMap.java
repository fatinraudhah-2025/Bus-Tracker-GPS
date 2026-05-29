/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

import java.util.HashMap;

/**
 *
 * @author fatin raudhah
 */
public class ScheduleMap {
    private HashMap<String, BusSchedule> mapID;
    private HashMap<String, BusSchedule> mapStop;
    
    public ScheduleMap(BusRoute r1, BusRoute r2, BusRoute r3){
        this.mapID = new HashMap<>();
        this.mapStop = new HashMap<>();
        
        initializeSchedules(r1, r2, r3);
        
    }
    private void initializeSchedules(BusRoute r1, BusRoute r2, BusRoute r3){
        BusSchedule bus1 = new BusSchedule("T1", r1, "07:30 AM", 15);
        BusSchedule bus2 = new BusSchedule("T2", r2, "08:00 AM", 10);
        BusSchedule bus3 = new BusSchedule("T3", r3, "08:15 AM", 20);
        
        mapID.put(bus1.busID.toUpperCase(), bus1);
        mapID.put(bus2.busID.toUpperCase(), bus2);
        mapID.put(bus3.busID.toUpperCase(), bus3);
        
        mapStop.put(r1.getRouteName().toUpperCase(), bus1);
        mapStop.put(r2.getRouteName().toUpperCase(), bus2);
        mapStop.put(r3.getRouteName().toUpperCase(), bus3);
    }
    
    public BusSchedule getScheduleByID(String busID) {
        return mapID.get(busID.toUpperCase());
    }
    
    public BusSchedule getScheduleByStop(String stopName){
        return mapStop.get(stopName.toUpperCase());
    }
    
}
