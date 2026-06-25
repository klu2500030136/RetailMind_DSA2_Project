package retailmind.optimization;

/**
 * Represents a marketing promotion for Activity Selection.
 */
public class Promotion {
    private String promotionName;
    private int startTime;
    private int endTime;
    private double expectedProfit;

    public Promotion(String promotionName, int startTime, int endTime, double expectedProfit) {
        this.promotionName = promotionName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expectedProfit = expectedProfit;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public double getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(double expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    @Override
    public String toString() {
        return promotionName + " (" + startTime + " - " + endTime + ")";
    }
}
