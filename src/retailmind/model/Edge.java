package retailmind.model;

/**
 * Represents an edge in the supplier network graph.
 */
public class Edge {
    private String destination;
    private double cost;

    /**
     * Creates an empty edge object.
     */
    public Edge() {
    }

    /**
     * Creates an edge with destination and cost.
     *
     * @param destination destination supplier name
     * @param cost connection cost
     */
    public Edge(String destination, double cost) {
        this.destination = destination;
        this.cost = cost;
    }

    /**
     * Gets the destination supplier.
     *
     * @return destination supplier name
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination supplier.
     *
     * @param destination destination supplier name
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets the connection cost.
     *
     * @return cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the connection cost.
     *
     * @param cost connection cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns a readable edge summary.
     *
     * @return edge details
     */
    @Override
    public String toString() {
        return destination + " (Cost: " + cost + ")";
    }
}
