package bustrackergps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainDashboard {

    // ── Shared data structures (built once, reused across UI actions) ─────────
    private RouteGraph  graph;
    private BusRoute    routeT1, routeT2, routeT3;
    private ScheduleMap scheduleMap;

    // Map of routeId -> BusRoute for easy lookup from ComboBox selection
    private Map<String, BusRoute> routeMap = new LinkedHashMap<>();

    // ── UI controls ───────────────────────────────────────────────────────────
    private JComboBox<String> routeSelector;
    private JComboBox<String> stopSelector;
    private JTextArea         etaOutput;
    private JTextArea         scheduleOutput;
    private JTextArea         gpsOutput;
    private JLabel            statusLabel;
    private javax.swing.Timer autoRefreshTimer;

    // ─────────────────────────────────────────────────────────────────────────
    //  Main entry point 
    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainDashboard dashboard = new MainDashboard();
            dashboard.createAndShowGUI();
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  System Integration 
    // ─────────────────────────────────────────────────────────────────────────
    private void initDataStructures() {
        // ── Member 1: Build the RouteGraph (nodes + edges) ────────────────────
        graph = new RouteGraph();

        BusStop mainGate      = new BusStop("S01", "Main Gate",           "Entrance / Exit",        3.120000, 101.650000);
        BusStop adminBlock    = new BusStop("S02", "Admin Block",          "Administrative Hub",     3.121000, 101.651000);
        BusStop library       = new BusStop("S03", "Central Library",      "Block A, Level 1",       3.122500, 101.652100);
        BusStop cafeteria     = new BusStop("S04", "Main Cafeteria",       "Central Campus",         3.123000, 101.653500);
        BusStop engFaculty    = new BusStop("S05", "Engineering Faculty",  "Block FKE",              3.124500, 101.651200);
        BusStop itFaculty     = new BusStop("S06", "IT Faculty",           "Block FCM",              3.121800, 101.654000);
        BusStop stadium       = new BusStop("S07", "Sports Stadium",       "West Campus",            3.126000, 101.650500);
        BusStop hostelA       = new BusStop("S08", "Hostel A",             "North Residential",      3.127500, 101.652800);
        BusStop hostelB       = new BusStop("S09", "Hostel B",             "North Residential",      3.127000, 101.654200);
        BusStop medicalCentre = new BusStop("S10", "Medical Centre",       "Health Building",        3.124000, 101.656000);

        graph.addStop(mainGate); graph.addStop(adminBlock); graph.addStop(library);
        graph.addStop(cafeteria); graph.addStop(engFaculty); graph.addStop(itFaculty);
        graph.addStop(stadium); graph.addStop(hostelA); graph.addStop(hostelB);
        graph.addStop(medicalCentre);

        graph.addBidirectionalConnection("S01","S02",350); graph.addBidirectionalConnection("S02","S03",400);
        graph.addBidirectionalConnection("S03","S05",300); graph.addBidirectionalConnection("S05","S07",500);
        graph.addBidirectionalConnection("S07","S08",450); graph.addBidirectionalConnection("S08","S09",150);
        graph.addBidirectionalConnection("S09","S01",600); graph.addBidirectionalConnection("S03","S04",200);
        graph.addBidirectionalConnection("S04","S06",250); graph.addBidirectionalConnection("S06","S02",300);
        graph.addBidirectionalConnection("S04","S10",350); graph.addBidirectionalConnection("S10","S01",400);
        graph.addConnection("S03","S09",700);

        // ── Member 1: Build BusRoutes (LinkedList of stops) ───────────────────
        routeT1 = new BusRoute("T1", "Outer Loop (Clockwise)", graph);
        routeT1.addStop("S01"); routeT1.addStop("S02"); routeT1.addStop("S03");
        routeT1.addStop("S05"); routeT1.addStop("S07"); routeT1.addStop("S08"); routeT1.addStop("S09");

        routeT2 = new BusRoute("T2", "Inner Shuttle", graph);
        routeT2.addStop("S02"); routeT2.addStop("S03"); routeT2.addStop("S04"); routeT2.addStop("S06");

        routeT3 = new BusRoute("T3", "Library-Hostel Express", graph);
        routeT3.addStop("S03"); routeT3.addStop("S09"); routeT3.addStop("S08");

        routeMap.put("T1 – Outer Loop (Clockwise)",    routeT1);
        routeMap.put("T2 – Inner Shuttle",             routeT2);
        routeMap.put("T3 – Library-Hostel Express",    routeT3);

        // ── Member 2: Build ScheduleMap (Hash Table of BusSchedule) ──────────
        scheduleMap = new ScheduleMap(routeT1, routeT2, routeT3);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  GUI
    // ─────────────────────────────────────────────────────────────────────────
    private void createAndShowGUI() {
        initDataStructures();

        JFrame frame = new JFrame("Campus Bus Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 520);
        frame.setMinimumSize(new Dimension(700, 460));

        // ── Title bar (dark panel) ────────────────────────────────────────────
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(0x1a, 0x1a, 0x3e));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 14, 20));

        JLabel titleLabel = new JLabel("🚌  Campus Bus Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Bus Tracker UI");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(0xaa, 0xaa, 0xcc));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);

        // ── Selector panel (left sidebar) ─────────────────────────────────────
        JPanel selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
        selectorPanel.setBackground(new Color(0x12, 0x12, 0x2a));
        selectorPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        selectorPanel.setPreferredSize(new Dimension(260, 0));

        // Route selector
        JLabel routeLabel = styledLabel("Select Route:");
        routeSelector = new JComboBox<>();
        for (String key : routeMap.keySet()) {
            routeSelector.addItem(key);
        }
        routeSelector.setSelectedIndex(-1); // no initial selection (like prompt)
        routeSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        routeSelector.addActionListener(e -> onRouteSelected());

        // Stop selector
        JLabel stopLabel = styledLabel("Select Stop:");
        stopSelector = new JComboBox<>();
        stopSelector.setSelectedIndex(-1);
        stopSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        stopSelector.addActionListener(e -> refreshETA());

        // Buttons
        JButton refreshBtn = new JButton("🔄  Refresh ETA Now");
        refreshBtn.setBackground(new Color(0x4a, 0x7f, 0xe8));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        refreshBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshBtn.addActionListener(e -> refreshETA());

        JButton simulateBtn = new JButton("📡  Run GPS Simulation (console)");
        simulateBtn.setBackground(new Color(0x2d, 0x6a, 0x4f));
        simulateBtn.setForeground(Color.WHITE);
        simulateBtn.setFocusPainted(false);
        simulateBtn.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        simulateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        simulateBtn.addActionListener(e -> runGpsSimulation());

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0x33, 0x33, 0x55));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Assemble selector panel
        selectorPanel.add(routeLabel);
        selectorPanel.add(Box.createVerticalStrut(4));
        selectorPanel.add(routeSelector);
        selectorPanel.add(Box.createVerticalStrut(12));
        selectorPanel.add(stopLabel);
        selectorPanel.add(Box.createVerticalStrut(4));
        selectorPanel.add(stopSelector);
        selectorPanel.add(Box.createVerticalStrut(16));
        selectorPanel.add(separator);
        selectorPanel.add(Box.createVerticalStrut(12));
        selectorPanel.add(refreshBtn);
        selectorPanel.add(Box.createVerticalStrut(8));
        selectorPanel.add(simulateBtn);

        // ── ETA display panel ─────────────────────────────────────────────────
        JLabel etaTitle = styledLabel("🏁  Live ETA — Ranked Arrivals");

        etaOutput = new JTextArea("Select a route and stop to see live ETA rankings.");
        etaOutput.setEditable(false);
        etaOutput.setLineWrap(true);
        etaOutput.setWrapStyleWord(true);
        etaOutput.setFont(new Font("Courier New", Font.PLAIN, 13));
        etaOutput.setBackground(new Color(0x0d, 0x0d, 0x20));
        etaOutput.setForeground(new Color(0x00, 0xff, 0x99));
        etaOutput.setCaretColor(new Color(0x00, 0xff, 0x99));
        etaOutput.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane etaScroll = new JScrollPane(etaOutput);
        etaScroll.setPreferredSize(new Dimension(0, 200));
        etaScroll.setBorder(BorderFactory.createLineBorder(new Color(0x22, 0x22, 0x44)));

        // ── Schedule info panel ────────────────────────────────────────────────
        JLabel schedTitle = styledLabel("📋  Schedule Info");

        scheduleOutput = new JTextArea("Schedule details will appear here.");
        scheduleOutput.setEditable(false);
        scheduleOutput.setLineWrap(true);
        scheduleOutput.setWrapStyleWord(true);
        scheduleOutput.setFont(new Font("Courier New", Font.PLAIN, 12));
        scheduleOutput.setBackground(new Color(0x0d, 0x0d, 0x20));
        scheduleOutput.setForeground(new Color(0xcc, 0xcc, 0xff));
        scheduleOutput.setCaretColor(new Color(0xcc, 0xcc, 0xff));
        scheduleOutput.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane schedScroll = new JScrollPane(scheduleOutput);
        schedScroll.setPreferredSize(new Dimension(0, 130));
        schedScroll.setBorder(BorderFactory.createLineBorder(new Color(0x22, 0x22, 0x44)));

        // ── GPS simulation log panel (shows simulator output inside the UI)
        JLabel gpsTitle = styledLabel("📡  GPS Simulation");

        gpsOutput = new JTextArea("GPS console output will appear here.");
        gpsOutput.setEditable(false);
        gpsOutput.setLineWrap(true);
        gpsOutput.setWrapStyleWord(true);
        gpsOutput.setFont(new Font("Courier New", Font.PLAIN, 12));
        gpsOutput.setBackground(new Color(0x0d, 0x0d, 0x20));
        gpsOutput.setForeground(new Color(0x99, 0xff, 0xcc));
        gpsOutput.setCaretColor(new Color(0x99, 0xff, 0xcc));
        gpsOutput.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane gpsScroll = new JScrollPane(gpsOutput);
        gpsScroll.setPreferredSize(new Dimension(0, 130));
        gpsScroll.setBorder(BorderFactory.createLineBorder(new Color(0x22, 0x22, 0x44)));

        // Assemble right/output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setBackground(new Color(0x12, 0x12, 0x2a));
        outputPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        outputPanel.add(etaTitle);
        outputPanel.add(Box.createVerticalStrut(6));
        outputPanel.add(etaScroll);
        outputPanel.add(Box.createVerticalStrut(16));
        outputPanel.add(schedTitle);
        outputPanel.add(Box.createVerticalStrut(6));
        outputPanel.add(schedScroll);
        outputPanel.add(Box.createVerticalStrut(12));
        outputPanel.add(gpsTitle);
        outputPanel.add(Box.createVerticalStrut(6));
        outputPanel.add(gpsScroll);

        // ── Status bar ─────────────────────────────────────────────────────────
        statusLabel = new JLabel("Ready. Auto-refresh every 5 seconds once a stop is selected.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(0x88, 0x88, 0x99));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(0x0a, 0x0a, 0x1a));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        // ── Root layout ────────────────────────────────────────────────────────
        JPanel mainContent = new JPanel(new BorderLayout(1, 0));
        mainContent.setBackground(new Color(0x12, 0x12, 0x2a));
        mainContent.add(selectorPanel, BorderLayout.WEST);
        mainContent.add(outputPanel, BorderLayout.CENTER);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x12, 0x12, 0x2a));
        root.add(titlePanel, BorderLayout.NORTH);
        root.add(mainContent, BorderLayout.CENTER);
        root.add(statusLabel, BorderLayout.SOUTH);

        frame.setContentPane(root);

        // ── Auto-refresh timer (every 5 seconds) using Swing Timer ────────────
        autoRefreshTimer = new javax.swing.Timer(5000, e -> refreshETA());
        autoRefreshTimer.start();

        // Stop timer on window close
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (autoRefreshTimer != null) {
                    autoRefreshTimer.stop();
                }
            }
        });

        frame.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  EVENT HANDLERS 
    // ─────────────────────────────────────────────────────────────────────────

    /** Called when the user selects a route — populates the stop dropdown. */
    private void onRouteSelected() {
        stopSelector.removeAllItems();
        stopSelector.setSelectedIndex(-1);
        etaOutput.setText("Select a stop to see ETAs.");
        scheduleOutput.setText("");

        BusRoute selected = getSelectedRoute();
        if (selected == null) return;

        for (BusStop stop : selected.getOrderedStops()) {
            stopSelector.addItem(stop.getName() + "  [" + stop.getStopId() + "]");
        }

        // Show schedule info for this route
        String routeId = selected.getRouteId();
        BusSchedule sched = scheduleMap.getScheduleByID(routeId);
        if (sched != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bus ID         : ").append(sched.busID).append("\n")
              .append("Route          : ").append(sched.route.getRouteName()).append("\n")
              .append("Departure Time : ").append(sched.departureTime).append("\n")
              .append("Frequency      : Every ").append(sched.frequency).append(" minutes\n")
              .append("Total Stops    : ").append(sched.route.getStopCount()).append("\n\n")
              .append("Stops:\n");

            int idx = 1;
            for (BusStop s : sched.route.getOrderedStops()) {
                sb.append(String.format("%2d. %-24s [%s]\n", idx++, s.getName(), s.getStopId()));
            }

            scheduleOutput.setText(sb.toString());
        }

        statusLabel.setText("Route selected: " + selected.getRouteName() + " — now select a stop.");
    }

    /**
     * Called when a stop is selected (or Refresh is pressed).
     * Uses Member 3 (GPS positions) + Member 4 (ETACalculator + ArrivalTracker)
     * to produce a ranked arrival list for the chosen stop.
     */
    private void refreshETA() {
        BusRoute  selectedRoute = getSelectedRoute();
        BusStop   selectedStop  = getSelectedStop();
        if (selectedRoute == null || selectedStop == null) return;

        // ── Simulate live GPS positions for all 3 buses (same as JavaFX) ─────
        String[] busIds = {"T1", "T2", "T3"};
        Random rng = new Random();

        // ── Build the ranked output string ────────────────────────────────────
        StringBuilder sb = new StringBuilder();
        sb.append("📍 Stop: ").append(selectedStop.getName())
          .append("  (").append(selectedStop.getLocation()).append(")\n");
        sb.append("─".repeat(52)).append("\n");

        // Calculate ETAs for all buses (replicates the manual list building from JavaFX)
        java.util.List<ArrivalTracker.BusArrival> arrivals = new ArrayList<>();

        for (String busId : busIds) {
            BusSchedule sched = scheduleMap.getScheduleByID(busId);
            if (sched == null) continue;

            BusStop firstStop = sched.route.getStopAt(0);
            double liveLat = firstStop.getLatitude()  + (rng.nextDouble() - 0.5) * 0.008;
            double liveLon = firstStop.getLongitude() + (rng.nextDouble() - 0.5) * 0.008;

            // ── Member 4: ETACalculator ───────────────────────────────────────
            double dist = ETACalculator.calculateDistance(liveLat, liveLon,
                              selectedStop.getLatitude(), selectedStop.getLongitude());
            int eta = ETACalculator.calculateETA(dist);

            arrivals.add(new ArrivalTracker.BusArrival(busId, selectedStop.getName(), dist, eta));
        }

        // Sort by ETA ascending (mirrors the PriorityQueue ordering)
        arrivals.sort(Comparator.comparingInt(a -> a.etaMinutes));

        int rank = 1;
        for (ArrivalTracker.BusArrival a : arrivals) {
            String medal = (rank == 1) ? "🥇" : (rank == 2) ? "🥈" : "🥉";
            sb.append(medal).append(" #").append(rank).append("  Bus ")
              .append(String.format("%-4s", a.busId))
              .append(" → arrives in  ").append(a.etaMinutes).append(" min")
              .append("  (").append(String.format("%.0f", a.distance)).append("m away)\n");
            rank++;
        }

        sb.append("─".repeat(52)).append("\n");
        String timeStr = new java.util.Date().toString().substring(11, 19);
        sb.append("⏱  Last updated: ").append(timeStr);

        etaOutput.setText(sb.toString());
        statusLabel.setText("ETA refreshed for: " + selectedStop.getName());
    }

    /**
     * Runs Member 3's GPSSimulator for the selected route — output goes to console.
     * This demonstrates the Queue dequeue simulation.
     */
    private void runGpsSimulation() {
        BusRoute selectedRoute = getSelectedRoute();
        if (selectedRoute == null) {
            statusLabel.setText("⚠  Please select a route first.");
            return;
        }

        String routeId = selectedRoute.getRouteId();
        BusSchedule sched = scheduleMap.getScheduleByID(routeId);
        if (sched == null) return;

        statusLabel.setText("📡 GPS simulation running for " + sched.busID + " in console... (check IDE output)");

        // Run in background thread so UI doesn't freeze during Thread.sleep calls
        Thread simThread = new Thread(() -> {
            // ── Member 3: UpcomingStops (Queue) + GPSSimulator ────────────────
            UpcomingStops routeQueue = new UpcomingStops();
            for (BusStop stop : sched.route.getOrderedStops()) {
                routeQueue.addStop(new BusStopGPS(stop.getName(), stop.getLatitude(), stop.getLongitude()));
            }

            double startLat = sched.route.getStopAt(0).getLatitude()  - 0.001;
            double startLon = sched.route.getStopAt(0).getLongitude() - 0.001;

            GPSSimulator gps = new GPSSimulator(sched.busID, startLat, startLon, routeQueue);

            // Clear UI log and create a logger that appends to the gpsOutput on the EDT
            SwingUtilities.invokeLater(() -> gpsOutput.setText(""));
            java.util.function.Consumer<String> logger = msg -> SwingUtilities.invokeLater(() -> {
                gpsOutput.append(msg + "\n");
                gpsOutput.setCaretPosition(gpsOutput.getDocument().getLength());
            });

            gps.startJourney(logger);   // Member 3's method — now emits updates to UI
        });
        simThread.setDaemon(true);
        simThread.start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    /** Returns the BusRoute matching the currently selected ComboBox item. */
    private BusRoute getSelectedRoute() {
        String key = (String) routeSelector.getSelectedItem();
        if (key == null) return null;
        return routeMap.get(key);
    }

    /** Returns the BusStop matching the currently selected stop ComboBox item. */
    private BusStop getSelectedStop() {
        String item = (String) stopSelector.getSelectedItem();
        if (item == null) return null;

        // Extract stopId from "Stop Name  [S03]" format
        int open  = item.lastIndexOf('[');
        int close = item.lastIndexOf(']');
        if (open < 0 || close < 0) return null;

        String stopId = item.substring(open + 1, close).trim();
        return graph.getStop(stopId);
    }

    /** Creates a consistently styled label for the dark UI. */
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(0x99, 0x99, 0xcc));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}