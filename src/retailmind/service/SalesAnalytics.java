package retailmind.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retailmind.dsa.FenwickTree;
import retailmind.dsa.SegmentTree;
import retailmind.util.FileManager;

/**
 * Provides sales analytics features using Segment Tree and Fenwick Tree.
 */
public class SalesAnalytics {
    private static final Path SALES_FILE = Paths.get("data", "sales.txt");
    private static final Path REPORTS_DIRECTORY = Paths.get("reports");
    private static final Path ANALYTICS_REPORT = REPORTS_DIRECTORY.resolve("analytics_report.txt");
    private static final Path REVENUE_REPORT = REPORTS_DIRECTORY.resolve("revenue_report.txt");
    private static final DateTimeFormatter REPORT_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private double[] dailyRevenue;
    private SegmentTree segmentTree;
    private FenwickTree fenwickTree;

    /**
     * Creates a SalesAnalytics service object and builds analytics trees.
     */
    public SalesAnalytics() {
        createReportFiles();
        loadSalesData();
    }

    /**
     * Loads sales.txt, skips malformed records safely, and builds DSA structures.
     */
    public void loadSalesData() {
        FileManager.createDataFiles();
        List<SalesRecord> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(SALES_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                SalesRecord record = parseSalesRecord(line);
                if (record != null) {
                    records.add(record);
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load sales data: " + exception.getMessage());
        }

        dailyRevenue = buildRevenueArray(records);
        segmentTree = new SegmentTree(dailyRevenue);
        fenwickTree = FenwickTree.fromArray(dailyRevenue);
    }

    /**
     * Gets total revenue between two one-based day numbers.
     *
     * @param startDay starting day
     * @param endDay ending day
     * @return total revenue
     */
    public double getRevenueBetweenDays(int startDay, int endDay) {
        validateDayRange(startDay, endDay);
        return segmentTree.query(startDay - 1, endDay - 1);
    }

    /**
     * Gets cumulative revenue from day 1 through the given day.
     *
     * @param day one-based day
     * @return cumulative revenue
     */
    public double getCumulativeRevenue(int day) {
        validateDay(day);
        return fenwickTree.getPrefixSum(day);
    }

    /**
     * Generates an analytics report entry for a range query.
     *
     * @param startDay starting day
     * @param endDay ending day
     * @param totalRevenue total revenue
     */
    public void generateAnalyticsReport(int startDay, int endDay, double totalRevenue) {
        String report = "Generated At: " + LocalDateTime.now().format(REPORT_TIME_FORMAT)
                + System.lineSeparator()
                + "Total Sales Between Day " + startDay + " and Day " + endDay + ": "
                + String.format("%.2f", totalRevenue)
                + System.lineSeparator();
        appendReport(ANALYTICS_REPORT, report);
    }

    /**
     * Generates a revenue report entry for a cumulative query.
     *
     * @param day target day
     * @param cumulativeRevenue cumulative revenue
     */
    public void generateRevenueReport(int day, double cumulativeRevenue) {
        String report = "Generated At: " + LocalDateTime.now().format(REPORT_TIME_FORMAT)
                + System.lineSeparator()
                + "Revenue till Day " + day + ": " + String.format("%.2f", cumulativeRevenue)
                + System.lineSeparator();
        appendReport(REVENUE_REPORT, report);
    }

    /**
     * Gets the largest day loaded from sales.txt.
     *
     * @return number of available days
     */
    public int getAvailableDayCount() {
        return dailyRevenue.length;
    }

    /**
     * Updates revenue for a specific day and rebuilds analytics trees.
     *
     * @param day day to update
     * @param amount additional revenue amount
     */
    public void updateRevenue(int day, double amount) {
        if (day <= 0) {
            System.out.println("Invalid day for revenue update.");
            return;
        }

        if (day > dailyRevenue.length) {
            expandRevenueArray(day);
        }

        dailyRevenue[day - 1] += amount;
        rebuildAnalyticsTrees();
        saveUpdatedSales();
    }

    /**
     * Creates report directory and files when absent.
     */
    private void createReportFiles() {
        try {
            Files.createDirectories(REPORTS_DIRECTORY);
            if (Files.notExists(ANALYTICS_REPORT)) {
                Files.createFile(ANALYTICS_REPORT);
            }
            if (Files.notExists(REVENUE_REPORT)) {
                Files.createFile(REVENUE_REPORT);
            }
        } catch (IOException exception) {
            System.out.println("Unable to create report files: " + exception.getMessage());
        }
    }

    /**
     * Parses one sales record line.
     *
     * @param line sales file line
     * @return sales record, or null if invalid
     */
    private SalesRecord parseSalesRecord(String line) {
        if (line == null || line.trim().isEmpty() || line.equalsIgnoreCase("Day,Revenue")) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length != 2) {
            System.out.println("Skipping malformed sales record: " + line);
            return null;
        }

        try {
            int day = Integer.parseInt(parts[0].trim());
            double revenue = Double.parseDouble(parts[1].trim());
            if (day <= 0 || revenue < 0) {
                System.out.println("Skipping invalid sales record: " + line);
                return null;
            }
            return new SalesRecord(day, revenue);
        } catch (NumberFormatException exception) {
            System.out.println("Skipping invalid sales record: " + line);
            return null;
        }
    }

    /**
     * Converts parsed sales records into a day-indexed revenue array.
     *
     * @param records parsed sales records
     * @return revenue array
     */
    private double[] buildRevenueArray(List<SalesRecord> records) {
        int maxDay = 0;
        for (SalesRecord record : records) {
            if (record.day > maxDay) {
                maxDay = record.day;
            }
        }

        double[] revenues = new double[maxDay];
        for (SalesRecord record : records) {
            revenues[record.day - 1] = record.revenue;
        }
        return revenues;
    }

    /**
     * Validates a day range for Segment Tree queries.
     *
     * @param startDay starting day
     * @param endDay ending day
     */
    private void validateDayRange(int startDay, int endDay) {
        validateDay(startDay);
        validateDay(endDay);
        if (startDay > endDay) {
            throw new IllegalArgumentException("Start day cannot be greater than end day.");
        }
    }

    /**
     * Validates one day number.
     *
     * @param day day to validate
     */
    private void validateDay(int day) {
        if (dailyRevenue.length == 0) {
            throw new IllegalArgumentException("No sales data available.");
        }
        if (day <= 0 || day > dailyRevenue.length) {
            throw new IllegalArgumentException("Day must be between 1 and " + dailyRevenue.length + ".");
        }
    }

    /**
     * Appends one report entry to a report file.
     *
     * @param reportFile target report file
     * @param report report content
     */
    private void appendReport(Path reportFile, String report) {
        createReportFiles();
        try (BufferedWriter writer = Files.newBufferedWriter(reportFile, java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(report);
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("Unable to write report: " + exception.getMessage());
        }
    }

    /**
     * Expands the revenue array to accommodate a new day.
     *
     * @param newDay the new day number
     */
    private void expandRevenueArray(int newDay) {
        double[] newRevenue = new double[newDay];
        System.arraycopy(dailyRevenue, 0, newRevenue, 0, dailyRevenue.length);
        dailyRevenue = newRevenue;
    }

    /**
     * Rebuilds Segment Tree and Fenwick Tree after revenue update.
     */
    private void rebuildAnalyticsTrees() {
        segmentTree = new SegmentTree(dailyRevenue);
        fenwickTree = FenwickTree.fromArray(dailyRevenue);
    }

    /**
     * Saves updated sales data to sales.txt.
     */
    private void saveUpdatedSales() {
        try (BufferedWriter writer = Files.newBufferedWriter(SALES_FILE)) {
            writer.write("Day,Revenue");
            writer.newLine();
            for (int i = 0; i < dailyRevenue.length; i++) {
                writer.write((i + 1) + "," + dailyRevenue[i]);
                writer.newLine();
            }
        } catch (IOException exception) {
            System.out.println("Unable to save updated sales data: " + exception.getMessage());
        }
    }

    /**
     * Small data holder for parsed sales records.
     */
    private static class SalesRecord {
        private final int day;
        private final double revenue;

        /**
         * Creates a sales record.
         *
         * @param day day number
         * @param revenue revenue amount
         */
        SalesRecord(int day, double revenue) {
            this.day = day;
            this.revenue = revenue;
        }
    }
}
