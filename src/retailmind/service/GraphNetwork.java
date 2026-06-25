package retailmind.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import retailmind.model.Edge;
import retailmind.util.FileManager;

/**
 * Provides graph-based supplier network features including BFS, DFS, cycle detection, MST, and route optimization.
 */
public class GraphNetwork {
    private static final Path REPORTS_DIRECTORY = Paths.get("reports");
    private static final Path NETWORK_ANALYSIS_REPORT = REPORTS_DIRECTORY.resolve("network_analysis.txt");
    private static final Path SHORTEST_PATH_REPORT = REPORTS_DIRECTORY.resolve("shortest_path_report.txt");
    private static final Path DELIVERY_SCHEDULE_REPORT = REPORTS_DIRECTORY.resolve("delivery_schedule.txt");
    private static final DateTimeFormatter REPORT_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Map<String, List<Edge>> network;
    private Set<String> suppliers;

    /**
     * Creates a GraphNetwork service object and loads network from file.
     */
    public GraphNetwork() {
        FileManager.createDataFiles();
        network = FileManager.loadNetwork();
        suppliers = new HashSet<>();
        extractSuppliers();
    }

    /**
     * Adds a supplier node to the network.
     *
     * @param supplier supplier name
     * @return true if the supplier was added, otherwise false
     */
    public boolean addSupplier(String supplier) {
        if (supplier == null || supplier.trim().isEmpty()) {
            System.out.println("Supplier name cannot be empty.");
            return false;
        }

        if (suppliers.contains(supplier)) {
            System.out.println("Supplier already exists in the network.");
            return false;
        }

        suppliers.add(supplier);
        network.putIfAbsent(supplier, new ArrayList<>());
        FileManager.saveNetwork(network);
        return true;
    }

    /**
     * Connects two suppliers with a cost.
     *
     * @param source source supplier
     * @param destination destination supplier
     * @param cost connection cost
     * @return true if the connection was added, otherwise false
     */
    public boolean connectSuppliers(String source, String destination, double cost) {
        if (source == null || destination == null || source.trim().isEmpty() || destination.trim().isEmpty()) {
            System.out.println("Supplier names cannot be empty.");
            return false;
        }

        if (cost < 0) {
            System.out.println("Connection cost cannot be negative.");
            return false;
        }

        if (!suppliers.contains(source) || !suppliers.contains(destination)) {
            System.out.println("One or both suppliers do not exist in the network.");
            return false;
        }

        // Check for duplicate connection
        List<Edge> edges = network.get(source);
        for (Edge edge : edges) {
            if (edge.getDestination().equals(destination)) {
                System.out.println("Connection already exists between these suppliers.");
                return false;
            }
        }

        network.get(source).add(new Edge(destination, cost));
        FileManager.saveNetwork(network);
        return true;
    }

    /**
     * Displays the supplier network in a tree-like format.
     */
    public void displayNetwork() {
        if (network.isEmpty()) {
            System.out.println("No supplier network available.");
            return;
        }

        System.out.println("\nSupplier Network:");
        System.out.println("=================");

        for (Map.Entry<String, List<Edge>> entry : network.entrySet()) {
            String supplier = entry.getKey();
            List<Edge> edges = entry.getValue();

            System.out.println(supplier);
            for (int i = 0; i < edges.size(); i++) {
                Edge edge = edges.get(i);
                String prefix = (i == edges.size() - 1) ? "`-- " : "|-- ";
                System.out.println(prefix + edge.getDestination() + " (Cost: " + edge.getCost() + ")");
            }
        }
    }

    /**
     * Performs BFS traversal starting from a given supplier.
     *
     * @param start starting supplier
     * @return list of suppliers in BFS order
     */
    public List<String> breadthFirstTraversal(String start) {
        if (start == null || !suppliers.contains(start)) {
            System.out.println("Invalid starting supplier.");
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            for (Edge edge : network.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(edge.getDestination())) {
                    visited.add(edge.getDestination());
                    queue.add(edge.getDestination());
                }
            }
        }

        return result;
    }

    /**
     * Performs DFS traversal starting from a given supplier.
     *
     * @param start starting supplier
     * @return list of suppliers in DFS order
     */
    public List<String> depthFirstTraversal(String start) {
        if (start == null || !suppliers.contains(start)) {
            System.out.println("Invalid starting supplier.");
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsRecursive(start, visited, result);
        return result;
    }

    /**
     * Recursive helper for DFS traversal.
     *
     * @param current current supplier
     * @param visited set of visited suppliers
     * @param result result list
     */
    private void dfsRecursive(String current, Set<String> visited, List<String> result) {
        visited.add(current);
        result.add(current);

        for (Edge edge : network.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(edge.getDestination())) {
                dfsRecursive(edge.getDestination(), visited, result);
            }
        }
    }

    /**
     * Checks if the network is connected starting from a given supplier.
     *
     * @param start starting supplier
     * @return true if the network is connected, otherwise false
     */
    public boolean isConnected(String start) {
        if (start == null || !suppliers.contains(start)) {
            System.out.println("Invalid starting supplier.");
            return false;
        }

        List<String> reachable = breadthFirstTraversal(start);
        return reachable.size() == suppliers.size();
    }

    /**
     * Detects if there is a cycle in the network using DFS.
     *
     * @return true if a cycle is detected, otherwise false
     */
    public boolean detectCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String supplier : suppliers) {
            if (detectCycleUtil(supplier, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recursive helper for cycle detection.
     *
     * @param current current supplier
     * @param visited set of visited suppliers
     * @param recursionStack suppliers in current recursion path
     * @return true if a cycle is detected, otherwise false
     */
    private boolean detectCycleUtil(String current, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(current)) {
            return true;
        }

        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);
        recursionStack.add(current);

        for (Edge edge : network.getOrDefault(current, new ArrayList<>())) {
            if (detectCycleUtil(edge.getDestination(), visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(current);
        return false;
    }

    /**
     * Generates Minimum Spanning Tree using Kruskal's algorithm.
     *
     * @return list of edges in MST with total cost
     */
    public MSTResult generateMST() {
        if (network.isEmpty()) {
            System.out.println("No network available for MST generation.");
            return new MSTResult(new ArrayList<>(), 0);
        }

        List<MSTEdge> allEdges = new ArrayList<>();
        for (Map.Entry<String, List<Edge>> entry : network.entrySet()) {
            String source = entry.getKey();
            for (Edge edge : entry.getValue()) {
                allEdges.add(new MSTEdge(source, edge.getDestination(), edge.getCost()));
            }
        }

        // Sort edges by cost
        allEdges.sort((e1, e2) -> Double.compare(e1.cost, e2.cost));

        // Kruskal's algorithm with Union-Find
        Map<String, String> parent = new HashMap<>();
        for (String supplier : suppliers) {
            parent.put(supplier, supplier);
        }

        List<MSTEdge> mstEdges = new ArrayList<>();
        double totalCost = 0;

        for (MSTEdge edge : allEdges) {
            String root1 = find(edge.source, parent);
            String root2 = find(edge.destination, parent);

            if (!root1.equals(root2)) {
                mstEdges.add(edge);
                totalCost += edge.cost;
                union(edge.source, edge.destination, parent);
            }
        }

        return new MSTResult(mstEdges, totalCost);
    }

    /**
     * Find operation for Union-Find.
     *
     * @param node node to find
     * @param parent parent map
     * @return root of the set
     */
    private String find(String node, Map<String, String> parent) {
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node), parent));
        }
        return parent.get(node);
    }

    /**
     * Union operation for Union-Find.
     *
     * @param node1 first node
     * @param node2 second node
     * @param parent parent map
     */
    private void union(String node1, String node2, Map<String, String> parent) {
        String root1 = find(node1, parent);
        String root2 = find(node2, parent);
        parent.put(root1, root2);
    }

    /**
     * Generates a comprehensive network analysis report.
     */
    public void generateNetworkReport() {
        createReportDirectory();

        MSTResult mstResult = generateMST();
        boolean hasCycle = detectCycle();
        boolean isConnected = suppliers.isEmpty() ? false : isConnected(suppliers.iterator().next());

        String report = "========================================\n"
                + "Supplier Network Analysis Report\n"
                + "========================================\n"
                + "Generated At: " + LocalDateTime.now().format(REPORT_TIME_FORMAT) + "\n"
                + "Total Suppliers: " + suppliers.size() + "\n"
                + "Total Connections: " + getTotalConnections() + "\n"
                + "Connectivity Status: " + (isConnected ? "Connected" : "Not Connected") + "\n"
                + "Cycle Status: " + (hasCycle ? "Cycle Detected" : "No Cycle Found") + "\n"
                + "MST Cost: " + String.format("%.2f", mstResult.totalCost) + "\n"
                + "========================================\n"
                + "Minimum Spanning Tree:\n"
                + "========================================\n";

        for (MSTEdge edge : mstResult.edges) {
            report += edge.source + " -> " + edge.destination + " (" + String.format("%.2f", edge.cost) + ")\n";
        }

        report += "========================================\n"
                + "Total MST Cost: " + String.format("%.2f", mstResult.totalCost) + "\n"
                + "========================================\n";

        try (BufferedWriter writer = Files.newBufferedWriter(NETWORK_ANALYSIS_REPORT,
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(report);
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("Unable to write network analysis report: " + exception.getMessage());
        }

        System.out.println("\nNetwork Analysis Report:");
        System.out.println(report);
        System.out.println("Report saved to reports/network_analysis.txt.");
    }

    /**
     * Extracts all suppliers from the network.
     */
    private void extractSuppliers() {
        for (String supplier : network.keySet()) {
            suppliers.add(supplier);
        }
        for (List<Edge> edges : network.values()) {
            for (Edge edge : edges) {
                suppliers.add(edge.getDestination());
            }
        }
    }

    /**
     * Gets the total number of connections in the network.
     *
     * @return total connections
     */
    private int getTotalConnections() {
        int count = 0;
        for (List<Edge> edges : network.values()) {
            count += edges.size();
        }
        return count;
    }

    /**
     * Creates the reports directory if it doesn't exist.
     */
    private void createReportDirectory() {
        try {
            Files.createDirectories(REPORTS_DIRECTORY);
            if (Files.notExists(NETWORK_ANALYSIS_REPORT)) {
                Files.createFile(NETWORK_ANALYSIS_REPORT);
            }
            if (Files.notExists(SHORTEST_PATH_REPORT)) {
                Files.createFile(SHORTEST_PATH_REPORT);
            }
            if (Files.notExists(DELIVERY_SCHEDULE_REPORT)) {
                Files.createFile(DELIVERY_SCHEDULE_REPORT);
            }
        } catch (IOException exception) {
            System.out.println("Unable to create report directory: " + exception.getMessage());
        }
    }

    /**
     * Finds the shortest path between source and destination using Dijkstra's algorithm.
     *
     * @param source starting supplier
     * @param destination ending supplier
     * @return path result with route and total cost
     */
    public PathResult dijkstra(String source, String destination) {
        if (source == null || destination == null || !suppliers.contains(source) || !suppliers.contains(destination)) {
            System.out.println("Invalid source or destination supplier.");
            return new PathResult(new ArrayList<>(), Double.POSITIVE_INFINITY);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();

        for (String supplier : suppliers) {
            distances.put(supplier, Double.POSITIVE_INFINITY);
            previous.put(supplier, null);
        }

        distances.put(source, 0.0);
        queue.add(new Node(source, 0.0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.name.equals(destination)) {
                break;
            }

            if (current.distance > distances.get(current.name)) {
                continue;
            }

            for (Edge edge : network.getOrDefault(current.name, new ArrayList<>())) {
                String neighbor = edge.getDestination();
                double newDist = distances.get(current.name) + edge.getCost();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current.name);
                    queue.add(new Node(neighbor, newDist));
                }
            }
        }

        List<String> path = reconstructPath(previous, source, destination);
        return new PathResult(path, distances.get(destination));
    }

    /**
     * Finds shortest paths from source to all suppliers using Bellman-Ford algorithm.
     *
     * @param source starting supplier
     * @return map of supplier to shortest distance, or null if negative cycle detected
     */
    public Map<String, Double> bellmanFord(String source) {
        if (source == null || !suppliers.contains(source)) {
            System.out.println("Invalid source supplier.");
            return new HashMap<>();
        }

        Map<String, Double> distances = new HashMap<>();
        List<GraphEdge> edges = new ArrayList<>();

        for (String supplier : suppliers) {
            distances.put(supplier, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);

        for (Map.Entry<String, List<Edge>> entry : network.entrySet()) {
            String src = entry.getKey();
            for (Edge edge : entry.getValue()) {
                edges.add(new GraphEdge(src, edge.getDestination(), edge.getCost()));
            }
        }

        // Relax edges V-1 times
        for (int i = 0; i < suppliers.size() - 1; i++) {
            for (GraphEdge edge : edges) {
                if (distances.get(edge.source) != Double.POSITIVE_INFINITY
                        && distances.get(edge.source) + edge.cost < distances.get(edge.destination)) {
                    distances.put(edge.destination, distances.get(edge.source) + edge.cost);
                }
            }
        }

        // Check for negative weight cycles
        for (GraphEdge edge : edges) {
            if (distances.get(edge.source) != Double.POSITIVE_INFINITY
                    && distances.get(edge.source) + edge.cost < distances.get(edge.destination)) {
                System.out.println("Negative Weight Cycle Detected");
                return null;
            }
        }

        return distances;
    }

    /**
     * Computes all-pairs shortest paths using Floyd-Warshall algorithm.
     *
     * @return distance matrix
     */
    public double[][] floydWarshall() {
        List<String> supplierList = new ArrayList<>(suppliers);
        int n = supplierList.size();
        double[][] dist = new double[n][n];

        // Initialize distance matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // Fill with direct edge costs
        for (Map.Entry<String, List<Edge>> entry : network.entrySet()) {
            int srcIdx = supplierList.indexOf(entry.getKey());
            for (Edge edge : entry.getValue()) {
                int destIdx = supplierList.indexOf(edge.getDestination());
                dist[srcIdx][destIdx] = edge.getCost();
            }
        }

        // Floyd-Warshall algorithm
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Double.POSITIVE_INFINITY && dist[k][j] != Double.POSITIVE_INFINITY
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    /**
     * Performs topological sort using Kahn's algorithm for delivery scheduling.
     *
     * @return topological order of suppliers
     */
    public List<String> topologicalSort() {
        if (network.isEmpty()) {
            System.out.println("No network available for topological sort.");
            return new ArrayList<>();
        }

        Map<String, Integer> inDegree = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        List<String> result = new ArrayList<>();

        for (String supplier : suppliers) {
            inDegree.put(supplier, 0);
        }

        for (Map.Entry<String, List<Edge>> entry : network.entrySet()) {
            for (Edge edge : entry.getValue()) {
                inDegree.put(edge.getDestination(), inDegree.get(edge.getDestination()) + 1);
            }
        }

        for (String supplier : suppliers) {
            if (inDegree.get(supplier) == 0) {
                queue.add(supplier);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            for (Edge edge : network.getOrDefault(current, new ArrayList<>())) {
                inDegree.put(edge.getDestination(), inDegree.get(edge.getDestination()) - 1);
                if (inDegree.get(edge.getDestination()) == 0) {
                    queue.add(edge.getDestination());
                }
            }
        }

        if (result.size() != suppliers.size()) {
            System.out.println("Cycle detected in the graph. Topological sort not possible.");
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * Generates a comprehensive shortest path report.
     */
    public void generateShortestPathReport() {
        createReportDirectory();

        double[][] distanceMatrix = floydWarshall();
        List<String> supplierList = new ArrayList<>(suppliers);

        String report = "========================================\n"
                + "All-Pairs Shortest Path Report\n"
                + "========================================\n"
                + "Generated At: " + LocalDateTime.now().format(REPORT_TIME_FORMAT) + "\n"
                + "========================================\n"
                + "Distance Matrix:\n"
                + "========================================\n";

        // Header row
        report += "    ";
        for (String supplier : supplierList) {
            report += String.format("%-8s", supplier.substring(0, Math.min(6, supplier.length())));
        }
        report += "\n";

        // Matrix rows
        for (int i = 0; i < supplierList.size(); i++) {
            report += String.format("%-4s", supplierList.get(i).substring(0, Math.min(4, supplierList.get(i).length())));
            for (int j = 0; j < supplierList.size(); j++) {
                if (distanceMatrix[i][j] == Double.POSITIVE_INFINITY) {
                    report += String.format("%-8s", "INF");
                } else {
                    report += String.format("%-8.0f", distanceMatrix[i][j]);
                }
            }
            report += "\n";
        }

        report += "========================================\n";

        try (BufferedWriter writer = Files.newBufferedWriter(SHORTEST_PATH_REPORT,
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(report);
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("Unable to write shortest path report: " + exception.getMessage());
        }

        System.out.println("\nAll-Pairs Shortest Path Report:");
        System.out.println(report);
        System.out.println("Report saved to reports/shortest_path_report.txt.");
    }

    /**
     * Generates a delivery schedule based on topological sort.
     */
    public void generateDeliverySchedule() {
        createReportDirectory();

        List<String> schedule = topologicalSort();

        String report = "========================================\n"
                + "Delivery Schedule Report\n"
                + "========================================\n"
                + "Generated At: " + LocalDateTime.now().format(REPORT_TIME_FORMAT) + "\n"
                + "========================================\n"
                + "Suggested Delivery Order:\n"
                + "========================================\n";

        if (schedule.isEmpty()) {
            report += "No valid delivery schedule possible (cycle detected).\n";
        } else {
            for (int i = 0; i < schedule.size(); i++) {
                report += (i + 1) + ". " + schedule.get(i);
                if (i < schedule.size() - 1) {
                    report += " ->";
                }
                report += "\n";
            }
        }

        report += "========================================\n";

        try (BufferedWriter writer = Files.newBufferedWriter(DELIVERY_SCHEDULE_REPORT,
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(report);
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("Unable to write delivery schedule: " + exception.getMessage());
        }

        System.out.println("\nDelivery Schedule Report:");
        System.out.println(report);
        System.out.println("Report saved to reports/delivery_schedule.txt.");
    }

    /**
     * Reconstructs path from previous map.
     *
     * @param previous previous node map
     * @param source start node
     * @param destination end node
     * @return reconstructed path
     */
    private List<String> reconstructPath(Map<String, String> previous, String source, String destination) {
        List<String> path = new ArrayList<>();
        String current = destination;

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        if (path.isEmpty() || !path.get(0).equals(source)) {
            return new ArrayList<>();
        }

        return path;
    }

    /**
     * Data holder for Dijkstra's algorithm nodes.
     */
    private static class Node implements Comparable<Node> {
        String name;
        double distance;

        Node(String name, double distance) {
            this.name = name;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    /**
     * Data holder for graph edges in Bellman-Ford.
     */
    private static class GraphEdge {
        String source;
        String destination;
        double cost;

        GraphEdge(String source, String destination, double cost) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }
    }

    /**
     * Data holder for path result.
     */
    public static class PathResult {
        public final List<String> path;
        public final double totalCost;

        PathResult(List<String> path, double totalCost) {
            this.path = path;
            this.totalCost = totalCost;
        }
    }

    /**
     * Data holder for MST edge.
     */
    public static class MSTEdge {
        public String source;
        public String destination;
        public double cost;

        MSTEdge(String source, String destination, double cost) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }
    }

    /**
     * Data holder for MST result.
     */
    public static class MSTResult {
        public final List<MSTEdge> edges;
        public final double totalCost;

        MSTResult(List<MSTEdge> edges, double totalCost) {
            this.edges = edges;
            this.totalCost = totalCost;
        }
    }
}

