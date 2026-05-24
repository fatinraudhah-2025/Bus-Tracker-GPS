/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds_project;
/**
 * Main.java
 * ─────────────────────────────────────────────────────────────────────────────
 * Step 1 – Bus Route and Schedule Tracker
 * Demonstrates: Graph (RouteGraph) + Linked List (BusRoute)
 *
 * Campus scenario  (MMU Cyberjaya-style layout):
 *   Three shuttle routes share a common network of 10 stops.
 *
 *   Route T1  – Outer Loop   (6 stops, clockwise ring)
 *   Route T2  – Inner Shuttle (4 stops, core academic zone)
 *   Route T3  – Express Link  (3 stops, library ↔ hostel express)
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("  STEP 1 : Registering Bus Stops (Nodes)");
        System.out.println("==============================================");

        RouteGraph graph = new RouteGraph();

        BusStop mainGate      = new BusStop("S01", "Main Gate",             "Entrance / Exit");
        BusStop adminBlock    = new BusStop("S02", "Admin Block",            "Administrative Hub");
        BusStop library       = new BusStop("S03", "Central Library",        "Block A, Level 1");
        BusStop cafeteria     = new BusStop("S04", "Main Cafeteria",         "Central Campus");
        BusStop engFaculty    = new BusStop("S05", "Engineering Faculty",    "Block FKE");
        BusStop itFaculty     = new BusStop("S06", "IT Faculty",             "Block FCM");
        BusStop stadium       = new BusStop("S07", "Sports Stadium",         "West Campus");
        BusStop hostelA       = new BusStop("S08", "Hostel A",               "North Residential");
        BusStop hostelB       = new BusStop("S09", "Hostel B",               "North Residential");
        BusStop medicalCentre = new BusStop("S10", "Medical Centre",         "Health Building");

        graph.addStop(mainGate);
        graph.addStop(adminBlock);
        graph.addStop(library);
        graph.addStop(cafeteria);
        graph.addStop(engFaculty);
        graph.addStop(itFaculty);
        graph.addStop(stadium);
        graph.addStop(hostelA);
        graph.addStop(hostelB);
        graph.addStop(medicalCentre);

        System.out.println("\n==============================================");
        System.out.println("  STEP 2 : Mapping Connections (Edges)");
        System.out.println("==============================================");

        graph.addBidirectionalConnection("S01", "S02",  350);
        graph.addBidirectionalConnection("S02", "S03",  400);
        graph.addBidirectionalConnection("S03", "S05",  300);
        graph.addBidirectionalConnection("S05", "S07",  500);
        graph.addBidirectionalConnection("S07", "S08",  450);
        graph.addBidirectionalConnection("S08", "S09",  150);
        graph.addBidirectionalConnection("S09", "S01",  600);
        graph.addBidirectionalConnection("S03", "S04",  200);
        graph.addBidirectionalConnection("S04", "S06",  250);
        graph.addBidirectionalConnection("S06", "S02",  300);
        graph.addBidirectionalConnection("S04", "S10",  350);
        graph.addBidirectionalConnection("S10", "S01",  400);
        graph.addConnection("S03", "S09", 700);

        graph.printGraph();

        System.out.println("==============================================");
        System.out.println("  STEP 3 : Building Bus Routes (Linked Lists)");
        System.out.println("==============================================");

        BusRoute routeT1 = new BusRoute("T1", "Outer Loop (Clockwise)", graph);
        System.out.println("\nBuilding Route T1...");
        routeT1.addStop("S01");
        routeT1.addStop("S02");
        routeT1.addStop("S03");
        routeT1.addStop("S05");
        routeT1.addStop("S07");
        routeT1.addStop("S08");
        routeT1.addStop("S09");

        BusRoute routeT2 = new BusRoute("T2", "Inner Shuttle", graph);
        System.out.println("\nBuilding Route T2...");
        routeT2.addStop("S02");
        routeT2.addStop("S03");
        routeT2.addStop("S04");
        routeT2.addStop("S06");

        BusRoute routeT3 = new BusRoute("T3", "Library-Hostel Express", graph);
        System.out.println("\nBuilding Route T3...");
        routeT3.addStop("S03");
        routeT3.addStop("S09");
        routeT3.addStop("S08");

        System.out.println("\n==============================================");
        System.out.println("  STEP 4 : Full Route Paths");
        System.out.println("==============================================");
        routeT1.printRoute();
        routeT2.printRoute();
        routeT3.printRoute();

        System.out.println("\n==============================================");
        System.out.println("  STEP 5 : Stop Retrieval Demos");
        System.out.println("==============================================");

        System.out.println("\n[T1] Stop at index 3 : " + routeT1.getStopAt(3));

        int libIndex = routeT2.indexOf("S03");
        System.out.println("[T2] 'Central Library' is stop #" + (libIndex + 1)
                           + " (index " + libIndex + ")");

        BusStop found = graph.getStop("S10");
        System.out.println("[Graph] Direct lookup S10 : " + found);

        System.out.println("\n[Graph] Neighbours of Central Library (S03):");
        for (RouteGraph.Edge edge : graph.getNeighbours("S03")) {
            System.out.println("         --> " + edge);
        }

        System.out.println("\n==============================================");
        System.out.println("  Step 1 Complete. Graph & Routes ready.");
        System.out.println("==============================================");
    }
}